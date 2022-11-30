package InvestHelper;

import java.util.HashMap;
import java.util.Map;


public class Client {

  //Отвечает за запись клиента и его данных(расходы, лимит в базу)

  public void setDate(String date) {
    this.date = date;
  }

  String date;
  private final Map<String, UserStock> investmentPortfolio;

  public Client() {
    investmentPortfolio = new HashMap<String, UserStock>();
  }


  public void addStockToInvestPortfolio(String nameOfCompany, int countOfStock, double stockPrice) {
    if (!investmentPortfolio.containsKey(nameOfCompany)) {
      UserStock userStock = new UserStock(stockPrice, countOfStock);
      investmentPortfolio.put(nameOfCompany, userStock);
    } else {
      //увеличиваем число акций
      int prevCount = investmentPortfolio.get(nameOfCompany).getCountStocks();
      countOfStock += prevCount;
      UserStock userStock = new UserStock(stockPrice, countOfStock);
      investmentPortfolio.put(nameOfCompany, userStock);
    }
  }

  public HashMap<String, UserStock> mapForJSON()//нужно для запси в json-базу
  {
    return (HashMap<String, UserStock>) this.investmentPortfolio;
  }




}
