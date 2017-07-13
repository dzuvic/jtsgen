# _jtsgen_: Convert Java Types to TypeScript

**tl;dr** Enable code completion of Java types in your TypeScript project.

Annotations: [ ![Download](https://api.bintray.com/packages/dzuvic/jtsgen/annotations/images/download.svg) ](https://bintray.com/dzuvic/jtsgen/annotations/_latestVersion)
 Processor: [ ![Download](https://api.bintray.com/packages/dzuvic/jtsgen/processor/images/download.svg) ](https://bintray.com/dzuvic/jtsgen/processor/_latestVersion)
 [ ![Download](https://travis-ci.org/dzuvic/jtsgen.svg?branch=master) ](https://travis-ci.org/dzuvic/jtsgen)
 [![Coverage Status](https://codecov.io/github/dzuvic/jtsgen/coverage.svg?branch=master)](https://codecov.io/github/dzuvic/jtsgen?branch=master)

## Features

This project emits TypeScript ambient types from Java sources.
`jtsgen` is implemented as an annotation processor, therefore it should be
easily integrated in your current build infrastructure. Usually there
are no other plugins required for your build system (maven, gradle). 

This project is still in development, so major changes are still on the
way and might break using it. Therefore either submit an issue on
[github](https://github.com/dzuvic/jtsgen/issues) or a pull request if
you want a specific feature being implemented.

Currently the following features are supported:

* Emitting types for `@TypeScript` annotated Java classes and interfaces
* Ignoring a type using `@TSIgnore` annotation
* creating a module with corresponding package.json. The name is constructed
  if not configured
* Configuration of the JavaScript / TypeScript module using the `@TSModule`
  annotation, e.g. the module name or the author of the exported ES module
* Configurable type conversion and exclusion using the `@TSModule`
  annotation. It also supports type parameters, e.g. a `ImmutableList<T>`
  can be be mapped to your own type `ImmutableArray<T>` with a
  corresponding (mapped) type `T`
* Java package as typescript name space, configurable
* converting getter/setter to TypeScript types
* `readonly` if no setter is found
* Name Space mapping to minimize the TypeScript name spaces. It can be
  configured or calculated.
* Inheritance (since 0.2.0)

Requirements: The annotation processor only depends on the JDK. Only JDK 8
is currently supported.

## Usage

The jtsgen annotation processor registers itself using the
[ServiceLoader](http://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html)
protocol. Therefore when the processor is available on the compile or
annotation class path it should be automatically invoked when compiling
the annotated Java classes. Any Java class, interface or enum with the
annotation `@TypeScript` will be converted, e.g.:

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

Hint: Don't use `jtsgen-processor` as a compile time or runtime dependency.
Either get you build system to use the `javac` annotation class path or
excluding it from the transitive dependencies, e.g. using
`compileOnly` in Gradle or `optional` in Maven.

The generated sources are currently beneath the java source output folder.
The output can be redirected using the regular `-s` option of `javac`.

An example is currently in development: [jtsgen-examples](https://github.com/dzuvic/jtsgen-example)

### The TSModule Annotation

Currently only one TSModule annotation is permitted in one compilation unit.
The annotation must be put to a package Element, like this:

```
@TSModule(
        moduleName = "namespace_test",
        nameSpaceMapping = "jts.modules.nsmap -> easy"
)
package jts.modules.nsmap;

import dz.jtsgen.annotations.TSModule;
```


The following annotation parameters are supported:

*  **moduleName**: The module name of the JavaScript/TypeScript Module. This must be a java package friendly name.
*  **author**: The author number for the package.json file
*  **authorUrl**: The authorURL for the package.json file
*  **version**: The version number for the package.json file, default is "1.0.0"
*  **license**: The license for the package.json file
*  **description**: he description for the package.json file
*  **customTypeMappings**: Custom Type Mapping for the module, the default is `{}`
*  **excludes**: regular expression to exclude type conversion, default is: `{"^sun", "^jdk.internal", "^java.lang.Comparable"}`
*  **nameSpaceMapping**: The name space mapping, the default is `{}`
*  **outputType**: The type of the output. Default is `OutputType.NAMESPACE_AMBIENT_TYPE`
*  **nameSpaceMappingStrategy**: Defines how the default name space is calculated. Default is `NameSpaceMappingStrategy.ALL_TO_ROOT` (since 0.2.0)



#### Custom Type Mapping

The annotation processor supports a simple mapping description language.
The custom Type Mapping for the module is  a list of strings, each
describing a type mapping. Each string consists of a Java Type (canonical
name with type params) and the resulting TypeScript Type. Both Types are
separated with an arrow, e.g.:

    java.util.Date -> IDateJSStatic

maps a `java.util.Date` to the TypeScript type `IDateJSStatic`

It also is possible to use type variables, e.g. :

    java.util.List<T> -> Array<T>


will convert any java.util.List or it's subtypes to an Array type in
TypeScript. If the matched java type has a type parameter the converted
type parameter will be inserted accordingly.

The annotation processor has the following conversions for declaration
types configured:


The Processor has no knowledge about the the necessary imports.


#### Name Space Mapping

TSModule accepts a list of name spaces, that should me mapped (shortened).
That list will be prepended to the calculated name space mapping. The
following name spave mapping strategies are available:

* `TOP_LEVEL_TO_ROOT`: The top level java types are mapped to the
  root name space. Everythin beneath is mapped into name spaces
* `ALL_TO_ROOT`: All types are mapped to the root name space, only
  the types of same name reside in their own name space
* `MANUAL`: No name space mapping is calculated


Some examples of :

* `a.b.c -> `: Maps a.b.c (and beneath) to root
* `a.b.c -> a.b`: Maps a.b.c to namespace a
* `=a.b.c -> `: Maps only a.b.c to the root

#### Configuring the output

The type of the output can be configured by the outputType parameter of
the TSModule annotation:

* `NAMESPACE_AMBIENT_TYPE` : exports a module with ambient types
  (d.ts and package.json) with a declared name space
* `NAMESPACE_FILE` : only the ambient types with namespaces
  in a single d.ts file
* `MODULE` : exports a declared module, e.g. using
  `declare module` at the top without ambient types
* `NO_MODULE` : exports a single file containing all converted types
  without any surrounding namespace or module declaration (since 0.2.0)



### Annotation Processor Params

with the following annotation processor parameters some of the settings
made using the `TSModule` annotation can be overridden:

* jtsgenLogLevel: enable additional logging. Use ine of the following
  `j.u.Logging` levels: ,`OFF` ,`SEVERE` ,`WARNING` ,`INFO` ,`CONFIG`
  ,`FINE` ,`FINER` ,`FINEST` ,`ALL`
* jtsgenModuleName: the name of the module, that should be exported
* jtsgenModuleVersion: the version number of the module
* jtsgenModuleDescription: the description of the module
* jtsgenModuleAuthor: the module author
* jtsgenModuleLicense: the npm license string of the module
* jtsgenModuleAuthorUr: the URL of the author

To use one of them, use the javac prefix `-A`, e.g.

    javac -AjtsgenLogLevel=FINEST MyClass.java`



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
    compileOnly "jtsgen:jtsgen-annotations:0.1.4"
    compileOnly "jtsgen:jtsgen-processor:0.1.4"
}
````


## License And Legal Notes

The following licenses apply `jtsgen`:

The **annotations** are **Apache 2.0** licensed. The **other parts** of `jtsgen`,
especially the processor, are **GPLv3** licensed. The license texts are
included in the file `LICENSE`. Because `jtsgen` as a sort of a compiler
plugin you shouldn't redistribute the compiler in your projects. It's
just like using OpenJDK: the generated code is *not* affected by
it's license, so it should be safe using it in most cases. Everything in
this chapter is not a legal advice in any form.

This project has to include the following legal notes:

* Oracle and Java are registered trademarks of Oracle and/or its affiliates.
  Other names may be trademarks of their respective owners. See
  https://www.oracle.com/legal/trademarks.html for details.

