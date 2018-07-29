.. _faq:

========
Appendix
========



Frequently Asked Questions
--------------------------

This is a list of Frequently Asked Questions about *tsgen*.



... Is there a standalone version available?

    *tsgen* is a kind of compiler plugin, therefore you need ``javac``
    to generate TypeScript.

... ``Option[T]`` should be converted to an optional type?

    TypeScript [TS] adds an ``| undefined`` to optional types
    if ``--strictNullChecks`` is enabled. So the following setting
    should to the same:

    .. code-block:: java

       @TSModule(
            moduleName = "myModule",
            customTypeMappings = {
               "java.util.Optional[T] -> `T` | undefined"
               }
       )

    But be sure, that your JSON serializer does the same.