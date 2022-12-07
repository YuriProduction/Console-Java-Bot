package TestClasses;

import InvestHelper.Client;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonClassTest {

  JsonClass testJson;
  Client ourClient;

  @BeforeEach
  void setUp() {
    testJson = new JsonClass();
    ourClient = new Client();

  }

  @Test
  void readBase() {
    Map<String, Client> testBase = new HashMap<>();
    String path = "D:\\JAVA\\UNIVERSITY\\Bot_consol\\ConsolniyBot\\src\\main\\resources\\test.json";
    String expectedValue = "{\"Base\":[{\"Stocks\":[{\"Company\":\"Газпром\",\"PriceOneStock\":166.89,\"Count\":230}],\"ID\":\"712512407\",\"Date\":\"Tue Dec 06 19:54:05 YEKT 2022\"},{\"Stocks\":[{\"Company\":\"ТМК ао\",\"PriceOneStock\":78.34,\"Count\":3},{\"Company\":\"Ростел\",\"PriceOneStock\":57.55,\"Count\":7},{\"Company\":\"Аэрофлот\",\"PriceOneStock\":23.82,\"Count\":5},{\"Company\":\"КАМАЗ\",\"PriceOneStock\":85.3,\"Count\":5},{\"Company\":\"Газпром\",\"PriceOneStock\":167.68,\"Count\":9},{\"Company\":\"ПИК\",\"PriceOneStock\":614.2,\"Count\":5}],\"ID\":\"1382884346\",\"Date\":\"Tue Dec 06 19:54:05 YEKT 2022\"},{\"Stocks\":[{\"Company\":\"Аэрофлот\",\"PriceOneStock\":24.82,\"Count\":17},{\"Company\":\"ЛСР\",\"PriceOneStock\":471.6,\"Count\":1}],\"ID\":\"1920982271\",\"Date\":\"Tue Dec 06 19:54:05 YEKT 2022\"}]}";
    testJson.readBase(testBase, path);
    Assertions.assertEquals(expectedValue, testJson.readBase(testBase, path));
  }

}