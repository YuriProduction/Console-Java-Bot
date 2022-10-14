package YuriPackage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {

  public int limit = Integer.MAX_VALUE;
  private int ostat;
  private Scanner in;
  private Map<String, Integer> map;

  public Client() {
    map = new HashMap<String, Integer>();
    in = new Scanner(System.in);
    ostat = Integer.MAX_VALUE;
  }

  public void addExpenses(int sum, String product) {
    if (map.containsKey(product)) {
      int temp = map.get(product);
      if (canAdd(temp + sum)) {
        map.put(product, temp + sum);
      } else {
        System.out.println("Your limit will be" +
            " achieved\nYou can't by this!\n" +
            "If you buy, thr limit is over");
      }
    } else {
      if (canAdd(sum)) {
        this.map.put(product, sum);
      } else {
        System.out.println("Your limit will be" +
            " achieved\nYou can't by this!\n" +
            "If you buy, thr limit is over");
      }
    }
  }

  public void addExpensesFromJSON(int sum, String product) {
    if (map.containsKey(product)) {
      int temp = map.get(product);
      if (canAdd(temp + sum)) {
        map.put(product, temp + sum);
      } else {
        System.out.println("Your limit will be" +
            " achieved\nYou can't by this!\n" +
            "If you buy, thr limit is over");
      }
    } else {
      if (canAdd(sum)) {
        map.put(product, sum);
      } else {
        System.out.println("Your limit will be" +
            " achieved\nYou can't by this!\n" +
            "If you buy, thr limit is over");
      }
    }
  }

  private void showTheOstat() {
    int sum = 0;
    for (Map.Entry<String, Integer> entry : map.entrySet()) {
      sum += entry.getValue();
    }
    System.out.println("Your ostat = " + (this.limit - sum));
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

  public void showStatistic() {
    for (Map.Entry<String, Integer> entry : map.entrySet()) {
      String key = entry.getKey();
      Integer val = entry.getValue();
      System.out.println("Product - " + key + "| Expenses = " + val);
    }
    showTheOstat();
  }

  ;

  public boolean setLimit(int LimitUser) throws IOException {
    limit = LimitUser;
    return true;
  }

  public void setLimitFromJSON(int lim) {
    this.limit = lim;
  }

  public HashMap<String, Integer> mapForJSON()//нужно для запси в json-базу
  {
    return (HashMap<String, Integer>) this.map;
  }

  public int LimitForJSON() {
    return this.limit;
  }

  public int distributionPeriod(int period) {
    int periodUnit = limit / period;
    System.out.println("Spending limit for today: " + periodUnit);
    return periodUnit;
  }
}
