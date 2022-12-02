package InvestHelper;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParserStocksTest {

  ParserStocks ourStocks;

  @BeforeEach
  public void prepareData() {

    ourStocks = new ParserStocks();
  }

  @Test
  void getTextForUserAboutQuotes() throws IOException {
    ourStocks.parseQuotesData();
    StringBuilder test = new StringBuilder("Компания   Цена(руб/шт)\n"
        + "Газпром--->166.98 \uD83D\uDCC8\n"
        + "Сбербанк--->137.95 \uD83D\uDCC8\n"
        + "Лукойл--->4599.5 \uD83D\uDCC8\n"
        + "Магнит--->4777 \uD83D\uDCC8\n"
        + "Новатэк--->1065 \uD83D\uDCC8\n"
        + "ГМК Норникель--->14700 \uD83D\uDCC8\n"
        + "Сургнфгз--->21.785 \uD83D\uDCC8\n"
        + "Сургнфгз-п--->24.845 \uD83D\uDCC8\n"
        + "Роснефть--->335.9 \uD83D\uDCC8\n"
        + "ВТБ--->0.01705 \uD83D\uDCC9\n"
        + "Транснф--->88850 \uD83D\uDCC9\n"
        + "Татнфт--->365.8 \uD83D\uDCC9\n"
        + "АЛРОСА--->65.07 \uD83D\uDCC9\n"
        + "МТС-ао--->234.7 \uD83D\uDCC8\n"
        + "МосБиржа--->87.25 \uD83D\uDCC8\n"
        + "СевСт-ао--->784.6 \uD83D\uDCC8\n"
        + "Красэсб--->9.08 \uD83D\uDCC8\n"
        + "Аэрофлот--->24.82 \uD83D\uDCC8\n"
        + "ЧеркизГ-ао--->2828 \uD83D\uDCC8\n"
        + "РусГидро--->0.7518 \uD83D\uDCC8\n"
        + "ИнтерРАОао--->3.293 \uD83D\uDCC8\n"
        + "ИркЭнерго--->14.02 \uD83D\uDCC8\n"
        + "ЛСР--->471.6 \uD83D\uDCC9\n"
        + "МРСК ЦП--->0.2195 \uD83D\uDCC9\n"
        + "+МосЭнерго--->1.737 \uD83D\uDCC8\n"
        + "Мечел--->102.21 \uD83D\uDCC9\n"
        + "Мечел ап--->134.75 \uD83D\uDCC8\n"
        + "ОГК-2--->0.5338 \uD83D\uDCC8\n"
        + "ПИК--->614.2 \uD83D\uDCC9\n"
        + "Ростел--->57.55 \uD83D\uDCC9\n"
        + "Ростел -ап--->57.4 \uD83D\uDCC9\n"
        + "Сбербанк АП--->132.45 \uD83D\uDCC9\n"
        + "ТГК-1--->0.008 \uD83D\uDCC8\n"
        + "Квадра--->0.012945 \uD83D\uDCC8\n"
        + "ТМК--->78.34 \uD83D\uDCC9\n"
        + "ЮТэйр--->7.35 \uD83D\uDCC9\n"
        + "КАМАЗ--->85.3 \uD83D\uDCC8\n"
        + "МРСК Центр--->0.2908 \uD83D\uDCC9\n"
        + "МРСК Ур--->0.1748 \uD83D\uDCC8\n"
        + "МРСК СЗ--->0.0256 \uD83D\uDCC9\n");
    Assertions.assertEquals(test ,ourStocks.getTextForUserAboutQuotes());
  }

}