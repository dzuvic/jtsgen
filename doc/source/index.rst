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


.. toctree::
   :maxdepth: 2
   :caption: First Steps:

   Introduction
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
