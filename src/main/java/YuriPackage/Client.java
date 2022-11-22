package YuriPackage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {

  //Отвечает за запись клиента и его данных(расходы, лимит в базу)
  private int limit = Integer.MAX_VALUE;
  private boolean OVERFLOW = false;
  private int ostat;
  private Scanner in;
  private String date;
  private Map<String, Integer> map;

  public Client() {
    map = new HashMap<String, Integer>();
    in = new Scanner(System.in);
    ostat = Integer.MAX_VALUE;
  }

  public int getOstat() {
    return ostat;
  }

  public boolean getOVERFLOW() {
    return OVERFLOW;
  }

  public void setOVERFLOW(boolean OVERFLOW) {
    this.OVERFLOW = OVERFLOW;
  }

  public void addExpenses(int sum, String product) {
    this.OVERFLOW = false;
    if (map.containsKey(product)) {
      int temp = map.get(product);
      if (canAdd(temp + sum)) {
        map.put(product, temp + sum);
      } else {
        this.OVERFLOW = true;
        System.out.println("Your limit will be" + " achieved\nYou can't by this!\n"
            + "If you buy, thr limit is over");
      }
    } else {
      if (canAdd(sum)) {
        this.map.put(product, sum);
      } else {
        this.OVERFLOW = true;
        System.out.println("Your limit will be" + " achieved\nYou can't by this!\n"
            + "If you buy, thr limit is over");
      }

    }
    ostat -= sum;
  }

  public void addExpensesFromJSON(int sum, String product) {
    if (map.containsKey(product)) {
      int temp = map.get(product);
      if (canAdd(temp + sum)) {
        map.put(product, temp + sum);
        this.date = new Date().toString();
      } else {
        System.out.println("Your limit will be" + " achieved\nYou can't by this!\n"
            + "If you buy, thr limit is over");
      }
    } else {
      if (canAdd(sum)) {
        map.put(product, sum);
      } else {

        System.out.println("Your limit will be" + " achieved\nYou can't by this!\n"
            + "If you buy, thr limit is over");
      }
    }
  }

  private String showTheOstat() {
    int sum = 0;
    for (Map.Entry<String, Integer> entry : map.entrySet()) {
      sum += entry.getValue();
    }
    //System.out.println("Your ostat = " + (this.limit - sum));
    return "Your ostat = " + (this.limit - sum);

  }

  private boolean canAdd(int item) {
    int sum = 0;
    for (Map.Entry<String, Integer> entry : map.entrySet()) {
      sum += entry.getValue();
    }
    if (sum + item < this.limit) {
      return true;
    }
    return false;
  }

  public String showStatistic() {
    String stat = "";
    for (Map.Entry<String, Integer> entry : map.entrySet()) {
      String key = entry.getKey();
      Integer val = entry.getValue();
      stat += "Product - " + key + "| Expenses = " + val + "\n";
    }
    stat += showTheOstat() + "\n";
    return stat;
  }


  public void setLimit(int LimitUser) {
    limit = LimitUser;
  }

  public void setLimitFromJSON(int lim) {
    this.limit = lim;
  }

  public HashMap<String, Integer> mapForJSON()//нужно для запси в json-базу
  {
    return (HashMap<String, Integer>) this.map;
  }


  public void setDate(String data) {
    this.date = data;
  }

  public int LimitForJSON() {
    return this.limit;
  }

}
