============
Introduction
============

The project ``jtsgen`` converts Java types as TypeScript types at compile time using an
annotation processor. The main use case of this project is getting code completion in
the typescript frontend project for types that are defined in the JVM backend project.
These backend types are usually written in Java (or Kotlin).

For example: the following class defines data type person, that represents the interface between
the TypeScipt based browser front end, e.g. angular or react, and a JAX-RS endpoint:

.. code-block:: java

    public class Person {

        private String name;
        private LocalDate birthdate;
        private Sex sex;

        public Person(String name, LocalDate birthdate, Sex sex) {
            this.name = name;
            this.birthdate = birthdate;
            this.sex = sex;
        }

        public String getName() { return name; }

        public void setName(String name) { this.name = name; }

        public LocalDate getBirthdate() { return birthdate; }

       //...
  }


The data might be transmitted and accessed using JSON, or whatever marshalling technology you like, but
in TypeScript you have to write something like the following code for being able using code completion
on that type. Either you write something like the follwing or just use this project, which generates the
following data type:

.. code-block:: typescript

  export enum Sex {
    M, F
  }

  export interface Person {
    birthdate: string;
    sex: Sex;
    name: string;
  }



It is important to know, how the marshalling maps type like BigDecimal to JSON. For that the convesion of
types could be easily configured by a DSL. And because this information how the Java types are mapped
to JSON are configured somewhere in the backend, ``jtsgen`` can also be configured at compile time, so the
configuration of ``jtsgen`` (package annotation) can be kept near the source the code that configured the
Java to JSON mapper, e.g. Jackons's ``ObjectMapper``.


Features
--------

Currently the following features are supported:

-  Emitting types for ``@TypeScript`` annotated Java classes and
   interfaces
-  Ignoring a type using ``@TSIgnore`` annotation. Supporting
   ``readonly`` using ``@TSReadOnly`` annotation
-  creating a module with corresponding package.json is supported. The name is
   constructed if not configured
-  Configuration of the JavaScript / TypeScript module using the
   ``@TSModule`` annotation, e.g. the module name or the author of the
   exported ES module
-  Configurable type conversion and exclusion using the ``@TSModule``
   annotation. It also supports type parameters, e.g. a
   ``ImmutableList<T>`` can be be mapped to your own type
   ``ImmutableArray<T>`` with a corresponding (mapped) type ``T``
-  Java package as typescript name space, configurable
-  converting getter/setter to TypeScript types
-  Name Space mapping to minimize the TypeScript name spaces. It can be
   configured or calculated.
-  Inheritance (since 0.2.0)
-  Type variables without angle brackets in the generated TypeScript types, e.g. ``T[]``
-  Generics: type variables, type bounds and mapping to TypeScript types are supported

**Requirements:** The annotation processor only depends on the JDK. Only JDK 8
is currently supported. It *should* run on JDK 9.

This project is still in development, so major changes might occur and
break when used, although it's used internally. Therefore either submit an issue on
`github <https://github.com/dzuvic/jtsgen/issues>`__ or a pull request
if you want a specific feature being implemented.

An example project ist also available on github: `jtsgen-examples <https://github.com/dzuvic/jtsgen-example>`_


Limits
------

As all annotation processors, only the types are transformed, no methods, or anything executable. For this you could
try something like `TeaVM <http://teavm.org>`_ or `Kotlin JS <https://kotlinlang.org/docs/reference/js-overview.html>`_.

Another limitation regarding name spaces exists only temporarily until some major refactoring has been done. Currently
the name space mapping is only available an java types, but not for type bounds. A type bound is converted without any
name space. Usually that should be fine, unless you're planning extracting everything without any name space mapping.