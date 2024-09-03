package ai.codium.cover.plugin

import spock.lang.Specification

class SourceFilePromptSpec extends Specification {
    void setup() {
    }
    // Creating a SourceFilePrompt with valid sourceFiles, fileName, and content
    def "should create SourceFilePrompt with valid parameters"() {
        given:
        List<String> sourceFiles = ["file1.java", "file2.java"]
        String fileName = "Main.java"
        String content = "public class Main {}"

        when:
        SourceFilePrompt prompt = new SourceFilePrompt(sourceFiles, fileName, content)

        then:
        prompt.sourceFiles == sourceFiles
        prompt.fileName == fileName
        prompt.content == content
    }
}
