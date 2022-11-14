package YuriPackage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Finder {

  protected String text;

  public String getAllMathes(String user_text) {
    String tempFind = "";
    StringBuilder result = new StringBuilder();
    Pattern pattern = Pattern.compile(user_text + ".*___?");
    Matcher matcher = pattern.matcher(this.text);
    while (matcher.find()) {
      tempFind = matcher.group();
      result.append(tempFind.substring(0, tempFind.length() - 3)).append("\n");
    }

    return result.toString();
  }

}
