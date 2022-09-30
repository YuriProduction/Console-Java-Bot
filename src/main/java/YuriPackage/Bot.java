package YuriPackage;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Bot implements Bootable {
    private Map<String,Client> Base;
    private Scanner in;
    public Bot(){
        Base = new HashMap<String,Client>();
        in = new Scanner(System.in);
    }

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
    @Override
    public  void RegistrateClient()
    {
        System.out.println("Input your unique Telegram nick");
        String tempClient = in.nextLine();
        if (Base.containsKey(tempClient))
        {
            System.out.println("You've already registered\n" +
                    "Plese, sign in a system");
            return;
        }
        else {
            Base.put(tempClient,new Client());
        }
    };
    public Client SignUP()
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

}

interface Bootable
{
    public void RegistrateClient();

}
