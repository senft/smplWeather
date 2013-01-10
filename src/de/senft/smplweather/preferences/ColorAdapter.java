package de.senft.smplweather.preferences;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import de.senft.smplweather.R;

public class ColorAdapter extends BaseAdapter {
    private Context context;

    public Integer[] colors = { R.color.dark_grey, R.color.dark_blue,
            R.color.dark_chromeblue, R.color.green, R.color.light_blue };

    public ColorAdapter(Context c) {
        this.context = c;
    }

    @Override
    public int getCount() {
        return colors.length;
    }

    @Override
    public Object getItem(int position) {
        return colors[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(context);
        view.setBackgroundColor(view.getResources().getColor(colors[position]));
        view.setLayoutParams(new GridView.LayoutParams(80, 80));
        return view;
    }
}
