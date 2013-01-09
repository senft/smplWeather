package de.senft.smplweather;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import de.senft.smplweather.request.WeatherRequest;
import de.senft.smplweather.request.WeatherRequestCallback;
import de.senft.smplweather.request.WundergroundReqest;

public class MainActivity extends Activity {

    private static final String TAG = "smplWeather";

    private TextView txtCity;
    private TextView txtCurrent;
    private TextView txtFore1;
    private TextView txtFore2;
    private TextView txtFore3;
    private ProgressBar progressBar;

    private Typeface tfSimplicity;

    private Condition[] conditions = new Condition[4];

    // Acquire a reference to the system Location Manager
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            refresh();
            return true;
        case R.id.menu_settings:
            Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_LONG)
                    .show();
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

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        txtCity.setTypeface(tfSimplicity);
        txtCurrent.setTypeface(tfSimplicity);
        txtFore1.setTypeface(tfSimplicity);
        txtFore2.setTypeface(tfSimplicity);
        txtFore3.setTypeface(tfSimplicity);

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
        // TODO check if locations changed -> city name
        txtCurrent.setVisibility(View.GONE);
        txtFore1.setVisibility(View.INVISIBLE);
        txtFore2.setVisibility(View.INVISIBLE);
        txtFore3.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Location currentLocation = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        String cityName = getCityName(currentLocation);
        txtCity.setText(cityName);
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

                txtFore1.setText("" + cond[0].getCelcius());
                txtFore2.setText("" + cond[1].getCelcius());
                txtFore3.setText("" + cond[2].getCelcius());

                Bitmap mIcon = BitmapFactory.decodeResource(getResources(),
                        conditions[1].getIcon());
                BitmapDrawable ic = new BitmapDrawable(Bitmap
                        .createScaledBitmap(mIcon, 24, 24, true));

                txtFore1.setCompoundDrawablesWithIntrinsicBounds(null, ic,
                        null, null);
                // txtFore2.setCompoundDrawablesWithIntrinsicBounds(0,
                // conditions[2].getIcon(), 0, 0);
                // txtFore3.setCompoundDrawablesWithIntrinsicBounds(0,
                // conditions[3].getIcon(), 0, 0);

                txtFore1.setVisibility(View.VISIBLE);
                txtFore2.setVisibility(View.VISIBLE);
                txtFore3.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                Toast.makeText(MainActivity.this,
                        "Failed to retrieve forecast.", Toast.LENGTH_LONG)
                        .show();
                txtFore1.setVisibility(View.INVISIBLE);
                txtFore2.setVisibility(View.INVISIBLE);
                txtFore3.setVisibility(View.INVISIBLE);

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
        Log.e(TAG, "Failed to map GPD coordinates to a city name.");
        return "Unknown";
    }

}
