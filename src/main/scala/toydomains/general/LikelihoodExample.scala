package toydomains.general

import edu.arizona.sista.odin._
import edu.arizona.sista.processors.corenlp.CoreNLPProcessor
import utils._


object LikelihoodExample extends App {
  // two example sentences
  val text = """|Donald ate the old cheese.
                |I saw Donald eat the old cheese.
                |I believe Donald ate the old cheese.
                |It's doubtful Donald ate all that cheese by himself.
                |It's doubtful that Rufus will win the election.
                |Many people believe that Fernando will win the election.
                |Many people don't believe that Horace can win the election.
                |Everyone seems to believe that Horace will surely lose the election.
                |Everyone believes Fernando will win the election.
                |""".stripMargin

  // read rules from general-rules.yml file in resources
  val source = io.Source.fromURL(getClass.getResource("/grammars/likelihood/master.yml"))
  val rules = source.mkString
  source.close()

  // creates an extractor engine using the rules and the default actions
  val extractor = ExtractorEngine(rules)

  // annotate the sentences
  val proc = new CoreNLPProcessor
  val doc = proc.annotate(text)

  // extract mentions from annotated document
  val mentions = extractor.extractFrom(doc).sortBy(m => (m.sentence, m.getClass.getSimpleName))

  // display the mentions
  displayMentions(mentions, doc)
}
