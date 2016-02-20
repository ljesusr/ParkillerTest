package test.parker.com.parkiller;
import android.location.Criteria;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import java.util.Timer;
import java.util.TimerTask;
import tools.AndroidFuntions;
import tools.AutoCompleteAddress;
import tools.GpsFuntions;
import tools.GpsTracker;
import tools.MapFuncion;
import tools.TwitterPost;


public class MapActivity extends FragmentActivity implements GoogleMap.OnMapClickListener {
    Timer timer = new Timer();
    private GoogleMap mMap;
    private boolean flatStarApp;
    private Button  btnStart;
    private TextView tvfooter;
    //tools class
    private GpsFuntions gpsFuntions;
    private GpsTracker gpsTracker;
    private MapFuncion mapFuncion;
    private AndroidFuntions androidFuntions;
    private TwitterPost updateStatus;
    private AutoCompleteAddress autoCompleteTextView;
    private AutoCompleteTextView autoCompView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        init();
        setUpMapIfNeeded();
        autoCompView = (AutoCompleteTextView) findViewById(R.id.tv_direccion);
        autoCompleteTextView = new AutoCompleteAddress(this,autoCompView,mapFuncion);

        timer.schedule( new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            gpsTracker.getLocation();
                            mapFuncion.updateUserPosition(gpsTracker.getLatitud(), gpsTracker.getLongitud());
                            if(flatStarApp)
                            mapFuncion.calculatedDistance(mapFuncion.getUserMaker().getPosition().latitude, mapFuncion.getUserMaker().getPosition().longitude, tvfooter);
                            }catch (Exception e){}
                        ;}});
                }}, 0, 5*1000);
        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setSpeedRequired(true);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mapFuncion.getUserMaker()==null){
            setUpMapIfNeeded();
            setUpMap();
        }
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }}
        if (mMap != null) {
            mMap.setOnMapClickListener(this);
        }
    }

    public void init(){
        gpsFuntions= new GpsFuntions(this);
        gpsTracker= new GpsTracker(this);
        updateStatus=new TwitterPost();
        androidFuntions= new AndroidFuntions(this);
        btnStart=(Button)findViewById(R.id.btn_start);
        tvfooter =(TextView)findViewById(R.id.tvfooter);
    }

    private void setUpMap() {
        mapFuncion=new MapFuncion(mMap,this);
        gpsTracker.getLocation();
        mapFuncion.setZoom(gpsTracker.getLatitud(), gpsTracker.getLongitud(), 16);
        mapFuncion.addUserMarker(gpsTracker.getLatitud(), gpsTracker.getLongitud());
    }

    @Override
    public void onMapClick(LatLng point) {
        if(mapFuncion.getMarkerDestino()==null){
            mapFuncion.addDestinoMarker(point.latitude,point.longitude);
        }else{
            if(!flatStarApp)
            mapFuncion.updateDestinoPosition(point.latitude,point.longitude);
        }
    }

    public void startApp(View v){
        if(mapFuncion.getMarkerDestino()!=null) {
            if (!flatStarApp) {
                btnStart.setText("Reiniciar");
                mapFuncion.loadCircleMarker();
                flatStarApp = true;
            } else {
                mMap.clear();
                autoCompView.setText("");
                tvfooter.setText("Seleccione una ubicación");
                mapFuncion.cleanValues();
                btnStart.setText("Inciar");

                setUpMap();
                flatStarApp = false;
            }}else
            androidFuntions.showMessageInToast("Seleccione un Destino");
    }

    public void returnLastedPosition(View v){
        mapFuncion.returnToUserPosition();
    }

    public void hybridMap(View v){
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }
    public void satelliteMap(View v){
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onBackPressed(){
        androidFuntions.sendApptoBackground();
    }



}



