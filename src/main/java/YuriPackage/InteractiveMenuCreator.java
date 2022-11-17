package YuriPackage;
import java.util.List;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class InteractiveMenuCreator {

  void createKeyboardCategoriesToUser(Long number_of_chat) {
    var per = InlineKeyboardButton
        .builder()
        .text("\uD83C\uDFEAОт Перекрёстка\uD83C\uDFEA")
        .callbackData("От Перекрёстка")
        .build();

    var veg = InlineKeyboardButton
        .builder()
        .text("\uD83E\uDD66С днём вегана\uD83E\uDD66")
        .callbackData("С днём вегана")
        .build();

    var milk_prod = InlineKeyboardButton
        .builder()
        .text("\uD83D\uDC2EМолоко, сыр, яйца\uD83D\uDC14")
        .callbackData("Молоко, сыр, яйца")
        .build();

    var makarony = InlineKeyboardButton
        .builder()
        .text("\uD83C\uDF5DМакароны, крупы, масло, специи\uD83E\uDDC2")
        .callbackData("Макароны, крупы, масло, специи")
        .build();
    var fruits_veget = InlineKeyboardButton
        .builder()
        .text("\uD83C\uDF45Овощи, фрукты, грибы\uD83C\uDF4E")
        .callbackData("Овощи, фрукты, грибы")
        .build();
    var ready_food = InlineKeyboardButton
        .builder()
        .text("\uD83C\uDF71Готовая еда\uD83C\uDF71")
        .callbackData("Готовая еда")
        .build();

    //создаем клавиатуру
    keyboard1 = InlineKeyboardMarkup.builder()
        .keyboardRow(List.of(per))
        .keyboardRow(List.of(veg))
        .keyboardRow(List.of(milk_prod))
        .keyboardRow(List.of(makarony))
        .keyboardRow(List.of(fruits_veget))
        .keyboardRow(List.of(ready_food))
        .build();

    //сендим клавиатуру

    createFinalMenu(number_of_chat, "<b>Категории</b>", keyboard1);
  }
  void createCommandsMenu(Long number_of_chat) {
    //создаем кнопочки
    var add = InlineKeyboardButton
        .builder()
        .text("Добавить товар в корзину")
        .callbackData("/add")
        .build();

    var limit = InlineKeyboardButton
        .builder()
        .text("Ввести сумму лимита на покупки")
        .callbackData("/limit")
        .build();

    var stat = InlineKeyboardButton
        .builder()
        .text("Посмотреть стоимость корзины и остаток")
        .callbackData("/statistics")
        .build();

    var products_and_prices = InlineKeyboardButton
        .builder()
        .text("Посмотреть текущие цены на товары в магазине \"Перекресток\"")
        .callbackData("/products_and_prices")
        .build();

    //создаем клавиатуру
    keyboard1 = InlineKeyboardMarkup.builder()
        .keyboardRow(List.of(products_and_prices))
        .keyboardRow(List.of(add))
        .keyboardRow(List.of(limit))
        .keyboardRow(List.of(stat))
        .build();

    //сендим клавиатуру

    createFinalMenu(number_of_chat, "<b>\uD83E\uDDEDFines interactive menu\uD83E\uDDED</b>",
        keyboard1);
  }
  void createFinalMenu(Long who, String txt, InlineKeyboardMarkup kb) {
    this.sm = SendMessage
        .builder()
        .chatId(who.toString())
        .parseMode("HTML")
        .text(txt)
        .replyMarkup(kb)
        .build();
  }
  private InlineKeyboardMarkup keyboard1;

  public SendMessage getSm() {
    return sm;
  }

  private SendMessage sm = new SendMessage();
}

