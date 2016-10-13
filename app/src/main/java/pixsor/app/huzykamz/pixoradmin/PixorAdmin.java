package pixsor.app.huzykamz.pixoradmin;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by HUZY_KAMZ on 10/2/2016.
 */
public class PixorAdmin  extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        //Enabling Offline Capability...On strings and Images as well .....
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);


    }
}
