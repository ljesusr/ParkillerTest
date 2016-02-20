package tools;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.os.Environment;
import android.os.StrictMode;
import android.os.Vibrator;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import test.parker.com.parkiller.R;

public class MapFuncion {
    GoogleMap mMap;
    Activity activity;
    Marker markerUser = null, markerDestino = null;
    boolean flagTweetControl=true;


    public MapFuncion(GoogleMap mMap, Activity activity) {
        this.mMap = mMap;
        this.activity = activity;
    }

    public void setZoom(double latitud, double longitud, int zoom) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longitud), zoom));
    }

    public void returnToUserPosition() {
        setZoom(markerUser.getPosition().latitude, markerUser.getPosition().longitude, 15);
    }

    public Marker getUserMaker() {
        return markerUser;
    }

    public void settUserMaker(Marker markerUser) {
        this.markerUser = markerUser;
    }

    public Marker getMarkerDestino() {
        return markerDestino;
    }

    public void setMarkerDestino(Marker markerDestino) {
        this.markerDestino = markerDestino;
    }

    public void updateUserPosition(double latitud, double longitud) {
        markerUser.setPosition(new LatLng(latitud, longitud));
    }

    public void updateDestinoPosition(double latitud, double longitud) {
        markerDestino.setPosition(new LatLng(latitud, longitud));

    }

    public void addUserMarker(double latitud, double longitud) {
        markerUser = mMap.addMarker(new MarkerOptions().position(new LatLng(latitud, longitud)).title("Usuario").
                icon(BitmapDescriptorFactory.fromResource(R.drawable.current_position)));
    }

    public void addDestinoMarker(double latitud, double longitud) {
        markerDestino = mMap.addMarker(new MarkerOptions().position(new LatLng(latitud, longitud)).title("Destino").
                icon(BitmapDescriptorFactory.fromResource(R.drawable.finish_position)));

    }

    public void addCircleToMap(double latitud, double longitud, int radio, int color) {
        LatLng latLng = new LatLng(latitud, longitud);
        int d = 500;
        Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint();
        p.setColor(color);
        c.drawCircle(d / 2, d / 2, d / 2, p);
        BitmapDescriptor bmD = BitmapDescriptorFactory.fromBitmap(bm);
        mMap.addGroundOverlay(new GroundOverlayOptions().
                image(bmD).
                position(latLng, radio * 2, radio * 2).
                transparency(0.4f));
    }


    public void loadCircleMarker() {
        for (int x = 0; x < 5; x++) {
            switch (x) {
                case 0:
                    addCircleToMap(getMarkerDestino().getPosition().
                            latitude, getMarkerDestino().getPosition().longitude, 250, activity.getResources().getColor(R.color.circle_200m));
                    break;
                case 1:
                    addCircleToMap(getMarkerDestino().getPosition().
                            latitude, getMarkerDestino().getPosition().longitude, 199, activity.getResources().getColor(R.color.circle_150m));
                    break;
                case 2:
                    addCircleToMap(getMarkerDestino().getPosition().
                            latitude, getMarkerDestino().getPosition().longitude, 150, activity.getResources().getColor(R.color.circle_100m));
                    break;
                case 3:
                    addCircleToMap(getMarkerDestino().getPosition().
                            latitude, getMarkerDestino().getPosition().longitude, 100, activity.getResources().getColor(R.color.circle_50m));
                    break;
                case 4:
                    addCircleToMap(getMarkerDestino().getPosition().
                            latitude, getMarkerDestino().getPosition().longitude, 10, activity.getResources().getColor(R.color.circle_10m));
                    break;
            }
        }
    }

    public void calculatedDistance(double latitud, double longitud, TextView tvfooter) {
        if (getMarkerDestino() != null) {
            Location locationA = new Location("point A");
            locationA.setLatitude(getMarkerDestino().getPosition().latitude);
            locationA.setLongitude(getMarkerDestino().getPosition().longitude);
            Location locationB = new Location("point B");
            locationB.setLatitude(latitud);
            locationB.setLongitude(longitud);
            float distance = locationA.distanceTo(locationB);

            if (distance < 10) {
                tvfooter.setText("Estás en el punto objetivo");
                if(flagTweetControl){
                    screenShot();
                    Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    v.vibrate(1000);
                    flagTweetControl=false;
                }}else flagTweetControl=true;
            if (distance > 10 && distance <= 50)
                tvfooter.setText("Estás muy próximo al punto objetivo");
            if (distance > 50 && distance <= 100)
                tvfooter.setText("Estás próximo al punto objetivo");
            if (distance > 100 && distance <= 200)
                tvfooter.setText("Estás lejos del punto objetivo");
            if (distance > 200) tvfooter.setText("Estás muy lejos del punto objetivo");
        }
    }

    public void screenShot() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;
            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                // TODO Auto-generated method stub
                bitmap = snapshot;
                try {
                    UUID uuid = UUID.randomUUID();
                     String filename = "parkiller_"+uuid.toString()+".png";
                     File sd = Environment.getExternalStorageDirectory();
                    File dest = new File(sd, filename);
                    try {
                        FileOutputStream out = new FileOutputStream(dest);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                        out.flush();
                        out.close();
                        new TwitterPost().sendTweet(new File(Environment.getExternalStorageDirectory(), filename ),getMarkerDestino());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }} catch (Exception e) {
                    e.printStackTrace();
                }}};
        mMap.snapshot(callback);
}

    public void cleanValues(){
        flagTweetControl=true;
        markerUser=null;
        markerDestino=null;
    }



}







