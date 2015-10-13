package toydomains.general;

import java.util.List;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.URL;
import scala.collection.Seq;
import scala.collection.JavaConversions;
import edu.arizona.sista.odin.*;
import edu.arizona.sista.processors.Document;
import edu.arizona.sista.processors.corenlp.CoreNLPProcessor;

class LikelihoodExampleJava {

    public static void main(String[] args) throws Exception {
        String text = "Donald ate the old cheese.\n"
            + "I saw Donald eat the old cheese.\n"
            + "I believe Donald ate the old cheese.\n"
            + "It's doubtful Donald ate all that cheese by himself.\n"
            + "It's doubtful that Rufus will win the election.\n"
            + "Many people believe that Fernando will win the election.\n"
            + "Many people don't believe that Horace can win the election.\n"
            + "Everyone seems to believe that Horace will surely lose the election.\n"
            + "Everyone believes Fernando will win the election.\n";
        // make new processor
        CoreNLPProcessor proc = new CoreNLPProcessor(true, false, 100);
        // read rules
        String rules = readResource("grammars/likelihood/master.yml");
        // make new extractor engine
        ExtractorEngine ee = ExtractorEngine.apply(rules);
        // annotate text
        Document doc = proc.annotate(text, true);
        // extract mentions
        Seq<Mention> mentions = ee.extractFrom(doc);
        // List<Mention> mentionList = JavaConversions.seqAsJavaList(mentions);
        // display mentions
        utils.package$.MODULE$.displayMentions(mentions, doc);
    }

    // read text file from resources
    private static String readResource(String path) throws Exception {
        URL url = LikelihoodExampleJava.class.getClassLoader().getResource(path);
        byte[] bytes = Files.readAllBytes(Paths.get(url.toURI()));
        return new String(bytes, Charset.defaultCharset());
    }

}
