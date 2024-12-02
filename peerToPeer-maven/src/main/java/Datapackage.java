import java.sql.Timestamp;
import java.util.Random;

public class Datapackage {
    private String timestamp;
    private final String location;
    private String humidity;
    private String temperature;

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public String getLocation()
    {
        return location;
    }

    public String getHumidity()
    {
        return humidity;
    }

    public String getTemperature()
    {
        return temperature;
    }


    public Datapackage(String location, String timestamp, String temperature, String humidity)
    {
        this.location = location;
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public Datapackage(String location, String timestamp, Random random)
    {
        this.location = location;
        this.timestamp = timestamp;
        shuffleHumidity(random);
        shuffleTemperature(random);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        return sb.append("Location: " + location + "\n")
                .append("Timestamp: " + timestamp + "\n")
                .append("Temperature: " + temperature + "\n")
                .append("Humidity: " + humidity)
                .toString();
    }

    public void shuffleTemperature(Random random)
    {
        int newTemperature = random.nextInt(60);
        newTemperature -= 20;
        temperature = newTemperature + "C";
    }

    public void shuffleHumidity(Random random)
    {
        int newHumidity = random.nextInt(101);
        humidity = newHumidity + "%";
    }


}
