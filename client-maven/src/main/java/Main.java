import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        Client client = new Client(2000);
        InputHandler inputHandler = new InputHandler();

        client.connect();

        while(true){
            inputHandler.printCommands();
            String input = inputHandler.getInput();
            if (inputHandler.verify(input)){
                client.executeCommand(input);
            } else {
                System.out.println("Command was not accepted");
            }
        }
    }
}
