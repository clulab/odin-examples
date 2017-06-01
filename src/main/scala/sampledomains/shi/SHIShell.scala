package sampledomains.shi

import java.io.File

import jline.console.ConsoleReader
import jline.console.history.FileHistory
import org.clulab.odin.{Mention, ExtractorEngine}
import org.clulab.processors.{Document, Processor}
import org.clulab.processors.fastnlp.FastNLPProcessor
import utils._

import scala.collection.immutable.{HashMap, ListMap}
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Interactive shell for parsing SHI docs
  * User: mihais
  * Date: 5/31/17
  */
object SHIShell extends App {

  val history = new FileHistory(new File(System.getProperty("user.home"), ".agroshellhistory"))
  sys addShutdownHook {
    history.flush() // flush file before exiting
  }

  val reader = new ConsoleReader
  reader.setHistory(history)

  val commands = ListMap(
    ":help" -> "show commands",
    // ":reload" -> "reload grammar",
    ":exit" -> "exit system"
  )

  // create the processor
  val fast: Processor = new FastNLPProcessor(useMalt = false, withDiscourse = false)

  // read rules from general-rules.yml file in resources
  val source = io.Source.fromURL(getClass.getResource("/grammars/shi/master.yml"))
  val rules = source.mkString
  source.close()

  // creates an extractor engine using the rules and the default actions
  val extractor = ExtractorEngine(rules)

  var proc = fast
  reader.setPrompt("(RAP)>>> ")
  println("\nWelcome to the RAPShell!")
  printCommands()

  var running = true

  while (running) {
    reader.readLine match {
      case ":help" =>
        printCommands()

      case ":reload" =>
        println("Not supported yet.")
        // TODO

      case ":exit" | null =>
        running = false

      case text =>
        parse(text)
    }
  }

  // manual terminal cleanup
  reader.getTerminal.restore()
  reader.shutdown()

  // summarize available commands
  def printCommands(): Unit = {
    println("\nCOMMANDS:")
    for ((cmd, msg) <- commands)
      println(s"\t$cmd\t=> $msg")
    println()
  }

  def parse(text:String): Unit = {
    // preprocessing
    val doc = annotate(text)

    // extract mentions from annotated document
    val mentions = extractor.extractFrom(doc).sortBy(m => (m.sentence, m.getClass.getSimpleName))

    // debug display the mentions
    displayMentions(mentions, doc)
  }

  def annotate(text:String):Document = {
    val doc = proc.mkDocument(text, keepText = false)
    proc.tagPartsOfSpeech(doc)
    proc.lemmatize(doc)
    proc.parse(doc)
    //proc.chunking(doc)
    doc.clear()
    doc
  }
}
