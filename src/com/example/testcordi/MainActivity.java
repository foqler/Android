package com.example.testcordi;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

  TextView tvEnabledGPS;
  TextView tvStatusGPS;
  TextView tvLocationGPS;
  TextView tvEnabledNet;
  TextView tvStatusNet;
  TextView tvLocationNet;

  private LocationManager locationManager;
  StringBuilder sbGPS = new StringBuilder();
  StringBuilder sbNet = new StringBuilder();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    tvEnabledGPS = (TextView) findViewById(R.id.tvEnabledGPS);
    tvStatusGPS = (TextView) findViewById(R.id.tvStatusGPS);
    tvLocationGPS = (TextView) findViewById(R.id.tvLocationGPS);
    tvEnabledNet = (TextView) findViewById(R.id.tvEnabledNet);
    tvStatusNet = (TextView) findViewById(R.id.tvStatusNet);
    tvLocationNet = (TextView) findViewById(R.id.tvLocationNet);

    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
  }

  @Override
  protected void onResume() {
    super.onResume();
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
        1000 * 10, 10, locationListener);
    locationManager.requestLocationUpdates(
        LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
        locationListener);
    checkEnabled();
  }

  @Override
  protected void onPause() {
    super.onPause();
    locationManager.removeUpdates(locationListener);
  }

  private LocationListener locationListener = new LocationListener() {

    @Override
    public void onLocationChanged(Location location) {
      tvEnabledGPS.setText("sds");
      showLocation(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
      checkEnabled();
    }

    @Override
    public void onProviderEnabled(String provider) {
      checkEnabled();
      showLocation(locationManager.getLastKnownLocation(provider));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
      if (provider.equals(LocationManager.GPS_PROVIDER)) {
        tvStatusGPS.setText("Status: " + String.valueOf(status));
      } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
        tvStatusNet.setText("Status: " + String.valueOf(status));
      }
    }
  };

  private void showLocation(Location location) {
	  Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, true);  
		location = locationManager.getLastKnownLocation(provider);
		if (location != null) {
			   double latitude = location.getLatitude();
			   double longitude = location.getLongitude();
			   tvEnabledGPS.setText(""+latitude+" - "+longitude);
	    }
  }

  private String formatLocation(Location location) {
    if (location == null){
      tvEnabledGPS.setText("SYJA");
      return "";
    }else{
    return String.format(
        "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
        location.getLatitude(), location.getLongitude(), new Date(
            location.getTime()));
    }
  }

  private void checkEnabled() {
    tvEnabledGPS.setText("Enabled: "
        + locationManager
            .isProviderEnabled(LocationManager.GPS_PROVIDER));
    tvEnabledNet.setText("Enabled: "
        + locationManager
            .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
  }

  public void onClickLocationSettings(View view) {
	Criteria criteria = new Criteria();
	String provider = locationManager.getBestProvider(criteria, true);  
	Location  location = locationManager.getLastKnownLocation(provider);
	if (location != null) {
		   double latitude = location.getLatitude();
		   double longitude = location.getLongitude();
		   tvEnabledGPS.setText(""+latitude+" - "+longitude);
    }
    //startActivity(new Intent(
    //    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
  };

}