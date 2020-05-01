package com.dyoniso.colorparty.adapter.color;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.dyoniso.colorparty.R;
import com.dyoniso.colorparty.model.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColorAdapter {
    private static final String TAG = ColorAdapter.class.getName();

    private List<Color> zColorList;
    private Context zContext;

    public ColorAdapter(Context context) {
        this.zColorList = new ArrayList<>();
        this.zContext = context;

        addColor();
    }

    public Color getColor(int position) {
        return zColorList.get(position);
    }

    public int colorCount() {
        return zColorList.size();
    }

    private void addColor() {
        int id = 0;
        int[] hexs = new int[0];
        String[] colorNames = {
                zContext.getString(R.string.color_block_name_0),
                zContext.getString(R.string.color_block_name_1),
                zContext.getString(R.string.color_block_name_2),
                zContext.getString(R.string.color_block_name_3),
                zContext.getString(R.string.color_block_name_4),
                zContext.getString(R.string.color_block_name_5),
                zContext.getString(R.string.color_block_name_6),
                zContext.getString(R.string.color_block_name_7),

        };

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hexs = new int[] {
                    zContext.getColor(R.color.color_block_0),
                    zContext.getColor(R.color.color_block_1),
                    zContext.getColor(R.color.color_block_2),
                    zContext.getColor(R.color.color_block_3),
                    zContext.getColor(R.color.color_block_4),
                    zContext.getColor(R.color.color_block_5),
                    zContext.getColor(R.color.color_block_6),
                    zContext.getColor(R.color.color_block_7)
            };
        }

        for (String colorName : colorNames) {
            zColorList.add(new Color(id, colorName, hexs[id]));
            id++;

            Log.i(TAG, "Added in ArrayList(Color), item: "+colorName);
        }
    }
}
