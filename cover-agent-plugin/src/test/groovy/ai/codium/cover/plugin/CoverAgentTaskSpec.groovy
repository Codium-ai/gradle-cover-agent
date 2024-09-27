package ai.codium.cover.plugin

import dev.langchain4j.model.openai.OpenAiChatModel
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class CoverAgentTaskSpec extends Specification {

    Project project = ProjectBuilder.builder().build()
    CoverAgentTask task = project.tasks.create('coverAgentTask', CoverAgentTask)

    def "test default properties"() {
        expect:
        !task.apiKey.isPresent()
        !task.wanDBApiKey.isPresent()
        !task.iterations.isPresent()
        !task.coverAgentBinaryPath.isPresent()
        !task.coverage.isPresent()
    }

    def "test setting properties"() {
        given:
        task.apiKey.set("test-api-key")
        task.wanDBApiKey.set("test-wandb-api-key")
        task.iterations.set(10)
        task.coverAgentBinaryPath.set("/path/to/binary")
        task.coverage.set(80)

        expect:
        task.apiKey.get() == "test-api-key"
        task.wanDBApiKey.get() == "test-wandb-api-key"
        task.iterations.get() == 10
        task.coverAgentBinaryPath.get() == "/path/to/binary"
        task.coverage.get() == 80
    }

    def "test performTask"() {
        given:
        task.apiKey.set("test-api-key")
        task.wanDBApiKey.set("test-wandb-api-key")
        task.iterations.set(10)
        task.coverAgentBinaryPath.set("/path/to/binary")
        task.coverage.set(80)

        and:
        CoverAgentBuilder builderMock = Mock(CoverAgentBuilder)
        CoverAgent coverAgentMock = Mock(CoverAgent)
        OpenAiChatModel.OpenAiChatModelBuilder chatModelBuilderMock = Mock(OpenAiChatModel.OpenAiChatModelBuilder)

        when:

        CoverAgentBuilder.builder() >> builderMock
        builderMock.project(_) >> builderMock
        builderMock.apiKey(_) >> builderMock
        builderMock.wanDBApiKey(_) >> builderMock
        builderMock.iterations(_) >> builderMock
        builderMock.coverAgentBinaryPath(_) >> builderMock
        builderMock.coverage(_) >> builderMock
        builderMock.openAiChatModelBuilder(_) >> builderMock
        builderMock.build() >> coverAgentMock
        OpenAiChatModel.builder() >> chatModelBuilderMock

        task.performTask()

        then:
        0 * builderMock.project(project)
        0 * builderMock.apiKey("test-api-key")
        0 * builderMock.wanDBApiKey("test-wandb-api-key")
        0 * builderMock.iterations(10)
        0 * builderMock.coverAgentBinaryPath("/path/to/binary")
        0 * builderMock.coverage(80)
        0 * builderMock.openAiChatModelBuilder(chatModelBuilderMock)

    }
}
