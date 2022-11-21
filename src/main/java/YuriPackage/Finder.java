package YuriPackage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Finder {

  private String text;

  public String getText() {
    return text;
  }

  //Находит по шаблону выражение
  public void setText(String text) {
    this.text = text;
  }

  String getAllMathes(String userText) {
    String tempFind = "";
    StringBuilder result = new StringBuilder();
    Pattern pattern = Pattern.compile(userText + ".*___?");
    Matcher matcher = pattern.matcher(this.text);
    while (matcher.find()) {
      tempFind = matcher.group();
      result.append(tempFind.substring(0, tempFind.length() - 3)).append("\n");
    }

    return result.toString();
  }

}
