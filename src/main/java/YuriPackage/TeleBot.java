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
        //Извлекаем из объекта сообщение пользователя
        Message inMess = update.getMessage();
        //Достаем из inMess id чата пользователя
        String chatId = inMess.getChatId().toString();
        //Получаем текст сообщения пользователя, отправляем в написанный нами обработчик
        String response = parseMessage(inMess.getText());
        //Создаем объект класса SendMessage - наш будущий ответ пользователю
        SendMessage outMess = new SendMessage();

        //Добавляем в наше сообщение id чата а также наш ответ
        outMess.setChatId(chatId);
        outMess.setText(response);

        //Отправка в чат
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

    //Сравниваем текст пользователя с нашими командами, на основе этого формируем ответ
    if (textMsg.equals("/start")) {
      response = "Привет \uD83D\uDC4B, меня зовут Финес. Я твой личный бот-финансист \uD83D\uDCB0."
          + "\nЖми /help, если хочешь узнать на что я способен \uD83E\uDDBE";
    } else if (textMsg.equals("/help")) {
      response =
          "\n" +
          "1)Введите \"/Register\" чтобы зарегистрироваться\n" +
          "2)Введите \"/Sign in\" чтобы зайти в систему\n" +
          "3)Введите \"/Add\" чтобы добавить расходы\n" +
          "4)Введите \"/Limit\" чтобы установить лимит по расходам на сегодня\n" +
          "5)Введите \"/Statistics\" чтобы показать статистику\n" +
          "6)Введите \"/Calculation\" чтобы вычислить лимит на 7-n дней\n" +
          "7)Введите \"/Exit\" чтобы выйти из системы\n";
    } else {
      response = "Сообщение не распознано";
    }

    return response;
  }

  private static String readUsingFiles(String fileName) throws IOException {
    return new String(Files.readAllBytes(Paths.get(fileName)));
  }//считываем данные из файла и возвращаем в виде строки
}
