package ai.codium.cover.plugin

import dev.langchain4j.model.openai.OpenAiChatModel
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.file.ProjectLayout
import org.gradle.api.logging.Logger
import org.gradle.api.tasks.TaskCollection
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.compile.JavaCompile
import spock.lang.Shared
import spock.lang.Specification

import org.gradle.api.provider.Provider

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O

class CoverAgentSpec extends Specification {

    @Shared
    CoverAgentBuilder builder = CoverAgentBuilder.builder()

    void setup() {}

    def "Not Found JavaCompileTask null "() {
        given:
        Project project = Mock(Project)
        builder.project(project)
        CoverAgent coverAgent = builder.build()
        TaskContainer container = Mock(TaskContainer)
        TaskCollection collection = Mock(TaskCollection)
        JavaCompile javaTestCompileTask = Mock(JavaCompile)

        when:
        coverAgent.setJavaTestClassPath()

        then:
        1 * project.getTasks() >> container
        1 * container.withType(JavaCompile.class) >> collection
        1 * collection.findByName("compileTestJava") >> null
    }

    def "init directories "() {
        given:
        Logger logger = Mock(Logger)
        Project project = Mock(Project)
        builder.project(project)
        ProjectLayout projectLayout = Mock(ProjectLayout)
        Directory projectDirectory = Mock(Directory)
        File direct = Mock(File)
        DirectoryProperty directoryProperty = Mock(DirectoryProperty)
        Provider buildDirectoryProvider = Mock(Provider)
        File realFile = new File('src/test/resources/build.gradle')


        when:
        CoverAgent coverAgent = builder.build()
        coverAgent.initDirectories()

        then:
        2 * project.getLayout() >> projectLayout
        1 * projectLayout.getProjectDirectory() >> projectDirectory
        1 * projectDirectory.getAsFile() >> realFile
        1 * projectLayout.getBuildDirectory() >> directoryProperty
        1 * directoryProperty.getAsFile() >> buildDirectoryProvider
        1 * buildDirectoryProvider.get() >> direct
        1 * project.getLogger() >> logger
        1 * direct.exists() >> false
        1 * direct.mkdirs() >> outcome

        where: outcome << [true, false]


    }


    def "init method initializes correctly"() {
        given:
        Logger logger = Mock(Logger)
        TaskContainer container = Mock(TaskContainer)
        TaskCollection collection = Mock(TaskCollection)
        JavaCompile javaTestCompileTask = Mock(JavaCompile)
        DirectoryProperty directoryProperty = Mock(DirectoryProperty)
        Directory directory = Mock(Directory)
        File file = Mock(File)
        FileCollection fileCollection = Mock(FileCollection)
        File realFile = new File('src/test/resources/build.gradle')
        Set<File> fileSet = Set.of(realFile)
        FileTree fileTree = Mock(FileTree)

        ProjectLayout projectLayout = Mock(ProjectLayout)
        Directory projectDirectory = Mock(Directory)

        Provider buildDirectoryProvider = Mock(Provider)

        Project project = Mock(Project)
        OpenAiChatModel.OpenAiChatModelBuilder aiChatModelBuilder = Mock(OpenAiChatModel.OpenAiChatModelBuilder)
        OpenAiChatModel aiChatModel = Mock(OpenAiChatModel)
        builder.project(project).openAiChatModelBuilder(aiChatModelBuilder)

        when:
        CoverAgent coverAgent = builder.build()
        coverAgent.init()

        then:
        1 * aiChatModelBuilder.apiKey(_) >> aiChatModelBuilder
        1 * aiChatModelBuilder.modelName(GPT_4_O) >> aiChatModelBuilder
        1 * aiChatModelBuilder.maxTokens(500) >> aiChatModelBuilder
        1 * aiChatModelBuilder.build() >> aiChatModel
        1 * project.getLogger() >> logger
        1 * logger.debug("Root Project path {}", _)
        1 * logger.debug("Build directory already exists: {}", _)

        _ * project.getTasks() >> container
        _ * container.withType(JavaCompile.class) >> collection
        _ * collection.findByName(_) >> javaTestCompileTask
        _ * javaTestCompileTask.getDestinationDirectory() >> directoryProperty
        _ * directoryProperty.get() >> directory
        _ * directory.getAsFile() >> file
        _ * file.getAbsolutePath() >> "/apth"
        _ * javaTestCompileTask.getClasspath() >> fileCollection
        _ * fileCollection.getFiles() >> fileSet
        _ * project.getLogger() >> logger
        _ * javaTestCompileTask.getSource() >> fileTree
        _ * fileTree.getFiles() >> fileSet

        2 * project.getLayout() >> projectLayout
        1 * projectLayout.getProjectDirectory() >> projectDirectory
        1 * projectDirectory.getAsFile() >> realFile
        1 * projectLayout.getBuildDirectory() >> directoryProperty
        1 * directoryProperty.getAsFile() >> buildDirectoryProvider
        1 * buildDirectoryProvider.get() >> realFile
    }

    def "invoke method processes test files and executes coverage agent"() {
        given:
        Logger logger = Mock(Logger)
        Project project = Mock(Project)
        builder.project(project)

        when:
        CoverAgent coverAgentOne = builder.build()
        coverAgentOne.invoke()


        then:
        1 * project.getLogger() >> logger
        1 * logger.debug("Path to coverAgentBinaryPath {}", _)
        1 * coverAgent.coverAgentExecutor.execute(_, _, _, _, _, _) >> "Success"
        1 * logger.debug("Success output from cover-agent: {}", "Success")
    }
}
