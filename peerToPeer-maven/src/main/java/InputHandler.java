import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class InputHandler {

    private final BufferedReader keyboardInputReader;

    public InputHandler()
    {
        keyboardInputReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public String getInput()
    {
        try {
            String currentInput = "No location selected";
            do {
                System.out.println("Input the client location | max 30 character");
                currentInput = keyboardInputReader.readLine();
            } while (!verifyInput(currentInput));
            return currentInput;
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private boolean verifyInput(String input)
    {
        boolean accepted;
        if (input.length() < 30) {
            System.out.println("Input: " + input + " was accepted");
            accepted = true;
        } else if (Objects.equals(input, "")) {
            System.out.println("Empty input is not allowed");
            accepted = false;
        } else if (Objects.equals(input, "*")) {
            System.out.println("* input is not allowed");
            accepted = false;
        }else {
            System.out.println("Input: " + input + " was rejected - too many characters");
            accepted = false;
        }
        return accepted;
    }
}
