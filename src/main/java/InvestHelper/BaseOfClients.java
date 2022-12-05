package InvestHelper;

import java.util.HashMap;
import java.util.Map;


public class BaseOfClients { //implements Bootable,ReadAndWrite

  //отвечает за корректную запись в Map(базу) клиентов
  private final Map<String, Client> base = new HashMap<>();

  private final HandlerJSON handlerJSON = new HandlerJSON();

  public void registateClient(String userID) {
    if (!base.containsKey(userID)) {
      base.put(userID,
          new Client());//ЕСЛИ СОДЕРЖИТ - ТО НИЧЕ НЕ ДЕЛАЕМ, ТАК КАК ОН СПОКОЙНО ВОЙДЕТ В СИСТЕМУ
    }
  }

  public Client signIN(String userID) {
    return base.get(userID); //так как он зарегистрирован на шаге выше
  }

  public void readToLocalBase() {
    handlerJSON.readBase(base);
  }

  public void updateToJSONBase() {

    handlerJSON.updateBase(base);
  }

}
