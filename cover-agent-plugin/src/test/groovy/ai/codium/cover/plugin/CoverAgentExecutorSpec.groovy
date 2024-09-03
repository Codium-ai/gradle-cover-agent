package ai.codium.cover.plugin

import org.gradle.api.Project
import org.gradle.process.ExecResult
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

    def "A Failure from the executor"() {
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

}
