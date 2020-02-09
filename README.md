# Angular Gradle Plugin

This plugin allows you to integrate Angular frontends into your Gradle build.

## Full Example

See the [Angular Gradle Demo project](https://github.com/Clashsoft/Angular-Gradle-Demo) for detailed instructions on how to set up Angular with Gradle.

## Installation

Add to the top of `build.gradle`:

```groovy
plugins {
    // ...
    id 'de.clashsoft.angular-gradle' version '0.1.2'
}
```

## Configuration

[Project Properties](https://docs.gradle.org/current/userguide/build_environment.html#sec:project_properties):

```properties
no-angular  # indicates Angular should not be included in the build. Useful for testing frontend and backend separately, e.g. with `ng serve`.
angular-dev # indicates that Angular should be built with `ng build --configuration=gradle` instead of `ng build --prod`.
```

In `build.gradle` (default values shown after `=`):

```groovy
angular {
    appDir = 'src/main/app' // root directory of your Angular app
    outputDir = "$appDir/dist/$project.name" // output directory of Angular build. Default assumes Angular project has the same name as the gradle project.
    packageManager = /* result of `ng config cli.packageManager`, plus .cmd on Windows) */'' // for installing packages prior to Angular build
    packageManagerArgs = [ 'install' ] // arguments for installing packages, passed to package manager
    buildArgs = [ 'build', hasProperty('angular-dev') ? '--configuration=gradle' : '--prod' ]
}
```
