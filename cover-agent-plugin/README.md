
# Steps to validate your changes and run locally the plugin
- Complete code change
```bash
  ### build and run all acceptance criteria
  ./gradlew clean check
```
```bash
  ### publish to your local repository to use in test project
  ./gradlew clean publishToMavenLocal
```
```bash
  ###
  cd ../test-project
  ## follow the read me to set up your environment
```
