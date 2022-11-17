package YuriPackage;

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

public class Bot { //implements Bootable,ReadAndWrite

  //отвечает за корректную запись в Map клиентов

  private Map<String, Client> base = new HashMap<>();

  private HandlerJSON handlerJSON = new HandlerJSON();

  void registateClient(String nick) {
    if (!base.containsKey(nick)) {
      base.put(nick,
          new Client());//ЕСЛИ СОДЕРЖИТ - ТО НИЧЕ НЕ ДЕЛАЕМ, ТАК КАК ОН СПОКОЙНО ВОЙДЕТ В СИСТЕМУ
    }
  }

  Client signIN(String nick) {
    return base.get(nick); //так как он зарегистрирован на шаге выше
  }

  void readToLocalBase() {
    handlerJSON.readBase();
    this.base = handlerJSON.getBase();
  }


  void updateToJSONBase() {
    handlerJSON.updateBase();
  }

}
