# BCEL ClassPathRepository OutOfMemoryError Example

This project supports the background of [BCEL-320](https://issues.apache.org/jira/browse/BCEL-320):
"A new ClassPathRepository that can scan many JAR files without OutOfMemoryError".

This project configures Java heap size for the JUnit test (`-Xmx256m`) to easily observe
OutOfMemoryError.

# Run

## Test with ClassPathRepository
```
~/bcel-oome-test$ mvn test -Dtest=BcelOomeTest#testClassPathRepository
...
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running suztomo.BcelOomeTest
[ERROR] Tests run: 1, Failures: 0, Errors: 1, Skipped: 0, Time elapsed: 5.779 s <<< FAILURE! - in suztomo.BcelOomeTest
[ERROR] testClassPathRepository(suztomo.BcelOomeTest)  Time elapsed: 5.762 s  <<< ERROR!
java.lang.OutOfMemoryError: GC overhead limit exceeded
	at suztomo.BcelOomeTest.iterateAllJavaClass(BcelOomeTest.java:45)
	at suztomo.BcelOomeTest.testClassPathRepository(BcelOomeTest.java:34)

[INFO] 
[INFO] Results:
[INFO] 
[ERROR] Errors: 
[ERROR]   BcelOomeTest.testClassPathRepository:34->iterateAllJavaClass:45 » OutOfMemory ...
[INFO] 
[ERROR] Tests run: 1, Failures: 0, Errors: 1, Skipped: 0
```

## Test with MemorySensitiveClassPathRepository

```
~/bcel-oome-test$ mvn test -Dtest=BcelOomeTest#testMemorySensitiveClassPathRepository
...
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running suztomo.BcelOomeTest
[ERROR] Tests run: 1, Failures: 0, Errors: 1, Skipped: 0, Time elapsed: 5.063 s <<< FAILURE! - in suztomo.BcelOomeTest
[ERROR] testMemorySensitiveClassPathRepository(suztomo.BcelOomeTest)  Time elapsed: 5.045 s  <<< ERROR!
java.lang.OutOfMemoryError: GC overhead limit exceeded
	at suztomo.BcelOomeTest.iterateAllJavaClass(BcelOomeTest.java:49)
	at suztomo.BcelOomeTest.testMemorySensitiveClassPathRepository(BcelOomeTest.java:43)

[INFO] 
[INFO] Results:
[INFO] 
[ERROR] Errors: 
[ERROR]   BcelOomeTest.testMemorySensitiveClassPathRepository:43->iterateAllJavaClass:49 » OutOfMemory
[INFO] 
[ERROR] Tests run: 1, Failures: 0, Errors: 1, Skipped: 0
```

# Environment

BCEL 3.6.1

OS: Linux 

OpenJDK 1.8.0_181 (Google-build)

Maven 3.6.1

# Root Cause Analysis
BCEL ClassPathRepository maintains a map `_loadedClass` that grows as the repository reads JavaClass
from its class path. When there are many JavaClass, the map may exceeds the size of JVM heap.

Existing MemorySensitiveClassPathRepository is intended to help such situation by using
SoftReference. However when the size of the JavaClass map is huge, it still causes OutOfMemoryError
due to too much garbage collection time. I think this MemorySensitiveClassPathRepository's behavior
is related to [JDK-6912889 : SoftReferences cause worst-case garbage collection](
https://bugs.java.com/bugdatabase/view_bug.do?bug_id=6912889) (Won't Fix).

# License

This test source code is under Apache License Version 2.0. The JAR files included in the test have
their own licenses.