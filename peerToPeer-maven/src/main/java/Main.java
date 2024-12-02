public class Main {

    public static void main(String[] args){
        InputHandler inputHandler = new InputHandler();
        String location = inputHandler.getInput();


        DatapackageHandler datapackageHandler = new DatapackageHandler();
        datapackageHandler.createMyDatapackage(location);

        Client client = new Client(datapackageHandler);
        CycleManager cycleManager = new CycleManager(datapackageHandler,client);


        Thread clientThread = new Thread(client);
        Thread cycleManagerThread = new Thread(cycleManager);
        Thread datapackageHandlerThread = new Thread(datapackageHandler);

        clientThread.start();
        cycleManagerThread.start();
        datapackageHandlerThread.start();
    }
}
