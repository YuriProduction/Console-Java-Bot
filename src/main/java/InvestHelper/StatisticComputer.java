package InvestHelper;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Map.Entry;

public class StatisticComputer {

  //если компания ушла с биржи - ее доходность обнуляется
  public String computeStatistics(final Map<String, UserStock> clientInvestPortfolio,
      final
      Map<String, Double> currentStatusOfStockMarket) {
    String statToUser = "\n1) - Название компании\n"
        + "2) - Количество акций\n"
        + "3) - Разница между текущей стоиомостью одной акции комании и "
        + "стоимостью, по которой покупали вы\n"
        + "4) Общие показатели акции с учетом количества\n\n";
    Double totalProfit = 0.0;
    String sign = "";
//    System.out.println("Клиентская база: " + clientInvestPortfolio);
//    System.out.println("Текущие котировки: " + currentStatusOfStockMarket);
    double commonProfitFromCompany = 0.0;
    DecimalFormat df = new DecimalFormat("#.###");
    //df.setRoundingMode(RoundingMode.CEILING);
    for
    (Entry<String, UserStock> entry : clientInvestPortfolio.entrySet()) {
      String company = entry.getKey();
      //Чтобы сверил, есть ли такая на рынке
      String companyMarketName = company.split("_")[0];
      UserStock stock = entry.getValue();
      int countOfStocks = stock.getCountStocks();
      Double priceOfClientOneStock = stock.getStockPrice();
      //Если компании уже нет на рынке
      if (!currentStatusOfStockMarket.containsKey(companyMarketName)) {
        continue;
      }
      Double currentMarketPriceOfOneStock = currentStatusOfStockMarket.get(companyMarketName);
      Double del = currentMarketPriceOfOneStock - priceOfClientOneStock;

      if (del > 0) {
        sign = "\uD83D\uDD3A";
      } else {
        sign = "\uD83D\uDD3B";
      }
      commonProfitFromCompany = del*countOfStocks;
      statToUser += "1)" + new StringFormater().format(company) + "\n2) " + countOfStocks + ""
          + " акций " + "\n3) " +
          df.format(del)+"руб "+sign +
          "\n4) " + df.format(commonProfitFromCompany) + "руб\n\n\n";
      totalProfit += commonProfitFromCompany;
    }
    statToUser += "Общий прирост доходов от акций : " + df.format(totalProfit) + "\n\n\n";
    return statToUser;
  }

}
