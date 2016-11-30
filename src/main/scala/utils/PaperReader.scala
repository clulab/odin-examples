package utils

import java.io.File

import ai.lum.common.ConfigUtils._
import ai.lum.nxmlreader.{NxmlDocument, NxmlReader}
import com.typesafe.config.ConfigFactory
import org.apache.commons.io.FilenameUtils
import org.clulab.processors.Document
import org.clulab.processors.bionlp.BioNLPProcessor

import scala.collection.parallel.ForkJoinTaskSupport
import scala.collection.parallel.mutable.ParArray


/**
  * Utilities for producing a Document from an .nxml or .txt file
  */
object PaperReader {

  val config = ConfigFactory.load()
  // the number of threads to use for parallelization
  val threadLimit: Int = config[Int]("threadLimit")
  val ignoreSections: List[String] = config[List[String]]("nxml.ignoreSections")
  val fileEncoding = config.getString("encoding")
  var proc = new BioNLPProcessor
  val nxmlReader = new NxmlReader(ignoreSections.toSet, transformText = proc.preprocessText)


  def mkDoc(text: String, docId: String): Document = {
    val doc = proc.annotate(text, keepText = true)
    doc.id = Some(s"$docId")
    doc
  }

  def mkDoc(nxml: NxmlDocument): Document = {
    // we are using the PMC as the chunk-id because we now read
    // the whole paper in a single chunk
    mkDoc(nxml.text, nxml.pmc)
  }

  def getContents(file: File): String = scala.io.Source.fromFile(file, fileEncoding).getLines.mkString

  /**
    * Produces Dataset from a directory of nxml and txt papers.
    * @param dir a File (directory) of nxml and txt papers
    * @return a Seq[Document]
    */
  def readPapers(dir: File): Seq[Document] = {
    val _ = proc.annotate("blah")
    require(dir.isDirectory, s"'${dir.getCanonicalPath}' is not a directory")
    // read papers in parallel
    val files = dir.listFiles.par
    // limit parallelization
    files.tasksupport =
      new ForkJoinTaskSupport(new scala.concurrent.forkjoin.ForkJoinPool(threadLimit))
    // build dataset
    val data: ParArray[Document] = for {
      file <- dir.listFiles.par // read papers in parallel
      // allow either nxml or txt files
      if file.getName.endsWith(".nxml") || file.getName.endsWith(".txt")
    } yield readPaper(file)
    data.seq
  }

  /**
    * Produces a Document from either a .nxml or txt file
    * @param file a File with either the .txt, or .nxml extension
    * @return [[Document]]
    */
  def readPaper(file: File): Document = file match {
    case nxml if nxml.getName.endsWith(".nxml") =>
      readNXMLPaper(nxml)
    case txt if txt.getName.endsWith(".txt") =>
      readPlainTextPaper(txt)
    case other =>
      throw new Exception(s"Given ${file.getAbsolutePath}, but readPaper doesn't support ${FilenameUtils.getExtension(other.getAbsolutePath)}")
  }

  private def readNXMLPaper(file: File): Document = {
    require(file.getName.endsWith(".nxml"), s"Given ${file.getAbsolutePath}, but readNXMLPaper only handles .nxml files!")
    //val paperID = FilenameUtils.removeExtension(file.getName)
    //info(s"reading paper $paperID . . .")
    mkDoc(nxmlReader.read(file))
  }

  private def readPlainTextPaper(file: File): Document = {
    require(file.getName.endsWith(".txt"), s"Given ${file.getAbsolutePath}, but readPlainText only handles .txt files!")
    val paperID = FilenameUtils.removeExtension(file.getName)
    val text = getContents(file)
    mkDoc(text, paperID)
  }
}
