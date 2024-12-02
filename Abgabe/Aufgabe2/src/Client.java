import java.io.IOException;
import java.net.*;
import java.util.ConcurrentModificationException;

public class Client implements Runnable {

    private final DatapackageHandler datapackageHandler;
    private final int[] portRange = new int[10];
    private DatagramSocket mySocket;
    int myPort;

    public Client(DatapackageHandler datapackageHandler)
    {
        this.datapackageHandler = datapackageHandler;
        for (int i = 1; i < 11; i++) {
            portRange[i - 1] = 50000 + i;
        }
    }

    public void connectToFreePortForReceiving()
    {
        System.out.println("Try connecting to free port");
        for (int j : portRange) {
            if (tryConnectToPort(j)) {
                System.out.printf("Successfully connected to port %d\n", j);
                myPort = j;
                return;
            } else {
                System.out.printf("Socket with port: %d in use\n", j);
            }
        }
        System.out.println("Could not find free sockets in port range. System exit");
        System.exit(-2);
    }

    private boolean tryConnectToPort(int port)
    {
        try {
            mySocket = new DatagramSocket(port);
            return true;
        } catch (SocketException e) {
            return false;
        }
    }


    public void getUpdate()
    {
        DatagramPacket packet = new DatagramPacket(new byte[2048], 2048);
        try {
            mySocket.receive(packet);
            System.out.println("Updating datapackageDatabase");
            datapackageHandler.update(new String(packet.getData()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ConcurrentModificationException e) {
            System.out.println(e);
        }
    }

    public void sendUpdate()
    {
        System.out.println("Sending Updates to other clients");
        String stringData = datapackageHandler.convertDatapackageDatabaseToString();
        byte[] data = stringData.getBytes();

        for (int j : portRange) {
            if (j == myPort) {
                continue;
            }
            try {
                DatagramPacket packet = new DatagramPacket(
                        data,
                        data.length,
                        InetAddress.getLocalHost(),
                        j);
                mySocket.send(packet);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public void run()
    {
        connectToFreePortForReceiving();
        while (true) {
            getUpdate();
        }
    }
}
