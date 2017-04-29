# _jtsgen_: Convert Java Types to TypeScript

**tl;dr** Enable code completion of Java types in your TypeScript project.

This project emits TypeScript ambient types from Java sources.
`jtsgen` is implemented as an annotation processor, therefore it should be
easily integrated in your current build infrastructure. Usually there
are no other plugins required for your build system (maven, gradle).

This is only a proof of concept and it currently support only some simple
type mappings, therefore either submit an issue on [github](https://github.com/dzuvic/jtsgen/issues)
or a pull request if you want a specific feature being implemented.

Currently the following features are supported:

* Emitting types for `@TypeScript` annotated Java classes and interfaces
* Ignoring a type using `@TSIgnore` annotation
* creating a module with corresponding package.json. The name is constructed
  if not configured
* Configuration of the JavaScript / TypeScript Module using the `@TSModule`
  annotation, e.g. the module name or the author of the exported ES module
* simple custom type conversion, e.g. java.util.Date to string can configured
  at the TSModule annotation. All custom annotation are in one place.
* Java package as typescript name space
* converting getter/setter to TypeScript types
* `readonly` if no setter is found


Requirements: The annotation processor only depends on the JDK. Only JDK 8
is supported.

## Usage

This project is still in development, so major changes are still on the
way and might break using it.


The jtsgen annotation processor registers itself using the
[ServiceLoader](http://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html)
protocol. Therefore when the processor is available on the compile or
annotation class path it should be automatically invoked when compiling
the annotated Java classes.

Hint: Don't use `jtsgen-processor` as a compile time or runtime dependency.
Either get you build system to use the `javac` annotation class path or
excluding it from the transitive dependencies, e.g. using
`compileOnly` in Gradle or `optional` in Maven.

The generated sources are currently beneath the java source output folder.
The output can be redirected using the regular `-s` option of `javac`.

An example is currently in development: [jtsgen-examples](https://github.com/dzuvic/jtsgen-example)

#### Simple Example

Annotate the class or the interface with a `@TypeScript` annotation like this:

````
@TypeScript
public interface InterFaceSample {
    int getSomeInt();
    String getSomeString();
}
````

When compiling this class a complete ES module including a valid `package.json`
is generated in the source output folder for a later deployment into
a npm compatible repository. Although this feature is not tested in any way.

### Using in gradle projects

Adding the following snippet to your gradle (sub-) project, the annotation
processor should run at automatically at compile time:

````
repositories {
    maven {
        url "http://dl.bintray.com/dzuvic/jtsgen"
    }
}

dependencies {
    compileOnly "jtsgen:jtsgen-annotations:0.0.2"
    compileOnly "jtsgen:jtsgen-processor:0.0.2"
}
````

### Processing Options

The processor currently support the following Options:

* `jtsgenLogLevel` The j.u.l. Logging for error reporting and debugging
* `jtsgenModuleName` The name of the default module


## License And Legal Notes

The following licenses apply `jtsgen`:

The annotations are Apache 2.0 licensed. The rest of `jtsgen`,
especially the processor , is GPLv3 licensed. The license texts are
included in the file `LICENSE`. Because `jtsgen` as a sort of a compiler
plugin you shouldn't redistribute the compiler in your projects. It's
just like using OpenJDK: the generated code is *not* affected by
it's license, so it should be safe using it in most cases. Everything in
this chapter is not a legal advice in any form.

This project has to include the following legal notes:

* Oracle and Java are registered trademarks of Oracle and/or its affiliates.
  Other names may be trademarks of their respective owners. See
  https://www.oracle.com/legal/trademarks.html for details.

