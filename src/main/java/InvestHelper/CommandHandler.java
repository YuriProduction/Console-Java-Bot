package InvestHelper;

import static java.lang.System.out;


import java.io.IOException;
import java.util.Date;
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

  private SendMessage outMess = new SendMessage();

  public Map<Boolean, String> getCurrentCommand() {
    return currentCommand;
  }

  public void setCurrentCommand(Map<Boolean, String> currentCommand) {
    this.currentCommand = currentCommand;
  }

  private final ReplyKeyboardMaker keyboardMaker = new ReplyKeyboardMaker();
  private Map<Boolean, String> currentCommand = new HashMap<Boolean, String>();

  public boolean isStillExecutable() {
    return stillExecutable;
  }

  private boolean stillExecutable = false;

  public void handleFirstTextOfCommand(String command, Long chatID, Client tempClient)
      throws IOException {
    this.outMess = new SendMessage();
    outMess.setChatId(chatID.toString());
    if (command.equals("/start")) {
      outMess.setReplyMarkup(keyboardMaker.setReplyKeyboardMarkup(command));
      outMess.setText(
          "Привет \uD83D\uDC4B, меня зовут Финес. Я твой личный бот-финансист \uD83D\uDCB0."
              + "\nЖми /help, если хочешь узнать на что я способен \uD83E\uDDBE");

    } else if (command.equals("/help")) {
      outMess.setReplyMarkup(keyboardMaker.setReplyKeyboardMarkup(command));
      outMess.setText(
          "\n" +
              "1)Введите \"/StockPrices\" чтобы посмотреть цены всех акций на фондовой бирже\n" +
              "2)Введите \"/AddStock\" чтобы добавить конкретную акцию\n" +
              "3)Введите \"/help\" чтобы получить помощь\n" +
              "4)Введите \"/Statistic\" чтобы получить статистику по текущим инвестицям\n" +
              "5)Введите \"/Sell\" чтобы продать акции конкретной компании\n"
      );
    } else if (command.equals("/AddStock")) {
      outMess.setText("Введите команию, акции которой вы хотите приобрести");
      stillExecutable = true;
      nameOfcompanyIsSentByUser = false;
    } else if (command.equals("/StockPrices")) {
      outMess.setText("Начинаем загрузку данных с биржи...Немного подождите");
      stillExecutable = true;
    } else if (command.equals("/Statistic")) {
      parserStocks.parseQuotesData();
      String stat = statisticComputer.computeStatistics(tempClient.getInvestmentPortfolio(),
          parserStocks.getQuotes());
      stat += "\nКуплено акций на сумму: " + tempClient.getTotalEXPENSES() + "руб\n";
      stat += "Заработано на продаже акций: " + tempClient.getTotalINCOME() + "руб\n";
      stat += "Баланс: " + (tempClient.getTotalINCOME() - tempClient.getTotalEXPENSES()) + "руб";
      StringBuilder stringBuilder = parserStocks.getTextForUserAboutQuotes();
      outMess.setText(stat);

    } else if (command.equals("/Sell")) {
      nameOfcompanyIsSentByUser = false;
      outMess.setText("Введите данные о компании, акции которой хотите продать.\n"
          + "Обратите внимание, что нужно ввести данные в том формате"
          + ", в котором бот выводил вам статистику (Пункт 1))");
      parserStocks.parseQuotesData();
    } else {
      outMess.setText("Сообщение не распознано");
    }
  }

  private final StatisticComputer statisticComputer = new StatisticComputer();
  private final ParserStocks parserStocks = new ParserStocks();

  public String stillExecutableMethodForQuotesReturn() throws IOException {
    stillExecutable = false;
    parserStocks.parseQuotesData();
    return parserStocks.getTextForUserAboutQuotes().toString();
  }

  public void parseStocks() throws IOException {
    stillExecutable = false;
    parserStocks.parseQuotesData();
    StringBuilder stringBuilder = parserStocks.getTextForUserAboutQuotes();
  }

  private boolean nameOfcompanyIsSentByUser = false;

  private String nameOfCompany = "Error company";


  public void doCommandLogic(String command, String textOfMessage, Long chatID, Client tempClient) {
    this.outMess = new SendMessage();

    outMess.setChatId(chatID.toString());
    if (command.equals("/AddStock")) {
      if (!nameOfcompanyIsSentByUser) {
        nameOfCompany = textOfMessage;
        out.println("Название компании: " + nameOfCompany);
        if (parserStocks.existsCompany(nameOfCompany)) {
          outMess.setText("Введите количество акций, которое вы желаете приобрести");
        } else {
          outMess.setText("Такой фирме на рынке нет или вы давно"
              + " не смотрели текущую ситуацию на бирже. "
              + "Выберите другую компанию "
              + "или нажмите \"/StockPrices\" и попробуйте еще раз");
          return;
        }

        nameOfcompanyIsSentByUser = true;
      } else {
        int countOfStocks = Integer.parseInt(textOfMessage);
        tempClient.addStockToInvestPortfolio(nameOfCompany, countOfStocks,
            parserStocks.getPriceOfStock(nameOfCompany));
        nameOfcompanyIsSentByUser = false;
        currentCommand.put(true, "Default command");//ставим дефолтную команду
        outMess.setText("Акции успешно добавлены!");
      }
    } else if (command.equals("/Sell")) {
      if (!nameOfcompanyIsSentByUser) {
        nameOfCompany = textOfMessage;
        out.println("Название компании: " + nameOfCompany);
        if (tempClient.haveSuchCompany(nameOfCompany)) {
          outMess.setText("Введите количество акций, которое вы желаете продать");
        } else {
          outMess.setText("Такой фирме нет в вашем инвестиционном портфеле!");
          return;
        }

        nameOfcompanyIsSentByUser = true;
      } else {
        int countOfStocks = Integer.parseInt(textOfMessage);
        if (!tempClient.correctCountOfActions(nameOfCompany, countOfStocks)) {
          outMess.setText("У вас нет столько акций! Введите число поменьше!");
        } else {
          String comp = nameOfCompany.split("_")[0];
          tempClient.sellStocks(nameOfCompany, countOfStocks, parserStocks.getPriceOfStock(comp));
          outMess.setText("Акции успешно проданы!");
        }

      }
    } else {//(Default command,/help,/s tart) //если команды выполнены, а пользователь что-то пишет
      outMess.setText("Вся логика выполнена. Команды перед вами. Делайте что хотите");
    }

  }

  public boolean isCommand(String argum) {
    for (String x : commandslist) {
      if (argum.equals(x)) {
        return true;
      }
    }
    return false;
  }

  private final String[] commandslist = new String[]{"/AddStock", "/start",
      "/help", "/StockPrices", "/Statistic", "/Sell"};

  private static int castDateToInt(String data) {
    char[] charData = data.toCharArray();
    String strDay = String.valueOf(charData[8]) + charData[9];
    return Integer.parseInt(strDay);
  }

}
