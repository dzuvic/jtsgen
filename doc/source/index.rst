.. jtsgen documentation master file, created by
   sphinx-quickstart on Sat Jul 29 20:05:59 2017.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

jtsgen: Java -> TypeScript Emitter
==================================

This project emits TypeScript ambient types from Java sources.
``jtsgen`` is implemented as an annotation processor, therefore it
should be easily integrated in your current build infrastructure.
Usually there are no other plugins required for your build system
(maven, gradle).

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

**Requirements:** The annotation processor only depends on the JDK. Only JDK 8
is currently supported.

This project is still in development, so major changes are still on the
way and might break using it. Therefore either submit an issue on
`github <https://github.com/dzuvic/jtsgen/issues>`__ or a pull request
if you want a specific feature being implemented.

An example is currently in development: jtsgen-examples

.. toctree::
   :maxdepth: 2
   :caption: First Steps:
   
   Usage
   Gradle
   Maven

.. toctree::
   :maxdepth: 2
   :caption: Customizing:

   Customizing
   Customizing-Mapping-DSL
   Customizing-NS-Map
   Customizing-Output


.. toctree::
	:maxdepth: 2
	:caption: Miscellaneous

	License
	Changelog
	Test cases <https://github.com/dzuvic/jtsgen/tree/master/processor/src/test>
	TypeScript <https://www.typescriptlang.org/docs/>


Indices and tables
==================

* :ref:`genindex`
* :ref:`modindex`
* :ref:`search`
