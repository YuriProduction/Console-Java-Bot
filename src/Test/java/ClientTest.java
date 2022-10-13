import YuriPackage.Client;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClientTest {

    private Client newClient;

    @Before
    public void setUp() throws IOException {
        newClient = new Client();
    }

    @Test
    public void addExpenses() {
        Client.limit = 1000;
        newClient.addExpenses(100, "sausage");
        newClient.addExpenses(300, "milk");
        newClient.addExpenses(500, "bread");
        newClient.showStatistic();
        //newClient.addExpenses(100, "spaghetti");
    }

    @Test
    public void setLimit() throws IOException {
        assertTrue(newClient.setLimit(1000));
        newClient.showStatistic();
        assertTrue(newClient.setLimit(10000000));
        newClient.showStatistic();
    }

    @Test
    public void distributionPeriod() {
        Client.limit = 10000;
        assertEquals(1428, newClient.distributionPeriod(7));
        assertEquals(333, newClient.distributionPeriod(30));
        assertEquals(166, newClient.distributionPeriod(60));
    }
}