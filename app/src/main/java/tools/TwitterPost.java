package tools;

import com.google.android.gms.maps.model.Marker;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import java.io.File;
public final class TwitterPost {

    public  void sendTweet(File path,Marker marker) {
        try {
            twitter4j.Twitter twitter = TwitterFactory.getSingleton();
            StatusUpdate status = new StatusUpdate("Posición \nLatitud:"+marker.getPosition().latitude+"\nLongitud:"+marker.getPosition().longitude );
            status.setMedia(path);
            twitter.updateStatus(status);
        } catch (TwitterException e) {
            e.printStackTrace();
        }}
    }