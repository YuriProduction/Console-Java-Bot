package YuriPackage;

import static java.lang.System.exit;
import static java.lang.System.out;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class TeleBot extends TelegramLongPollingBot {

  private final Bot botHoldingBase = new Bot();
  private final String[] commandslist = new String[]{"/add", "/limit", "/statistics", "/start",
      "/help", "/menu", "/products_and_prices", "Молоко, сыр, яйца", "С днём вегана",
      "От Перекрёстка", "Макароны, крупы, масло, специи", "Овощи, фрукты, грибы", "Готовая еда",
      "/find", "/categories"};
  private Map<Boolean, String> currentCommand = new HashMap<Boolean, String>();
  private Client tempClient = new Client();
  private CommandHandler commandHandler = new CommandHandler();
  private InteractiveMenuCreator creatorMenu = new InteractiveMenuCreator();


  private void sendKeyboardCategoriesToUser(Long numberOfChat) {
    creatorMenu.createKeyboardCategoriesToUser(numberOfChat);
    //сендим клавиатуру
    SendMessage sendMessage = creatorMenu.getSendMessage();
    try {
      execute(sendMessage);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }


  private boolean isCommand(String argum) {
    for (String x : commandslist) {
      if (argum.equals(x)) {
        return true;
      }
    }
    return false;
  }

  private void fixUsingCommand(String argum) {
    currentCommand.put(true, argum);
  }

  private void sendFirstTextOfCommand(String command, Long chatID)
      throws TelegramApiException, IOException {
    SendMessage outMess = new SendMessage();
    outMess.setChatId(chatID.toString());
    try {
      commandHandler.handleFirstTextOfCommand(command, chatID);
      SendMessage outPutMess = commandHandler.getOutMess();//Переименуешь тут как нужно
      SendMessage outPutMessForYuri = commandHandler.getOutMessforButtons();
      execute(outPutMess);
      if (outPutMessForYuri != null) {
        execute(outPutMessForYuri);

      }

    } catch (Exception e) {
      //тут просто дописать какой ex выкинуть
    }

  }

  private void doCommandLogic(String command, String textOfMessage, Long chatId)
      throws TelegramApiException {
    SendMessage outMess = new SendMessage();
    outMess.setChatId(chatId.toString());

    try {
      commandHandler.doCommandLogic(command, textOfMessage, chatId);
      SendMessage outPutMess = commandHandler.getOutMess();//Переименуешь тут как нужно
      execute(outPutMess);
    } catch (Exception e) {

    }

  }

  @Override
  public String getBotUsername() {
    return "Fines_and_Ferb_Fin_bot";
  }

  @Override
  public String getBotToken() {
    String token = getTokenFromEnvironmentVariables();
    if (token != null) {
      return token;
    } else {
      System.out.println("Not token in enviroment variables");
      exit(-1);
      return null;
    }
  }

  private void mainLogic(String textOfMessage, long chatId, String userUniqNick) {
    botHoldingBase.registateClient(userUniqNick);
    tempClient = botHoldingBase.signIN(userUniqNick);

    if (isCommand(textOfMessage)) {
      fixUsingCommand(textOfMessage);
      try {
        sendFirstTextOfCommand(textOfMessage,
            chatId); //отправляем первое сообщение и завершаем логику
      } catch (TelegramApiException e) {
        throw new RuntimeException(e);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return;
    }
    String command = currentCommand.get(true);//смотрим, какая команда используется
    try {
      doCommandLogic(command, textOfMessage, chatId);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public void onUpdateReceived(Update update) {
    botHoldingBase.readToLocalBase();
    var msg = update.getMessage();
    String textOfMessage = null;
    if (msg == null) {
      if (isUserTouchButton(update)) {
        textOfMessage = update.getCallbackQuery().getData();
        var user = update.getCallbackQuery().getFrom();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        String userUniqNick = user.getUserName();
        mainLogic(textOfMessage, chatId, userUniqNick);
      }
    } else {
      textOfMessage = msg.getText();
      out.println(textOfMessage);
      var user = msg.getFrom();
      long chatId = msg.getChatId();
      out.println(user.getUserName());
      String userUniqNick = user.getUserName();
      mainLogic(textOfMessage, chatId, userUniqNick);
    }
    botHoldingBase.updateToJSONBase();

  }

  private boolean isUserTouchButton(Update update) {
    if (update.hasCallbackQuery()) {
      return true;
    }
    return false;
  }
  //readUsingFiles посмотри Юра где его можно заюзать либо убери
  private String readUsingFiles(String fileName) throws IOException {
    return new String(Files.readAllBytes(Paths.get(fileName)));
  }

  //sendCommandsMenu посмотри Юра где его можно заюзать либо убери
  private void sendCommandsMenu(Long numberOfChat) {
    creatorMenu.createCommandsMenu(numberOfChat);
    SendMessage sm = creatorMenu.getSendMessage();
    try {
      execute(sm);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  private String getTokenFromEnvironmentVariables() {
    Map<String, String> env = System.getenv();
    for (String envName : env.keySet()) {
      if (envName.equals("TOKEN_TELE")) {
        return env.get(envName);
      }
    }
    return null;
  }

}