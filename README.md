# meitrex-common

This repository contains utility classes for MEITREX.

## How to add this repository to your project

In the `settings.gradle` file, add the following line:

```groovy
sourceControl {
    gitRepository(uri('https://github.com/IT-REX-Platform/gits-common')) {
        producesModule('de.unistuttgart.iste.gits:gits-common')
    }
}
```

In the `build.gradle` file, add the following dependency:

```groovy
implementation "de.unistuttgart.iste.gits:gits-common:{version}"
```
Replace `{version}` with the version you want to use. 
You can find the versions in the tags of this repository.

When this repository has changed, you can update the version in your project by running the following command:

```bash
./gradlew build --refresh-dependencies
```

In IntelliJ, you need to reload the Gradle project by clicking on the refresh button in the Gradle tab.

## Creating a new version

When you want to make changes in this repository and use them in the other services, you need to create a new version.
This is done by creating a new tag in this repository.

```bash
git tag 1.0.0 # Replace 1.0.0 with the new version
git push --tags
```

The tag should be the previous version + 0.0.1 for minor changes, + 0.1.0 for breaking changes and + 1.0.0 for major changes.

