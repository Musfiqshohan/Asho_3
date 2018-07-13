package applab.com.asho_3;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by musfiq on 7/6/18.
 */

public class Asho_3 extends Application {

    public void onCreate(){

        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
