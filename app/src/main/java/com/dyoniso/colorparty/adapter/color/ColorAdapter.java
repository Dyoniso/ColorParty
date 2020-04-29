package com.dyoniso.colorparty.adapter.color;

import android.util.Log;

import com.dyoniso.colorparty.model.Color;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter {
    private static final String TAG = ColorAdapter.class.getName();

    private List<Color> zColorList;

    public ColorAdapter() {
        this.zColorList = new ArrayList<>();

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
        String[] colorNames = {"Vermelho", "Azul", "Amarelo", "Verde"};

        for (String colorName : colorNames) {
            zColorList.add(new Color(id, colorName));
            id++;

            Log.i(TAG, "Added in ArrayList(Color), item: "+colorName);
        }
    }
}
