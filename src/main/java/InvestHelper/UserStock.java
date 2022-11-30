package InvestHelper;

public class UserStock {

  private double stockPrice;

  public double getStockPrice() {
    return stockPrice;
  }

  public void setStockPrice(double stockPrice) {
    this.stockPrice = stockPrice;
  }

  public int getCountStocks() {
    return countStocks;
  }

  public void setCountStocks(int countStocks) {
    this.countStocks = countStocks;
  }

  private int countStocks;

  public UserStock(double stockPrice, int countStocks)
  {
    this.stockPrice = stockPrice;
    this.countStocks = countStocks;
  }
}
