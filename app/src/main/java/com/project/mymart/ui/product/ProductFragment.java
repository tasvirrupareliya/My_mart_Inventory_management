package com.project.mymart.ui.product;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.project.mymart.MainActivity2;
import com.project.mymart.R;
import com.project.mymart.ui.product.adapterandgetmethod.getproducts;
import com.project.mymart.ui.product.adapterandgetmethod.product_adapter;
import com.project.mymart.ui.profile.ProfileViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.project.mymart.MainActivity2.fab;
import static com.project.mymart.ui.category.CategoryFragment.ar;

public class ProductFragment extends Fragment {

    SwipeMenuListView product_listView;
    ArrayList<getproducts> products_arrayList;
    String url_addproduct = "https://mymartproject.000webhostapp.com/app/productinsert.php", url_categoryretrive = "https://mymartproject.000webhostapp.com/app/categoryretrive.php", url_productupdate = "https://mymartproject.000webhostapp.com/app/productupdate.php", url_retriveproduct = "https://mymartproject.000webhostapp.com/app/retriveproducts.php", url_pdelete = "https://mymartproject.000webhostapp.com/app/productdelete.php";
    product_adapter adapter;
    ArrayList<String> arrayList2;
    ImageView product_icon;
    Bitmap bitmap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        product_listView = (SwipeMenuListView) view.findViewById(R.id.product_listview);
        products_arrayList = new ArrayList<getproducts>();

        adapter = new product_adapter(getContext(), R.layout.product_item_design, products_arrayList);
        product_listView.setAdapter(adapter);

        setHasOptionsMenu(true);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getContext().getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0x19, 0x87,
                        0x54)));
                // set item width
                openItem.setWidth(170);
                openItem.setIcon(R.drawable.ic_baseline_edit_24);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getContext().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xcc,
                        0x00, 0x00)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_baseline_delete_24);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        product_listView.setMenuCreator(creator);
        product_listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        updatepdata(position);
                        break;
                    case 1:
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("Delete entry");
                        alert.setMessage("Are you sure you want to delete?");
                        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                deletedata(products_arrayList.get(position).getProduct_id());
                            }
                        });
                        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // close dialog
                                dialog.cancel();
                            }
                        });
                        alert.show();
                        break;
                }
                return false;
            }
        });

        retrive_product();

        fab.setVisibility(View.VISIBLE);
        fab.setImageResource(R.drawable.ic_baseline_add_24);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertproduct();
            }
        });
    }

    private void updateproductimage() {
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                product_icon.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String imagestore(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);

        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(imageByte, Base64.DEFAULT);
    }

    private void insertproduct() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view1 = getLayoutInflater().inflate(R.layout.updateproducts_design, null);

        Spinner productcategory_spinner = (Spinner) view1.findViewById(R.id.productcategory_spinner);
        Spinner productstatus_spinner = (Spinner) view1.findViewById(R.id.productstatus_spinner);
        EditText p_name = (EditText) view1.findViewById(R.id.p_name);
        EditText p_mrp = (EditText) view1.findViewById(R.id.p_mrp);
        TextView heading_dialogboxproduct = (TextView) view1.findViewById(R.id.heading_dialogboxproduct);
        EditText p_id = (EditText) view1.findViewById(R.id.p_id);
        product_icon = (ImageView) view1.findViewById(R.id.product_icon);
        Button btn_updateinsertproduct = (Button) view1.findViewById(R.id.btn_updateproduct);
        Button btn_close_product = (Button) view1.findViewById(R.id.btn_close_product);

        btn_updateinsertproduct.setText(getString(R.string.add));
        heading_dialogboxproduct.setText(getString(R.string.add_product));

        builder.setView(view1);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        final ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Available");
        arrayList.add("Unavailable");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, arrayList);
        productstatus_spinner.setAdapter(arrayAdapter);

        arrayList2 = new ArrayList<>();
        categorynameretrive(productcategory_spinner);

        btn_close_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        product_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateproductimage();
            }
        });
        btn_updateinsertproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (p_name.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Enter Product Name", Toast.LENGTH_LONG).show();
                } else if (p_mrp.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Enter Product MRP", Toast.LENGTH_LONG).show();
                } else if (product_icon.getDrawable() == null) {
                    Toast.makeText(getContext(), "No image uploaded", Toast.LENGTH_SHORT).show();
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Please Wait..");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    StringRequest request = new StringRequest(Request.Method.POST, url_addproduct, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            alertDialog.dismiss();

                            if (response.equalsIgnoreCase("Product Added")) {
                                Toast.makeText(getContext(), "Refresh the app", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            alertDialog.dismiss();
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("product_name", p_name.getText().toString());
                            params.put("category_id", String.valueOf(productcategory_spinner.getSelectedItemPosition()) + 1);
                            params.put("product_image", imagestore(bitmap));
                            params.put("mrp", p_mrp.getText().toString());
                            params.put("product_status", productstatus_spinner.getSelectedItem().toString());
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    requestQueue.add(request);
                }
            }
        });
        alertDialog.show();
    }

    private void updatepdata(int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view1 = getLayoutInflater().inflate(R.layout.updateproducts_design, null);

        Spinner productcategory_spinner = (Spinner) view1.findViewById(R.id.productcategory_spinner);
        Spinner productstatus_spinner = (Spinner) view1.findViewById(R.id.productstatus_spinner);
        EditText p_name = (EditText) view1.findViewById(R.id.p_name);
        EditText p_mrp = (EditText) view1.findViewById(R.id.p_mrp);
        TextView heading_dialogboxproduct = (TextView) view1.findViewById(R.id.heading_dialogboxproduct);
        EditText p_id = (EditText) view1.findViewById(R.id.p_id);
        product_icon = (ImageView) view1.findViewById(R.id.product_icon);
        Button btn_updateinsertproduct = (Button) view1.findViewById(R.id.btn_updateproduct);
        Button btn_close_product = (Button) view1.findViewById(R.id.btn_close_product);

        btn_updateinsertproduct.setText(getString(R.string.update));
        heading_dialogboxproduct.setText(getString(R.string.update_product));

        p_id.setText(products_arrayList.get(position).getProduct_id());
        p_name.setText(products_arrayList.get(position).getProduct_name());
        Glide.with(getContext()).load(products_arrayList.get(position).getProduct_image()).into(product_icon);
        p_mrp.setText(products_arrayList.get(position).getMrp());

        builder.setView(view1);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        final ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Available");
        arrayList.add("Unavailable");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, arrayList);
        productstatus_spinner.setAdapter(arrayAdapter);

        arrayList2 = new ArrayList<>();

        categorynameretrive(productcategory_spinner);

        btn_close_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        product_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateproductimage();
            }
        });

        btn_updateinsertproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Please Wait..");
                progressDialog.setCancelable(false);
                progressDialog.show();

                StringRequest request = new StringRequest(Request.Method.POST, url_productupdate, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        alertDialog.dismiss();

                        if (response.equalsIgnoreCase("Product Updated")) {
                            Toast.makeText(getContext(), "Refresh the app", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        alertDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("product_id", p_id.getText().toString().trim());
                        params.put("product_name", p_name.getText().toString().trim());
                        params.put("category_id", String.valueOf(productcategory_spinner.getSelectedItemPosition() + 1));
                        params.put("product_image", imagestore(bitmap));
                        params.put("mrp", p_mrp.getText().toString().trim());
                        params.put("product_status", productstatus_spinner.getSelectedItem().toString());

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(request);
            }
        });
        alertDialog.show();
    }

    private void categorynameretrive(Spinner spinner) {
        StringRequest request = new StringRequest(Request.Method.POST, url_categoryretrive, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("category");
                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String category_id = object.getString("category_id");
                            String category_name = object.getString("category_name");
                            String category_status = object.getString("category_status");

                            arrayList2.add(object.getString("category_name"));

                            spinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrayList2));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void deletedata(final String position) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url_pdelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                if (response.equalsIgnoreCase("Product Deleted")) {
                    Toast.makeText(getContext(), "Refresh the app", Toast.LENGTH_SHORT).show();
                    products_arrayList.remove(position);
                    //adapter.remove(position);
                    Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("product_id", position);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void retrive_product() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, url_retriveproduct, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                products_arrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String product_id = object.getString("product_id");
                            String product_name = object.getString("product_name");
                            String category_id = object.getString("category_id");
                            String product_image = object.getString("product_image");
                            String mrp = object.getString("mrp");
                            String product_status = object.getString("product_status");

                            products_arrayList.add(new getproducts(product_id, product_name, category_id, product_image, mrp, product_status));
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    public void onResume() {
        super.onResume();
        ((MainActivity2) getActivity()).setActionBarTitle(getString(R.string.add_product));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.three_dots, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_activity:
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(ProductFragment.this).attach(ProductFragment.this).commit();
                return true;
        }
        return true;
    }
}
