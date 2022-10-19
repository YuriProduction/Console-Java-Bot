
import YuriPackage.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ClientTest {

  Client ourClient;

  @BeforeEach
  public void prepareData() {

    ourClient = new Client();
  }

  @Test
  void addExpenses() {
    ourClient.setLimit(1000);
    ourClient.addExpenses(100, "sausage");
    Assertions.assertEquals(900, ourClient.getOstat());
    ourClient.addExpenses(300, "milk");
    Assertions.assertEquals(600, ourClient.getOstat());
    ourClient.addExpenses(500, "bread");
    Assertions.assertEquals(100, ourClient.getOstat());
  }

  @Test
  void setLimit() {
    ourClient.setLimit(1000);
    Assertions.assertEquals(1000, ourClient.getOstat());
    ourClient.setLimit(10000000);
    Assertions.assertEquals(10000000, ourClient.getOstat());
  }

  @ParameterizedTest
  @ValueSource(strings = {"1428"})
  public void distributionPeriod(String value) {
    ourClient.setLimit(10000);
    Assertions.assertEquals(Integer.parseInt(value), ourClient.distributionPeriod(7));
//    assertEquals(333, newClient.distributionPeriod(30));
//    assertEquals(166, newClient.distributionPeriod(60));
  }
}