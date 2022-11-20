package com.project.mymart.ui.product.adapterandgetmethod;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.adapter.FragmentViewHolder;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.project.mymart.R;
import com.project.mymart.ui.category.addcategory_adapter.Category_adapter;
import com.project.mymart.ui.category.addcategory_adapter.get_setmethod;

import java.util.List;

public class product_adapter extends ArrayAdapter {

    Context context;
    int resource;
    private ImageLoader imageLoader;
    List<getproducts> objects;
    FragmentViewHolder fragmentViewHolder;
    LayoutInflater layoutInflater;

    public product_adapter(@NonNull Context context, int resource, @NonNull List<getproducts> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.layoutInflater = LayoutInflater.from(context);
    }

    private class FragmentViewHolder {

        TextView product_id, product_name, product_category, product_mrp, product_status;
        ImageView product_image;
    }

   /* private void loadImage(int position) {

        imageLoader = CustomVolleyRequest.getInstance(getContext().getApplicationContext())
                .getImageLoader();
        imageLoader.get(objects.get(position).getProduct_image(), ImageLoader.getImageListener(fragmentViewHolder.product_image,
                R.drawable.ic_baseline_account_circle_24, android.R.drawable
                        .ic_dialog_alert));
        //fragmentViewHolder.product_image.setImageUrl(objects.get(position).getProduct_image(), imageLoader);
    }*/

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        fragmentViewHolder = new FragmentViewHolder();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.product_item_design, null);
            fragmentViewHolder = new FragmentViewHolder();

            fragmentViewHolder.product_id = (TextView) convertView.findViewById(R.id.product_id);
            fragmentViewHolder.product_name = (TextView) convertView.findViewById(R.id.product_name);
            fragmentViewHolder.product_category = (TextView) convertView.findViewById(R.id.product_category);
            fragmentViewHolder.product_status = (TextView) convertView.findViewById(R.id.product_status);
            fragmentViewHolder.product_mrp = (TextView) convertView.findViewById(R.id.product_mrp);
            fragmentViewHolder.product_image = (ImageView) convertView.findViewById(R.id.product_image);

            convertView.setTag(fragmentViewHolder);
        } else {

            fragmentViewHolder = (FragmentViewHolder) convertView.getTag();
        }

        fragmentViewHolder.product_id.setText(objects.get(position).getProduct_id());
        fragmentViewHolder.product_name.setText(objects.get(position).getProduct_name());
        fragmentViewHolder.product_category.setText(objects.get(position).getCategory_id());
        fragmentViewHolder.product_status.setText(objects.get(position).getProduct_status());
        fragmentViewHolder.product_mrp.setText(objects.get(position).getMrp());
        Glide.with(context).load(objects.get(position).getProduct_image()).into(fragmentViewHolder.product_image);

        if (fragmentViewHolder.product_status.getText().toString().equals("Available")) {
            fragmentViewHolder.product_status.setTextColor(Color.parseColor("#198754"));
        } else {
            fragmentViewHolder.product_status.setTextColor(Color.RED);
        }
        return convertView;
    }
}
