# Gradle Cover Agent Plugin

The Gradle Cover Agent Plugin is designed to help you increase code coverage in your Java projects. It integrates with Gradle to provide tasks that can be configured to run code coverage tools and report the results.

## Features

- Configurable API keys for integration with external services.
- Customizable number of iterations and coverage percentage.
- Integration with OpenAI for advanced code analysis.
- Easy setup and configuration through Gradle extensions.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 21 or higher
- Gradle 8.0 or higher

### Installation

To include the plugin in your Gradle project, add the following to your `build.gradle` file:

```groovy
plugins {
    id 'ai.codium.cover.plugin' version '0.0.1'
}
```

### Configuration
You can configure the plugin using the coverAgent extension in your build.gradle file:
```groovy
coverAgent {
    apiKey = 'your-api-key'
    wanDBApiKey = 'your-wandb-api-key'
    iterations = 5
    coverage = 80
    coverAgentBinaryPath = '/path/to/cover-agent-binary'
}
```

### Running the Plugin
To run the plugin, use the following Gradle command:
```bash
./gradlew coverAgentTask
```

### Contributing
We welcome contributions to the Gradle Cover Agent Plugin! Here are some ways you can contribute:

### Reporting Issues
If you find a bug or have a feature request, please create an issue in the GitHub repository.


### Submitting Pull Requests
Fork the repository.
Create a new branch for your feature or bugfix.
Make your changes and commit them with clear and concise messages.
Push your changes to your fork.
Create a pull request against the main branch of the original repository.
Code Style
Please follow the existing code style and conventions used in the project. Ensure that your code passes all tests and does not introduce any new warnings.

### Running Tests
To run the tests locally, use the following command:
```bash
./gradlew clean check
```

### Publishing to Local Repository
To publish the plugin to your local Maven repository for testing, use the following command:
```bash
./gradlew clean publishToMavenLocal
```

### Testing in a Sample Project
Navigate to your test project directory:
```bash
cd test-project
```
Follow the README in the test project to set up your environment and include the locally published plugin.
[Cover Agent Test Project README.md](test-project/README.md)
