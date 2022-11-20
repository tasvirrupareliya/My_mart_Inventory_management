package com.project.mymart.ui.reports.adapterandget;

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

public class adapter_sales extends ArrayAdapter {

    Context context;
    int resource;
    List<get_salesdata> objects;
    FragmentViewHolder fragmentViewHolder;
    LayoutInflater layoutInflater;

    public adapter_sales(@NonNull Context context, int resource, @NonNull List<get_salesdata> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.layoutInflater = LayoutInflater.from(context);
    }

    private class FragmentViewHolder {

        TextView order_date, client_name, client_contact, grand_total;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        fragmentViewHolder = new FragmentViewHolder();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.sales_report_item_design, null);
            fragmentViewHolder = new FragmentViewHolder();

            fragmentViewHolder.order_date = (TextView) convertView.findViewById(R.id.order_date);
            fragmentViewHolder.client_name = (TextView) convertView.findViewById(R.id.client_name);
            fragmentViewHolder.client_contact = (TextView) convertView.findViewById(R.id.client_contact);
            fragmentViewHolder.grand_total = (TextView) convertView.findViewById(R.id.grand_total);

            convertView.setTag(fragmentViewHolder);
        } else {

            fragmentViewHolder = (FragmentViewHolder) convertView.getTag();
        }

        fragmentViewHolder.order_date.setText(objects.get(position).getOrder_date());
        fragmentViewHolder.client_name.setText(objects.get(position).getClient_name());
        fragmentViewHolder.client_contact.setText(objects.get(position).getClient_contact());
        fragmentViewHolder.grand_total.setText(objects.get(position).getGrand_total());

        /*if (fragmentViewHolder.cstatus.getText().toString().equals("Available")) {
            fragmentViewHolder.cstatus.setTextColor(Color.parseColor("#198754"));
        } else {
            fragmentViewHolder.cstatus.setTextColor(Color.RED);
        }*/
        return convertView;
    }
}
