package YuriPackage;

import static java.lang.System.exit;
import static java.lang.System.out;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class TeleBot extends TelegramLongPollingBot {

  private Map<Boolean, String> currentCommand = new HashMap<Boolean, String>();

  private Bot bot_holding_base = new Bot();
  private Client tempClient = new Client();

  private String[] commands_list = new String[]{"/add", "/limit", "/statistics", "/start",
      "/help","/menu"};
  private boolean sumIsAdded = false;

  private boolean isCommand(String argum) {
    for (String x : commands_list) {
      if (argum.equals(x)) {
        return true;
      }
    }
    return false;
  }

  private void fixUsingCommand(String argum) {
    currentCommand.put(true, argum);
  }

  private void SendFirstTextOfCommand(String command, Long chatID) throws TelegramApiException {
    SendMessage outMess = new SendMessage();
    outMess.setChatId(chatID.toString());
    if (command.equals("/start")) {
      outMess.setText(
          "������ \uD83D\uDC4B, ���� ����� �����. � ���� ������ ���-��������� \uD83D\uDCB0."
              + "\n��� /help, ���� ������ ������ �� ��� � �������� \uD83E\uDDBE");
      execute(outMess);
    } else if (command.equals("/help")) {
      outMess.setText(
          "\n" +
              "1)������� \"/start\" ����� ������ ������ � �����\n" +
              "2)������� \"/help\" ����� �������� ������ �����\n" +
              "3)������� \"/add\" ����� �������� �������\n" +
              "4)������� \"/limit\" ����� ���������� ����� �� �������� �� �������\n" +
              "5)������� \"/statistics\" ����� �������� ����������\n"
              +
              "6)������� \"/menu\" ����� ������� ������������� ����\n"
      );

      execute(outMess);
    } else if (command.equals("/add")) {
      outMess.setText("������� �����");
      execute(outMess);
    } else if (command.equals("/limit")) {
      outMess.setText("������� �����, �� ������� ������� ���� ������� �� ������ ������� ��������");
      execute(outMess);
    } else if (command.equals("/statistics")) {
      String stat = tempClient.showStatistic();
      outMess.setText(stat);
      execute(outMess);
      currentCommand.put(true,"Default command");//������ ��������� �������,
    } else if (command.equals("/menu"))
    {
      SendMenu(chatID);
      currentCommand.put(true,"Default command");//������ ��������� �������,
    }
    else {
      outMess.setText("��������� �� ����������");
      execute(outMess);
    }
  }

  private int tempSUM = 0;
  private String tempGOOD = "";
  private void DoCommandLogic(String command, String textOfMessage,Long chat_id)
      throws TelegramApiException {
    SendMessage outMess = new SendMessage();

    outMess.setChatId(chat_id.toString());
    if (command.equals("/add")){
      //�� �����, ��� ������ ��������� ��� ����������
      //"������� �����" ���������
      if (!sumIsAdded)
      {
        tempSUM = Integer.parseInt(textOfMessage);
        //���� ����� ��� �� ��������� - ������ ��������
        //addSum(text)
        //������ ������ �����
        outMess.setText("������� �����");
        execute(outMess);
        sumIsAdded = true;
      }
      else {
        //addGood(text)
        //����� ��������, �������� ����������
        tempGOOD = textOfMessage;
        tempClient.addExpenses(tempSUM,tempGOOD);//��������� �������
        //�������� �����
        sumIsAdded  = false;
        tempSUM = 0;
        tempGOOD = "";
        currentCommand.put(true,"Default command");//������ ��������� �������
      }
    }
    else if (command.equals("/limit"))
    {
      //setLimit(text)
      tempClient.setLimit(Integer.parseInt(textOfMessage));
      outMess.setText("����� ����������");
      execute(outMess);
      currentCommand.put(true,"Default command");//������ ��������� �������,
      // ������� ����� �� ��������������
      // � ������� � else
    }
    else {//(Default command,/help,/start) //���� ������� ���������, � ������������ ���-�� �����
        outMess.setText("��� ������ ���������. ������� ����� ����. ������� ��� ������");
        execute(outMess);
    }
  }


  @Override
  public String getBotUsername() {
    return "Finance_Yur_and_Serg_Bot";
  }

  @Override
  public String getBotToken() {
    String token = GetTokenFromEnvironmentVariables();
    if (token != null) {
      return token;
    } else {
      System.out.println("Not token in enviroment variables");
      exit(-1);
      return null;
    }
  }

  private void mainLogic(String textOfMessage,long chat_id,String user_uniq_nick)
  {
    bot_holding_base.registateClient(user_uniq_nick);
    tempClient = bot_holding_base.signIN(user_uniq_nick);

    if (isCommand(textOfMessage)) {
      fixUsingCommand(textOfMessage);
      try {
        SendFirstTextOfCommand(textOfMessage,
            chat_id); //���������� ������ ��������� � ��������� ������
      } catch (TelegramApiException e) {
        throw new RuntimeException(e);
      }
      return;
    }
    String command = currentCommand.get(true);//�������, ����� ������� ������������
    try {
      DoCommandLogic(command,textOfMessage,chat_id);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public void onUpdateReceived(Update update) {
    //currentCommand.put(true,"Defolt command");//������ ��������� �������, ������� � ������ ���� ����� �� ������������
    bot_holding_base.readBase();
    var msg = update.getMessage();
    String textOfMessage = null;
    if (msg==null)
    {
      if (isUserTouchButton(update))
      {
        textOfMessage = update.getCallbackQuery().getData();
        var user = update.getCallbackQuery().getFrom();
        long chat_id = update.getCallbackQuery().getMessage().getChatId();
        String user_uniq_nick = user.getUserName();
        mainLogic(textOfMessage,chat_id,user_uniq_nick);
      }
    }
    else
    {
      textOfMessage = msg.getText();
      out.println(textOfMessage);
      var user = msg.getFrom();
      long chat_id = msg.getChatId();
      out.println(user.getUserName());
      String user_uniq_nick = user.getUserName();
      mainLogic(textOfMessage,chat_id,user_uniq_nick);
    }
    bot_holding_base.updateBase();

  }

  private boolean isUserTouchButton(Update update)
  {
    if (update.hasCallbackQuery())
        return true;
    return false;
  }
  private void sendMenu(Long who, String txt, InlineKeyboardMarkup kb) {
    SendMessage sm = SendMessage
        .builder()
        .chatId(who.toString())
        .parseMode("HTML")
        .text(txt)
        .replyMarkup(kb)
        .build();

    try {
      execute(sm);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }




  private String readUsingFiles(String fileName) throws IOException {
    return new String(Files.readAllBytes(Paths.get(fileName)));
  }//��������� ������ �� ����� � ���������� � ���� ������

  private void SendMenu(Long number_of_chat) {
    //������� ��������
    var add = InlineKeyboardButton
        .builder()
        .text("�������� ����� � �������")
        .callbackData("/add")
        .build();

    var limit = InlineKeyboardButton
        .builder()
        .text("������ ����� �������(��� ����� �� �������)")
        .callbackData("/limit")
        .build();

    var stat = InlineKeyboardButton
        .builder()
        .text("���������� ����������")
        .callbackData("/statistics")
        .build();

    //������� ����������
    keyboard1 = InlineKeyboardMarkup.builder()
        .keyboardRow(List.of(add))
//        .keyboardRow(List.of(url))
        .keyboardRow(List.of(limit))
        .keyboardRow(List.of(stat))
        .build();

    //������ ����������

    sendMenu(number_of_chat, "<b>\uD83E\uDDEDNavigation\uD83E\uDDED</b>", keyboard1);

    //____________________________________________________//
  }

  private String GetTokenFromEnvironmentVariables() {
    Map<String, String> env = System.getenv();
    for (String envName : env.keySet()) {
      if (envName.equals("TOKEN_TELE")) {
        return env.get(envName);
      }
    }
    return null;
  }

  private InlineKeyboardMarkup keyboard1;
}
