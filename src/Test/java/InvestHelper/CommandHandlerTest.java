package InvestHelper;

import java.io.IOException;
import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CommandHandlerTest {

  CommandHandler commandHandler;
  Client client;

  @BeforeEach
  void setUp() {
    commandHandler = new CommandHandler();
    client = new Client();

  }


  @Test
  void handleFirstTextOfCommand() throws IOException {
    Date date = new Date();
    client.setDate(String.valueOf(date));
    String chatId = "711111117";
    commandHandler.handleFirstTextOfCommand("/start", Long.valueOf(chatId), client);
    String actual = (commandHandler.getOutMess()).toString();
    String expected = "SendMessage(chatId="+Long.valueOf(chatId)+", text=Привет \uD83D\uDC4B, меня зовут Финес. Я твой личный бот-финансист \uD83D\uDCB0.\n"
        + "Жми /help, если хочешь узнать на что я способен \uD83E\uDDBE, parseMode=null, disableWebPagePreview=null, disableNotification=null, replyToMessageId=null, replyMarkup=ReplyKeyboardMarkup(keyboard=[[KeyboardButton(text=/help, requestContact=null, requestLocation=null, requestPoll=null, webApp=null)]], resizeKeyboard=true, oneTimeKeyboard=true, selective=null, inputFieldPlaceholder=null), entities=null, allowSendingWithoutReply=null, protectContent=null)";
    Assertions.assertEquals(expected, actual);
  }


  @Test
  void doCommandLogic() {
    String chatId = "711111117";
    commandHandler.doCommandLogic("/AddStock", "Сбербанк", Long.valueOf(chatId), client);
    String expected = "SendMessage(chatId="+chatId+", text=Такой фирме на рынке нет или вы давно не смотрели текущую ситуацию на бирже. Выберите другую компанию или нажмите \"/StockPrices\" и попробуйте еще раз, parseMode=null, disableWebPagePreview=null, disableNotification=null, replyToMessageId=null, replyMarkup=null, entities=null, allowSendingWithoutReply=null, protectContent=null)";
    String actual = (commandHandler.getOutMess()).toString();
    Assertions.assertEquals(expected, actual);
  }

  @ParameterizedTest
  @ValueSource(strings = {"/AddStock", "/start",
      "/help", "/StockPrices", "/Statistic"})
  void isCommand(String command) {
    Assertions.assertTrue(commandHandler.isCommand(command));
  }
}