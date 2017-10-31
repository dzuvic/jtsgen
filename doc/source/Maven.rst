Using in Maven Projects
=======================

The annotation processor should run aromatically if ``jtsgen-processor`` is in the class path. Please note, that it is
not advised making the processor transitive dependent in you project. Use either the ``provided`` or ``optional`` scope
to break the dependency tree at this point. If it is necessary creating the TypesScript files in different location,
the following strategies might solve this problem depending on the version of the maven-compiler plugin:

  - for versions >= 2.2: maybe ``annotationProcessors`` could be useful
  - for versions >= 2.2: with ``generatedSourcesDirectory`` the ouput path of the generated source files can be changed
  - for versions >= 3.1: using ``compilerArgs`` the annotation processor can be fed with additional
    :ref:`processing-parameters`
  - for versions >= 3.5: with ``annotationProcessorPaths`` the processor could be completely removed from the
    dependency tree

For a full set of options you should take a look at the manual of the
`Maven Compiler Plugin <https://maven.apache.org/plugins/maven-compiler-plugin/compile-mojo.html>`_.

The following full example generates the typescript files in ``../client/src`` and logs some info debug messages ::


    <project>
        <modelVersion>4.0.0</modelVersion>
        <groupId>jtsgen-example</groupId>
        <artifactId>api</artifactId>
        <version>1.0.0-SNAPSHOT</version>

        <dependencies>
            <dependency>
                <groupId>com.github.dzuvic</groupId>
                <artifactId>jtsgen-processor</artifactId>
                <version>0.3.0</version>
                <scope>provided</scope>
            </dependency>
        </dependencies>

        <build>
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.3</version>
                    <configuration>
                        <compilerVersion>1.8</compilerVersion>
                        <source>1.8</source>
                        <target>1.8</target>
                        <generatedSourcesDirectory>../client/src</generatedSourcesDirectory>
                        <compilerArgs>
                            <arg>-AjtsgenLogLevel=info</arg>
                        </compilerArgs>
                    </configuration>
                </plugin>
            </plugins>
        </build>
    </project>

