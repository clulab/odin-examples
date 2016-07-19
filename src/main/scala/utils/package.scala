import org.clulab.odin._
import org.clulab.processors.{Sentence, Document}

package object utils {

  def displayMentions(mentions: Seq[Mention], doc: Document): Unit = {
    val mentionsBySentence = mentions groupBy (_.sentence) mapValues (_.sortBy(_.start)) withDefaultValue Nil
    for ((s, i) <- doc.sentences.zipWithIndex) {
      println(s"sentence #$i")
      println(s.getSentenceText)
      println("Tokens: " + (s.words.indices, s.words, s.tags.get).zipped.mkString(", "))
      printSyntacticDependencies(s)
      println

      val sortedMentions = mentionsBySentence(i).sortBy(_.label)
      val (events, entities) = sortedMentions.partition(_ matches "Event")
      val (tbs, rels) = entities.partition(_.isInstanceOf[TextBoundMention])
      val sortedEntities = tbs ++ rels.sortBy(_.label)
      println("entities:")
      sortedEntities foreach displayMention

      println
      println("events:")
      events foreach displayMention
      println("=" * 50)
    }
  }

  def printSyntacticDependencies(s:Sentence): Unit = {
    if(s.dependencies.isDefined) {
      println(s.dependencies.get.toString)
    }
  }

  def displayMention(mention: Mention) {
    val boundary = s"\t${"-" * 30}"
    println(s"${mention.labels} => ${mention.text}")
    println(boundary)
    println(s"\tRule => ${mention.foundBy}")
    val mentionType = mention.getClass.toString.split("""\.""").last
    println(s"\tType => $mentionType")
    println(boundary)
    mention match {
      case tb: TextBoundMention =>
        println(s"\t${tb.labels.mkString(", ")} => ${tb.text}")
      case em: EventMention =>
        println(s"\ttrigger => ${em.trigger.text}")
        displayArguments(em)
      case rel: RelationMention =>
        displayArguments(rel)
      case _ => ()
    }
    println(s"$boundary\n")
  }


  def displayArguments(b: Mention): Unit = {
    b.arguments foreach {
      case (argName, ms) =>
        ms foreach { v =>
          println(s"\t$argName ${v.labels.mkString("(", ", ", ")")} => ${v.text}")
        }
    }
  }
}
