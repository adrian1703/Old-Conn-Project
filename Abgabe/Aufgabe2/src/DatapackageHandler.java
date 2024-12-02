import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class DatapackageHandler implements Runnable {
    private Random random;
    private List<Datapackage> datapackageDatabase;
    private final Object lock = new Object();
    public Datapackage myDatapackage;

    public DatapackageHandler()
    {
        this.random = new Random();
        datapackageDatabase = new CopyOnWriteArrayList<>();

    }

    public void createMyDatapackage(final String location)
    {

        myDatapackage =
                new Datapackage(location,
                        new Timestamp(System.currentTimeMillis()).toString(),
                        random);
        datapackageDatabase.add(myDatapackage);
    }


    /**
     * The string will have the following pattern:
     * "location1*timestamp1*temperature1*humidity1*location2*timestamp2*..."
     *
     * @param datapackages
     * @return
     */
    public List<Datapackage> extractDatapackages(String datapackages)
    {
        datapackages = datapackages.replace("\0", "");
        List<Datapackage> extracedDatapackages = new ArrayList<>();
        String[] rawData = datapackages.split("\\*");
        for (int i = 0; i < rawData.length / 4; i++) {
            int index = i * 4;
            extracedDatapackages.add(new Datapackage(
                    rawData[index],
                    rawData[index + 1],
                    rawData[index + 2],
                    rawData[index + 3]));
        }
        return extracedDatapackages;
    }

    public String convertDatapackageDatabaseToString()
    {
        StringBuilder sb = new StringBuilder();
        synchronized (lock) {
            for (Datapackage datapackage : datapackageDatabase) {
                sb
                        .append(datapackage.getLocation()).append("*")
                        .append(datapackage.getTimestamp()).append("*")
                        .append(datapackage.getTemperature()).append("*")
                        .append(datapackage.getHumidity()).append("*");

            }
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * Compares each datapackage in toUpdateWith with all data
     * packages in datapackageDatabase
     * If location of two datapackages is the same and if the timestamp
     * of datapackage to update with is newer than this datapackage
     * replaces the old one.
     * Similarly if no location which matches the one in datapackageInDatabase
     * is found the datapackage of toUpdateWith is also added the datapackageDatabase
     * All changes are compiled and finally datapackageDatabase is overwritten
     *
     * @param toUpdateWith List of received datapackes
     */
    public void updateDatapackageList(List<Datapackage> toUpdateWith)
    {
        synchronized (lock) {
            List<Datapackage> newDatabase = new ArrayList<>(datapackageDatabase);
            boolean alreadyExists;
            for (Datapackage datapackageToUpdateWith : toUpdateWith) {
                alreadyExists = false;
                for (Datapackage datapackageInDatabase : datapackageDatabase) {
                    //check if locations match
                    if (Objects.equals(
                            datapackageToUpdateWith.getLocation(),
                            datapackageInDatabase.getLocation())
                    ) {
                        //check if transmitted status has a newer timestamp
                        if (timestampIsAfterTimestamp(
                                datapackageToUpdateWith.getTimestamp(),
                                datapackageInDatabase.getTimestamp()
                        )) {
                            newDatabase.add(datapackageToUpdateWith);
                            newDatabase.remove(datapackageInDatabase);
                        }
                        alreadyExists = true;
                        break;
                    }
                }
                if (!alreadyExists) {
                    newDatabase.add(datapackageToUpdateWith);
                }
            }
            datapackageDatabase = newDatabase;
        }
    }

    /*
        returns true if the first timestamp string is newer than the second
     */
    private boolean timestampIsAfterTimestamp(String timestamp1, String timestamp2)
    {
        Timestamp ts1 = Timestamp.valueOf(timestamp1);
        Timestamp ts2 = Timestamp.valueOf(timestamp2);
        return ts1.after(ts2);
    }

    public void update(String packet)
    {
        List<Datapackage> data = extractDatapackages(packet);
        updateDatapackageList(data);
    }

    private void printDatapackageDatabase()
    {
        synchronized (lock) {
            System.out.println("*******DatapackageDatabase********");
            for (Datapackage datapackage : datapackageDatabase) {
                System.out.println(datapackage.toString());
            }
            System.out.println("**********************************");
        }
    }

    @Override
    public void run()
    {
        try {
            while (true) {
                Thread.sleep(5000);
                printDatapackageDatabase();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
