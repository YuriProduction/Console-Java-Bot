import YuriPackage.Client;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ClientTest {

  Client ourClient;

  @BeforeEach
  public void prepareData() {

    ourClient = new Client();
  }

  @ParameterizedTest(name = "№{index} -> Expenses = {0}")
  @ValueSource(ints = {100, 200})
  void testAddExpenses(int Expenses) {

    try {
      Method addEx = Client.class.getDeclaredMethod("addExpenses", int.class, String.class);
      Method Limit = Client.class.getDeclaredMethod("setLimit", int.class);
      Method Stats = Client.class.getDeclaredMethod("showStatistic");
      Method ost = Client.class.getDeclaredMethod("getOstat");

      addEx.setAccessible(true);
      Limit.setAccessible(true);
      Stats.setAccessible(true);
      ost.setAccessible(true);

      Limit.invoke(ourClient, 1000);

      addEx.invoke(ourClient, Expenses, "moloko");
      Assertions.assertEquals("Product - moloko| Expenses = " + Expenses + "\n"
          + "Your ostat = " + ost.invoke(ourClient) + "\n", Stats.invoke(ourClient));

      addEx.invoke(ourClient, Expenses, "sausage");
      Assertions.assertEquals("Product - moloko| Expenses = " + Expenses + "\n"
          + "Product - sausage| Expenses = " + Expenses + "\n"
          + "Your ostat = " + ost.invoke(ourClient) + "\n", Stats.invoke(ourClient));
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }

  @ParameterizedTest(name = "№{index} -> Limit = {0}")
  @ValueSource(ints = {1734, 2020, 100001, 222222, Integer.MAX_VALUE})
  void testSetLimit(int Limit) {
    try {
      Method setLim = Client.class.getDeclaredMethod("setLimit", int.class);
      Method ost = Client.class.getDeclaredMethod("getOstat");

      setLim.setAccessible(true);
      ost.setAccessible(true);

      setLim.invoke(ourClient, Limit);
      Assertions.assertEquals(Limit, ost.invoke(ourClient));
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }

  @ParameterizedTest(name = "№{index} -> Sum = {0}")
  @ValueSource(ints = {300,560,999})
  void testCanAdd(int sum){
    try {
      Method canAdd = Client.class.getDeclaredMethod("canAdd", int.class);
      Method Limit = Client.class.getDeclaredMethod("setLimit", int.class);

      canAdd.setAccessible(true);
      Limit.setAccessible(true);

      Limit.invoke(ourClient, 1000);

      Assertions.assertTrue(Boolean.parseBoolean(canAdd.invoke(ourClient, sum).toString()));

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

}