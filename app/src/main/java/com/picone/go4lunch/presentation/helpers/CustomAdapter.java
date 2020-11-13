package com.picone.go4lunch.presentation.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.picone.go4lunch.R;

import java.util.Arrays;

import static com.picone.go4lunch.presentation.helpers.GetBitmapFromVectorUtil.getBitmapFromVectorDrawable;

public class CustomAdapter extends ArrayAdapter<String> {

    Context context;
    String[] languages;
    int[] flags;

    public CustomAdapter(@NonNull Context context, String[] languages, int[] flags) {
        super(context, R.layout.spinner_item, languages);
        this.context = context;
        this.languages = languages;
        this.flags = flags;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = View.inflate(context, R.layout.spinner_item, null);
        TextView textView = row.findViewById(R.id.spinnerTextView);
        ImageView imageView = row.findViewById(R.id.spinnerImages);
        textView.setText(languages[position]);
        imageView.setImageBitmap(getBitmapFromVectorDrawable(context, flags[position]));

        return row;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View row = View.inflate(context, R.layout.spinner_item, null);
        TextView textView = row.findViewById(R.id.spinnerTextView);
        ImageView imageView = row.findViewById(R.id.spinnerImages);
        textView.setText(languages[position]);
        imageView.setImageBitmap(getBitmapFromVectorDrawable(context, flags[position]));

        return row;
    }
}