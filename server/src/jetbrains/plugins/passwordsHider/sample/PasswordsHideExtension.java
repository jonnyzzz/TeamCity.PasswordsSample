package jetbrains.plugins.passwordsHider.sample;

import jetbrains.buildServer.serverSide.BuildStartContext;
import jetbrains.buildServer.serverSide.BuildStartContextProcessor;
import jetbrains.buildServer.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.TreeSet;

import static jetbrains.buildServer.parameters.PasswordParametersFilterCore.*;

/**
 * This extension triggers replacement of passwords in
 * - properties files that are send back from agent
 * - in agent logs
 *
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 */
public class PasswordsHideExtension implements BuildStartContextProcessor {
  private final Passwords myPasswords;

  public PasswordsHideExtension(@NotNull Passwords passwords) {
    myPasswords = passwords;
  }

  public void updateParameters(@NotNull final BuildStartContext context) {
    final Collection<String> passwords = myPasswords.getPasswordsToReplace(context.getBuild()).keySet();
    if (passwords.isEmpty()) return;

    String currentValue = context.getSharedParameters().get(VALUES_LIST_CONFIG_PARAMETER_NAME);
    if (!StringUtil.isEmptyOrSpaces(currentValue)) {
      passwords.addAll(unpackParameters(currentValue));
    }

    context.addSharedParameter(VALUES_LIST_CONFIG_PARAMETER_NAME, packParameters(new TreeSet<String>(passwords)));
  }
}
