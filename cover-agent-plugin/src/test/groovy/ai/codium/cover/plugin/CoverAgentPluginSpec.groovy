package ai.codium.cover.plugin


import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification
import spock.lang.TempDir

class CoverAgentPluginSpec extends Specification {
    @TempDir
    File testProjectDir
    File buildFile
    File src
    File main
    File hava
    File test
    File thava
    File mainJavaFile
    File testJavaFile
    File testCalcJavaFile
    File mockCoverAgentFile

    def setup() {
        buildFile = new File(testProjectDir, 'build.gradle')
        File resourceBuildFile = new File('src/test/resources/build.gradle')
        if (resourceBuildFile.exists()) {
            buildFile << resourceBuildFile.text
        } else {
            println("Resource file not found: ${resourceBuildFile.absolutePath}")
        }
        println("${buildFile.absolutePath}: ${buildFile.text}")

        src = new File(testProjectDir, 'src')
        src.mkdirs()
        main = new File(src, 'main')
        main.mkdirs()
        hava = new File(main, 'java')
        hava.mkdirs()

        test = new File(src, 'test')
        test.mkdirs()
        thava = new File(test, 'java')
        thava.mkdirs()

        String packagePath = 'ai/codium/test'
        File sourcePackage = new File(hava, packagePath)
        sourcePackage.mkdirs()
        File testPackage = new File(thava, packagePath)
        testPackage.mkdirs()

        mainJavaFile = new File(sourcePackage, 'Main.java')
        mainJavaFile.createNewFile()

        File calcFile = new File(sourcePackage, 'Calc.java')
        calcFile.createNewFile()

        testJavaFile = new File(testPackage, 'MainTest.java')
        testJavaFile.createNewFile()

        testCalcJavaFile = new File(testPackage, 'CalcTest.java')
        testCalcJavaFile.createNewFile()


        File mainJavaResource = new File('src/test/resources/Main.java')
        if (mainJavaResource.exists()) {
            mainJavaFile << mainJavaResource.text
        } else {
            println("Resource file not found: ${mainJavaResource.absolutePath}")
        }
        File calcJavaResource = new File('src/test/resources/Calc.java')
        if (calcJavaResource.exists()) {
            calcFile << calcJavaResource.text
        } else {
            println("Resource file not found: ${calcJavaResource.absolutePath}")
        }


        File testJavaResource = new File('src/test/resources/MainTest.java')
        if (testJavaResource.exists()) {
            testJavaFile << testJavaResource.text
        } else {
            println("Resource file not found: ${testJavaResource.absolutePath}")
        }

        File testCalcJavaResource = new File('src/test/resources/CalcTest.java')
        if (testCalcJavaResource.exists()) {
            testCalcJavaFile << testCalcJavaResource.text
        } else {
            println("Resource file not found: ${testCalcJavaResource.absolutePath}")
        }

        mockCoverAgentFile = new File(testProjectDir, 'mock.sh')
        mockCoverAgentFile.createNewFile()
        mockCoverAgentFile.setExecutable(true)

        File mockCoverAgentResource = new File('src/test/resources/mock.sh')
        if (mockCoverAgentResource.exists()) {
            mockCoverAgentFile << mockCoverAgentResource.text
        } else {
            println("Resource file not found: ${mockCoverAgentResource.absolutePath}")
        }
        println("Testing file path ${testJavaFile.absoluteFile.absolutePath}")
    }

    /**
     * If you set environment key OPENAI_API_KEY to a proper key and run this with a valid key the test
     * will fail based it is not expecting it but the mock.sh will be called to see the complete happy cycle.
     * The cover-agent binary is not called still though only the Model for finding the source file for test
     * */
    def "lifecycle test of gradle plugin will not make it complete based on API KEY invalid"() {
        // change up this map if you want to see full lifecycle you need to set your OPENAI_API_KEY
        //Map map = Map.of("OPENAI_API_KEY", System.getenv("OPENAI_API_KEY"))
        Map map = Map.of("OPENAI_API_KEY", "I_AM_BAD_KEY")
        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withArguments('coverAgent')
                .withEnvironment(map)
                .withPluginClasspath()
                .forwardStdOutput(new PrintWriter(System.out))
                .forwardStdError(new PrintWriter(System.err))
                .build()

        then:
        result.output.contains("Incorrect API key provided: I_AM_BAD_KEY")

    }

}