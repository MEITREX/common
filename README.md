# gits-common

This repository contains utility classes for GITS.

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
implementation('de.unistuttgart.iste.gits:gits-common') {
    version {
        branch = 'main'
    }
}
```

When this repository has changed, you can update the version in your project by running the following command:

```bash
./gradlew build --refresh-dependencies
```

In IntelliJ, you need to reload the Gradle project by clicking on the refresh button in the Gradle tab.
