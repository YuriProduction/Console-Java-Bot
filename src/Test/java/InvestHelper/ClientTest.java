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
  StringFormater stringFormater;

  @BeforeEach
  public void prepareData() {

    ourClient = new Client();
    statClient = new StatisticComputer();
    stockCur = new ParserStocks();
    stringFormater = new StringFormater();
  }


  @Test
  void testDate() {
    Date date = new Date();
    ourClient.setDate(date.toString());
    Assertions.assertEquals(date.toString(), ourClient.getDate());
  }

  @ParameterizedTest(name = "№{index} -> Акция = {0} \\ Цена акции = {1}")
  @CsvSource(value = {
      "Сбербанк, 100.02",
      "Газпром, 300.2"
  })
  void getInvestmentPortfolio(String name, Double price) throws IOException {
    DecimalFormat df = new DecimalFormat("#.###");
    stockCur.parseQuotesData();
    String nameCompany = name;
    int countOfStock = 2;
    Double stockPrice = price;
    ourClient.addStockToInvestPortfolioForTests(name, countOfStock, stockPrice);
    Double diffPrice = stockCur.getPriceOfStock(name) - stockPrice;
    Double ourStocks = countOfStock * diffPrice;
    Double profit = diffPrice * countOfStock;

    Double currentPrice = stockCur.getPriceOfStock(name);

    String sign = "";
    if (diffPrice <= 0) {
      sign = "\uD83D\uDD3B";
    } else {
      sign = "\uD83D\uDD3A";
    }


    String expected = "\n"
        + "1) - Название компании\n"
        + "2) - Цена покупки\n"
        + "3) - Текущая цена\n"
        + "4) - Разница между текущей стоиомостью одной акции комании и стоимостью, по которой покупали вы\n"
        + "5 - Общие показатели акции с учетом количества\n"
        + "6) - Количество акций \n"
        + "\n"
        + "1)"+stringFormater.format(nameCompany)
        + "\n2) "+stockPrice+"\n"
        + "3) "  + currentPrice + "\n"
        + "4) " + df.format(diffPrice) + "руб " + sign
        + "\n5) " + df.format(ourStocks) + "руб" + sign
        + "\n6) " + countOfStock + " акций"
        + "\n\n"
        + "\n"
        + "Общий прирост доходов от акций : "+df.format(profit)+"\n"
        + "\n"
        + "\n";
    Assertions.assertEquals(expected,
        statClient.computeStatistics(ourClient.getInvestmentPortfolio(), stockCur.getQuotes()));
  }

  @Test
  public void addStockToInvestPortfolio2()
  {
    ourClient.addStockToInvestPortfolio("Газпром",12,167.12);
    Assertions.assertEquals((int)ourClient.getTotalEXPENSES()-1,(int)167.12*12);
  }

  @Test
  public void sellStocks()
  {
    ourClient.addStockToInvestPortfolioForTests("Газпром",12,167.12);
    ourClient.sellStocks("Газпром",12,169.12);
    Assertions.assertEquals(ourClient.haveSuchCompany("Газпром"),false);
    Assertions.assertEquals(ourClient.getTotalINCOME()-ourClient.getTotalEXPENSES(),24);
  }


  @ParameterizedTest(name = "№{index} -> Акция = {0} \\ Цена акции = {1}")
  @CsvSource(value = {
      "Сбербанк, 100.02",
      "Газпром, 300.2"
  })
  void addStockToInvestPortfolio(String name, Double price) throws IOException {

    DecimalFormat df = new DecimalFormat("#.###");
    stockCur.parseQuotesData();
    String nameCompany = name;
    int countOfStock = 2;
    Double stockPrice = price;
    ourClient.addStockToInvestPortfolioForTests(nameCompany, countOfStock, stockPrice);
    ourClient.addStockToInvestPortfolioForTests(nameCompany, countOfStock, stockPrice);
    Double diffPrice = stockCur.getPriceOfStock(nameCompany) - stockPrice;
    Double ourStocks = countOfStock * diffPrice;
    Double profit = diffPrice * countOfStock;

    Double currentPrice = stockCur.getPriceOfStock(name);

    String sign = "";
    if (diffPrice <= 0) {
      sign = "\uD83D\uDD3B";
    } else {
      sign = "\uD83D\uDD3A";
    }

    String expected = "\n"
        + "1) - Название компании\n"
        + "2) - Цена покупки\n"
        + "3) - Текущая цена\n"
        + "4) - Разница между текущей стоиомостью одной акции комании и стоимостью, по которой покупали вы\n"
        + "5 - Общие показатели акции с учетом количества\n"
        + "6) - Количество акций \n"
        + "\n"
        + "1)"+stringFormater.format(nameCompany)
        + "\n2) "+stockPrice+"\n"
        + "3) "  + currentPrice + "\n"
        + "4) " + df.format(diffPrice) + "руб " + sign
        + "\n5) " + df.format(ourStocks) + "руб" + sign
        + "\n6) " + countOfStock + " акций"
        + "\n\n"
        + "\n"
        + "Общий прирост доходов от акций : "+df.format(profit)+"\n"
        + "\n"
        + "\n";



    Assertions.assertEquals(expected,
        statClient.computeStatistics(ourClient.getInvestmentPortfolio(), stockCur.getQuotes()));

  }
}