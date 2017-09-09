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

Because in TypeScript the types ``Array`` and ``Map`` differ from ``[]`` or ``{}`` jtsgen is able to embed the type in
a literal way. After the arrow the type variables can be expresed using the back tick character, e.g:

::

    java.util.List<T> -> `T`[]


**Limits**: There are some constraints using those expressions: it is not possible to express the name spaces at the
right hand side in a proper way. jtsgen adds a namespace to the java declaration types. Currently acessing this name
space is out of scope.


Default Conversions
-------------------

The following mappings can not be configured, for now:
    * The numerical primitive types are mapped to ``number``
    * The primitive boolean is mapped to ``boolean``
    * An array is mapped to \`T\`[]

The annotation processor has the following mapping for declaration types configured:

    * java.lang.Void -> Void
    * java.lang.Object -> Object
    * java.lang.String -> string
    * java.lang.Character -> string
    * java.lang.Number \|-> number
    * java.lang.Boolean -> boolean
    * java.util.Collection<T> -> `T`[]
    * java.util.Map<U,V> -> { [key: \`U\`]: \`V\`; }

The Processor has no knowledge about the the necessary imports.

Mapping-DSL
-----------

The Mapping DSL defined in ANTLR BNF variant:

::

    mapping : origin  whsp* arrow whsp* target;
    arrow : '->' | '|->'
    origin :  jident  ( '.' , jident )*   tsAngleType?
    target :  ( jident  '.' )*  tstypes+
    tsLit :  tsChar*
    tsAngleType : '<'  jident  ( ','  jident )* '>'
    tsLitType : '`'  jident  '`'
    tsTypes : tstype | ( tstype whtsp )*
    tsType :   tsLit | tsangletype | tslittype | whtsp

    jident :  ('a'-'z' | 'A' - 'Z' | '_' )  ('a'-'z' | 'A' - 'Z' | '_'  |  '0' - '9')*
    tsChar :  * all chars expecpt '<' | '>' | '`' *
