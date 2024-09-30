package ai.qodo.cover.plugin;

import dev.langchain4j.model.openai.OpenAiChatModel;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.TaskAction;

public abstract class CoverAgentTask extends DefaultTask {
    private Property<String> apiKey;
    private Property<String> wanDBApiKey;
    private Property<Integer> iterations;
    private Property<Integer> coverage;
    private Property<String> coverAgentBinaryPath;
    private CoverAgent coverAgent;
    private Project project;

    public CoverAgentTask() {
        init(getProject());
    }

    private void init(Project project) {
        this.project = project;
        ObjectFactory objectFactory = this.project.getObjects();
        this.apiKey = objectFactory.property(String.class);
        this.wanDBApiKey = objectFactory.property(String.class);
        this.iterations = objectFactory.property(Integer.class);
        this.coverAgentBinaryPath = objectFactory.property(String.class);
        this.coverage = objectFactory.property(Integer.class);
    }


    /**
     * This is your API key for OpenAI to access their api.
     * @return String api key for OpenAI to use in calling the LLM.
     */
    @Optional
    @Input
    public Property<String> getApiKey() {
        return apiKey;
    }

    /**
     * Not currently implemented, placeholder for future support.
     * @return
     */
    @Optional
    @Input
    public Property<String> getWanDBApiKey() {
        return wanDBApiKey;
    }

    /**
     * This is the path to the CoverAgent binary specific to your OS
     *
     * @return path to the binary example (/Users/davidparry/code/github/base/cover-agent/dist/cover-agent)
     */
    @Optional
    @Input
    public Property<String> getCoverAgentBinaryPath() {
        return coverAgentBinaryPath;
    }

    /**
     * This is how many calls to allow using your API keyAI to OpenAI per Test/Source files found.
     * @return number of times to call the LLM
     */
    @Optional
    @Input
    public Property<Integer> getIterations() {
        return iterations;
    }

    /**
     * The target code coverage you want to obtain
     * @return whole number for percentage example 25 for 25% coverage
     */
    @Optional
    @Input
    public Property<Integer> getCoverage() {
        return coverage;
    }

    @TaskAction
    public void performTask() {
        CoverAgentBuilder builder = CoverAgentBuilder.builder();
        builder.project(this.project);
        OpenAiChatModel.OpenAiChatModelBuilder chatModelBuilder = OpenAiChatModel.builder();
        if (apiKey.isPresent()) {
            builder.apiKey(apiKey.get());
            chatModelBuilder.apiKey(apiKey.get());
        }
        if (wanDBApiKey.isPresent()) {
            builder.wanDBApiKey(wanDBApiKey.get());
        }
        if (iterations.isPresent()) {
            builder.iterations(iterations.get());
        }
        if (coverAgentBinaryPath.isPresent()) {
            builder.coverAgentBinaryPath(coverAgentBinaryPath.get());
        }
        if (coverage.isPresent()) {
            builder.coverage(coverage.get());
        }
        builder.openAiChatModelBuilder(chatModelBuilder);
        coverAgent = builder.build();
        coverAgent.init();
        coverAgent.invoke();
    }


}
