package thangtv.com.trather.ui;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by uendno on 03-Nov-15.
 */
public class ParseApplication extends Application{
    private static String APPLICATION_ID = "AGvShiLrsaTQf93lhDDO2NmzT2dBvPk6oxpgLIYh";
    private static String CLIENT_KEY = "RqEzhoVUNXoXo50UE4lcDocqX0L6Uv3LhMpMW0uQ";
    @Override
    public void onCreate() {
        super.onCreate();
        //enable local datastore
        Parse.enableLocalDatastore(this);

        //add initialization code
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);

        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }
}
