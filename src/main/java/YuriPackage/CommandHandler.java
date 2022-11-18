package YuriPackage;

import static java.lang.System.out;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class CommandHandler {

  //Управляет командами, приходящими от польователя

  public SendMessage getOutMess() {
    return outMess;
  }

  public void setOutMess(SendMessage outMess) {
    this.outMess = outMess;
  }

  private SendMessage outMess = new SendMessage();

  public SendMessage getOutMessforButtons() {
    return outMessforButtons;
  }

  private SendMessage outMessforButtons = null;

  public Map<String, List<String>> getCategories() {
    return categories;
  }

  public void setCategories(Map<String, List<String>> categories) {
    this.categories = categories;
  }

  public Client getTempClient() {
    return tempClient;
  }

  public void setTempClient(Client tempClient) {
    this.tempClient = tempClient;
  }

  public Map<Boolean, String> getCurrentCommand() {
    return currentCommand;
  }

  public void setCurrentCommand(Map<Boolean, String> currentCommand) {
    this.currentCommand = currentCommand;
  }

  Parser readFromPerekrestok = new Parser();
  private Map<Boolean, String> currentCommand = new HashMap<Boolean, String>();

  private Client tempClient = new Client();

  private InteractiveMenuCreator creator = new InteractiveMenuCreator();

  private Map<String, List<String>> categories = new HashMap<>();

  private int tempSUM = 0;

  private boolean sumIsAdded = false;

  void handleFirstTextOfCommand(String command, Long chatID)
      throws TelegramApiException, IOException {
    this.outMess = new SendMessage();
    outMess.setChatId(chatID.toString());
    if (command.equals("/start")) {
      outMess.setText(
          "Привет \uD83D\uDC4B, меня зовут Финес. Я твой личный бот-финансист \uD83D\uDCB0."
              + "\nЖми /help, если хочешь узнать на что я способен \uD83E\uDDBE");

    } else if (command.equals("/help")) {
      outMess.setText(
          "\n" +
              "1)Введите \"/start\" чтобы начать работу с ботом\n" +
              "2)Введите \"/help\" чтобы получить список комад\n" +
              "3)Введите \"/add\" чтобы добавить товар в корзину\n" +
              "4)Введите \"/limit\" чтобы установить лимит на покупки\n" +
              "5)Введите \"/statistics\" чтобы показать стоимость корзины и ваш остаток\n"
              +
              "6)Введите \"/menu\" чтобы открыть интерактивное меню\n"
              + "7)Введите \"/products_and_prices\" чтобы посмотреть текущие цены"
              + "на товары в магазине \"Перекресток\"\n"

      );
    } else if (command.equals("/add")) {
      outMess.setText("Введите сумму");
    } else if (command.equals("/find")) {
      outMessforButtons = null;
      outMess.setText("Введите товар(например, \"Молоко\")");
    } else if (command.equals("/limit")) {
      outMess.setText("Введите сумму, за пределы которой ваши расходы не должны сегодня выходить");
    } else if (command.equals("/statistics")) {
      String stat = tempClient.showStatistic();
      outMess.setText(stat);
      currentCommand.put(true, "Default command");//ставим дефолтную команду,
    } else if (command.equals("/menu")) {
      creator.createCommandsMenu(chatID);
      outMess = creator.getSm();
      currentCommand.put(true, "Default command");//ставим дефолтную команду,
    } else if (command.equals("/products_and_prices")) {

      //outMess.setText("Вычисляем статистику, немного подождите...");

      String result_prod_and_prices = sendDataForUser();
      outMess.setText(result_prod_and_prices);
      outMess.setText(
          result_prod_and_prices + "Введите /find, чтобы найти какой-то конкретный товар");

      this.outMessforButtons = new SendMessage();
      creator.createKeyboardCategoriesToUser(chatID);
      outMessforButtons = creator.getSm();
      outMessforButtons.setChatId(chatID.toString());
      currentCommand.put(true, "Default command");//ставим дефолтную команду
    } else if (command.equals("/categories")) {

      creator.createKeyboardCategoriesToUser(chatID);
      outMess = creator.getSm();
      String result_prod_and_prices = sendDataForUser();
    } else if (command.equals("От Перекрёстка") || command.equals("С днём вегана")
        || command.equals("Молоко, сыр, яйца") || command.equals("Макароны, крупы, масло, специи")
        || command.equals("Овощи, фрукты, грибы") || command.equals("Готовая еда")) {
      if (categories == null) {
        outMess.setText(
            "Сначала обновите страницу, для этого выберите пункт \"Посмотреть текущие цены на товары в магазине \"Перекресток\" \" ");
        return;
      }
      String prod_of_suit_category = extractCategory(command);
      outMess.setText(prod_of_suit_category);
      out.println("Command of category!");
      currentCommand.put(true, "Default command");//ставим дефолтную команду
    } else {
      outMess.setText("Сообщение не распознано");
    }
  }

  void doCommandLogic(String command, String textOfMessage, Long chatID)
      throws TelegramApiException {
    this.outMess = new SendMessage();
    outMess.setChatId(chatID.toString());
    if (command.equals("/add")) {
      //мы знаем, что первое сообщение уже отправлено
      //"Введите сумму" добавлено
      if (!sumIsAdded) {
        tempSUM = Integer.parseInt(textOfMessage);
        //Если сумма еще не добавлена - просим добавить
        //addSum(text)
        //просим ввести товар
        outMess.setText("Введите товар");
        sumIsAdded = true;
      } else {
        //addGood(text)
        //товар добавлен, затираем переменную
        String tempGOOD = textOfMessage;
        tempClient.addExpenses(tempSUM, tempGOOD);//добавляем расходы
        if (tempClient.getOVERFLOW()) {
          outMess.setText("Вы выходите за пределы установленной суммы");
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
      currentCommand.put(true, "Default command");//ставим дефолтную команду,
      // которая никак не обрабатывается
      // и попадет в else
    } else if (command.equals("/find")) {
      //setLimit(text)
      outMessforButtons = null;
      Finder finder = new Finder();
      finder.setText(this.readFromPerekrestok.getParserFinder().getText().toString());
      String mathes = finder.getAllMathes(textOfMessage);
      if (mathes == null || mathes.equals("")) {
        outMess.setText("Нет такого товара, найдите другой!");
        return;
      }
      outMess.setText(mathes);
      currentCommand.put(true, "Default command");//ставим дефолтную команду,
      // которая никак не обрабатывается
      // и попадет в else
    } else {//(Default command,/help,/start) //если команды выполнены, а пользователь что-то пишет
      outMess.setText("Вся логика выполнена. Команды перед вами. Делайте что хотите");
    }
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
}
