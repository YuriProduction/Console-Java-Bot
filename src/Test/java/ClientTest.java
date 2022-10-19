import YuriPackage.Client;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
//import org.junit.jupiter.api.BeforeEach;
import static org.junit.Assert.assertEquals;

public class ClientTest {

  Client newClient;

  @BeforeEach
  public void prepareData() {

    newClient = new Client();
  }

  @Test
  public void addExpenses() {
//    сверения статистики после нескольких
//    пользовательских операций
//    с ожидаемым остатком

    newClient.setLimit(1000);
    newClient.addExpenses(100, "sausage");
    assertEquals(900, newClient.getOstat());
    newClient.addExpenses(300, "milk");
    assertEquals(600, newClient.getOstat());
    newClient.addExpenses(500, "bread");
    assertEquals(100, newClient.getOstat());
  }


  @Test
  public void setLimit() {
    newClient.setLimit(1000);
    assertEquals(1000, newClient.getOstat());
    newClient.setLimit(10000000);
    assertEquals(10000000, newClient.getOstat());
  }

  @ParameterizedTest
  @ValueSource(strings = {"1428"})
  public void distributionPeriod(String value) {
    newClient.setLimit(10000);
    Assertions.assertEquals(Integer.parseInt(value), newClient.distributionPeriod(7));
//    assertEquals(333, newClient.distributionPeriod(30));
//    assertEquals(166, newClient.distributionPeriod(60));
  }


}