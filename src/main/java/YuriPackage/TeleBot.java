package YuriPackage;

import static java.lang.System.exit;
import static java.lang.System.out;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class TeleBot extends TelegramLongPollingBot {

  //Отвечает за взаимодейсвие с интерфейсом пользователя.
  //За маршрутизацию сообщений.
  private Map<Boolean, String> currentCommand = new HashMap<Boolean, String>();

  private final Bot bot_holding_base = new Bot();
  private Client tempClient = new Client();

  private InteractiveMenuCreator creator = new InteractiveMenuCreator();

  private CommandHandler cmd = new CommandHandler();

  private Map<String, List<String>> categories = null;

  private void sendKeyboardCategoriesToUser(Long number_of_chat) {
    creatorMenu.createKeyboardCategoriesToUser(number_of_chat);
    //сендим клавиатуру
    SendMessage sm = creatorMenu.getSm();
    try {
      execute(sm);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  private final String[] commandslist = new String[]{"/add", "/limit", "/statistics", "/start",
      "/help", "/menu", "/products_and_prices", "Молоко, сыр, яйца", "С днём вегана",
      "От Перекрёстка", "Макароны, крупы, масло, специи", "Овощи, фрукты, грибы", "Готовая еда",
      "/find", "/categories"};
  private boolean sumIsAdded = false;

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
      cmd.handleFirstTextOfCommand(command, chatID);
      SendMessage outPutMess = cmd.getOutMess();//Переименуешь тут как нужно
      SendMessage outPutMessForYuri = cmd.getOutMessforButtons();
      execute(outPutMess);
      if (outPutMessForYuri != null) {
        execute(outPutMessForYuri);

      }

    } catch (Exception e) {
      //тут просто дописать какой ex выкинуть
    }

  }

  private int tempSUM = 0;


  private void doCommandLogic(String command, String textOfMessage, Long chat_id)
      throws TelegramApiException {
    SendMessage outMess = new SendMessage();
    outMess.setChatId(chat_id.toString());

    try {
      cmd.doCommandLogic(command, textOfMessage, chat_id);
      SendMessage outPutMess = cmd.getOutMess();//Переименуешь тут как нужно
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

  private void mainLogic(String textOfMessage, long chat_id, String user_uniq_nick) {
    bot_holding_base.registateClient(user_uniq_nick);
    tempClient = bot_holding_base.signIN(user_uniq_nick);

    if (isCommand(textOfMessage)) {
      fixUsingCommand(textOfMessage);
      try {
        sendFirstTextOfCommand(textOfMessage,
            chat_id); //отправляем первое сообщение и завершаем логику
      } catch (TelegramApiException e) {
        throw new RuntimeException(e);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return;
    }
    String command = currentCommand.get(true);//смотрим, какая команда используется
    try {
      doCommandLogic(command, textOfMessage, chat_id);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }

  }


  Parser readFromPerekrestok = new Parser();

  @Override
  public void onUpdateReceived(Update update) {
    bot_holding_base.readToLocalBase();
    var msg = update.getMessage();
    String textOfMessage = null;
    if (msg == null) {
      if (isUserTouchButton(update)) {
        textOfMessage = update.getCallbackQuery().getData();
        var user = update.getCallbackQuery().getFrom();
        long chat_id = update.getCallbackQuery().getMessage().getChatId();
        String user_uniq_nick = user.getUserName();
        mainLogic(textOfMessage, chat_id, user_uniq_nick);
      }
    } else {
      textOfMessage = msg.getText();
      out.println(textOfMessage);
      var user = msg.getFrom();
      long chat_id = msg.getChatId();
      out.println(user.getUserName());
      String user_uniq_nick = user.getUserName();
      mainLogic(textOfMessage, chat_id, user_uniq_nick);
    }
    bot_holding_base.updateToJSONBase();

  }

  private boolean isUserTouchButton(Update update) {
    if (update.hasCallbackQuery()) {
      return true;
    }
    return false;
  }

  private String readUsingFiles(String fileName) throws IOException {
    return new String(Files.readAllBytes(Paths.get(fileName)));
  }

  private void sendCommandsMenu(Long number_of_chat) {
    creatorMenu.createCommandsMenu(number_of_chat);
    SendMessage sm = creatorMenu.getSm();
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

  private InlineKeyboardMarkup keyboard1;
  private InteractiveMenuCreator creatorMenu = new InteractiveMenuCreator();
}