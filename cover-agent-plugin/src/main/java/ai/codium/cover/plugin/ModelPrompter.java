package ai.codium.cover.plugin;

import com.google.gson.Gson;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import org.gradle.api.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ModelPrompter {
    private static final String SYSTEM_PROMPT = """
            The userPrompt input will be provided in JSON format following this valid json schema:{"title":
            "TestWithSourceFile","$schema": "http://json-schema.org/draft-07/schema#","type": "object","properties"
            :{"sourceFiles":{"type": "array","description": "The list of source files to pick from only, do not make
            your own up and you must pick one of these as the best match","items":{"type": "string"}},"fileName"
            :{"type": "string","description":"The file name of the Test that is testing one of the source files
            located in the json array element name sourceFiles"},"content":{"type":"string","description":
            "The contents of the test file json element name fileName this is a unit test written in either Java,
            Kotlin, or Groovy. This is the source you must use to determine the best matched source file from one of
            the json items in the json array element sourceFiles."}},"required": ["sourceFiles", "fileName", "content"]
            ,"additionalProperties":false}
            You must find the best file path that from the sourceFiles element and return the match in this mandatory
            json schema:{"title": "MatchedSourceFile" ,"$schema": "http://json-schema.org/draft-07/schema#","type":
            "object","properties":{"filepath":{"type": "string","description":"The file path of one of the entries in
            the json array element sourceFiles that is in the scheme titled TestWithSourceFile. Pick the best match of
             a file that is being tested by the json element fileName and the contents of the source in element source.
             "}},"required":["filepath"],"additionalProperties":false}
            """;
    private final Logger logger;
    private static final Gson GSON = new Gson();
    private ChatLanguageModel model;

    public ModelPrompter(Logger logger, ChatLanguageModel model) {
        this.logger = logger;
        this.model = model;
    }

    private static String extractJson(String text) {
        if (text != null) {
            return text.substring(text.indexOf('{'), text.indexOf('}') + 1);
        } else {
            return "{}";
        }
    }

    public TestInfoResponse chatter(List<File> sourceFiles, File testFile) throws CoverError {
        try {
            List<String> absolutePaths = sourceFiles.stream().map(File::getAbsolutePath).collect(Collectors.toList());
            SourceFilePrompt prompt = new SourceFilePrompt(absolutePaths, testFile.getName(), readFile(testFile));
            String userJson = GSON.toJson(prompt);
            logger.debug("User Prompt to Model: {}", userJson);
            ChatMessage systemChat = new SystemMessage(SYSTEM_PROMPT);
            ChatMessage userChat = new UserMessage(userJson);
            Response<AiMessage> message = model.generate(systemChat, userChat);
            logger.info("Model Response {} for TestFile {}", message.content().text(), testFile);
            String jsonString = extractJson(message.content().text());
            logger.debug("Json extracted {}", jsonString);
            return GSON.fromJson(jsonString, TestInfoResponse.class);
        } catch (Exception e) {
            logger.error("A failure happened trying to get the source file that matches the provided test {}",
                    e.getMessage());
            throw new CoverError("Huge error happened need to fix before proceeding ", e);
        }
    }

    private String readFile(File file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8);
    }


}
