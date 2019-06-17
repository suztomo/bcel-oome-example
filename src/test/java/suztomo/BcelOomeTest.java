package suztomo;

import com.google.common.reflect.ClassPath.ClassInfo;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.bcel.util.ClassPath;
import org.apache.bcel.util.ClassPathRepository;
import org.apache.bcel.util.MemorySensitiveClassPathRepository;
import org.apache.bcel.util.Repository;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test to reproduce OutOfMemoryError when ClassPathRepository reads many JAR files.
 */
public class BcelOomeTest {

  private File[] jars;

  @Before
  public void setup() {
    Path resourcesDirectory = Paths.get("src", "test", "resources");
    jars = resourcesDirectory.toFile().listFiles();
  }

  @Test
  public void testClassPathRepository() throws Exception {
    iterateAllJavaClass(new ClassPathRepository(classPath()));
  }

  @Test
  public void testMemorySensitiveClassPathRepository() throws Exception {
    iterateAllJavaClass(new MemorySensitiveClassPathRepository(classPath()));
  }

  private void iterateAllJavaClass(Repository repository) throws Exception {
    for (File jar : jars) {
      for (String className : classNames(jar)) {
        repository.loadClass(className);
      }
    }
  }

  private ClassPath classPath() {
    return new ClassPath(Arrays.stream(jars).map(file -> file.getAbsolutePath())
        .collect(Collectors.joining(":")));
  }

  private static List<String> classNames(File jar) throws Exception {
    URL jarUrl = jar.toURI().toURL();
    // Setting parent as null because we don't want other classes than this jar file
    URLClassLoader classLoaderFromJar = new URLClassLoader(new URL[]{jarUrl}, null);

    com.google.common.reflect.ClassPath classPath =
        com.google.common.reflect.ClassPath.from(classLoaderFromJar);

    return classPath.getAllClasses().stream()
        .map(ClassInfo::getName)
        .collect(Collectors.toList());
  }

}
