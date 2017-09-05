Name Space Mapping
==================

TSModule accepts a list of name spaces, that should me mapped
(shortened). That list will be prepended to the calculated name space
mapping. The following name spave mapping strategies are available:

-  ``TOP_LEVEL_TO_ROOT``: The top level java types are mapped to the
   root name space. Everything beneath is mapped into name spaces
-  ``ALL_TO_ROOT``: All types are mapped to the root name space, only
   the types of same name reside in their own name space
-  ``MANUAL``: No name space mapping is calculated

Some examples of :

-  ``a.b.c ->``: Maps a.b.c (and beneath) to root
-  ``a.b.c -> a.b``: Maps a.b.c to namespace a
-  ``=a.b.c ->``: Maps only a.b.c to the root