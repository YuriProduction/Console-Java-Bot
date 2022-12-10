package InvestHelper;

import java.util.ArrayList;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class ReplyKeyboardMaker {

  public ReplyKeyboardMarkup setReplyKeyboardMarkup(String command) {
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    replyKeyboardMarkup.setResizeKeyboard(true); //подгоняем размер
    replyKeyboardMarkup.setOneTimeKeyboard(true); //скрываем после использования

    ArrayList<KeyboardRow> keyboardStartRows = new ArrayList<>();// Лист для кнопки старт
    ArrayList<KeyboardRow> keyboardInfoRows = new ArrayList<>();// Лист для кнопки инфо
    ArrayList<KeyboardRow> keyboardCaseRows = new ArrayList<>();// Лист для кнопок портфеля

    KeyboardRow keyboardStartRow = new KeyboardRow();
    keyboardStartRow.add(new KeyboardButton("/start"));// кнопка старт
    KeyboardRow keyboardInfoRow = new KeyboardRow();
    keyboardInfoRow.add(new KeyboardButton("/help"));// кнопка инфа о рынке

    KeyboardRow keyboardAddRow = new KeyboardRow();
    keyboardAddRow.add(new KeyboardButton("/AddStock"));// кнопка добавления акции в портфель
    KeyboardRow keyboardShowcaseRow = new KeyboardRow();
    keyboardShowcaseRow.add(new KeyboardButton("/StockPrices"));
    KeyboardRow keyboardBriefcaseRow = new KeyboardRow();
    keyboardBriefcaseRow.add(new KeyboardButton("/Statistic"));// кнопка просмотра портфеля
    keyboardStartRows.add(keyboardStartRow);// добавил кнопки в листы
    KeyboardRow keyboardSellRow = new KeyboardRow();
    keyboardSellRow.add(new KeyboardButton("/Sell"));
    keyboardInfoRows.add(keyboardInfoRow);
    keyboardCaseRows.add(keyboardAddRow);
    keyboardCaseRows.add(keyboardShowcaseRow);
    keyboardCaseRows.add(keyboardBriefcaseRow);
    keyboardCaseRows.add(keyboardSellRow);

    if (command
        == null) { // eсли бот начал свою работу, т.е. пользователь не ввел еще не какую команду
      replyKeyboardMarkup.setKeyboard(keyboardStartRows); // создаем кнопку с командой start
    } // после нажатия кнопка пропадет и создадутся другие кнопки
    else if (command.equals("/start")) {
      replyKeyboardMarkup.setKeyboard(keyboardInfoRows);
    } else if (command.equals("/help")) {
      replyKeyboardMarkup.setKeyboard(keyboardCaseRows);
    }

    return replyKeyboardMarkup;
  }

}
