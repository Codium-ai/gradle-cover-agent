package ai.codium.cover.plugin

import dev.langchain4j.model.openai.OpenAiChatModel
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.file.ProjectLayout
import org.gradle.api.logging.Logger
import org.gradle.api.tasks.TaskCollection
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.compile.CompileOptions
import org.gradle.api.tasks.compile.JavaCompile
import org. gradle. api. artifacts. ConfigurationContainer
import spock.lang.Shared
import spock.lang.Specification
import  org.gradle.api.artifacts.Configuration
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

    // need to mock out javaCompileCommand
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

        CompileOptions javaCompileOptions = Mock(CompileOptions)

        when:
        CoverAgent coverAgent = builder.build()
        coverAgent.init()

        then:
        _ * javaTestCompileTask.getOptions() >> javaCompileOptions
        _ * javaCompileOptions.getAllCompilerArgs() >> [""]

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

    // Successful execution of the invoke method with valid project setup
    def "should execute invoke method successfully with valid project setup"() {
        given:
        def project = Mock(Project)
        def logger = Mock(Logger)
        def modelPrompter = Mock(ModelPrompter)
        def coverAgentExecutor = Mock(CoverAgentExecutor)
        def builder = new CoverAgentBuilder()
                .apiKey("validApiKey")
                .wanDBApiKey("validWanDBApiKey")
                .iterations(10)
                .coverage(80)
                .coverAgentBinaryPath("/path/to/binary")
                .project(project)
                .openAiChatModelBuilder(Mock(OpenAiChatModel.OpenAiChatModelBuilder))
                .modelPrompter(modelPrompter)
                .coverAgentExecutor(coverAgentExecutor)
        TaskContainer container = Mock(TaskContainer)
        TaskCollection collection = Mock(TaskCollection)
        JavaCompile javaTestCompileTask = Mock(JavaCompile)
        ConfigurationContainer configurationContainer = Mock(ConfigurationContainer)
        DependencyHandler dependencyHandler = Mock(DependencyHandler)
        Dependency dependency = Mock(Dependency)
        Configuration conf = Mock(Configuration)
        Set<File> files = [new File('src/test/resources/build.gradle')]

        when:
        CoverAgent coverAgent = builder.build()
        coverAgent.javaCompileCommand = Optional.of("src/test/resources/mock.sh")
        coverAgent.javaTestCompileCommand = Optional.of("src/test/resources/mock.sh")
        coverAgent.javaTestSourceFiles.add(new File('src/test/resources/CalcTest.java'))
        coverAgent.invoke()

        then:
        _ * conf.resolve() >> files
        _ * project.getDependencies() >> dependencyHandler
        _ * project.getConfigurations() >> configurationContainer
        _ * dependencyHandler.create(_) >> dependency
        _ * configurationContainer.detachedConfiguration(_) >> conf
        _ * project.getTasks() >> container
        _ * container.withType(JavaCompile.class) >> collection
        _ * collection.findByName(_) >> javaTestCompileTask

        _ * project.getLogger() >> logger
        _ * modelPrompter.chatter(_, _) >> new TestInfoResponse("sourceFilePath")
        _ * coverAgentExecutor.execute(_, _, _, _, _, _) >> "success"
    }
}
