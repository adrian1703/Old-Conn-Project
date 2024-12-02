import java.io.*;
import java.net.Socket;

public class Client {

    private Socket serverSocket = null;
    private InputStream inputSocket;
    private OutputStream outputSocket;
    private int bufferSize;

    public Client(int bufferSize)
    {
        this.bufferSize = bufferSize;
    }

    public void connect()
    {
        try {
            serverSocket = new Socket("localhost", 50001);
            inputSocket = serverSocket.getInputStream();
            outputSocket = serverSocket.getOutputStream();
        } catch (IOException e) {
            System.out.println("ERROR while connecting");
            e.printStackTrace();
            try {
                serverSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                System.exit(-3);
            }
        }
    }

    private void sendMessage(String command)
    {
        PrintStream outputSocketWriter = new PrintStream(new BufferedOutputStream(outputSocket));
        outputSocketWriter.print(command);
        outputSocketWriter.flush();
    }

    private void receiveMessage()
    {
        BufferedReader inputSocketReader = new BufferedReader(new InputStreamReader(inputSocket));
        char[] buffer = new char[bufferSize];
        try {
            int length = inputSocketReader.read(buffer, 0, buffer.length);
            String receivedMessage = (new String(buffer, 0, length));
            System.out.println(receivedMessage);
        } catch (IOException e) {
            System.out.println("Error while receiving message from server");
            e.printStackTrace();
            try {
                serverSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                System.exit(-4);
            }
        }
    }

    public void executeCommand(String command)
    {
        sendMessage(command);
        quitIfCommanded(command);
        receiveMessage();
    }

    private void quitIfCommanded(String command)
    {
        if (command.equals("QUIT")) {
            try {
                Thread.sleep(4000);
                System.out.println("Bye Bye");
                System.exit(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


}
