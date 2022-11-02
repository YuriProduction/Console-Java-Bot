package YuriPackage;

import java.util.Date;
import java.io.IOException;

public class Main {

  public static void main(String[] args) throws IOException {
//    Bot bot = new Bot();
//    bot.work();
    //System.out.println("Кириллица");
    System.out.println(CastDateToInt(new Date().toString()));
    char a = '1';
    char b = '2';
    System.out.println(String.valueOf(a) + b);
    System.out.println(new Date());
    System.out.println(CastDateToInt("Tue Nov 00 20:59:18 YEKT 2022"));
  }

  public static int CastDateToInt(String data) {
    char[] char_data = data.toCharArray();
    String str_day = String.valueOf(char_data[8]) + char_data[9];
    return Integer.parseInt(str_day);
  }

}
