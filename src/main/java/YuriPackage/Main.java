package YuriPackage;
import java.util.Objects;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Bot bot = new Bot();
        Client tempClient = new Client();
        Bot.GreetClient();
        boolean InSystem = false;//Показывает, есть клиент в системе или нет
        while (true)
        {
            String action = in.nextLine();
            if (Objects.equals(action, "\\Register")) {
                bot.RegistrateClient();
                System.out.println("Now, sign up to use all properties!");
            }
            else if (Objects.equals(action, "\\Sign up")) {
                tempClient = bot.SignUP();
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
        }
    }
}
