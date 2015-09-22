package toydomains.bio

import edu.arizona.sista.odin._
import utils._
import edu.arizona.sista.processors.bionlp.BioNLPProcessor

object BioExample extends App {
  // two example sentences
  val text = """|TGFBR2 phosphorylates SMAD2 and inhibits the ubiquitination of SMAD3.
                |TGFBR2 binds to TGFBR1 and SMAD3.
                |""".stripMargin

  // read rules from bio-rules.yml file in resources
  val source = io.Source.fromURL(getClass.getResource("/grammars/likelihood/master.yml"))
  val rules = source.mkString
  source.close()

  // creates an extractor engine using the rules
  // no actions are given, so the mentions will be returned as found
  val extractor = ExtractorEngine(rules)

  // annotate the sentences
  val proc = new BioNLPProcessor
  val doc = proc.annotate(text)

  // extract mentions from annotated document
  val mentions = extractor.extractFrom(doc)

  // display the mentions
  displayMentions(mentions, doc)
}
