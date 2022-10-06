package YuriPackage;

import java.io.File;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

public class Bot  { //implements Bootable,ReadAndWrite
    private static final Map<String,Client> Base = new HashMap<>();
    private  static final Scanner  in  = new Scanner(System.in);
//    public Bot(){
//        Base = new HashMap<String,Client>();
//        in = new Scanner(System.in);
//    }

    public static void GreetClient()
    {
        System.out.println("Hello!It's a console bot" +
                " which can help you to keep track " +
                "of daily expenses" +
                "\n" +
                "1)Input \"\\Register\" to sign up\n" +
                "2)Input \"\\Sign up\" to sign in a system\n" +
                "3)Input \"\\Add\" to add the expense\n" +
                "4)Input \"\\Limit\" to set the limit of money for today\n" +
                "5)Input \"\\Statistics\" to show all of your expenses\n" +
                "6)Input \"\\Exit\" to leave current session\n");
    }
//    @Override
    public static void RegistrateClient()
    {
        System.out.println("Input your unique Telegram nick");
        String tempClient = in.nextLine();
        if (Base.containsKey(tempClient))
        {
            System.out.println("You've already registered\n" +
                    "Please, sign in a system");
            return;
        }
        else {
            Base.put(tempClient,new Client());
        }
    };
    public static Client SignUP()
    {
        System.out.println("Input your unique Telegram nick");
        String tempClient = in.nextLine();
        if (!Base.containsKey(tempClient))
        {
            System.out.println("You't registrated!");
            return null;
        }
        else {
            System.out.println("Signed up successfully");
            return Base.get(tempClient);
        }

    }

    public static void Work()
    {
        Scanner in = new Scanner(System.in);
//        Bot bot = new Bot();
        Client tempClient = new Client();
        Bot.GreetClient();
        boolean InSystem = false;//Показывает, есть клиент в системе или нет
        while (true)
        {
            Bot.ReadBase();
            String action = in.nextLine();
            if (Objects.equals(action, "\\Register")) {
                Bot.RegistrateClient();
                System.out.println("Now, sign up to use all properties!");
            }
            else if (Objects.equals(action, "\\Sign up")) {
                tempClient = Bot.SignUP();
                if (tempClient == null)
                    continue;
                InSystem = true;
            }
            else if (Objects.equals(action, "\\Limit"))
            {
                if (!InSystem)
                {
                    System.out.println("Register or sign up a system!");
                    continue;
                }
                else {
                    assert tempClient != null;//но он и не будет null
                    tempClient.SetLimit();
                }
            }
            else if (Objects.equals(action, "\\Add"))
            {
                if (!InSystem)
                {
                    System.out.println("Register or sign in a system!");
                    continue;
                }
                else {
                    assert tempClient != null;//но он и не будет null
                    tempClient.AddExpenses();
                }
            }
            else if (Objects.equals(action, "\\Statistics"))
            {
                if (!InSystem)
                {
                    System.out.println("Register or sign up a system!");
                    continue;
                }
                else {
                    assert tempClient != null;//но он и не будет null
                    tempClient.ShowStatistic();
                }
            }
            else if (Objects.equals(action, "\\Exit"))
            {
                InSystem = false;
                Bot.GreetClient();//человек вышел - значит
                // с ботом будет работать
                // другой, возможно, не знает, как с ним работать
            }
            Bot.UpdateBase();
        }
    }
    public static void ReadBase(){
        try(FileReader fileReader = new FileReader("text.json"))
        {
            Path file = Paths.get("text.json");
            String input = Files.readString(file);
            Client TempClient = new Client();//новый клиент в словарь
            JSONObject jsonObject = (JSONObject)JSONValue.parse(input);
            JSONArray jsonArray = (JSONArray)jsonObject.get("Map");

            for (Object item : jsonArray)
            {
                TempClient = new Client();
                JSONObject map = (JSONObject)item;
                String name = (String)map.get("Name");
                long lim = (Long)map.get("Limit");
                TempClient.SetLimitFromJSON((int)lim);
                JSONArray jsonArray1 = (JSONArray)map.get("Products");
//                System.out.println(name + "\t" + lim);
//                System.out.println();
                for (Object item1 : jsonArray1)
                {
                    JSONObject map1 = (JSONObject)item1;
                    String title = (String)map1.get("title");
                    long price = (Long)map1.get("price");
                    TempClient.AddExpensesFromJSON((int)price,title);
//                    System.out.println(title + '\t' + price);
//                    System.out.println();
                }
//                System.out.println("\n_________\n");
                Base.put(name,TempClient);
            }
           // jsonObject.remove("Map");
//            File file1 = new File("temp.txt");
//            if (file1.delete())
//                System.out.println("Deleted successfully");
            //так буду удалять json файл
//            for (String key : Base.keySet())
//            {
//                System.out.println(key+'\n');
//            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    };
    public static void UpdateBase()
    {
        try(FileWriter file = new FileWriter("D:\\JAVA\\UNIVERSITY\\Bot_consol\\ConsolniyBot\\text.json");) {
            JSONObject main_obj = new JSONObject();
            JSONArray mp = new JSONArray();
            Client TempClient = new Client();
            HashMap<String,Integer> tempClientBase;
            for (Map.Entry<String,Client> entry: Base.entrySet())
            {
                JSONObject obj = new JSONObject();
                String UNIQ_NAME = entry.getKey();
                TempClient = entry.getValue();
                tempClientBase = TempClient.MapForJSON();
                obj.put("Name",UNIQ_NAME);
                obj.put("Limit",TempClient.LimitForJSON());
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
//            obj.put("Name","Jurex");
//            obj.put("Limit",1000);
//            JSONArray products = new JSONArray();
//            JSONObject obj1 = new JSONObject();
//            obj1.put("title","Milk");
//            obj1.put("price",100);
//            products.add(obj1);
//            obj.put("Products",products);
//            mp.add(obj);
//            main_obj.put("Map",mp);
            main_obj.put("Map",mp);
            file.write(main_obj.toJSONString());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.out.println("Not today");
        }
    };


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
