package com.project.mymart.ui.supplier.sup_adapter;

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

import java.util.List;

public class sup_adapter extends ArrayAdapter {

    Context context;
    int resource;
    List<sup_getdata> objects;
    LayoutInflater layoutInflater;
    FragmentViewHolder fragmentViewHolder;

    public sup_adapter(@NonNull Context context, int resource, @NonNull List<sup_getdata> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.layoutInflater = LayoutInflater.from(context);
    }

    private class FragmentViewHolder {

        TextView suppid;
        TextView suppcompanyname;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        fragmentViewHolder = new FragmentViewHolder();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.supplier_item_design, null);
            fragmentViewHolder = new FragmentViewHolder();

            fragmentViewHolder.suppid = (TextView) convertView.findViewById(R.id.sup_id);
            fragmentViewHolder.suppcompanyname = (TextView) convertView.findViewById(R.id.sup_companyname);

            convertView.setTag(fragmentViewHolder);
        } else {

            fragmentViewHolder = (FragmentViewHolder) convertView.getTag();
        }

        fragmentViewHolder.suppid.setText(objects.get(position).getSupplier_id());
        fragmentViewHolder.suppcompanyname.setText(objects.get(position).getCompanyname());
        return convertView;
    }
}
