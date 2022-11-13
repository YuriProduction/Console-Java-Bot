package YuriPackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Parser extends Thread {

  protected  Map<String, List<String>> categories = null;
  protected Map<String, List<String>> getCategories() throws IOException {
    categories = new HashMap<>();
    Document doc = Jsoup.connect("https://www.perekrestok.ru/cat").get();
    String price;
    String product;
    for (int i = 1; i <= 6;
        i++) { // Цикл пробегает только по 6 категориям, поменям 6 на n будет выводить n категорий
      List<String> contents = new ArrayList<>();
      String selector =
          "#app > div > main > div > div > div > div.catalog__list > div.sc-gsTCUz.coCsbI > div:nth-child("
              + i + ") > div > a";
      Elements element = doc.select(
          selector); // Selector переместил в отдельную переменную, для лучшей читабельности
      Elements hrefElements = doc.select(
          selector); // Selector - это такой путь до определенного элемента
      String categoriesText = element.text(); // *.text() достает текст из html файла

      String href = hrefElements.attr("href"); // *.attr(key) достает внутренную ссылку по ключу
      Document content = Jsoup.connect("https://www.perekrestok.ru" + href)
          .get(); // открываю ссылку-категорию, чтобы считать от туда данные
      Elements products = content.select(
          "div.product-card__title"); // ищем данные на страницы по определенному классу и считываем их
      Elements prices = content.select(
          "div.price-new"); // ищем данные на страницы по определенному классу и считываем их
      for (int j = 0; j < 3; j++) { // Поставил границу 1, чтобы проверить вывод контента с страницы
        price = prices.get(j).text(); // записываем сумму
        product = products.get(j).text(); // записываем продукт
        contents.add(product + "___" + price);

      }
      categories.put(categoriesText,
          contents); // поместил в общий HashMap название категории и лист с ценнами и продуктами
      //System.out.println(contents);

    }
    return categories;

  }

  @Override
  public void run()
  {
    try {
      categories = getCategories();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

