package com.dyoniso.colorparty.tool;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {
    public static void create(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
