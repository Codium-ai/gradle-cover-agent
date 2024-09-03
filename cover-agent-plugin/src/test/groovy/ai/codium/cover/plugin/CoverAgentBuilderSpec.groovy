package ai.codium.cover.plugin

import org.gradle.api.Project
import spock.lang.Specification

class CoverAgentBuilderSpec extends Specification {
    void setup() {
    }
    // Builder correctly initializes all fields
    def "should initialize all fields correctly when built"() {
        given:
        Project
        def builder = new CoverAgentBuilder()
                .apiKey("testApiKey")
                .wanDBApiKey("testWanDBApiKey")
                .iterations(5)
                .coverage(80)
                .coverAgentBinaryPath("/path/to/binary")
                .modelPrompter(Mock(ModelPrompter))
                .javaClassPath(Optional.of("/path/to/class"))
                .javaTestClassPath(Optional.of("/path/to/test/class"))
                .projectPath("/path/to/project")
                .javaClassDir(Optional.of("/path/to/class/dir"))
                .buildDirectory("/path/to/build")
                .coverAgentExecutor(Mock(CoverAgentExecutor))
                .project(Mock(Project))

        when:
        def coverAgent = builder.build()

        then:
        coverAgent.apiKey == "testApiKey"
        coverAgent.wanDBApiKey == "testWanDBApiKey"
        coverAgent.iterations == 5
        coverAgent.coverage == 80
        coverAgent.coverAgentBinaryPath == "/path/to/binary"
        coverAgent.modelPrompter != null
        coverAgent.javaClassPath.get() == "/path/to/class"
        coverAgent.javaTestClassPath.get() == "/path/to/test/class"
        coverAgent.projectPath == "/path/to/project"
        coverAgent.javaClassDir.get() == "/path/to/class/dir"
        coverAgent.buildDirectory == "/path/to/build"
        coverAgent.coverAgentExecutor != null
        coverAgent.project != null
    }

}
