package InvestHelper;

import static java.lang.System.exit;
import static java.lang.System.out;

import InvestHelper.BaseOfClients;
import InvestHelper.CommandHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class TeleBot extends TelegramLongPollingBot {

  private final BaseOfClients botHoldingBase = new BaseOfClients();

  //Менеджер потоков следит, чтобы каждому потоку выделялся свой обработчик команд
  private final Map<String, CommandHandler> managerOfThreads = new HashMap<>();

  //Менеджер команд у пользователя
  private final Map<String, String> managerOfThreadCommands = new HashMap<>();

  private void sendFirstTextOfCommand(String command, Long chatID)
      throws TelegramApiException, IOException {
    SendMessage outMess = new SendMessage();
    outMess.setChatId(chatID.toString());
    try {
      CommandHandler commandHandler;
      //выбираем конкретного клиента
      commandHandler = managerOfThreads.get(String.valueOf(chatID));
      commandHandler.handleFirstTextOfCommand(command, chatID,
          botHoldingBase.signIN(String.valueOf(chatID)));
      outMess = commandHandler.getOutMess();//Переименуешь тут как нужно
      execute(outMess);
      if (commandHandler.isStillExecutable()) {
        if (!command.equals("/AddStock")) {
          outMess.setText(commandHandler.stillExecutableMethodForQuotesReturn());
          execute(outMess);
        } else {
          //считывает акции, чтобы пользователь мог добавить даже без просмотра
          commandHandler.parseStocks();
        }

      }
    } catch (Exception e) {
      //тут просто дописать какой ex выкинуть
    }

  }

  private void doCommandLogic(String command, String textOfMessage, Long chatId)
      throws TelegramApiException {
    try {
      CommandHandler commandHandler = managerOfThreads.get(String.valueOf(chatId));
      //commandHandler.doCommandLogic(command, textOfMessage, chatId, tempClient);
      commandHandler.doCommandLogic(command, textOfMessage, chatId,
          botHoldingBase.signIN(String.valueOf(chatId)));
      SendMessage outPutMess = commandHandler.getOutMess();//Переименуешь тут как нужно\
      execute(outPutMess);
    } catch (Exception ex) {
      ex.printStackTrace();
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

  private void mainLogic(String textOfMessage, long chatId) {
    String defaultCommand = new String("Default command");
    botHoldingBase.registateClient(String.valueOf(chatId));
    //если уже содержит, то у него уже есть обработчик
    if (!managerOfThreadCommands.containsKey(String.valueOf(chatId)))
      managerOfThreads.put(String.valueOf(chatId), new CommandHandler());
    CommandHandler commandHandler1;
    commandHandler1 = managerOfThreads.get(String.valueOf(chatId));
    if (commandHandler1.isCommand(textOfMessage)) {
      managerOfThreadCommands.put(String.valueOf(chatId), textOfMessage);
      try {
        sendFirstTextOfCommand(textOfMessage,
            chatId); //отправляем первое сообщение и завершаем логику
      } catch (TelegramApiException e) {
        throw new RuntimeException(e);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      //!//
      return;
    }
    try {
      doCommandLogic(managerOfThreadCommands.get(String.valueOf(chatId)), textOfMessage, chatId);
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
      SendMessage outMess = new SendMessage();
      if (isUserTouchButton(update)) {
        textOfMessage = update.getCallbackQuery().getData();
        var user = update.getCallbackQuery().getFrom();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        mainLogic(textOfMessage, chatId);
      }
    } else {
      textOfMessage = msg.getText();
      out.println(textOfMessage);
      var user = msg.getFrom();
      long chatId = msg.getChatId();
      out.println(user.getUserName());
      mainLogic(textOfMessage, chatId);
    }
    botHoldingBase.updateToJSONBase();

  }

  private boolean isUserTouchButton(Update update) {
    if (update.hasCallbackQuery()) {
      return true;
    }
    return false;
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