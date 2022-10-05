package YuriPackage;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {
    private int Limit = Integer.MAX_VALUE;
    private int ostat;
    private Scanner in;
    private Map<String,Integer> map;
    public Client()
    {
        map = new HashMap<String,Integer>();
        in = new Scanner(System.in);
        ostat = Integer.MAX_VALUE;
    }
    public void AddExpenses()
    {
        int sum;String product;
        System.out.println("Input sum");
        sum = Integer.parseInt(in.nextLine());//после nextInt()
        // некорректно работает nextLine(),считывая пустую строку
        //поэтому так
        System.out.println("Input product");
        product = in.nextLine();
        if (map.containsKey(product))
        {
            int temp = map.get(product);
            if (CanAdd(temp+sum))
                map.put(product, temp + sum);
            else
                System.out.println("Your limit will be" +
                        " achieved\nYou can't by this!\n" +
                        "If you buy, thr limit is over");
        }
        else {
            if (CanAdd(sum))
                map.put(product,sum);
            else
                System.out.println("Your limit will be" +
                        " achieved\nYou can't by this!\n" +
                        "If you buy, thr limit is over");
        }
    }
    public void AddExpensesFromJSON(int sum,String product)
    {
        if (map.containsKey(product))
        {
            int temp = map.get(product);
            if (CanAdd(temp+sum))
                map.put(product, temp + sum);
            else
                System.out.println("Your limit will be" +
                        " achieved\nYou can't by this!\n" +
                        "If you buy, thr limit is over");
        }
        else {
            if (CanAdd(sum))
                map.put(product,sum);
            else
                System.out.println("Your limit will be" +
                        " achieved\nYou can't by this!\n" +
                        "If you buy, thr limit is over");
        }
    }
    private void ShowTheOstat()
    {
        int sum = 0;
        for (Map.Entry<String,Integer> entry: map.entrySet())
        {
            sum+= entry.getValue();
        }
        System.out.println("Your ostat = " + (this.Limit - sum));
    }
    private boolean CanAdd(int item)
    {
        int sum = 0;
        for (Map.Entry<String,Integer> entry: map.entrySet())
        {
            sum+= entry.getValue();
        }
        if (sum+item<this.Limit)
            return true;
        return false;
    }

    public void ShowStatistic()
    {
        for (Map.Entry<String,Integer> entry: map.entrySet())
        {
            String key = entry.getKey();
            Integer val = entry.getValue();
            System.out.println("Product - " + key + "| Expenses = " + val);
        }
        ShowTheOstat();
    };

    public void SetLimit()
    {
        System.out.println("Input limit of your daily costs");
        this.Limit = Integer.parseInt(in.nextLine());
    }
    public void SetLimitFromJSON(int lim)
    {
        this.Limit = lim;
    }


}
