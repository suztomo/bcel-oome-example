package org.apache.bcel.util;

import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.bcel.classfile.JavaClass;

/**
 * Repository that maintains least-recently-used (LRU) cache of {@link JavaClass}.
 */
public class LruCacheClassPathRepository extends ClassPathRepository {

  private final LinkedHashMap<String, JavaClass> loadedClass;

  public LruCacheClassPathRepository(final ClassPath path, final int cacheSize) {
    super(path);

    int initialCapacity = (int) (0.75 * cacheSize);
    boolean accessOrder = true; // Evicts least-recently-accessed
    loadedClass = new LinkedHashMap<String, JavaClass>(initialCapacity, cacheSize,
        accessOrder) {
      protected boolean removeEldestEntry(Map.Entry<String, JavaClass> eldest) {
        return size() > cacheSize;
      }
    };
  }

  @Override
  public void storeClass(final JavaClass javaClass) {
    // Not storing parent's _loadedClass
    loadedClass.put(javaClass.getClassName(), javaClass);
    javaClass.setRepository(this);
  }

  @Override
  public JavaClass findClass(final String className) {
    return loadedClass.get(className);
  }
}
