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

