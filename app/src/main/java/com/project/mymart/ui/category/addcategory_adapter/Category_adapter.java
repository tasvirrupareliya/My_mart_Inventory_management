package com.project.mymart.ui.category.addcategory_adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentViewHolder;

import com.project.mymart.R;

import java.util.List;

public class Category_adapter extends ArrayAdapter {

    Context context;
    int resource;
    List<get_setmethod> objects;
    FragmentViewHolder fragmentViewHolder;
    LayoutInflater layoutInflater;

    public Category_adapter(@NonNull Context context, int resource, @NonNull List<get_setmethod> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.layoutInflater = LayoutInflater.from(context);
    }

    private class FragmentViewHolder {

        TextView cname;
        TextView cstatus;
        TextView cid;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        fragmentViewHolder = new FragmentViewHolder();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.design_category_item, null);
            fragmentViewHolder = new FragmentViewHolder();

            fragmentViewHolder.cname = (TextView) convertView.findViewById(R.id.txt_cname);
            fragmentViewHolder.cstatus = (TextView) convertView.findViewById(R.id.txt_status);
            fragmentViewHolder.cid = (TextView) convertView.findViewById(R.id.cid);

            convertView.setTag(fragmentViewHolder);
        } else {

            fragmentViewHolder = (FragmentViewHolder) convertView.getTag();
        }

        fragmentViewHolder.cname.setText(objects.get(position).getCname());
        fragmentViewHolder.cstatus.setText(objects.get(position).getCstatus());
        fragmentViewHolder.cid.setText(objects.get(position).getCid());

        if (fragmentViewHolder.cstatus.getText().toString().equals("Available")) {
            fragmentViewHolder.cstatus.setTextColor(Color.parseColor("#198754"));
        } else {
            fragmentViewHolder.cstatus.setTextColor(Color.RED);
        }
        return convertView;
    }
}
