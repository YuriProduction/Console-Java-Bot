package YuriPackage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class Bot  { //implements Bootable,ReadAndWrite
    private final Map<String,Client> base = new HashMap<>();
    private  final Scanner  in  = new Scanner(System.in);
//    public Bot(){
//        Base = new HashMap<String,Client>();
//        in = new Scanner(System.in);
//    }

    private void greetClient()
    {
        System.out.println("Hello!It's a console bot" +
                " which can help you to keep track " +
                "of daily expenses" +
                "\n" +
                "1)Input \"\\Register\" to sign up\n" +
                "2)Input \"\\Sign in\" to sign in a system\n" +
                "3)Input \"\\Add\" to add the expense\n" +
                "4)Input \"\\Limit\" to set the limit of money for today\n" +
                "5)Input \"\\Statistics\" to show all of your expenses\n" +
                "6)Input \"\\Calculation\" to calculate for the entered period\n" +
                "7)Input \"\\Exit\" to leave current session\n");
    }
//    @Override
    public void registrateClient()
    {
        System.out.println("Input your unique Telegram nick");
        String tempClient = in.nextLine();
        if (base.containsKey(tempClient))
        {
            System.out.println("You've already registered\n" +
                    "Please, sign in a system");
        }
        else {
            base.put(tempClient,new Client());
        }
    }
    public Client signIN()
    {
        System.out.println("Input your unique Telegram nick");
        String tempClient = in.nextLine();
        if (!base.containsKey(tempClient))
        {
            System.out.println("You't registrated!");
            return null;
        }
        else {
            System.out.println("Signed up successfully");
            return base.get(tempClient);
        }

    }

    public void work() throws IOException {
        Scanner in = new Scanner(System.in);
//        Bot bot = new Bot();
        Client tempClient = new Client();
        this.greetClient();
        boolean inSystem = false;//Показывает, есть клиент в системе или нет
        while (true)
        {
            this.readBase();
            String action = in.nextLine();
            if (Objects.equals(action, "\\Register")) {
                this.registrateClient();
                System.out.println("Now, sign up to use all properties!");
            }
            else if (Objects.equals(action, "\\Sign in")) {
                tempClient = this.signIN();
                if (tempClient == null)
                    continue;
                inSystem = true;
            }
            else if (Objects.equals(action, "\\Limit"))
            {
                if (!inSystem)
                {
                    System.out.println("Register or sign up a system!");
                    continue;
                }
                else {
                    assert tempClient != null;//но он и не будет null
                    try{
                        System.out.println("Input limit of your daily costs");
                        int LimitUser = Integer.parseInt(in.nextLine());
                        tempClient.setLimit(LimitUser);
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("Incorrect input\n" +
                                "Enter a number:");
                        int LimitUser = Integer.parseInt(in.nextLine());
                        tempClient.setLimit(LimitUser);
                    }
                }
            }
            else if (Objects.equals(action, "\\Add"))
            {
                if (!inSystem)
                {
                    System.out.println("Register or sign in a system!");
                    continue;
                }
                else {
                    System.out.println("Input sum: ");
                    int sumUser = Integer.parseInt(in.nextLine());
                    System.out.println("Input product: ");
                    String product = in.nextLine();
                    assert tempClient != null;//но он и не будет null
                    tempClient.addExpenses(sumUser, product);
                }
            }
            else if (Objects.equals(action, "\\Statistics"))
            {
                if (!inSystem)
                {
                    System.out.println("Register or sign up a system!");
                    continue;
                }
                else {
                    assert tempClient != null;//но он и не будет null
                    tempClient.showStatistic();
                }
            }
            else if (Objects.equals(action,"\\Calculation")) {
                if (!inSystem)
                {
                    System.out.println("Register or sign up a system!");
                    continue;
                }
                else{
                    assert tempClient != null;
                    System.out.println("Enter the period for the calculation");
                    int UserPeriod = Integer.parseInt(in.nextLine());
                    tempClient.distributionPeriod(UserPeriod);
                }
            }
            else if (Objects.equals(action, "\\Exit"))
            {
                inSystem = false;
                this.greetClient();//человек вышел - значит
                // с ботом будет работать
                // другой, возможно, не знает, как с ним работать
                // здесь должен быть наверное break >>> ???
            }
            this.updateBase();
        }
    }
    private void readBase(){
        try(FileReader fileReader = new FileReader("text.json"))
        {
            Path file = Paths.get("text.json");
            String input = Files.readString(file);
            Client tempClient = new Client();//новый клиент в словарь
            JSONObject jsonObject = (JSONObject)JSONValue.parse(input);
            JSONArray jsonArray = (JSONArray)jsonObject.get("Map");

            for (Object item : jsonArray)
            {
                tempClient = new Client();
                JSONObject map = (JSONObject)item;
                String name = (String)map.get("Name");
                long lim = (Long)map.get("Limit");
                tempClient.setLimitFromJSON((int)lim);
                JSONArray jsonArray1 = (JSONArray)map.get("Products");
                for (Object item1 : jsonArray1)
                {
                    JSONObject map1 = (JSONObject)item1;
                    String title = (String)map1.get("title");
                    long price = (Long)map1.get("price");
                    tempClient.addExpensesFromJSON((int)price,title);
                }
                if (!base.containsKey(name))
                    base.put(name,tempClient);
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    };
    private void updateBase()
    {
        try(FileWriter file = new FileWriter("C:\\Учеба ООП\\ConsoleJavaBot\\Console-Java-Bot\\text.json");) {
            JSONObject main_obj = new JSONObject();
            JSONArray mp = new JSONArray();
            Client tempClient = new Client();
            HashMap<String,Integer> tempClientBase;
            for (Map.Entry<String,Client> entry: base.entrySet())
            {
                JSONObject obj = new JSONObject();
                String uniq_NAME = entry.getKey();
                tempClient = entry.getValue();
                tempClientBase = tempClient.mapForJSON();
                obj.put("Name",uniq_NAME);
                obj.put("Limit",tempClient.LimitForJSON());
                JSONArray products = new JSONArray();
                for (Map.Entry<String,Integer> entry1: tempClientBase.entrySet())
                {
                    JSONObject obj1 = new JSONObject();
                    obj1.put("title",entry1.getKey());
                    obj1.put("price",entry1.getValue());
                    products.add(obj1);
                }
                obj.put("Products",products);
                mp.add(obj);
            }
            main_obj.put("Map",mp);
            file.write(main_obj.toJSONString());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.out.println("Not today");
        }
    }


}

//interface ReadAndWrite{
//    public void ReadBase();
//    public void WriteBase();
//}

//interface Bootable
//{
//    public void RegistrateClient();
//
//}
