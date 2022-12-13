package InvestHelper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TeleBotTest {

  TeleBot teleBot;

  @BeforeEach
  void signIN()
  {
    teleBot = new TeleBot();
  }


  @Test
  void getBotToken() {
    String token = "No token";
    Map<String, String> env = System.getenv();
    for (String envName : env.keySet()) {
      if (envName.equals("TOKEN_TELE")) {
        token = env.get(envName);
      }
    }
    Assertions.assertEquals(token,teleBot.getBotToken());
  }
}