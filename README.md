# Extended Time Calculator #

Extended Time Calculator is small tool written in JavaFX with a sqlite3 DB that is useful for timing exams for students with disabilities. 

These students often require extended time, and it can become difficult to manage multiple exams simultaneously when they all require different amounts of extended time. The tool calculates end times, and times the exams for you.

It also allows users to add accommodations, and view statistics regarding past exams.


### Pre-requisites for build

* WiX toolset
* Latest JDK
* Gradle
* Windows box for compilation

### Installation ###

```
gradle buildInstaller
```

This will output a msi installer in the extended-time-calculator directory
