import YuriPackage.Client;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClientTest {

  private Client newClient;

  @Before
  public void setUp() {
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
  public void setLimit(){
    newClient.setLimit(1000);
    assertEquals(1000,newClient.getOstat());
    newClient.setLimit(10000000);
    assertEquals(10000000,newClient.getOstat());
  }

  @Test
  public void distributionPeriod() {
    newClient.setLimit(10000);
    assertEquals(1428, newClient.distributionPeriod(7));
    assertEquals(333, newClient.distributionPeriod(30));
    assertEquals(166, newClient.distributionPeriod(60));
  }


}