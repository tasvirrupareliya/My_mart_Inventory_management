package com.project.mymart.ui.dashboard.adapterandgetset;

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
import androidx.viewpager2.adapter.FragmentViewHolder;

import com.project.mymart.R;
import com.project.mymart.ui.category.addcategory_adapter.Category_adapter;
import com.project.mymart.ui.category.addcategory_adapter.get_setmethod;

import java.util.ArrayList;
import java.util.List;

public class todo_adapter extends ArrayAdapter {

    Context context;
    int resource;
    List<todo_textget> objects;
    FragmentViewHolder fragmentViewHolder;
    LayoutInflater layoutInflater;

    public todo_adapter(@NonNull Context context, int resource, @NonNull List<todo_textget> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.layoutInflater = LayoutInflater.from(context);
    }

    private class FragmentViewHolder {

        TextView todotext;
        TextView todoid;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        fragmentViewHolder = new FragmentViewHolder();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.todo_design, null);
            fragmentViewHolder = new FragmentViewHolder();

            fragmentViewHolder.todoid = (TextView) convertView.findViewById(R.id.todo_id);
            fragmentViewHolder.todotext = (TextView) convertView.findViewById(R.id.todo_text);

            convertView.setTag(fragmentViewHolder);
        } else {

            fragmentViewHolder = (FragmentViewHolder) convertView.getTag();
        }

        fragmentViewHolder.todoid.setText(objects.get(position).getTodo_id());
        fragmentViewHolder.todotext.setText(objects.get(position).getTodo_name());
        return convertView;
    }

}
