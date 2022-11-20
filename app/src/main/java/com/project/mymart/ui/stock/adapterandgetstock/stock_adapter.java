package com.project.mymart.ui.stock.adapterandgetstock;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.adapter.FragmentViewHolder;

import com.project.mymart.R;
import com.project.mymart.ui.dashboard.adapterandgetset.todo_adapter;

import java.util.List;

public class stock_adapter extends ArrayAdapter {

    Context context;
    int resource;
    List<getstocksdata> objects;
    FragmentViewHolder fragmentViewHolder;
    LayoutInflater layoutInflater;

    public stock_adapter(@NonNull Context context, int resource, @NonNull List<getstocksdata> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.layoutInflater = LayoutInflater.from(context);
    }

    private class FragmentViewHolder {

        TextView stock_id, stockproduct_name, supplier_id, category_id, current_stock, sale_price, supplier_price, inventory_date, update_date;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        fragmentViewHolder = new FragmentViewHolder();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.stock_itemdesign, null);
            fragmentViewHolder = new FragmentViewHolder();

            fragmentViewHolder.stock_id = (TextView) convertView.findViewById(R.id.stock_id);
            fragmentViewHolder.stockproduct_name = (TextView) convertView.findViewById(R.id.stock_name);
            fragmentViewHolder.supplier_id = (TextView) convertView.findViewById(R.id.supplier_name);
            // fragmentViewHolder.category_id = (TextView) convertView.findViewById(R.id.);
            fragmentViewHolder.current_stock = (TextView) convertView.findViewById(R.id.current_stock);
            fragmentViewHolder.sale_price = (TextView) convertView.findViewById(R.id.sale_price);
            fragmentViewHolder.supplier_price = (TextView) convertView.findViewById(R.id.supplier_price);
            //fragmentViewHolder.inventory_date = (TextView) convertView.findViewById(R.id.);
            //fragmentViewHolder.update_date = (TextView) convertView.findViewById(R.id.);

            convertView.setTag(fragmentViewHolder);
        } else {

            fragmentViewHolder = (FragmentViewHolder) convertView.getTag();
        }

        fragmentViewHolder.stock_id.setText(objects.get(position).getStock_id());
        fragmentViewHolder.stockproduct_name.setText(objects.get(position).getStockproduct_name());
        fragmentViewHolder.supplier_id.setText(objects.get(position).getSupplier_id());
        //fragmentViewHolder.category_id.setText(objects.get(position).getTodo_name());
        fragmentViewHolder.current_stock.setText(objects.get(position).getQuantity());
        fragmentViewHolder.sale_price.setText(objects.get(position).getSale_rate());
        fragmentViewHolder.supplier_price.setText(objects.get(position).getSupplier_rate());
        /*fragmentViewHolder.inventory_date.setText(objects.get(position).getTodo_name());
        fragmentViewHolder.update_date.setText(objects.get(position).getTodo_name());*/
        return convertView;
    }
}
