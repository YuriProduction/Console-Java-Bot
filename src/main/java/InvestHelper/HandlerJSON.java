package InvestHelper;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class HandlerJSON {

  //Управляет считыванием и обновлением JSON-базы

  public void readBase(Map<String, Client> base) {
    try (FileReader fileReader = new FileReader(
        "D:\\JAVA\\UNIVERSITY\\Bot_consol\\ConsolniyBot\\text.json")) {
      Path file = Paths.get("D:\\JAVA\\UNIVERSITY\\Bot_consol\\ConsolniyBot\\text.json");
      String input = Files.readString(file);
      System.out.println(input);
      Client tempClient = new Client();//новый клиент в словарь
      JSONObject jsonObject = (JSONObject) JSONValue.parse(input);
      JSONArray jsonArray = (JSONArray) jsonObject.get("Base");

      for (Object item : jsonArray) {
        tempClient = new Client();
        JSONObject map = (JSONObject) item;
        String userID = (String) map.get("ID");
        String date = (String) map.get("Date");
        tempClient.setDate(date);
        int totalINCOME = (int)(long)(map.get("totalINCOME"));
        int totalEXPENSES = (int)(long)(map.get("totalEXPENSES"));
        tempClient.setTotalEXPENSES(totalEXPENSES);
        tempClient.setTotalINCOME(totalINCOME);
        JSONArray jsonArray1 = (JSONArray) map.get("Stocks");
        for (Object item1 : jsonArray1) {
          JSONObject map1 = (JSONObject) item1;
          String company = (String) map1.get("Company");
          double priceOneStock = (Double) map1.get("PriceOneStock");
          long countOfStocks = (Long) map1.get("Count");
          tempClient.addStockToInvestPortfolioForJsonReading(company, (int) countOfStocks, priceOneStock);
        }

        if (!base.containsKey(userID)) {//если первый раз считываем или записали нового
          base.put(userID, tempClient);
        }
      }

    } catch (
        Exception ex) {
      ex.printStackTrace();
    }
  }


  public void updateBase(Map<String, Client> base) {
    try (FileWriter file = new FileWriter(
        "D:\\JAVA\\UNIVERSITY\\Bot_consol\\ConsolniyBot\\text.json")) {

      JSONObject mainObj = new JSONObject();
      JSONArray mp = new JSONArray();
      Client tempClient = new Client();
      HashMap<String, UserStock> tempStockBase;
      for (Map.Entry<String, Client> entry : base.entrySet()) {
        JSONObject obj = new JSONObject();
        String userID = entry.getKey();
        tempClient = entry.getValue();
        tempStockBase = tempClient.mapForJSON();
        obj.put("ID", userID);
        obj.put("Date", new Date().toString());
        obj.put("totalINCOME",tempClient.getTotalINCOME());
        obj.put("totalEXPENSES",tempClient.getTotalEXPENSES());
        JSONArray stocks = new JSONArray();
        for (Map.Entry<String, UserStock> entry1 : tempStockBase.entrySet()) {
          JSONObject obj1 = new JSONObject();
          obj1.put("Company", entry1.getKey());
          System.out.println(entry1.getKey());
          UserStock tempUserStock = entry1.getValue();
          obj1.put("Count", tempUserStock.getCountStocks());
          obj1.put("PriceOneStock", tempUserStock.getStockPrice());
          stocks.add(obj1);
        }
        obj.put("Stocks", stocks);
        mp.add(obj);
      }
      mainObj.put("Base", mp);
      file.write(mainObj.toJSONString());
    } catch (Exception ex) {
      ex.printStackTrace();
      System.out.println("Not today");
    }

  }

}
