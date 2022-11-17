package YuriPackage;

import static java.lang.System.exit;
import static java.lang.System.out;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class TeleBot extends TelegramLongPollingBot {

  private Map<Boolean, String> currentCommand = new HashMap<Boolean, String>();

  private final Bot bot_holding_base = new Bot();
  private Client tempClient = new Client();

  private CommandHandler cmd = new CommandHandler();

  private Map<String, List<String>> categories = null;

  private String extractCategory(String message) {
    List<String> tempList = this.categories.get(message);//по ключу берем
    // нужный список продуктов
    StringBuilder result = new StringBuilder();
    result.append(message).append("\n");
    for (String str : tempList) {
      String name_of_product = str.split("___")[0];
      String price_of_product = str.split("___")[1];
      result.append(name_of_product)
          .append("\t - ")
          .append(price_of_product)
          .append("\n");
    }
    return result.toString();
  }

  private String sendDataForUser() throws IOException {
    this.categories = readFromPerekrestok.getCategories();
    StringBuilder result = new StringBuilder();
    for (Entry<String, List<String>> entry : categories.entrySet()) {
      String key = entry.getKey();
      List<String> value = entry.getValue();
      result.append(key).append("\n");
      for (int i = 0; i < value.size(); i++) {
        String name_of_product = value.get(i).split("___")[0];
        String price_of_product = value.get(i).split("___")[1];
        result.append(name_of_product)
            .append("\t - ")
            .append(price_of_product)
            .append("\n");
      }
      result.append("\n");
    }

    return result.toString();

  }

  private void sendKeyboardCategoriesToUser(Long number_of_chat) {
    creatorMenu.createKeyboardCategoriesToUser(number_of_chat);
    //сендим клавиатуру
    SendMessage sm = creatorMenu.getSm();
    try {
      execute(sm);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  private final String[] commandslist = new String[]{"/add", "/limit", "/statistics", "/start",
      "/help", "/menu", "/products_and_prices", "Молоко, сыр, яйца", "С днём вегана",
      "От Перекрёстка",
      "Макароны, крупы, масло, специи",
      "Овощи, фрукты, грибы", "Готовая еда", "/find"};
  private boolean sumIsAdded = false;

  private boolean isCommand(String argum) {
    for (String x : commandslist) {
      if (argum.equals(x)) {
        return true;
      }
    }
    return false;
  }

  private void fixUsingCommand(String argum) {
    currentCommand.put(true, argum);
  }


  private void sendFirstTextOfCommand(String command, Long chatID)
      throws TelegramApiException, IOException {
    SendMessage outMess = new SendMessage();
    outMess.setChatId(chatID.toString());
    try{
      cmd.handleFirstTextOfCommand(command, chatID);
      SendMessage outPutMess = cmd.getOutMess();//Переименуешь тут как нужно
      execute(outPutMess);
    }
    catch(Exception e){
      //тут просто дописать какой ex выкинуть
    }

  }
//  private void sendFirstTextOfCommand(String command, Long chatID)
//      throws TelegramApiException, IOException {
//    SendMessage outMess = new SendMessage();
//    outMess.setChatId(chatID.toString());
//    if (command.equals("/start")) {
//      outMess.setText(
//          "Привет \uD83D\uDC4B, меня зовут Финес. Я твой личный бот-финансист \uD83D\uDCB0."
//              + "\nЖми /help, если хочешь узнать на что я способен \uD83E\uDDBE");
//      execute(outMess);
//    } else if (command.equals("/help")) {
//      outMess.setText(
//          "\n" +
//              "1)Введите \"/start\" чтобы начать работу с ботом\n" +
//              "2)Введите \"/help\" чтобы получить список комад\n" +
//              "3)Введите \"/add\" чтобы добавить товар в корзину\n" +
//              "4)Введите \"/limit\" чтобы установить лимит на покупки\n" +
//              "5)Введите \"/statistics\" чтобы показать стоимость корзины и ваш остаток\n"
//              +
//              "6)Введите \"/menu\" чтобы открыть интерактивное меню\n"
//              + "7)Введите \"/products_and_prices\" чтобы посмотреть текущие цены"
//              + "на товары в магазине \"Перекресток\"\n"
//
//      );
//
//      execute(outMess);
//    } else if (command.equals("/add")) {
//      outMess.setText("Введите сумму");
//      execute(outMess);
//    } else if (command.equals("/find")) {
//      outMess.setText("Введите товар(например, \"Молоко\")");
//      execute(outMess);
//    } else if (command.equals("/limit")) {
//      outMess.setText("Введите сумму, за пределы которой ваши расходы не должны сегодня выходить");
//      execute(outMess);
//    } else if (command.equals("/statistics")) {
//      String stat = tempClient.showStatistic();
//      outMess.setText(stat);
//      execute(outMess);
//      currentCommand.put(true, "Default command");//ставим дефолтную команду,
//    } else if (command.equals("/menu")) {
//      sendCommandsMenu(chatID);
//      currentCommand.put(true, "Default command");//ставим дефолтную команду,
//    } else if (command.equals("/products_and_prices")) {
//      outMess.setText("Вычисляем статистику, немного подождите...");
//      execute(outMess);
//      String result_prod_and_prices = this.sendDataForUser();
//      outMess.setText(result_prod_and_prices);
//      execute(outMess);
//      sendKeyboardCategoriesToUser(chatID);
//      outMess.setText("Введите /find, чтобы найти какой-то конкретный товар");
//      execute(outMess);
//      currentCommand.put(true, "Default command");//ставим дефолтную команду
//    } else if (command.equals("От Перекрёстка") || command.equals("С днём вегана")
//        || command.equals("Молоко, сыр, яйца") || command.equals("Макароны, крупы, масло, специи")
//        || command.equals("Овощи, фрукты, грибы") || command.equals("Готовая еда")) {
//      if (categories == null) {
//        outMess.setText(
//            "Сначала обновите страницу, для этого выберите пункт \"Посмотреть текущие цены на товары в магазине \"Перекресток\" \" ");
//        execute(outMess);
//        return;
//      }
//      String prod_of_suit_category = extractCategory(command);
//      outMess.setText(prod_of_suit_category);
//      out.println("Command of category!");
//      execute(outMess);
//      currentCommand.put(true, "Default command");//ставим дефолтную команду
//    } else {
//      outMess.setText("Сообщение не распознано");
//      execute(outMess);
//    }
//  }


  private int tempSUM = 0;


  private void doCommandLogic(String command, String textOfMessage, Long chat_id)
      throws TelegramApiException {
    SendMessage outMess = new SendMessage();

    outMess.setChatId(chat_id.toString());
    if (command.equals("/add")) {
      //мы знаем, что первое сообщение уже отправлено
      //"Введите сумму" добавлено
      if (!sumIsAdded) {
        tempSUM = Integer.parseInt(textOfMessage);
        //Если сумма еще не добавлена - просим добавить
        //addSum(text)
        //просим ввести товар
        outMess.setText("Введите товар");
        execute(outMess);
        sumIsAdded = true;
      } else {
        //addGood(text)
        //товар добавлен, затираем переменную
        String tempGOOD = textOfMessage;
        tempClient.addExpenses(tempSUM, tempGOOD);//добавляем расходы
        if (tempClient.getOVERFLOW()) {
          outMess.setText("Вы выходите за пределы установленной суммы");
          execute(outMess);
          return;
        }
        //затираем даные
        sumIsAdded = false;
        tempSUM = 0;
        tempGOOD = "";
        currentCommand.put(true, "Default command");//ставим дефолтную команду
      }
    } else if (command.equals("/limit")) {
      //setLimit(text)
      tempClient.setLimit(Integer.parseInt(textOfMessage));
      outMess.setText("Лимит установлен");
      execute(outMess);
      currentCommand.put(true, "Default command");//ставим дефолтную команду,
      // которая никак не обрабатывается
      // и попадет в else
    } else if (command.equals("/find")) {
      //setLimit(text)
      Finder finder = new Finder();
      finder.setText(this.readFromPerekrestok.getParserFinder().getText().toString());
      String mathes = finder.getAllMathes(textOfMessage);
      if (mathes == null || mathes.equals("")) {
        outMess.setText("Нет такого товара, найдите другой!");
        execute(outMess);
        return;
      }
      outMess.setText(mathes);
      execute(outMess);
      currentCommand.put(true, "Default command");//ставим дефолтную команду,
      // которая никак не обрабатывается
      // и попадет в else
    } else {//(Default command,/help,/start) //если команды выполнены, а пользователь что-то пишет
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
    String token = getTokenFromEnvironmentVariables();
    if (token != null) {
      return token;
    } else {
      System.out.println("Not token in enviroment variables");
      exit(-1);
      return null;
    }
  }

  private void mainLogic(String textOfMessage, long chat_id, String user_uniq_nick) {
    bot_holding_base.registateClient(user_uniq_nick);
    tempClient = bot_holding_base.signIN(user_uniq_nick);

    if (isCommand(textOfMessage)) {
      fixUsingCommand(textOfMessage);
      try {
        sendFirstTextOfCommand(textOfMessage,
            chat_id); //отправляем первое сообщение и завершаем логику
      } catch (TelegramApiException e) {
        throw new RuntimeException(e);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return;
    }
    String command = currentCommand.get(true);//смотрим, какая команда используется
    try {
      doCommandLogic(command, textOfMessage, chat_id);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }

  }


  Parser readFromPerekrestok = new Parser();

  @Override
  public void onUpdateReceived(Update update) {
    bot_holding_base.readToLocalBase();
    var msg = update.getMessage();
    String textOfMessage = null;
    if (msg == null) {
      if (isUserTouchButton(update)) {
        textOfMessage = update.getCallbackQuery().getData();
        var user = update.getCallbackQuery().getFrom();
        long chat_id = update.getCallbackQuery().getMessage().getChatId();
        String user_uniq_nick = user.getUserName();
        mainLogic(textOfMessage, chat_id, user_uniq_nick);
      }
    } else {
      textOfMessage = msg.getText();
      out.println(textOfMessage);
      var user = msg.getFrom();
      long chat_id = msg.getChatId();
      out.println(user.getUserName());
      String user_uniq_nick = user.getUserName();
      mainLogic(textOfMessage, chat_id, user_uniq_nick);
    }
    bot_holding_base.updateToJSONBase();

  }

  private boolean isUserTouchButton(Update update) {
    if (update.hasCallbackQuery()) {
      return true;
    }
    return false;
  }

  private String readUsingFiles(String fileName) throws IOException {
    return new String(Files.readAllBytes(Paths.get(fileName)));
  }

  private void sendCommandsMenu(Long number_of_chat) {
    creatorMenu.createCommandsMenu(number_of_chat);
    SendMessage sm = creatorMenu.getSm();
    try {
      execute(sm);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  private String getTokenFromEnvironmentVariables() {
    Map<String, String> env = System.getenv();
    for (String envName : env.keySet()) {
      if (envName.equals("TOKEN_TELE")) {
        return env.get(envName);
      }
    }
    return null;
  }

  private InlineKeyboardMarkup keyboard1;
  private InteractiveMenuCreator creatorMenu = new InteractiveMenuCreator();
}