package android.example.earth;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {
    private EarthquakeAdapter mAdapter;
    private static final int EARTHQUAKE_LOADER_ID = 1;

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);

        mAdapter = new EarthquakeAdapter(this, R.layout.list_item, new ArrayList<>() );

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = findViewById(R.id.list);

        earthquakeListView.setEmptyView(findViewById(R.id.empty_view));


        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener((parent, view, position, id) -> {

            /* Opening full information from a website USGS
            Earthquake currentEarthquake = mAdapter.getItem(position);
            Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
            startActivity(websiteIntent);
            */

            Earthquake currentEarthquake = mAdapter.getItem(position);
            String lat = String.parse()

        });

        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String minMagnitude = sharedPreferences.getString(getString(R.string.settings_min_magnitude_key), getString(R.string.settings_min_magnitude_default));
        String limit = sharedPreferences.getString(getString(R.string.settings_limit_key), getString(R.string.settings_limit_default));
        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri basseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = basseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", limit);
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Earthquake>> loader, List<Earthquake> data) {
        TextView emptyView = findViewById(R.id.empty_view);
        emptyView.setText(R.string.no_earthquakes);

        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        mAdapter.clear();
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Earthquake>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
