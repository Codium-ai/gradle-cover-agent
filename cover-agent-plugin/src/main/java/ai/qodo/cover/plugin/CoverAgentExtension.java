package ai.qodo.cover.plugin;

import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

public abstract class CoverAgentExtension {
    private final Property<String> apiKey;
    private final Property<String> wanDBApiKey;
    private final Property<Integer> iterations;
    private final Property<Integer> coverage;
    private final Property<String> coverAgentBinaryPath;

    public CoverAgentExtension(Project project) {
        ObjectFactory factory = project.getObjects();
        this.apiKey = factory.property(String.class);
        this.wanDBApiKey = factory.property(String.class);
        this.iterations = factory.property(Integer.class);
        this.coverage = factory.property(Integer.class);
        this.coverAgentBinaryPath = factory.property(String.class);
    }

    public Property<Integer> getIterations() {
        return iterations;
    }

    public Property<String> getApiKey() {
        return apiKey;
    }

    public Property<String> getWanDBApiKey() {
        return wanDBApiKey;
    }

    public Property<Integer> getCoverage() {
        return coverage;
    }

    public Property<String> getCoverAgentBinaryPath() {
        return coverAgentBinaryPath;
    }

}
