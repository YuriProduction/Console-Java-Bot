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

  public int getTotalEXPENSES() {
    return totalEXPENSES;
  }

  public void setTotalEXPENSES(int totalEXPENSES) {
    this.totalEXPENSES = totalEXPENSES;
  }

  public int getTotalINCOME() {
    return totalINCOME;
  }

  public void setTotalINCOME(int totalINCOME) {
    this.totalINCOME = totalINCOME;
  }

  private int totalEXPENSES;
  private int totalINCOME;

  public Map<String, UserStock> getInvestmentPortfolio() {
    return investmentPortfolio;
  }

  private final Map<String, UserStock> investmentPortfolio;

  public Client() {
    investmentPortfolio = new HashMap<String, UserStock>();
  }

  public boolean haveSuchCompany(String nameOfCompany) {
    return this.investmentPortfolio.containsKey(nameOfCompany);
  }

  public boolean correctCountOfActions(String nameOfCompany, int countOfStocks) {
    if (!haveSuchCompany(nameOfCompany)) {
      return false;
    }
    UserStock stock = investmentPortfolio.get(nameOfCompany);
    return countOfStocks <= stock.getCountStocks();
  }

  public void sellStocks(String nameOfCompany, int countOfStocksToSell,double priceOfOneStock) {
    if (!haveSuchCompany(nameOfCompany) || !correctCountOfActions(nameOfCompany,
        countOfStocksToSell)) {
      throw new RuntimeException();
    }
    int currentCountOfStocks = this.investmentPortfolio.get(nameOfCompany).getCountStocks();
    //уменьшается количество акций в портфеле
    this.investmentPortfolio.get(nameOfCompany)
        .setCountStocks(currentCountOfStocks - countOfStocksToSell);
    this.totalINCOME += priceOfOneStock*countOfStocksToSell;
    if (currentCountOfStocks - countOfStocksToSell == 0)
      this.investmentPortfolio.remove(nameOfCompany);
  }

  public void addStockToInvestPortfolioForJsonReading(String nameOfCompany, int countOfStock,
      double stockPrice) {
    UserStock userStock = new UserStock(stockPrice, countOfStock);
    investmentPortfolio.put(nameOfCompany, userStock);
  }

  public void addStockToInvestPortfolio(String nameOfCompany, int countOfStock, double stockPrice) {
    UserStock userStock = new UserStock(stockPrice, countOfStock);
    this.totalEXPENSES += stockPrice*countOfStock;
    investmentPortfolio.put(nameOfCompany + "_" + new Date().toString(), userStock);
  }

  public HashMap<String, UserStock> mapForJSON()//нужно для запси в json-базу
  {
    return (HashMap<String, UserStock>) this.investmentPortfolio;
  }


}
