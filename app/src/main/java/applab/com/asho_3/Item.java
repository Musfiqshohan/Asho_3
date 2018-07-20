package applab.com.asho_3;


import android.net.Uri;

/** * Created by amit rawat on 11/27/2017. */public class Item {
    private String name;

    private Uri downloadUri;


    public Item(String name, Uri downloadUri) {

        this.name = name;
        this.downloadUri = downloadUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getdownloadUri() {
        return downloadUri;
    }

    public void setdownloadUri(Uri downloadUri) {
        this.downloadUri = downloadUri;
    }


}