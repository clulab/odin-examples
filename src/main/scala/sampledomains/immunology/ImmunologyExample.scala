package sampledomains.immunology

import org.clulab.odin.ExtractorEngine
import org.clulab.processors.fastnlp.FastNLPProcessor


object ImmunologyExample extends App {
  // read rules from general-rules.yml file in resources
  val source = io.Source.fromURL(getClass.getResource("/grammars/immunology/master.yml"))
  val rules = source.mkString
  source.close()

  // creates an extractor engine using the rules and the default actions
  val extractor = ExtractorEngine(rules)

  // annotate the sentences
  val proc = new FastNLPProcessor


}
