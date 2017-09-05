Change Log
==========

jtsgen-0.3.0 (UNRELEASED)
-------------------------

New Features
~~~~~~~~~~~~

- ``@TSReadOnly`` annotated members are exported as ``readonly``
- support for literal mapping of types, e.g. ``Array<T>`` can be mapped to ``T[]``
- migrated documentation from markdown to sphinx

Breaking Changes
~~~~~~~~~~~~~~~~

-  The default mechanism that generates ``readonly`` when only getters
   are visible has been removed. Use the ``@TSReadonly`` annotation to
   generate readonly members
-  The default conversion of collections and maps are changed to ``T[]`` and ``{ index: K: V; }``

`jtsgen-0.2.0 <https://github.com/dzuvic/jtsgen/tree/jtsgen-0.2.0>`__ (2017-07-14)
----------------------------------------------------------------------------------

New Features
~~~~~~~~~~~~

-  Support for inheritance added
-  Selectable name space mapping strategy
-  Output file without any module or name space declaration

Breaking Change
~~~~~~~~~~~~~~~

-  The default name space mapping changes to "ALL\_ROOT"
-  Defining a name space mapping doe not replace the calculated any more
-  Renamed the OutputType enum members

Tickets
~~~~~~~

**Implemented enhancements:**

-  change default name space mapping in order to avoid name spaces at
   all `#26 <https://github.com/dzuvic/jtsgen/issues/26>`__
-  Missing "NO\_MODULE" OutputType
   `#25 <https://github.com/dzuvic/jtsgen/issues/25>`__
-  Please support inheritance
   `#23 <https://github.com/dzuvic/jtsgen/issues/23>`__
-  support exporting for direct usage
   `#15 <https://github.com/dzuvic/jtsgen/issues/15>`__
-  Support for no name space mapping
   `#29 <https://github.com/dzuvic/jtsgen/issues/29>`__

`jtsgen-0.1.4 <https://github.com/dzuvic/jtsgen/tree/jtsgen-0.1.4>`__ (2017-05-31)
----------------------------------------------------------------------------------

`Full
Changelog <https://github.com/dzuvic/jtsgen/compare/jtsgen-0.1.3...jtsgen-0.1.4>`__

**Implemented enhancements:**

-  support exporting for direct usage
   `#15 <https://github.com/dzuvic/jtsgen/issues/15>`__

`jtsgen-0.1.3 <https://github.com/dzuvic/jtsgen/tree/jtsgen-0.1.3>`__ (2017-05-27)
----------------------------------------------------------------------------------

`Full
Changelog <https://github.com/dzuvic/jtsgen/compare/jtsgen-0.1.2...jtsgen-0.1.3>`__

**Implemented enhancements:**

-  export java.lang.Object to Object instead of any
   `#21 <https://github.com/dzuvic/jtsgen/issues/21>`__

**Fixed bugs:**

-  enum not used, but converted, when namespac mapping removes the
   package `#19 <https://github.com/dzuvic/jtsgen/issues/19>`__
-  java.lang.Number -> number is not conveted
   `#18 <https://github.com/dzuvic/jtsgen/issues/18>`__
-  remove jtsgen directory in the output. only use the modulename as
   directory `#14 <https://github.com/dzuvic/jtsgen/issues/14>`__

**Closed issues:**

-  enable coverage using jacoco
   `#22 <https://github.com/dzuvic/jtsgen/issues/22>`__

`jtsgen-0.1.2 <https://github.com/dzuvic/jtsgen/tree/jtsgen-0.1.2>`__ (2017-05-15)
----------------------------------------------------------------------------------

`Full
Changelog <https://github.com/dzuvic/jtsgen/compare/jtsgen-0.1.1...jtsgen-0.1.2>`__

**Implemented enhancements:**

-  support exporting only the d.ts file
   `#16 <https://github.com/dzuvic/jtsgen/issues/16>`__

**Fixed bugs:**

-  compile time dependendcy to guava
   `#13 <https://github.com/dzuvic/jtsgen/issues/13>`__

`jtsgen-0.1.1 <https://github.com/dzuvic/jtsgen/tree/jtsgen-0.1.1>`__ (2017-05-13)
----------------------------------------------------------------------------------

`Full
Changelog <https://github.com/dzuvic/jtsgen/compare/jtsgen-0.1.0...jtsgen-0.1.1>`__

**Implemented enhancements:**

-  name space mapping
   `#10 <https://github.com/dzuvic/jtsgen/issues/10>`__

`jtsgen-0.1.0 <https://github.com/dzuvic/jtsgen/tree/jtsgen-0.1.0>`__ (2017-05-10)
----------------------------------------------------------------------------------

`Full
Changelog <https://github.com/dzuvic/jtsgen/compare/jtsgen-0.0.2...jtsgen-0.1.0>`__

**Implemented enhancements:**

-  recursive type conversion of embedded types
   `#11 <https://github.com/dzuvic/jtsgen/issues/11>`__
-  Support for Generics and nesting Types
   `#8 <https://github.com/dzuvic/jtsgen/issues/8>`__
-  Support for Enums `#6 <https://github.com/dzuvic/jtsgen/issues/6>`__

**Fixed bugs:**

-  name space generation missing last character
   `#9 <https://github.com/dzuvic/jtsgen/issues/9>`__

`jtsgen-0.0.2 <https://github.com/dzuvic/jtsgen/tree/jtsgen-0.0.2>`__ (2017-04-26)
----------------------------------------------------------------------------------

**Implemented enhancements:**

-  support for visibility of types and class attributes
   `#5 <https://github.com/dzuvic/jtsgen/issues/5>`__
-  Add support for ignoring part of the Java Type
   `#3 <https://github.com/dzuvic/jtsgen/issues/3>`__
-  User defined conversions
   `#2 <https://github.com/dzuvic/jtsgen/issues/2>`__

**Closed issues:**

-  publish jtsgen to a public repo
   `#1 <https://github.com/dzuvic/jtsgen/issues/1>`__

\* *This Change Log was automatically generated by
`github\_changelog\_generator <https://github.com/skywinder/Github-Changelog-Generator>`__*
