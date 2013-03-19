package jetbrains.plugins.passwordsHider.sample;

import jetbrains.buildServer.UserDataKey;
import jetbrains.buildServer.UserDataStorage;
import jetbrains.buildServer.messages.BuildMessage1;
import jetbrains.buildServer.messages.BuildMessagesTranslator;
import jetbrains.buildServer.messages.DefaultMessagesInfo;
import jetbrains.buildServer.serverSide.RunningBuildEx;
import jetbrains.buildServer.serverSide.SRunningBuild;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;

/**
 * This extension replaces passwords in build logs
 * Created 18.03.13 21:57
 *
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 */
public class BuildLogFilter implements BuildMessagesTranslator {
  private final UserDataKey<BuildMessagesTranslator> KEY = new UserDataKey<BuildMessagesTranslator>(BuildMessagesTranslator.class, "Build Log Filter plugin");
  private final Passwords myPasswords;

  public BuildLogFilter(@NotNull final Passwords passwords) {
    myPasswords = passwords;
  }

  /***
   * This extension point is used to replace all password text in the build log on the server
   */
  @NotNull
  public List<BuildMessage1> translateMessages(@NotNull final SRunningBuild build,
                                               @NotNull final BuildMessage1 message) {
    final UserDataStorage storage = ((RunningBuildEx) build).getUserDataStorage();

    BuildMessagesTranslator value = storage.getValue(KEY);
    if (value == null) {
      final Collection<String> passwords = myPasswords.getPasswordsToReplace(build).values();
      final PasswordReplacer filter = createPasswordsFilter(passwords);
      value = filter == null ? EMPTY_PROCESSOR : new PasswordsProcessor(filter);
      storage.setValue(KEY, value);
    }

    return value.translateMessages(build, message);
  }

  private final BuildMessagesTranslator EMPTY_PROCESSOR = new BuildMessagesTranslator() {
    @NotNull
    public List<BuildMessage1> translateMessages(@NotNull final SRunningBuild build, @NotNull final BuildMessage1 message) {
      return Collections.singletonList(message);
    }
  };

  private static class PasswordsProcessor implements BuildMessagesTranslator {
    private final PasswordReplacer myReplacer;

    private PasswordsProcessor(@NotNull final PasswordReplacer replacer) {
      myReplacer = replacer;
    }

    @NotNull
    public List<BuildMessage1> translateMessages(@NotNull final SRunningBuild build, @NotNull final BuildMessage1 originalMessage) {
      final Object data = originalMessage.getValue();
      if (!DefaultMessagesInfo.MSG_TEXT.equals(originalMessage.getTypeId()) || data == null || !(data instanceof String)) {
        return Collections.singletonList(originalMessage);
      }

      final String text = myReplacer.replacePasswords((String) data);
      return Collections.singletonList(DefaultMessagesInfo.createTextMessage(originalMessage, text));
    }
  }

  @NotNull
  private static Pattern createPasswordsPattern(@NotNull Set<String> passwords) {
    final StringBuilder sb = new StringBuilder();
    for (String v : passwords) {
      if (sb.length() > 0) sb.append("|");
      sb.append("(").append(Pattern.quote(v)).append(")");
    }
    return Pattern.compile(sb.toString());
  }

  @Nullable
  public static PasswordReplacer createPasswordsFilter(@NotNull final Collection<String> passwords) {
    if (passwords.isEmpty()) return null;
    final Pattern pt = createPasswordsPattern(new TreeSet<String>(passwords));
    return new PasswordReplacer() {
      @NotNull
      public String replacePasswords(@NotNull final String source) {
        return pt.matcher(source).replaceAll("*****");
      }
    };
  }

  private interface PasswordReplacer {
    @NotNull
    String replacePasswords(@NotNull String s);
  }
}
