Using in Gradle projects
========================

Adding the following snippet to your gradle (sub-) project, the annotation processor should run at automatically at
compile time. Since 0.3.0 ``jtsgen`` has been distributed on maven central, so no other repository have to be defined.
The following simple example should extract the TypeScript output to the gradle build dir::

    dependencies {
        compileOnly "com.github.dzuvic:jtsgen-annotations:0.3.0"
        compileOnly "com.github.dzuvic:jtsgen-processor:0.3.0"
    }



The brave ones could try the current SNAPSHOT version using the following dependency::

    dependencies {
        compileOnly "com.github.dzuvic:jtsgen-annotations:0.3.1-SNAPSHOT"
        compileOnly "com.github.dzuvic:jtsgen-processor:0.3.1-SNAPSHOT"
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