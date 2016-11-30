package sampledomains.immunology

import java.io.File
import org.clulab.odin.ExtractorEngine
import utils.PaperReader
import ai.lum.common.ConfigUtils._
import utils.displayMention


object ImmunologyExample extends App {
  // read rules from general-rules.yml file in resources
  val source = io.Source.fromURL(getClass.getResource("/grammars/immunology/master.yml"))
  val rules = source.mkString
  source.close()

  // creates an extractor engine using the rules and the default actions
  val ee = ExtractorEngine(rules)
  
  val serializedDocs = PaperReader.config[File]("reader.serializedPapersDir")
  val mentions = for {
    d <- PaperReader.deserializeJSONDocuments(serializedDocs)
    // extract mentions from each document
    m <- ee.extractFrom(d)
  } yield m

  // print each mention
  mentions foreach displayMention
}
