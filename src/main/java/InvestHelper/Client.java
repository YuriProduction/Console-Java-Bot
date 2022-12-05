package InvestHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Client {

  //Отвечает за запись клиента и его данных(расходы, лимит в базу)

  public void setDate(String date) {
    this.date = date;
  }

  public String getDate() {
    return date;
  }

  private String date;

  public Map<String, UserStock> getInvestmentPortfolio() {
    return investmentPortfolio;
  }

  private final Map<String, UserStock> investmentPortfolio;

  public Client() {
    investmentPortfolio = new HashMap<String, UserStock>();
  }


  public void addStockToInvestPortfolioForJsonReading(String nameOfCompany, int countOfStock, double stockPrice) {
      UserStock userStock = new UserStock(stockPrice, countOfStock);
      investmentPortfolio.put(nameOfCompany, userStock);
  }
  public void addStockToInvestPortfolio(String nameOfCompany, int countOfStock, double stockPrice) {
    UserStock userStock = new UserStock(stockPrice, countOfStock);
    investmentPortfolio.put(nameOfCompany + "_" + new Date().toString(), userStock);
  }

  public HashMap<String, UserStock> mapForJSON()//нужно для запси в json-базу
  {
    return (HashMap<String, UserStock>) this.investmentPortfolio;
  }




}
