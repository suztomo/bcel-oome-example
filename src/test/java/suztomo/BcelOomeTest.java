package suztomo;

import static org.junit.Assert.assertTrue;


import com.google.common.reflect.ClassPath.ClassInfo;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

/**
 * Unit test to reproduce OutOfMemoryError when ClassPathRepository reads many JAR files.
 */
public class BcelOomeTest {

  @Test
  public void shouldAnswerWithTrue() {
    assertTrue(true);
  }

  List<String> classNames(Path jar) throws Exception {
    URL jarUrl = jar.toUri().toURL();
    // Setting parent as null because we don't want other classes than this jar file
    URLClassLoader classLoaderFromJar = new URLClassLoader(new URL[]{jarUrl}, null);

    com.google.common.reflect.ClassPath classPath =
        com.google.common.reflect.ClassPath.from(classLoaderFromJar);

    return classPath.getAllClasses().stream()
        .map(ClassInfo::getName)
        .collect(Collectors.toList());
  }
}
