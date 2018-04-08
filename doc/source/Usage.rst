Using jtsgen
============

The jtsgen annotation processor registers itself using the
`ServiceLoader <http://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html>`__
protocol. Therefore when the processor is available on the compile or
annotation class path it should be automatically invoked when compiling
the annotated Java classes. Any Java class, interface or enum with the
annotation ``@TypeScript`` will be converted, e.g.:

::

    @TypeScript
    public interface InterFaceSample {
        int getSomeInt();
        String getSomeString();
    }

When compiling this class a complete ES module including a valid
``package.json`` is generated in the source output folder for a later
deployment into a npm compatible repository.

Hint: Don't use the ``jtsgen-processor`` as a compile time or runtime
dependency. Either get you build system to use the ``javac`` annotation
class path or excluding it from the transitive dependencies, e.g. using
``compileOnly`` in Gradle or ``optional`` in Maven.

The generated sources are currently beneath the java source output folder.
The output can be redirected using the regular ``-s`` option of javac.

.. _processing-parameters:

Processing Parameters
---------------------


The *tsgen* annotation processor supports with the following parameters:

* jtsgenLogLevel: enable additional logging. Use ine of the following
  ``j.u.Logging`` levels: ``OFF`` , ``SEVERE`` , ``WARNING`` , ``INFO`` , ``CONFIG``
  , ``FINE`` , ``FINER`` , ``FINEST`` , ``ALL``
* jtsgenModuleName: the name of the module, that should be exported
* jtsgenModuleVersion: the version number of the module
* jtsgenModuleDescription: the description of the module
* jtsgenModuleAuthor: the module author
* jtsgenModuleLicense: the npm license string of the module
* jtsgenModuleAuthorUr: the URL of the author

To use one of them, use the javac prefix ``-A``, e.g. ::

    javac -AjtsgenLogLevel=FINEST MyClass.java

All these settings do override everything set via an annotation, e.g. ``TSModule``.
