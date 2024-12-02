import java.sql.Timestamp;
import java.util.Random;

public class CycleManager implements Runnable {
    private final Random random = new Random();
    private final Datapackage myDatapackage;
    private final Client client;

    public CycleManager(DatapackageHandler datapackageHandler, Client client)
    {
        this.client = client;
        this.myDatapackage = datapackageHandler.myDatapackage;
    }


    public void run()
    {
        while (true) {
            try {
                Thread.sleep(random.nextInt(20000) + 5000);
                shuffleMyDatapackage();
                Thread.sleep(100);
                client.sendUpdate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void shuffleMyDatapackage()
    {
        myDatapackage.shuffleTemperature(random);
        myDatapackage.shuffleHumidity(random);
        myDatapackage.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
    }




}
