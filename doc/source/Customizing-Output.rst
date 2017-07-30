Output: TypeScript Modules
==========================

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
