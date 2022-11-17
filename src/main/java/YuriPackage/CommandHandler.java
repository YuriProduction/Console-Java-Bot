package YuriPackage;

import static java.lang.System.out;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class CommandHandler {

  public SendMessage getOutMess() {
    return outMess;
  }

  public void setOutMess(SendMessage outMess) {
    this.outMess = outMess;
  }

  private SendMessage outMess = new SendMessage();

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

  private Map<Boolean, String> currentCommand = new HashMap<Boolean, String>();

  private Client tempClient = new Client();

  private InteractiveMenuCreator creator = new InteractiveMenuCreator();

  private Map<String, List<String>> categories = new HashMap<>();

  //короче надо, чтоб в этот метод мог обработать команду
  //я не знаю как сделать, чтобы он мог ее отправлять
  // но можно просто чтоб хотя б обрабатывал и записывал это в перменную SendMessage
  //посмотри, как я сделал класс InteractiveMenuCreator
  //и как он применяется в TeleBot, надо сделать аналогично с этим классом
  //метод handleFirstTextOfCommand вроде не единственный обрабатывает команды
  //посмотри в телеботе какие еще методы обрабатывают команды
  // и допиши их сюда

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
//    } else if (command.equals("/products_and_prices")) {
//
//      outMess.setText("Вычисляем статистику, немного подождите...");
//      String result_prod_and_prices = this.sendDataForUser();
//      outMess.setText(result_prod_and_prices);
//      creator.createKeyboardCategoriesToUser(chatID);
//      outMess = creator.getSm();
//      outMess.setText("Введите /find, чтобы найти какой-то конкретный товар");
//      currentCommand.put(true, "Default command");//ставим дефолтную команду
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
