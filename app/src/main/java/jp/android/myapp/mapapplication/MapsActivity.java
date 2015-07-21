package jp.android.myapp.mapapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity {

    //private SQLiteDatabase db;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        Intent intent = getIntent();

        if(intent != null) {
            String add = intent.getStringExtra("address");
            //String name = intent.getStringExtra("name");
            double latitude = 0.00;
            double longitude = 0.00;

            try {
                //登録した住所の緯度経度を取得
                Address address = getLatLongFromLocationName(add);
                latitude = address.getLatitude();
                longitude = address.getLongitude();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //現在地を取得
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Location myLocate = locationManager.getLastKnownLocation("gps");
            double myLatitude = 0.00;
            double myLongitude = 0.00;
            if (myLocate != null) {
                //現在地の緯度経度
                myLatitude = myLocate.getLatitude();
                myLongitude = myLocate.getLongitude();
            }

            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 18);
            mMap.moveCamera(cu);

            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(add));
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);

            // 現在地から目標まで線で結ぶ
            PolylineOptions line = new PolylineOptions();
            line.add(new LatLng(latitude, longitude)); // 登録地
            line.add(new LatLng(myLatitude, myLongitude)); // 現在地
            line.color(0xcc00ffff);
            line.width(10);
            line.geodesic(true);
            mMap.addPolyline(line);

            // 航空写真に変更
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            // 渋滞状況を表示
            mMap.setTrafficEnabled(true);
        }
    }

    private Address getLatLongFromLocationName(String locationName) throws IOException {

        Geocoder gCoder = new Geocoder(this, Locale.getDefault());

        List<Address> addressList = gCoder.getFromLocationName(locationName, 1);
        Address address = addressList.get(0);

        return address;
    }
   /* private Cursor searchToDB() throws Exception {

        MyDBHelper dbHelper = new MyDBHelper(this);
        db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("select * from " + MyDBHelper.TABLE_NAME, null);
        return c;

    }
*/
}
