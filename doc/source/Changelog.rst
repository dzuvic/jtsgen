==========
Change Log
==========

The changelog is sorted descending by release date and contains at least one chapter about:

- *New features*
- *Breaking Changes*
- *Deprecated Features*, which will be removed in the next months
- *Removed Features*
- *Fixed*


jtsgen-0.5.0 (UNRELEASED)
-------------------------

New Features
~~~~~~~~~~~~

- The documentation has been expanded with a section about the contributors, building
  and extending ``tsgen``
- Tests for JDK 11 have been added
- new options for generating enum


Planned Features
~~~~~~~~~~~~~~~~

- support for converting the documentation


`jtsgen-0.4.0 <https://github.com/dzuvic/jtsgen/tree/jtsgen-0.4.0>`__ (2018-05-06)
----------------------------------------------------------------------------------

New Features
~~~~~~~~~~~~

- Added documentation for Maven and Kotlin projects
- Added Support for type bounds on classes and interfaces
- Added support converting types without an annotation
- Added support for non standard properties
- Added support for different name mapping strategies

Breaking Changes
~~~~~~~~~~~~~~~~

Type bounds of classes will be converted, but i don't expect that this shouldn't break any existing code.


Tickets
~~~~~~~

**Implemented enhancements:**

-  publish snapshot `#48 <https://github.com/dzuvic/jtsgen/issues/48>`__
-  Kotlin Boolean doesn't show up
   `#42 <https://github.com/dzuvic/jtsgen/issues/42>`__
-  Bug: Generics in Ouput are missing
   `#33 <https://github.com/dzuvic/jtsgen/issues/33>`__

**Fixed bugs:**

-  Generating bogus Java types due to not aborting the converter
   recursion `#46 <https://github.com/dzuvic/jtsgen/issues/46>`__
-  Bug: wrong dependency in Processor
   `#40 <https://github.com/dzuvic/jtsgen/issues/40>`__

**Closed issues:**

-  Support generating Ouptput without an annotation
   `#41 <https://github.com/dzuvic/jtsgen/issues/41>`__
-  Add documentaion for maven
   `#38 <https://github.com/dzuvic/jtsgen/issues/38>`__
-  Support for converting external types
   `#36 <https://github.com/dzuvic/jtsgen/issues/36>`__
-  rename project packages prior deployment on maven central
   `#12 <https://github.com/dzuvic/jtsgen/issues/12>`__

**Merged pull requests:**

-  Feature/46 early dsl
   `#51 <https://github.com/dzuvic/jtsgen/pull/51>`__
   (`dzuvic <https://github.com/dzuvic>`__)
-  Feature/48 publish snapshot
   `#49 <https://github.com/dzuvic/jtsgen/pull/49>`__
   (`dzuvic <https://github.com/dzuvic>`__)
-  Feature/33 generics
   `#47 <https://github.com/dzuvic/jtsgen/pull/47>`__
   (`dzuvic <https://github.com/dzuvic>`__)


`jtsgen-0.3.0 <https://github.com/dzuvic/jtsgen/tree/jtsgen-0.3.0>`__ (2017-10-30)
----------------------------------------------------------------------------------

New Features
~~~~~~~~~~~~

- ``@TSReadOnly`` annotated members are exported as ``readonly``
- support for literal mapping of types, e.g. ``Array<T>`` can be mapped to ``T[]``
- migrated documentation from markdown to sphinx
- migrated from bintray to maven central

Breaking Changes
~~~~~~~~~~~~~~~~

-  The default mechanism that generates ``readonly`` when only getters
   are visible has been removed. Use the ``@TSReadonly`` annotation to
   generate readonly members
-  The default conversion of collections and maps are changed to ``T[]`` and ``{ index: K: V; }``
-  the artifact are distributed on maven central. Removing the custom repository used till 0.2.x releases should be
   sufficient

Tickets
~~~~~~~

**Implemented enhancements:**

-  Bug: Java Bean Protocol not complete
   `#32 <https://github.com/dzuvic/jtsgen/issues/32>`__
-  Map Collection<T> to T[] instead of List<T>
   `#28 <https://github.com/dzuvic/jtsgen/issues/28>`__
-  Capability generate User-Defined Type Guards (enable basic TypeCheck
   at Runtime) `#27 <https://github.com/dzuvic/jtsgen/issues/27>`__

**Fixed bugs:**

-  NPE when Type Mapping is not available
   `#34 <https://github.com/dzuvic/jtsgen/issues/34>`__

**Closed issues:**

-  Make it available in maven central
   `#37 <https://github.com/dzuvic/jtsgen/issues/37>`__
-  iInheritance: Only add member, when not in super types
   `#30 <https://github.com/dzuvic/jtsgen/issues/30>`__
-  TSOption or TSReadOnly Annotation
   `#17 <https://github.com/dzuvic/jtsgen/issues/17>`__

**Merged pull requests:**

-  [add] support for boolean class attributes #33
   `#35 <https://github.com/dzuvic/jtsgen/pull/35>`__
   (`fvonberg <https://github.com/fvonberg>`__)



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
