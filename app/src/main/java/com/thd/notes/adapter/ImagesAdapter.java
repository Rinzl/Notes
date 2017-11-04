package com.thd.notes.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thd.notes.R;

import java.util.ArrayList;

public class ImagesAdapter extends ArrayAdapter<Bitmap> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<Bitmap> data;
    private OnDeleteClick onDeleteClick;
    public interface OnDeleteClick {
        void onClick(int position);
    }

    public void setOnDeleteClick(OnDeleteClick onDeleteClick) {
        this.onDeleteClick = onDeleteClick;
    }

    public ImagesAdapter(Context context, int layoutResourceId, ArrayList<Bitmap> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.ivPicture);
            holder.textView = (TextView) row.findViewById(R.id.tv);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Bitmap image = data.get(position);
        holder.image.setImageBitmap(image);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClick.onClick(position);
            }
        });
        return row;
    }

    private static class ViewHolder {
        ImageView image;
        TextView textView;
    }
}