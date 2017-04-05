# Java Type Emitter For TypeScript: _jtsgen_

This project is s proof of concept that emits TypeScript ambient types
from Java sources.
`jtsgen` is implemented as an annotation processor, therefore it should be
easily integrated in your current build infrastructure. Usually there
are no other plugins required for your build system (maven, gradle).

This is only a proof of concept and it currently support only some simple
type mappings, therefore either submit an issue on [github](https://github.com/dzuvic/jtsgen/issues)
or a submit pull request.

Currently the following features are supported:

* Emitting types for `@TypeScript` annotated Java classes and interfaces
* Package name as typescript name space
* converting getter/setter to TypeScript types
* common top level package as module name
* `readonly` if no setter is found
* creating a module with corresponding package.json


## Usage

Currently you should not use it. This project is still in development
so no usable artifacts are published, yet.

Either take a look at the tests or the included sample project regarding
feedback getting this project to a usable state.

The output is currently inside the java source output folder in the
package (=directory) jtsgen.

### Processing Options

The processor currently support the following Options:

* `jtsgenLogLevel` The j.u.l. Logging for error reporting and debugging
* `jtsgenModuleName` The name of the default module


## License And Legal Notes

Currently `jtsgen` is GPLv3 licensed. The license text is included in the
 file `LICENSE`. Because `jtsgen` as a sort of a compiler plugin you
shouldn't redistribute the compiler in your projects. Just like when
using OpenJDK, the generated code is *not* affected by it's license,
so it should be safe using it in most cases. Everything in this
chapter is not a legal advice in any form.

This project has to include the following legal notes:

* Oracle and Java are registered trademarks of Oracle and/or its affiliates.
  Other names may be trademarks of their respective owners. See
  https://www.oracle.com/legal/trademarks.html for details.

