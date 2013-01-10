package de.senft.smplweather.preferences;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import de.senft.smplweather.R;

public class FixedColorPicker extends Preference implements OnItemClickListener {
    private Context context;

    public FixedColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        setLayoutResource(R.layout.fixed_color_picker);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        GridView gridView = (GridView) view.findViewById(R.id.gridView1);
        gridView.setAdapter(new ColorAdapter(context));

        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Log.i("Picker", "clicked");
    }

}
