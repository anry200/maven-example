# maven-example
Used to show how library can be published to maven.

# How to publish your android lib to MavenLocal

It could be used for testing/debug. You may want to check your library before push it to jcenter.

mavenLocal - is maven repository points to cached artifacts on local PC
Its location for Ubuntu: "~./m2/repository"


## 1. Library setup
Check project https://github.com/dcendents/android-maven-gradle-plugin

### 1.1 Chose module which will be the library.

### 1.2 Minimal library module setup is:

**library_module_name/build.gradle**
```

classpath 'com.github.dcendents:android-maven-gradle-plugin:1.5'

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath 'com.github.dcendents:android-maven-gradle-plugin:1.5'
    }
}

apply plugin: 'com.android.library'
apply plugin: 'com.github.dcendents.android-maven'

...


//Maven plugin settings
group = 'com.github.anry100.lib.local'
version = '1.0.0'
```

### 1.3 Publish your library to localMaven repository

``` 
run "./gradlew install"
```

## 2. Main project setup

### 2.1 update project/build.gradle file to be used mavenLocal()

```
allprojects {
  repositories {
    jcenter()
    mavenLocal()
  }
}
```

### 2.2 update project/app/build.gradle

```
apply plugin: 'com.android.application'

...

dependencies {
  compile 'com.android.support:appcompat-v7:25.0.0'
  //compile project(':local') //use library as module
  compile 'com.github.anry200.lib:local:1.0.0@aar' //use as library from maven
}
```



# Publish library to internal Nexus.
**Nexus** - is a server for maven projects, it can be used on a company server.
http://www.sonatype.org/nexus/
Alternatives http://binary-repositories-comparison.github.io/

## 1. Install Nexus
Let's say it was installed and configures as docker container. With next settings

```
url: http://nexus.anry200.github.com/
repository: android_lib
user: android_lib_nexus
password: secure123
```

https://github.com/sonatype/docker-nexus3


## 2. update library module to publish
To set up library was used this awesome project https://github.com/bmuschko/gradle-nexus-plugin

**project/nexus/build.gradle**
```
buildscript {
  repositories {
    jcenter()
  }

  dependencies {
    classpath 'com.bmuschko:gradle-nexus-plugin:2.3.1'
  }
}

apply plugin: 'com.android.library'
apply plugin: 'com.bmuschko.nexus'

...


//Maven library settings
group = 'com.github.anry200.lib'
version = '1.0.0'

extraArchive {
  sources = false
  tests = false
  javadoc = false
}

nexus {
  sign = false
  repositoryUrl = 'http://nexus.anry200.github.com/repository/android_lib/'
  snapshotRepositoryUrl = 'http://nexus.anry200.github.com/repository/android_lib/'
}
```

add to **~/.gradle/gradle.properties**
```
nexusUsername=android_lib_nexus
nexusPassword=secure123
```

## 3. publish your library to Nexus

./gradlew module_name:uploadArchives

##3. Main project setup

project/build.gradle

allprojects {
  repositories {
    jcenter()
    mavenLocal()
    maven { url "http://nexus.anry200.github.com/repository/android_lib/"} //used for internal libraries.
  }
}
