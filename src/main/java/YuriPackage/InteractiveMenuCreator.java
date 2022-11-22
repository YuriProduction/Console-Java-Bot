package YuriPackage;

import java.util.List;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class InteractiveMenuCreator {

  //Создает кнопки и посылает их в TeleBot для обработки и отправки пользователю
  private InlineKeyboardMarkup keyboard1;
  private SendMessage sendMessage = new SendMessage();


  public void createKeyboardCategoriesToUser(Long numberOfChat) {
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

    var milkProd = InlineKeyboardButton
        .builder()
        .text("\uD83D\uDC2EМолоко, сыр, яйца\uD83D\uDC14")
        .callbackData("Молоко, сыр, яйца")
        .build();

    var makarony = InlineKeyboardButton
        .builder()
        .text("\uD83C\uDF5DМакароны, крупы, масло, специи\uD83E\uDDC2")
        .callbackData("Макароны, крупы, масло, специи")
        .build();
    var fruitsVeget = InlineKeyboardButton
        .builder()
        .text("\uD83C\uDF45Овощи, фрукты, грибы\uD83C\uDF4E")
        .callbackData("Овощи, фрукты, грибы")
        .build();
    var readyFood = InlineKeyboardButton
        .builder()
        .text("\uD83C\uDF71Готовая еда\uD83C\uDF71")
        .callbackData("Готовая еда")
        .build();

    //создаем клавиатуру
    keyboard1 = InlineKeyboardMarkup.builder()
        .keyboardRow(List.of(per))
        .keyboardRow(List.of(veg))
        .keyboardRow(List.of(milkProd))
        .keyboardRow(List.of(makarony))
        .keyboardRow(List.of(fruitsVeget))
        .keyboardRow(List.of(readyFood))
        .build();

    //сендим клавиатуру

    createFinalMenu(numberOfChat, "<b>Категории</b>", keyboard1);
  }


  public void createCommandsMenu(Long numberOfChat) {

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

    var productsAndPrices = InlineKeyboardButton
        .builder()
        .text("Посмотреть текущие цены на товары в магазине \"Перекресток\"")
        .callbackData("/products_and_prices")
        .build();

    //создаем клавиатуру
    keyboard1 = InlineKeyboardMarkup.builder()
        .keyboardRow(List.of(productsAndPrices))
        .keyboardRow(List.of(add))
        .keyboardRow(List.of(limit))
        .keyboardRow(List.of(stat))
        .build();

    //сендим клавиатуру
    createFinalMenu(numberOfChat, "<b>\uD83E\uDDEDFines interactive menu\uD83E\uDDED</b>",
        keyboard1);
  }


  public void createFinalMenu(Long who, String txt, InlineKeyboardMarkup kb) {
    this.sendMessage = SendMessage
        .builder()
        .chatId(who.toString())
        .parseMode("HTML")
        .text(txt)
        .replyMarkup(kb)
        .build();
  }

  public SendMessage getSendMessage() {
    return sendMessage;
  }
}

