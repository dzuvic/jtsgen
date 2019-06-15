=================
Build and Develop
=================


This chapter describes the internal structures of *tsgen* for
development purpose. Also the build / release process and how
to contribute to *tsgen*.


Build & Release
===============



Build *tsgen*
-------------

To build *tsgen* you only need a JDK 8+ compatible environment on your machine, e.g.:

- AdoptOpenJDK  -- Available on `https://adoptopenjdk.net <https://adoptopenjdk.net>`_
- Amazon Correto -- Available on `https://aws.amazon.com/corretto <https://aws.amazon.com/corretto>`_
- Azul OpenJDK 8+ -- Available on `https://www.azul.com/downloads/zulu <https://www.azul.com/downloads/zulu>`_
- IcedTea 3.8+ -- Available on `https://icedtea.classpath.org <https://icedtea.classpath.org/wiki/Main_Page>`_
- Oracle JDK 8+ -- Available on `https://java.com <https://java.com>`_
- SapMachine --  Available on `https://sap.github.io/SapMachine <https://sap.github.io/SapMachine>`_

Tools like `SdkMan <https://sdkman.io>`_ are recommended for switching between various JDK
versions.

Since version 0.5.0 the final releases have been built with IcdedTea.


Building on Linux
~~~~~~~~~~~~~~~~~

To build this project execute the following command:

.. code-block:: bash

    $ ./gradlew build
    Starting a Gradle Daemon, 1 incompatible and 1 stopped Daemons could not be reused, use --status for details

    Deprecated Gradle features were used in this build, making it incompatible with Gradle 5.0.
    See https://docs.gradle.org/4.8/userguide/command_line_interface.html#sec:command_line_warnings

    BUILD SUCCESSFUL in 14s
    18 actionable tasks: 10 executed, 8 up-to-date

    

Development
===========
    
    

Structures
----------

*tsgen* is an annotation processor, therefore it applies to an
 implicit contract between the compiler and the processor. It only
 extracts the type information, mainly from the sources. Remember:
 some types are erased, especially on the classes

   
It processes the *Java* sources in the following stages:

 #. The annotation processor ``TsGenProcessor`` is started by the
    compiler. It Analyzes only classes, that are annotated by the types
    from the ``dz.jtsgen.annotations`` package.
 #. Before any Java conversion is processed, the a configuration
    structure is built by combining the command line arguments for the
    compiler and the information added to the ``TSModule`` annotation
 #. After the configuration has been determined, the name space
    mappings are resolved.
 #. The processor converts the Java types to an internal, AST like,
    structure for the render. There are multiple converters involved in
    this stage
 #. It generates TypeScript code into the sources folder, which might
    be changed in the near future


.. literalinclude:: ../../processor/src/main/java/dz/jtsgen/processor/model/TSType.java 
    :linenos:
    :language: java
   


