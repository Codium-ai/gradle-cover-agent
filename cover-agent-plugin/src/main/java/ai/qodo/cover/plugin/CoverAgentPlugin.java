package ai.qodo.cover.plugin;


import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class CoverAgentPlugin implements Plugin<Project> {
    static final Integer DEFAULT_ITERATIONS = Integer.valueOf(1);
    static final Integer DEFAULT_PERCENTAGE = Integer.valueOf(75);

    @Override
    public void apply(Project project) {
        project.getLogger().info("Running plugin version {}", "0.0.1");
        project.getExtensions().create("coverAgent", CoverAgentExtension.class, project);
        project.getTasks().register("coverAgentTask", CoverAgentTask
                .class, task -> {
            task.setGroup("verification");
            task.setDescription("Runs the cover agent task attempting to increase code coverage");
            CoverAgentExtension extension = project.getExtensions().findByType(CoverAgentExtension.class);
            if (extension != null) {
                task.getApiKey().set(extension.getApiKey());
                task.getWanDBApiKey().set(extension.getWanDBApiKey());
                task.getCoverAgentBinaryPath().set(extension.getCoverAgentBinaryPath());
                if (extension.getIterations().isPresent()) {
                    task.getIterations().set(extension.getIterations());
                } else {
                    task.getIterations().set(DEFAULT_ITERATIONS);
                }
                if (extension.getCoverage().isPresent()) {
                    task.getCoverage().set(extension.getCoverage());
                } else {
                    task.getCoverage().set(DEFAULT_PERCENTAGE);
                }

            }
            task.getOutputs().upToDateWhen(t -> false);
        });
    }
}
