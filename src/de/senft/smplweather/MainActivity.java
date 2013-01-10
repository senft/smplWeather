package de.senft.smplweather;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import de.senft.smplweather.request.CachedRequest;
import de.senft.smplweather.request.WeatherRequest;
import de.senft.smplweather.request.WeatherRequestCallback;
import de.senft.smplweather.request.WundergroundReqest;

public class MainActivity extends Activity {

    private static final String TAG = "smplWeather";

    // The time a request is up-to-date (in ms)
    private final int TIMEOUT_REQUEST = 3600000;

    private SharedPreferences pref;

    private CachedRequest lastRequest;

    private TextView txtCity;
    private TextView txtCurrent;
    private TextView txtFore1;
    private TextView txtFore2;
    private TextView txtFore3;
    private TextView txtFore1Date;
    private TextView txtFore2Date;
    private TextView txtFore3Date;
    private ProgressBar progressBar;

    private Typeface tfSimplicity;

    private Condition[] conditions = new Condition[4];

    // Acquire a reference to the system Location Manager
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        pref.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(
                    SharedPreferences sharedPreferences, String key) {
                Log.i(TAG, "Changed: " + key);
            }
        });

        lastRequest = new CachedRequest(this);

        initUI();

        locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);

        refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_refresh:
            refresh(true);
            return true;
        case R.id.menu_settings:
            // Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_LONG)
            // .show();
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void initUI() {
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        tfSimplicity = Typeface.createFromAsset(this.getAssets(),
                "fonts/simplicity.ttf");

        txtCity = (TextView) findViewById(R.id.txtCity);
        txtCurrent = (TextView) findViewById(R.id.txtCurrent);
        txtFore1 = (TextView) findViewById(R.id.txtFore1);
        txtFore2 = (TextView) findViewById(R.id.txtFore2);
        txtFore3 = (TextView) findViewById(R.id.txtFore3);

        txtFore1Date = (TextView) findViewById(R.id.txtFore1Date);
        txtFore2Date = (TextView) findViewById(R.id.txtFore2Date);
        txtFore3Date = (TextView) findViewById(R.id.txtFore3Date);

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        txtCity.setTypeface(tfSimplicity);
        txtCurrent.setTypeface(tfSimplicity);
        txtFore1.setTypeface(tfSimplicity);
        txtFore2.setTypeface(tfSimplicity);
        txtFore3.setTypeface(tfSimplicity);
        txtFore1Date.setTypeface(tfSimplicity);
        txtFore2Date.setTypeface(tfSimplicity);
        txtFore3Date.setTypeface(tfSimplicity);

        txtFore1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, conditions[1].getText(),
                        Toast.LENGTH_LONG).show();
            }
        });

        txtFore2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, conditions[2].getText(),
                        Toast.LENGTH_LONG).show();
            }
        });

        txtFore3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, conditions[3].getText(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void refresh() {
        refresh(false);
    }

    private void refresh(boolean forceUpdate) {
        Location currentLocation = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        final String currentCityName = getCityName(currentLocation);

        if (!forceUpdate
                && currentCityName.equals(lastRequest.getCityName())
                && lastRequest.getTime() + TIMEOUT_REQUEST > System
                        .currentTimeMillis()) {
            // Data should still be up-to-date
            Log.i(TAG, "Getting data from storage.");

            txtCity.setText("" + lastRequest.getCityName());
            txtCurrent.setText("" + lastRequest.getCurrentTemp());
            txtFore1.setText(lastRequest.getMinTempFore(1) + " / "
                    + lastRequest.getMaxTempFore(1));
            txtFore2.setText(lastRequest.getMinTempFore(2) + " / "
                    + lastRequest.getMaxTempFore(2));
            txtFore3.setText(lastRequest.getMinTempFore(3) + " / "
                    + lastRequest.getMaxTempFore(3));

            txtFore1Date.setText(lastRequest.getDateFore(1));
            txtFore2Date.setText(lastRequest.getDateFore(2));
            txtFore3Date.setText(lastRequest.getDateFore(3));

            txtCity.setVisibility(View.VISIBLE);
            txtCurrent.setVisibility(View.VISIBLE);
            txtFore1.setVisibility(View.VISIBLE);
            txtFore2.setVisibility(View.VISIBLE);
            txtFore3.setVisibility(View.VISIBLE);
            txtFore1Date.setVisibility(View.VISIBLE);
            txtFore2Date.setVisibility(View.VISIBLE);
            txtFore3Date.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;
        }

        txtCurrent.setVisibility(View.GONE);
        txtFore1.setVisibility(View.INVISIBLE);
        txtFore2.setVisibility(View.INVISIBLE);
        txtFore3.setVisibility(View.INVISIBLE);
        txtFore1Date.setVisibility(View.INVISIBLE);
        txtFore2Date.setVisibility(View.INVISIBLE);
        txtFore3Date.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        txtCity.setText(currentCityName);
        txtCity.setVisibility(View.VISIBLE);

        WeatherRequest request = new WundergroundReqest(this,
                currentLocation.getLatitude(), currentLocation.getLongitude());

        request.getCurrentCondition(new WeatherRequestCallback() {

            @Override
            public void onSuccess(Condition[] cond) {
                Condition currentCondition = cond[0];
                conditions[0] = currentCondition;

                txtCurrent.setText("" + currentCondition.getCelcius());
                txtCurrent.setCompoundDrawablesWithIntrinsicBounds(0,
                        currentCondition.getIcon(), 0, 0);
                progressBar.setVisibility(View.GONE);
                txtCurrent.setVisibility(View.VISIBLE);

                lastRequest.setCurrentTemp(currentCondition.getCelcius());
                lastRequest.setTime(System.currentTimeMillis());
                lastRequest.setCityName(currentCityName);
            }

            @Override
            public void onError() {
                Toast.makeText(MainActivity.this,
                        "Failed to retrieve conditions.", Toast.LENGTH_LONG)
                        .show();
                txtCurrent.setVisibility(View.INVISIBLE);
                Log.e(TAG, "Failed to retreive conditions.");
            }

        });

        request.getForecast(new WeatherRequestCallback() {

            @Override
            public void onSuccess(Condition[] cond) {

                conditions[1] = cond[0];
                conditions[2] = cond[1];
                conditions[3] = cond[2];

                txtFore1.setText(cond[0].getMinC() + " / " + cond[0].getMaxC());
                txtFore2.setText(cond[1].getMinC() + " / " + cond[1].getMaxC());
                txtFore3.setText(cond[2].getMinC() + " / " + cond[2].getMaxC());

                txtFore1Date.setText(cond[0].getDate());
                txtFore2Date.setText(cond[1].getDate());
                txtFore3Date.setText(cond[2].getDate());

                Bitmap mIcon = BitmapFactory.decodeResource(getResources(),
                        conditions[1].getIcon());
                BitmapDrawable ic1 = new BitmapDrawable(Bitmap
                        .createScaledBitmap(mIcon, 160, 160, true));

                mIcon = BitmapFactory.decodeResource(getResources(),
                        conditions[1].getIcon());
                BitmapDrawable ic2 = new BitmapDrawable(Bitmap
                        .createScaledBitmap(mIcon, 160, 160, true));

                mIcon = BitmapFactory.decodeResource(getResources(),
                        conditions[1].getIcon());
                BitmapDrawable ic3 = new BitmapDrawable(Bitmap
                        .createScaledBitmap(mIcon, 160, 160, true));

                txtFore1.setCompoundDrawablesWithIntrinsicBounds(null, ic1,
                        null, null);
                txtFore2.setCompoundDrawablesWithIntrinsicBounds(null, ic2,
                        null, null);
                txtFore3.setCompoundDrawablesWithIntrinsicBounds(null, ic3,
                        null, null);

                txtFore1.setVisibility(View.VISIBLE);
                txtFore2.setVisibility(View.VISIBLE);
                txtFore3.setVisibility(View.VISIBLE);
                txtFore1Date.setVisibility(View.VISIBLE);
                txtFore2Date.setVisibility(View.VISIBLE);
                txtFore3Date.setVisibility(View.VISIBLE);

                lastRequest.setDateFore(1, cond[0].getDate());
                lastRequest.setDateFore(2, cond[1].getDate());
                lastRequest.setDateFore(3, cond[2].getDate());
                lastRequest.setMinTempFore(1, cond[0].getMinC());
                lastRequest.setMinTempFore(2, cond[1].getMinC());
                lastRequest.setMinTempFore(3, cond[2].getMinC());
                lastRequest.setMaxTempFore(1, cond[0].getMaxC());
                lastRequest.setMaxTempFore(2, cond[1].getMaxC());
                lastRequest.setMaxTempFore(3, cond[2].getMaxC());
            }

            @Override
            public void onError() {
                Toast.makeText(MainActivity.this,
                        "Failed to retrieve forecast.", Toast.LENGTH_LONG)
                        .show();
                txtFore1.setVisibility(View.INVISIBLE);
                txtFore2.setVisibility(View.INVISIBLE);
                txtFore3.setVisibility(View.INVISIBLE);
                txtFore1Date.setVisibility(View.INVISIBLE);
                txtFore2Date.setVisibility(View.INVISIBLE);
                txtFore3Date.setVisibility(View.INVISIBLE);

                Log.e(TAG, "Failed to retreive forecast.");
            }

        });
    }

    private String getCityName(Location location) {
        Geocoder gcd = new Geocoder(this.getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            if (addresses.size() > 0) {
                return addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "Failed to map GPS coordinates to a city name.");
        return "Unknown";
    }
}