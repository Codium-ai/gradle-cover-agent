package ai.codium.cover.plugin

import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.output.Response
import org.gradle.api.logging.Logger;
import spock.lang.Specification

class ModelPrompterSpec extends Specification {

    def "should log user prompt to model"() {
        given:
        Logger logger = Mock(Logger)
        ChatLanguageModel model = Mock(ChatLanguageModel)
        ModelPrompter prompter = new ModelPrompter(logger, model)
        List<File> sourceFiles = [new File("src/test/java/TestFile1.java")]
        File testFile = new File('src/test/resources/Calc.java')
        Response<AiMessage> rsp = Mock( Response<AiMessage>)
        AiMessage message = Mock(AiMessage)
        when:
        TestInfoResponse response = prompter.chatter(sourceFiles, testFile)

        then:
        1 * model.generate(_,_) >> rsp
        _ * rsp.content() >> message
        _ * message.text() >> 'json     {\"filepath\": \"path_here\"} '
        response.filepath() == "path_here"
    }

    def "failure can not contact or call model error"() {
        given:
        Logger logger = Mock(Logger)
        ChatLanguageModel model = Mock(ChatLanguageModel)
        ModelPrompter prompter = new ModelPrompter(logger, model)
        List<File> sourceFiles = [new File("src/test/java/TestFile1.java")]
        File testFile = new File("src/test/java/TestFile.java")

        when:
        prompter.chatter(sourceFiles, testFile)

        then:
        thrown(CoverError)
    }

    def"extract empty no value from model empty object" () {
        given:
        Logger logger = Mock(Logger)
        ChatLanguageModel model = Mock(ChatLanguageModel)
        ModelPrompter prompter = new ModelPrompter(logger, model)

        expect: prompter.extractJson(null) == "{}"
    }

}
