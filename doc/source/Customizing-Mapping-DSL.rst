Custom Type Mapping
===================

The annotation processor supports a simple mapping description language. The custom Type Mapping for the module is a
list of strings, each describing a type mapping. Each string consists of a Java Type (canonical name with type params)
and the resulting TypeScript Type. Both Types are separated with an arrow, e.g. :

::

    java.util.Date -> IDateJSStatic

maps a ``java.util.Date`` to the TypeScript type ``IDateJSStatic``

It also is possible to use type variables, e.g. :

::

    java.util.List<T> -> Array<T>

will convert any java.util.List or it's subtypes to an Array type in TypeScript. If the matched java type has a type
parameter the converted type parameter will be inserted accordingly.

Default Conversions
-------------------

The annotation processor has the following conversions for declaration types configured:

    * java.lang.Void -> Void
    * java.lang.Object -> Object
    * java.lang.String -> string
    * java.lang.Character -> string
    * java.lang.Number \|-> number
    * java.lang.Boolean -> boolean
    * java.util.Collection<T> -> Array<T>
    * java.util.Map<U,V> -> Map<U,V>

The Processor has no knowledge about the the necessary imports.

Mapping-DSL
-----------

The Mapping DSL defined in ANTLR BNF variant:

::

    mapping : origin  whsp* arrow whsp* target;
    arrow : '->' | '|->'
    origin :  jident  ( '.' , jident )*   tsAngleType?
    jident :  ('a'-'z' | 'A' - 'Z' | '_' )  ('a'-'z' | 'A' - 'Z' | '_'  |  '0' - '9')*
    tsChar :  * all chars expecpt '<' | '>' | '`' *
    target :  ( jident , '.' )*  tstypes
    tsLit :  tsChar*
    tsAngleType : '<'  jident  ( ',' , jident )* '>'
    tsLitType : '`'  jident  '`'
    tsTypes : tstype | ( tstype whtsp )*
    tsType :   tsLit | tsangletype | tslittype | whtsp