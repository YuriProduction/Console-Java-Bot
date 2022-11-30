package InvestHelper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParserStocks {

  public boolean existsCompany(String company) {
    if (quotes.containsKey(company)) {
      System.out.println(quotes);
      return true;
    }
    return false;
  }

  public Double getPriceOfStock(String company) {
    if (existsCompany(company)) {
      return quotes.get(company);
    }
    return null;
  }

  public StringBuilder getTextForUserAboutQuotes() {
    return textForUserAboutQuotes;
  }

  private StringBuilder textForUserAboutQuotes = new StringBuilder();

  //котировки
  private final Map<String, Double> quotes = new HashMap<>();


  public void parseQuotesData() throws IOException {
    textForUserAboutQuotes.append("Компания   ").append("Цена(руб/шт)").append("\n");
    String url = "https://investfuture.ru/securities";
    Document doc = Jsoup.parse(new URL(url).openStream(), "utf-8", url);
    String upOrDown = "";
    Elements table = doc.select("table.table-hover > tbody > tr");
    for (Element item : table) {
      String fullInfoAboutCompany = item.select("td").text();
      String[] fullInfoAboutCompanyArray = fullInfoAboutCompany.split(" ");
      System.out.println(fullInfoAboutCompany);
      String company = fullInfoAboutCompanyArray[1];
      String lastPriceOfActions = fullInfoAboutCompanyArray[5];
      String growth = fullInfoAboutCompanyArray[7];
      char sign = growth.charAt(0);
      if (sign == '-') {
        upOrDown = "\uD83D\uDCC9";
      } else {
        upOrDown = "\uD83D\uDCC8";
      }
      quotes.put(company, Double.parseDouble(lastPriceOfActions));
      textForUserAboutQuotes.append(company).append("--->").append(lastPriceOfActions)
          .append(" " + upOrDown).append("\n");
//      System.out.println("Компания " + company + "\n" +
//          "Цена акций " + lastPriceOfActions + "Прирост " + growth + upOrDown);
//      System.out.println();
    }
  }


}
