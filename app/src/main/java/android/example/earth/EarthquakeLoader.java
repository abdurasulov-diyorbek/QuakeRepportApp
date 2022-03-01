package android.example.earth;

import android.content.AsyncTaskLoader;
import android.content.Context;
import androidx.annotation.Nullable;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private final String mUrl;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Earthquake> loadInBackground() {
        if(mUrl == null){
            return null;
        }

        List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;
    }
}
