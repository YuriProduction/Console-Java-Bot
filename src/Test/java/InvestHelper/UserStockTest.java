package InvestHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserStockTest {

  UserStock ourStock;

  @BeforeEach
  public void prepareData() {

    ourStock = new UserStock(0, 0);
  }

  @ParameterizedTest
  @ValueSource(doubles = {165.05, 1378.09})
  void getStockPrice(Double price) {
    ourStock.setStockPrice(price);
    assertEquals(price, ourStock.getStockPrice());
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 10, 1000})
  void getCountStocks(int count) {
    ourStock.setCountStocks(count);
    assertEquals(count, ourStock.getCountStocks());
  }
}