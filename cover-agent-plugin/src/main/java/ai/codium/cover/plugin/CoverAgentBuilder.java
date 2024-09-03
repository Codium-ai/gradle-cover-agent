package ai.codium.cover.plugin;

import org.gradle.api.Project;

import java.util.Optional;

public class CoverAgentBuilder {
    private String apiKey;
    private String wanDBApiKey;
    private int iterations;
    private int coverage;
    private String coverAgentBinaryPath;
    private ModelPrompter modelPrompter;
    private Optional<String> javaClassPath = Optional.empty();
    private Optional<String> javaTestClassPath = Optional.empty();
    private String projectPath;
    private Optional<String> javaClassDir = Optional.empty();
    private String buildDirectory;
    private CoverAgentExecutor coverAgentExecutor;
    private Project project;

    public static CoverAgentBuilder builder() {
        return new CoverAgentBuilder();
    }

    public CoverAgentBuilder apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public CoverAgentBuilder wanDBApiKey(String wanDBApiKey) {
        this.wanDBApiKey = wanDBApiKey;
        return this;
    }

    public CoverAgentBuilder iterations(int iterations) {
        this.iterations = iterations;
        return this;
    }

    public CoverAgentBuilder coverage(int coverage) {
        this.coverage = coverage;
        return this;
    }

    public CoverAgentBuilder coverAgentBinaryPath(String coverAgentBinaryPath) {
        this.coverAgentBinaryPath = coverAgentBinaryPath;
        return this;
    }

    public CoverAgentBuilder modelPrompter(ModelPrompter modelPrompter) {
        this.modelPrompter = modelPrompter;
        return this;
    }

    public CoverAgentBuilder javaClassPath(Optional<String> javaClassPath) {
        this.javaClassPath = javaClassPath;
        return this;
    }

    public CoverAgentBuilder javaTestClassPath(Optional<String> javaTestClassPath) {
        this.javaTestClassPath = javaTestClassPath;
        return this;
    }

    public CoverAgentBuilder projectPath(String projectPath) {
        this.projectPath = projectPath;
        return this;
    }

    public CoverAgentBuilder javaClassDir(Optional<String> javaClassDir) {
        this.javaClassDir = javaClassDir;
        return this;
    }

    public CoverAgentBuilder buildDirectory(String buildDirectory) {
        this.buildDirectory = buildDirectory;
        return this;
    }

    public CoverAgentBuilder coverAgentExecutor(CoverAgentExecutor coverAgentExecutor) {
        this.coverAgentExecutor = coverAgentExecutor;
        return this;
    }

    public CoverAgentBuilder project(Project project) {
        this.project = project;
        return this;
    }

    public CoverAgent build() {
        return new CoverAgent(this);
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getWanDBApiKey() {
        return wanDBApiKey;
    }

    public int getIterations() {
        return iterations;
    }

    public int getCoverage() {
        return coverage;
    }

    public String getCoverAgentBinaryPath() {
        return coverAgentBinaryPath;
    }

    public ModelPrompter getModelPrompter() {
        return modelPrompter;
    }

    public Optional<String> getJavaClassPath() {
        return javaClassPath;
    }

    public Optional<String> getJavaTestClassPath() {
        return javaTestClassPath;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public Optional<String> getJavaClassDir() {
        return javaClassDir;
    }

    public String getBuildDirectory() {
        return buildDirectory;
    }

    public CoverAgentExecutor getCoverAgentExecutor() {
        return coverAgentExecutor;
    }

    public Project getProject() {
        return project;
    }
}
