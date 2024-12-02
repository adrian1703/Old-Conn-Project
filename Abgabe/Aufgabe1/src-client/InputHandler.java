import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class InputHandler {

    private final BufferedReader keyboardInputReader;
    private final ArrayList<String> allowedCommands;

    public InputHandler()
    {
        keyboardInputReader = new BufferedReader(new InputStreamReader(System.in));
        allowedCommands = new ArrayList<>();
        fillAllowedCommands();
    }

    public String getInput()
    {
        try {
            return keyboardInputReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private void fillAllowedCommands()
    {
        allowedCommands.add("QUIT");
        allowedCommands.add("LIST");
    }


    public boolean verify(String Command)
    {
        for (String s : allowedCommands) {
            if (s.equals(Command)) {
                return true;
            }
        }
        if (Command.startsWith("GET ")) {
            return true;
        } else {
            return false;
        }
    }

    public void printCommands()
    {
        System.out.println("Choose a command. Commands are case sensitive :)");
        for (String s : allowedCommands) {
            System.out.println(s);
        }
        System.out.println("GET filename");
        System.out.println();
    }
}
