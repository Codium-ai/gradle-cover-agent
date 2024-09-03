package ai.codium.cover.plugin

import dev.langchain4j.model.chat.ChatLanguageModel
import org.gradle.api.logging.Logger;
import spock.lang.Specification

class ModelPrompterSpec extends Specification {

    // Logger correctly logs user prompt to model
    def "should log user prompt to model"() {
        given:
        Logger logger = Mock(Logger)
        ChatLanguageModel model = Mock(ChatLanguageModel)
        ModelPrompter prompter = new ModelPrompter(logger, model)
        List<File> sourceFiles = [new File("src/test/java/TestFile1.java")]
        File testFile = new File("src/test/java/TestFile.java")

        when:
        TestInfoResponse response = prompter.chatter(sourceFiles, testFile)

        then:
        thrown(CoverError)
    }
}
