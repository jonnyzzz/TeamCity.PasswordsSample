package jetbrains.plugins.passwordsHider.sample;

import jetbrains.buildServer.serverSide.SBuild;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

/**
 * Created 19.03.13 11:17
 *
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 */
public class Passwords {
  /**
   * @return map of properties that values should be replaced
   */
  @NotNull
  public Map<String, String> getPasswordsToReplace(@NotNull final SBuild build) {
    ///TODO: Add here code to map of properties that you're going to hide
    String key = "some.property";
    String value = build.getParametersProvider().get(key);
    return Collections.singletonMap(key, value);
  }

}
