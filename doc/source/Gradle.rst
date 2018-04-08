Using in Gradle projects
========================

Adding the following snippet to your gradle (sub-) project, the
annotation processor should run at automatically at compile
time. Since 0.3.0 *tsgen* has been distributed on maven central, so
no other repository has to be defined. 

The used version should be held in a variable like the following

.. parsed-literal::
   
  buildscript {
      ext {
        jtsgen_Version=" \ |release|\ "
      }
   }

   
The brave ones could also try a development version, e.g.
|next-release|. So the dependencies to jtsgen could be defined like
the following. This should extract the TypeScript output to the gradle
build dir automatically:

   
.. code-block:: groovy
		
    dependencies {
        compileOnly "com.github.dzuvic:jtsgen-annotations:${jtsgen_Version}"
        compileOnly "com.github.dzuvic:jtsgen-processor:${jtsgen_Version}"
    }


    

Customizing the output dir
--------------------------

The output is customized by adding the source directory to the annotation processor:

.. code-block:: groovy
  
    def tsOutDir="${buildDir}/ts"

    compileJava {
        options.compilerArgs = [ "-s", tsOutDir ]
        dependsOn(createTsDir)
    }


Generating Types for Kotlin classes
-----------------------------------

If your project contains Kotlin classes that should be converted to
Kotlin, you could try the `kapt
<https://kotlinlang.org/docs/reference/kapt.html>`_ compiler plugin.

For example:


.. code-block:: groovy

  apply plugin: 'kotlin-kapt'

  dependencies {

    compileOnly "com.github.dzuvic:jtsgen-annotations:${jtsgen_Version}"
    compileOnly "com.github.dzuvic:jtsgen-processor:${jtsgen_Version}"

    kapt "com.github.dzuvic:jtsgen-processor:${jtsgen_Version}"
   }

  kapt {
    correctErrorTypes = true
    arguments {
        arg("jtsgenModuleVersion", version)
      }
  }


