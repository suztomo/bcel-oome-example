# BCEL ClassPathRepository OutOfMemoryError Example

This project supports the background of [BCEL-320](https://issues.apache.org/jira/browse/BCEL-320):
A new ClassPathRepository that can scan 200 JAR files without OutOfMemoryError.

BCEL ClassPathRepository maintains a map `_loadedClass` that grows as the repository reads JavaClass
from its class path.




# License

This test source code is under Apache License Version 2.0. The JAR files included in the test have
their own licenses.