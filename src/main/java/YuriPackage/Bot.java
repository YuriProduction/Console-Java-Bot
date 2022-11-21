package YuriPackage;

import java.util.HashMap;
import java.util.Map;

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
    handlerJSON.readBase(base);
  }

  void updateToJSONBase() {
    handlerJSON.updateBase(base);
  }

}
