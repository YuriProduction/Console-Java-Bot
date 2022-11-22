import static org.junit.jupiter.api.Assertions.assertTrue;

import YuriPackage.TeleBot;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TeleBotTest {

  TeleBot teleBot;

  @BeforeEach
  void setUp() {
    teleBot = new TeleBot();
  }

  @ParameterizedTest(name = "№{index} -> Command = {0}")
  @ValueSource(strings = {"/add", "/limit", "/statistics", "/start",
      "/help", "/menu", "/products_and_prices", "Молоко, сыр, яйца", "С днём вегана",
      "От Перекрёстка",
      "Макароны, крупы, масло, специи",
      "Овощи, фрукты, грибы", "Готовая еда"})
  void testIsCommand(String command) {
    try {
      Method method = TeleBot.class.getDeclaredMethod("isCommand", String.class);

      method.setAccessible(true);

      assertTrue(Boolean.parseBoolean(method.invoke(teleBot, command).toString()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}