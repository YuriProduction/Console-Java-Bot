package InvestHelper;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class StatisticComputer {

  //если компания ушла с биржи - ее доходность обнуляется
  public String computeStatistics(final Map<String, UserStock> clientInvestPortfolio,
      final
      Map<String, Double> currentStatusOfStockMarket) {
    String statToUser = "\n1) - Название компании\n"
        + "2) - Разница между текущей стоиомостью одной акции комании и "
        + "стоимостью, по которой покупали вы\n"
        + "3) Общие показатели акции с учетом количества\n\n";
    Double totalProfit = 0.0;
    String sign = "";
    double commonProfitFromCompany = 0.0;
    DecimalFormat df = new DecimalFormat("#.###");
    df.setRoundingMode(RoundingMode.CEILING);
    for
    (Map.Entry<String, UserStock> entry : clientInvestPortfolio.entrySet()) {
      String company = entry.getKey();
      UserStock stock = entry.getValue();
      int countOfStocks = stock.getCountStocks();
      Double priceOfClientOneStock = stock.getStockPrice();
      //Если компании уже нет на рынке
      if (!currentStatusOfStockMarket.containsKey(company)) {
        continue;
      }
      Double currentMarketPriceOfOneStock = currentStatusOfStockMarket.get(company);
      Double del = currentMarketPriceOfOneStock - priceOfClientOneStock;
      if (del > 0) {
        sign = "\uD83D\uDD3A";
      } else {
        sign = "\uD83D\uDD3B";
      }
      commonProfitFromCompany = del*countOfStocks;
      statToUser += "1)" + company + " 2) " +
          df.format(del)+"руб "+sign +
          " 3) " + df.format(commonProfitFromCompany) + "руб\n";
      totalProfit += commonProfitFromCompany;
    }
    statToUser += "Общий доход активов: " + df.format(totalProfit);
    return statToUser;
  }

}
