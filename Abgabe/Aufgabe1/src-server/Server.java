

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ServerSocket serverSocket;
    BufferedReader inputReader;
    PrintStream outputWriter;
    Socket clientSocket;
    FileManager fileManager;
    int bufferSize; //for message length
    String currentCommand;
    boolean hasConnection = false;

    public Server(int bufferSize)
    {
        this.fileManager = new FileManager();
        this.bufferSize = bufferSize;
        try {
            serverSocket = new ServerSocket(50001);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptNewConnection() throws IOException
    {
        clientSocket = serverSocket.accept();
        outputWriter = new PrintStream(new BufferedOutputStream(clientSocket.getOutputStream()));
        inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        hasConnection = true;
        System.out.println("Establishing new connection");
    }

    private void receiveMessage() throws IOException
    {
        char[] buffer = new char[bufferSize];
        int length = inputReader.read(buffer, 0, bufferSize);
        currentCommand = new String(buffer, 0, length);
    }

    private void sendMessage(String message)
    {
        outputWriter.print(message);
        outputWriter.flush();
    }


    private void executeCommand() throws IOException
    {
        if (currentCommand.equals("QUIT")) {
            clientSocket.close();
            hasConnection = false;
            System.out.println("Closing connection");
        } else if (currentCommand.equals("LIST")) {
            String message = fileManager.getFileList();
            sendMessage(message);
            System.out.println("Sending FileList\n" + message);
        } else if (currentCommand.startsWith("GET ")) {
            String message = fileManager.getFileContent(currentCommand.substring(4));
            sendMessage(message);
            System.out.println("Sending FileContent\n" + message);
        }
    }

    public void run()
    {
        try {
            while (true) {
                if (!hasConnection) {
                    acceptNewConnection();
                }
                receiveMessage();
                executeCommand();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
