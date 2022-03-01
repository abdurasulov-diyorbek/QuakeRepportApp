package android.example.earth;

public class Earthquake {
    private double mag;
    private String location;
    private Long mTimeMilliseconds;
    private String url;

    public Earthquake(double mag, String location, Long TimeMilliseconds, String url) {
        this.mag = mag;
        this.location = location;
        this.mTimeMilliseconds = TimeMilliseconds;
        this.url = url;
    }

    public double getMag() {
        return mag;
    }


    public String getLocation() {
        return location;
    }

    public Long getTime() {
        return mTimeMilliseconds;
    }

    public String getUrl() {
        return url;
    }
}
