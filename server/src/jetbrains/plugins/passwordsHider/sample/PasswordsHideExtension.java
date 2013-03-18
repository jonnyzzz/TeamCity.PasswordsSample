package jetbrains.plugins.passwordsHider.sample;

import jetbrains.buildServer.serverSide.BuildStartContext;
import jetbrains.buildServer.serverSide.BuildStartContextProcessor;
import jetbrains.buildServer.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

import static jetbrains.buildServer.parameters.PasswordParametersFilterCore.*;

/**
 * Created 18.03.13 21:42
 *
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 */
public class PasswordsHideExtension implements BuildStartContextProcessor {
  public void updateParameters(@NotNull final BuildStartContext context) {
    final Collection<String> passwords = computeParametersToHide(context);
    if (passwords.isEmpty()) return;

    String currentValue = context.getSharedParameters().get(VALUES_LIST_CONFIG_PARAMETER_NAME);
    if (!StringUtil.isEmptyOrSpaces(currentValue)) {
      passwords.addAll(unpackParameters(currentValue));
    }

    context.addSharedParameter(VALUES_LIST_CONFIG_PARAMETER_NAME, packParameters(new TreeSet<String>(passwords)));
  }

  @NotNull
  private Collection<String> computeParametersToHide(@NotNull BuildStartContext context) {

    ///TODO: Add here code to list properties that you're going to hide
    return Arrays.asList("some.property");
  }
}
