Using in Gradle projects
========================

Adding the following snippet to your gradle (sub-) project, the annotation processor should run at automatically at
compile time. The following simple example should extract the TypeScript output to the gradle build dir::

    repositories {
        maven {
            url "http://dl.bintray.com/dzuvic/jtsgen"
        }
    }

    dependencies {
        compileOnly "jtsgen:jtsgen-annotations:0.2.0"
        compileOnly "jtsgen:jtsgen-processor:0.2.0"
    }


Since 0.3.0 ``jtsgen`` has been distributed on maven central, so no other repository have to be defined. The brave ones
could try the current SNAPSHOT version using the following dependency::

    dependencies {
        compileOnly "jtsgen:jtsgen-annotations:0.3.0-SNAPSHOT"
        compileOnly "jtsgen:jtsgen-processor:0.3.0-SNAPSHOT"
    }


Customizing the output dir
--------------------------

The output is customized by adding the source directory to the annotation processor::

    def tsOutDir="${buildDir}/ts"

    compileJava {
        options.compilerArgs = [ "-s", tsOutDir ]
        dependsOn(createTsDir)
    }


Generating Types for Kotlin classes
-----------------------------------

ToDo