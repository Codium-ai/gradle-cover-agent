package ai.codium.cover.plugin

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.process.ExecResult
import org.gradle.process.ExecSpec
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.TempDir

class CoverAgentExecutorSpec extends Specification {
    @TempDir
    File testProjectDir

    @Shared
    File mockCoverAgentFile


    void cleanup() {}

    void setup() {
        mockCoverAgentFile = new File(testProjectDir, 'mock.sh')
        mockCoverAgentFile.createNewFile()
        mockCoverAgentFile.setExecutable(true)

        File mockCoverAgentResource = new File('src/test/resources/mock.sh')
        if (mockCoverAgentResource.exists()) {
            mockCoverAgentFile << mockCoverAgentResource.text
        } else {
            println("Resource file not found: ${mockCoverAgentResource.absolutePath}")
        }
    }

    def "Happy path calling the executor"() {
        given:
        Project project = Mock(Project)
        ExecResult execResult = Mock(ExecResult)

        CoverAgentExecutor executor = new CoverAgentExecutor.Builder()
                .coverAgentBinaryPath(mockCoverAgentFile.absolutePath)
                .wanDBApiKey("valid_wandb_api_key")
                .apiKey("valid_api_key")
                .coverage(1)
                .iterations(2)
                .build()

        when:
        String result = executor.execute(project, "sourceFile", "testFile", "jacocoReportPath", "commandString", "projectPath")

        then:
        1 * project.exec(_) >> execResult
        1 * execResult.getExitValue() >> 0
        result != null
    }

    def "A Failure from the executor mocked "() {
        given:
        Project project = Mock(Project)
        ExecResult execResult = Mock(ExecResult)

        CoverAgentExecutor executor = new CoverAgentExecutor.Builder()
                .coverAgentBinaryPath(mockCoverAgentFile.absolutePath)
                .wanDBApiKey("valid_wandb_api_key")
                .apiKey("valid_api_key")
                .coverage(1)
                .iterations(2)
                .build()

        when:
        executor.execute(project, "sourceFile", "testFile", "jacocoReportPath", "commandString", "projectPath")

        then:
        1 * project.exec(_) >> execResult
        1 * execResult.getExitValue() >> 1
        thrown(CoverError)
    }

    def "Call with ExecSpec to function"() {
        given:
        Project project = Mock(Project)
        ExecResult execResult = Mock(ExecResult)
        ExecSpec spec = Mock(ExecSpec)
        CoverAgentExecutor executor = new CoverAgentExecutor.Builder()
                .coverAgentBinaryPath(mockCoverAgentFile.absolutePath)
                .wanDBApiKey(wandbkey)
                .apiKey(apiKey)
                .coverage(1)
                .iterations(2)
                .build()

        when:
        Action<ExecSpec> action = executor.getExecSpecAction("sourceFile", "testFile", "jacocoReportPath",
                "commandString", "projectPath")
        action.execute(spec)

        then:
        wanCall * spec.environment(CoverAgentExecutor.WANDB_API_KEY, wandbkey)
        apiCall * spec.environment(CoverAgentExecutor.OPENAI_API_KEY, apiKey)
        1 * spec.setWorkingDir("projectPath")

        where:
        apiKey       | wandbkey   | wanCall | apiCall
        "api"        | null       | 0       | 1
        null         | "wandbkey" | 1       | 0
        "anotherkey" | ""         | 0       | 1


    }

}
