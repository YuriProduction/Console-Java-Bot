package YuriPackage;

import static java.lang.System.exit;
import static java.lang.System.out;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  private Map<Boolean, String> currentCommand = new HashMap<Boolean, String>();

  private Bot bot_holding_base = new Bot();
  private Client tempClient = new Client();

  private String[] commands_list = new String[]{"/add", "/limit", "/statistics", "/start",
      "/help"};
  private boolean sumIsAdded = false;

  private boolean isCommand(String argum) {
    for (String x : commands_list) {
      if (argum.equals(x)) {
        return true;
      }
    }
    return false;
  }

  private void fixUsingCommand(String argum) {
    currentCommand.put(true, argum);
  }

  private void SendFirstTextOfCommand(String command, Long chatID) throws TelegramApiException {
    SendMessage outMess = new SendMessage();
    outMess.setChatId(chatID.toString());
    if (command.equals("/start")) {
      outMess.setText(
          "Привет \uD83D\uDC4B, меня зовут Финес. Я твой личный бот-финансист \uD83D\uDCB0."
              + "\nЖми /help, если хочешь узнать на что я способен \uD83E\uDDBE");
      execute(outMess);
    } else if (command.equals("/help")) {
      outMess.setText(
          "\n" +
              "1)Введите \"/start\" чтобы начать работу с ботом\n" +
              "2)Введите \"/help\" чтобы получить список комад\n" +
              "3)Введите \"/add\" чтобы добавить расходы\n" +
              "4)Введите \"/limit\" чтобы установить лимит по расходам на сегодня\n" +
              "5)Введите \"/statistics\" чтобы показать статистику\n");

      execute(outMess);
    } else if (command.equals("/add")) {
      outMess.setText("Введите сумму");
      execute(outMess);
    } else if (command.equals("/limit")) {
      outMess.setText("Введите сумму, за пределы которой ваши расходы не должны сегодня выходить");
      execute(outMess);
    } else if (command.equals("/statistics")) {
      String stat = tempClient.showStatistic();
      outMess.setText(stat);
      execute(outMess);
      currentCommand.put(true,"Default command");//ставим дефолтную команду,
    }
    else {
      outMess.setText("Сообщение не распознано");
      execute(outMess);
    }
  }

  private int tempSUM = 0;
  private String tempGOOD = "";
  private void DoCommandLogic(String command, String textOfMessage,Long chat_id)
      throws TelegramApiException {
    SendMessage outMess = new SendMessage();
    bot_holding_base.readBase();

    outMess.setChatId(chat_id.toString());
    if (command.equals("/add")){
      //мы знаем, что первое сообщение уже отправлено
      //"Введите сумму" добавлено
      if (!sumIsAdded)
      {
        tempSUM = Integer.parseInt(textOfMessage);
        //Если сумма еще не добавлена - просим добавить
        //addSum(text)
        //просим ввести товар
        outMess.setText("Введите товар");
        execute(outMess);
        sumIsAdded = true;
      }
      else {
        //addGood(text)
        //товар добавлен, затираем переменную
        tempGOOD = textOfMessage;
        tempClient.addExpenses(tempSUM,tempGOOD);//добавляем расходы
        //затираем даные
        sumIsAdded  = false;
        tempSUM = 0;
        tempGOOD = "";
        currentCommand.put(true,"Default command");//ставим дефолтную команду
      }
    }
    else if (command.equals("/limit"))
    {
      //setLimit(text)
      tempClient.setLimit(Integer.parseInt(textOfMessage));
      outMess.setText("Лимит установлен");
      execute(outMess);
      currentCommand.put(true,"Default command");//ставим дефолтную команду,
      // которая никак не обрабатывается
      // и попадет в else
    }
    else {//(Default command,/help,/start) //если команды выполнены, а пользователь что-то пишет
        outMess.setText("Вся логика выполнена. Команды перед вами. Делайте что хотите");
        execute(outMess);
    }
  }


  @Override
  public String getBotUsername() {
    return "Finance_Yur_and_Serg_Bot";
  }

  @Override
  public String getBotToken() {
    String token = GetTokenFromEnvironmentVariables();
    if (token != null) {
      return token;
    } else {
      System.out.println("Not token in enviroment variables");
      exit(-1);
      return null;
    }
  }

  @Override
  public void onUpdateReceived(Update update) {
    //currentCommand.put(true,"Defolt command");//ставим дефолтную команду, которая в случае чего никак не обработается
    var msg = update.getMessage();
    var user = msg.getFrom();
    out.println(user.getUserName());
    String user_uniq_nick = user.getUserName();
    bot_holding_base.readBase();
    bot_holding_base.registateClient(user_uniq_nick);
    tempClient = bot_holding_base.signIN(user_uniq_nick);

    String textOfMessage = msg.getText();
    Long chat_id = msg.getChatId();
    if (isCommand(textOfMessage)) {
      fixUsingCommand(textOfMessage);
      try {
        SendFirstTextOfCommand(textOfMessage,
            chat_id); //отправляем первое сообщение и завершаем логику
      } catch (TelegramApiException e) {
        throw new RuntimeException(e);
      }
      return;
    }
    String command = currentCommand.get(true);//смотрим, какая команда используется
    try {
      DoCommandLogic(command,textOfMessage,chat_id);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
    bot_holding_base.updateBase();

  }

  private void SendMessageToUser(String command, Long chat_id) throws TelegramApiException {
    SendMessage outMess = new SendMessage();
    if (command.equals("/Add")) {
      outMess.setChatId(chat_id.toString());
      outMess.setText("Введите сумму");
      //какая-то логика
      execute(outMess);

    }
  }


  private void sendMenu(Long who, String txt, InlineKeyboardMarkup kb) {
    SendMessage sm = SendMessage
        .builder()
        .chatId(who.toString())
        .parseMode("HTML")
        .text(txt)
        .replyMarkup(kb)
        .build();

    try {
      execute(sm);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  private String readUsingFiles(String fileName) throws IOException {
    return new String(Files.readAllBytes(Paths.get(fileName)));
  }//считываем данные из файла и возвращаем в виде строки

  private void SendMenu(Long number_of_chat) {
    //создаем кнопочки
    var add = InlineKeyboardButton
        .builder()
        .text("Add")
        .callbackData("add")
        .build();

    var limit = InlineKeyboardButton
        .builder()
        .text("Limit")
        .callbackData("limit")
        .build();

    var stat = InlineKeyboardButton
        .builder()
        .text("Statistics")
        .callbackData("statistics")
        .build();

    var url = InlineKeyboardButton
        .builder()
        .text("GitHub")
        .url("https://core.telegram.org/bots/api")
        .build();

    //создаем клавиатуру
    keyboard1 = InlineKeyboardMarkup.builder()
        .keyboardRow(List.of(add))
        .keyboardRow(List.of(url))
        .keyboardRow(List.of(limit))
        .keyboardRow(List.of(stat))
        .build();

    //сендим клавиатуру

    sendMenu(number_of_chat, "<b>\uD83E\uDDEDNavigation\uD83E\uDDED</b>", keyboard1);

    //____________________________________________________//
  }

  private String GetTokenFromEnvironmentVariables() {
    Map<String, String> env = System.getenv();
    for (String envName : env.keySet()) {
      if (envName.equals("TOKEN_TELE")) {
        return env.get(envName);
      }
    }
    return null;
  }

  private InlineKeyboardMarkup keyboard1;
}
