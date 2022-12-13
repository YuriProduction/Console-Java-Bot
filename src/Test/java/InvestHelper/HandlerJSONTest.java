package InvestHelper;

import java.util.Date;
import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class HandlerJSONTest {

  BaseOfClients baseOfClients;
  HandlerJSON handlerJSON;
  Client client;
  @BeforeEach

  void setUp() {
    baseOfClients = new BaseOfClients();
    client = new Client();
    handlerJSON = new HandlerJSON();
  }

  @ParameterizedTest
  @CsvSource(value = {
      "Сбербанк, 153.02, 12",
      "Газпром, 302.2, 12"
  })
  void testBase(String name, Double price, int count) {
    String path = "D:\\JAVA\\UNIVERSITY\\Bot_consol\\ConsolniyBot\\src\\main\\resources\\test.json";
    HashMap<String, Client> base = new HashMap<>();
    String userId = "7771118889";
    base.put(userId, client);
    client.addStockToInvestPortfolio(name, 12, price);
    handlerJSON.updateBase(base,path);
    String nameForJson = name+"_"+new Date();
    System.out.println(name);
    String expected = "{\"Base\":[{\"Stocks\":[{\"Company\":\""+nameForJson+"\",\"PriceOneStock\":"+price+",\"Count\":"+count+"}],\"totalINCOME\":"+ client.getTotalINCOME() +",\"ID\":\""+userId+"\",\"totalEXPENSES\":"+ client.getTotalEXPENSES()+",\"Date\":\""+new Date()+"\"}]}";
    String actual = handlerJSON.readBase(base, path);
    Assertions.assertEquals(expected, actual);
  }

}