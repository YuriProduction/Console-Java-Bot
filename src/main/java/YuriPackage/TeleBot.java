package YuriPackage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


public class TeleBot extends TelegramLongPollingBot {

  @Override
  public String getBotUsername() {
    return "Finance_Yur_and_Serg_Bot";
  }

  @Override
  public String getBotToken() {
    String token = null;
    try {
      token = readUsingFiles(
          "D:\\JAVA\\UNIVERSITY\\Bot_consol\\ConsolniyBot\\src\\main\\resources\\TOKEN.txt");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return token;
  }

  @Override
  public void onUpdateReceived(Update update) {
    var msg = update.getMessage();
    var user = msg.getFrom();
    System.out.println(user.getFirstName() + " wrote " +  msg.getText()+"\nHis own nick is @" + user.getUserName() +'\n');
  }



  private static String readUsingFiles(String fileName) throws IOException {
    return new String(Files.readAllBytes(Paths.get(fileName)));
  }//считываем данные из файла и возвращаем в виде строки
}
