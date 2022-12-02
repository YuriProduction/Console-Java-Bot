package InvestHelper;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ClientTest {

  Client ourClient;
  StatisticComputer statClient;

  ParserStocks stockCur;

  @BeforeEach
  public void prepareData() {

    ourClient = new Client();
    statClient = new StatisticComputer();
    stockCur = new ParserStocks();
  }


  @Test
  void testDate() {
    Date date = new Date();
    ourClient.setDate(date.toString());
    Assertions.assertEquals(date.toString(), ourClient.getDate());
  }

  @ParameterizedTest(name = "№{index} -> Акция = {0} \\ Цена акции = {1}")
  @CsvSource(value = {
      "Газпром, 165.02",
      "Газпром, 300.01",
      "Сбербанк, 100.02"
  })
  void getInvestmentPortfolio(String name, Double price) throws IOException {
    DecimalFormat df = new DecimalFormat("#.###");
    stockCur.parseQuotesData();
    String nameCompany = name;
    int countOfStock = 2;
    Double stockPrice = price;
    ourClient.addStockToInvestPortfolio(nameCompany, countOfStock, stockPrice);
    Double diffPrice = stockCur.getPriceOfStock(nameCompany) - stockPrice;
    Double ourStocks = countOfStock * diffPrice;
    Double profit = diffPrice * countOfStock;

    String sign = "";
    if (diffPrice <= 0) {
      sign = "\uD83D\uDD3B";
    } else {
      sign = "\uD83D\uDD3A";
    }

    Assertions.assertEquals("\n1) - Название компании\n"
            + "2) - Разница между текущей стоиомостью одной акции комании и стоимостью, по которой покупали вы\n"
            + "3) Общие показатели акции с учетом количества\n"
            + "\n"
            + "1)"+name+" 2) " + df.format(diffPrice) + "руб " + sign + " 3) " + df.format(ourStocks)
            + "руб\n"
            + "Общий доход активов: " + df.format(profit) + "",
        statClient.computeStatistics(ourClient.getInvestmentPortfolio(), stockCur.getQuotes()));
  }

  @ParameterizedTest(name = "№{index} -> Акция = {0} \\ Цена акции = {1}")
  @CsvSource(value = {
      "Газпром, 165.02",
      "Газпром, 300.01",
      "Сбербанк, 100.02"
  })
  void addStockToInvestPortfolio(String name, Double price) throws IOException {

    DecimalFormat df = new DecimalFormat("#.###");
    stockCur.parseQuotesData();
    String nameCompany = name;
    int countOfStock = 2;
    Double stockPrice = price;
    ourClient.addStockToInvestPortfolio(nameCompany, countOfStock, stockPrice);
    Double diffPrice = stockCur.getPriceOfStock(nameCompany) - stockPrice;
    Double ourStocks = countOfStock * diffPrice;
    Double profit = diffPrice * countOfStock;

    String sign = "";
    if (diffPrice <= 0) {
      sign = "\uD83D\uDD3B";
    } else {
      sign = "\uD83D\uDD3A";
    }

    Assertions.assertEquals("\n1) - Название компании\n"
            + "2) - Разница между текущей стоиомостью одной акции комании и стоимостью, по которой покупали вы\n"
            + "3) Общие показатели акции с учетом количества\n"
            + "\n"
            + "1)"+name+" 2) " + df.format(diffPrice) + "руб " + sign + " 3) " + df.format(ourStocks)
            + "руб\n"
            + "Общий доход активов: " + df.format(profit) + "",
        statClient.computeStatistics(ourClient.getInvestmentPortfolio(), stockCur.getQuotes()));

    ourClient.addStockToInvestPortfolio(nameCompany, countOfStock, stockPrice);

    Assertions.assertEquals("\n1) - Название компании\n"
            + "2) - Разница между текущей стоиомостью одной акции комании и стоимостью, по которой покупали вы\n"
            + "3) Общие показатели акции с учетом количества\n"
            + "\n"
            + "1)"+name+" 2) " + df.format(diffPrice) + "руб " + sign + " 3) " + df.format(ourStocks*2)
            + "руб\n"
            + "Общий доход активов: " + df.format(profit*2) + "",
        statClient.computeStatistics(ourClient.getInvestmentPortfolio(), stockCur.getQuotes()));

  }
}