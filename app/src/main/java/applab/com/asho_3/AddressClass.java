package applab.com.asho_3;

/**
 * Created by musfiq on 8/4/18.
 */

public class AddressClass {


    private String latitude;
    private String longitude;

    public AddressClass(String latitude, String longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


}
