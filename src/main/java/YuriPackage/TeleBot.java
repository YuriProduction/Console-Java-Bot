package YuriPackage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


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
    System.out.println(
        user.getFirstName() + " wrote " + msg.getText() + "\nHis own nick is @" + user.getUserName()
            + '\n');
    System.out.println(
        user.getFirstName() + " wrote " + msg.getText() + "\nHis own nick is @" + user.getUserName()
            + '\n');

    try {
      if (update.hasMessage() && update.getMessage().hasText()) {
        //��������� �� ������� ��������� ������������
        Message inMess = update.getMessage();
        //������� �� inMess id ���� ������������
        String chatId = inMess.getChatId().toString();
        //�������� ����� ��������� ������������, ���������� � ���������� ���� ����������
        String response = parseMessage(inMess.getText());
        //������� ������ ������ SendMessage - ��� ������� ����� ������������
        SendMessage outMess = new SendMessage();

        //��������� � ���� ��������� id ���� � ����� ��� �����
        outMess.setChatId(chatId);
        outMess.setText(response);

        //�������� � ���
        try {
          execute(outMess);
        } catch (TelegramApiException ex) {
          throw new RuntimeException(ex);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public String parseMessage(String textMsg) {
    String response;

    //���������� ����� ������������ � ������ ���������, �� ������ ����� ��������� �����
    if (textMsg.equals("/start")) {
      response = "������ \uD83D\uDC4B, ���� ����� �����. � ���� ������ ���-��������� \uD83D\uDCB0."
          + "\n��� /help, ���� ������ ������ �� ��� � �������� \uD83E\uDDBE";
    } else if (textMsg.equals("/help")) {
      response =
          "\n" +
          "1)������� \"/Register\" ����� ������������������\n" +
          "2)������� \"/Sign in\" ����� ����� � �������\n" +
          "3)������� \"/Add\" ����� �������� �������\n" +
          "4)������� \"/Limit\" ����� ���������� ����� �� �������� �� �������\n" +
          "5)������� \"/Statistics\" ����� �������� ����������\n" +
          "6)������� \"/Calculation\" ����� ��������� ����� �� 7-n ����\n" +
          "7)������� \"/Exit\" ����� ����� �� �������\n";
    } else {
      response = "��������� �� ����������";
    }

    return response;
  }

  private static String readUsingFiles(String fileName) throws IOException {
    return new String(Files.readAllBytes(Paths.get(fileName)));
  }//��������� ������ �� ����� � ���������� � ���� ������
}
