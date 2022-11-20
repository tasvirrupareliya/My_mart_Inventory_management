package com.project.mymart.ui.stock;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bumptech.glide.Glide;
import com.project.mymart.MainActivity2;
import com.project.mymart.R;
import com.project.mymart.ui.category.CategoryFragment;
import com.project.mymart.ui.category.addcategory_adapter.get_setmethod;
import com.project.mymart.ui.dashboard.adapterandgetset.todo_adapter;
import com.project.mymart.ui.dashboard.adapterandgetset.todo_textget;
import com.project.mymart.ui.product.adapterandgetmethod.getproducts;
import com.project.mymart.ui.stock.adapterandgetstock.getstocksdata;
import com.project.mymart.ui.stock.adapterandgetstock.stock_adapter;
import com.project.mymart.ui.supplier.sup_adapter.sup_getdata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.project.mymart.MainActivity2.fab;

public class StockFragment extends Fragment {

    SwipeMenuListView stock_listview;
    ArrayList<getstocksdata> arrayList;
    stock_adapter adapter;
    ArrayList<String> arrayList2, arrayList3, arrayList4;
    TextView heading_dialogboxstock;
    EditText supplier_price, sale_price, product_stock_quantity, stockid;
    Button btn_close_stock, btn_addupdatestock;
    Spinner stockproduct_spinner, stockproductsupplier_spinner, stockcategory_spinner;
    String url_updatestock = "https://mymartproject.000webhostapp.com/app/stockupdate.php", url_suppretrive = "https://mymartproject.000webhostapp.com/app/supplierretrive.php", url_retriveproduct = "https://mymartproject.000webhostapp.com/app/retriveproducts.php", url_categoryretrive = "https://mymartproject.000webhostapp.com/app/categoryretrive.php", url_addstock = "https://mymartproject.000webhostapp.com/app/stockinsert.php", url_stockdelete = "https://mymartproject.000webhostapp.com/app/deletestock.php", url_stockretrive = "https://mymartproject.000webhostapp.com/app/retrivestock.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stock, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        stock_listview = (SwipeMenuListView) view.findViewById(R.id.stock_listview);
        arrayList = new ArrayList<getstocksdata>();

        adapter = new stock_adapter(getContext(), R.layout.stock_itemdesign, arrayList);
        stock_listview.setAdapter(adapter);

        setHasOptionsMenu(true);
        retrive_stock();

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
        stock_listview.setMenuCreator(creator);
        stock_listview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        updatedata(position);
                        break;
                    case 1:
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("Delete entry");
                        alert.setMessage("Are you sure you want to delete?");
                        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                deletedata(arrayList.get(position).getStock_id());
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

        fab.setVisibility(View.VISIBLE);
        fab.setImageResource(R.drawable.ic_baseline_add_24);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view1 = getLayoutInflater().inflate(R.layout.stock_insertupdate_design, null);

                stockproduct_spinner = (Spinner) view1.findViewById(R.id.stockproduct_spinner);
                heading_dialogboxstock = (TextView) view1.findViewById(R.id.heading_dialogboxstock);
                stockproductsupplier_spinner = (Spinner) view1.findViewById(R.id.stockproductsupplier_spinner);
                stockcategory_spinner = (Spinner) view1.findViewById(R.id.stockcategory_spinner);
                supplier_price = (EditText) view1.findViewById(R.id.supplier_price);
                sale_price = (EditText) view1.findViewById(R.id.sale_price);
                product_stock_quantity = (EditText) view1.findViewById(R.id.product_stock_quantity);
                btn_close_stock = (Button) view1.findViewById(R.id.btn_close_stock);
                btn_addupdatestock = (Button) view1.findViewById(R.id.btn_addupdatestock);

                btn_addupdatestock.setText(getString(R.string.add));
                heading_dialogboxstock.setText(getString(R.string.add_stock));

                builder.setView(view1);
                final AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);

                arrayList2 = new ArrayList<>();
                categorynameretrive(stockcategory_spinner);

                arrayList3 = new ArrayList<>();
                retrive_productlist(stockproduct_spinner);

                arrayList4 = new ArrayList<>();
                retrivesupplier_companyname(stockproductsupplier_spinner);

                btn_close_stock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btn_addupdatestock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (supplier_price.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "Enter Supplier Price", Toast.LENGTH_LONG).show();
                        } else if (sale_price.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "Enter Sale Price", Toast.LENGTH_LONG).show();
                        } else if (product_stock_quantity.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "Enter Product Quantity", Toast.LENGTH_LONG).show();
                        } else {
                            final ProgressDialog progressDialog = new ProgressDialog(getContext());
                            progressDialog.setMessage("Please Wait..");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            StringRequest request = new StringRequest(Request.Method.POST, url_addstock, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    alertDialog.dismiss();

                                    if (response.equalsIgnoreCase("Stock Added")) {
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
                                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }

                            ) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("stockproduct_name", stockproduct_spinner.getSelectedItem().toString());
                                    params.put("supplier_id", stockproductsupplier_spinner.getSelectedItem().toString());
                                    params.put("category_id", String.valueOf(stockcategory_spinner.getSelectedItemPosition() + 1));
                                    params.put("quantity", product_stock_quantity.getText().toString().trim());
                                    params.put("sale_rate", sale_price.getText().toString().trim());
                                    params.put("supplier_rate", supplier_price.getText().toString().trim());
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
        });
    }

    private void updatedata(int position) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view1 = getLayoutInflater().inflate(R.layout.stock_insertupdate_design, null);

        stockproduct_spinner = (Spinner) view1.findViewById(R.id.stockproduct_spinner);
        heading_dialogboxstock = (TextView) view1.findViewById(R.id.heading_dialogboxstock);
        stockproductsupplier_spinner = (Spinner) view1.findViewById(R.id.stockproductsupplier_spinner);
        stockcategory_spinner = (Spinner) view1.findViewById(R.id.stockcategory_spinner);
        supplier_price = (EditText) view1.findViewById(R.id.supplier_price);
        sale_price = (EditText) view1.findViewById(R.id.sale_price);
        product_stock_quantity = (EditText) view1.findViewById(R.id.product_stock_quantity);
        btn_close_stock = (Button) view1.findViewById(R.id.btn_close_stock);
        btn_addupdatestock = (Button) view1.findViewById(R.id.btn_addupdatestock);
        stockid = (EditText) view1.findViewById(R.id.stock_id);
        LinearLayout full_productname = (LinearLayout) view1.findViewById(R.id.full_productname);
        LinearLayout full_categoryname = (LinearLayout) view1.findViewById(R.id.full_categoryname);
        LinearLayout full_suppliername = (LinearLayout) view1.findViewById(R.id.full_suppliername);

        btn_addupdatestock.setText(getString(R.string.update));
        heading_dialogboxstock.setText(getString(R.string.update_stock));
        full_categoryname.setVisibility(View.GONE);
        full_suppliername.setVisibility(View.GONE);
        full_productname.setVisibility(View.GONE);

        stockid.setText(arrayList.get(position).getStock_id());
        supplier_price.setText(arrayList.get(position).getSupplier_rate());
        product_stock_quantity.setText(arrayList.get(position).getQuantity());
        sale_price.setText(arrayList.get(position).getSale_rate());

        builder.setView(view1);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btn_close_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_addupdatestock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Please Wait..");
                progressDialog.setCancelable(false);
                progressDialog.show();

                StringRequest request = new StringRequest(Request.Method.POST, url_updatestock, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        alertDialog.dismiss();

                        if (response.equalsIgnoreCase("Stock Updated")) {
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
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("stock_id", stockid.getText().toString());
                        params.put("quantity", product_stock_quantity.getText().toString());
                        params.put("sale_rate", sale_price.getText().toString());
                        params.put("supplier_rate", supplier_price.getText().toString());
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(request);
            }
        });
        alertDialog.show();
    }

    private void retrivesupplier_companyname(Spinner spinner) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, url_suppretrive, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("suppliers");
                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String supplier_id = object.getString("supplier_id");
                            String companyname = object.getString("companyname");
                            String website = object.getString("website");
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String contact = object.getString("contact");
                            String email = object.getString("email");
                            String address_line1 = object.getString("address_line1");
                            String address_line2 = object.getString("address_line2");
                            String state = object.getString("state");
                            String city = object.getString("city");
                            String pincode = object.getString("pincode");
                            String username = object.getString("username");

                            arrayList4.add(object.getString("companyname"));

                            spinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrayList4));
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

    private void retrive_productlist(Spinner spinner) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, url_retriveproduct, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
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

                            arrayList3.add(object.getString("product_name"));

                            spinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrayList3));
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

    private void deletedata(String position) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url_stockdelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                if (response.equalsIgnoreCase("Stock Deleted")) {
                    Toast.makeText(getContext(), "Refresh the app", Toast.LENGTH_SHORT).show();
                    arrayList.remove(position);
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
                params.put("stock_id", position);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void retrive_stock() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, url_stockretrive, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                arrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("stock");
                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String stock_id = object.getString("stock_id");
                            String stockproduct_name = object.getString("stockproduct_name");
                            String supplier_id = object.getString("supplier_id");
                            String category_id = object.getString("category_id");
                            String quantity = object.getString("quantity");
                            String sale_rate = object.getString("sale_rate");
                            String supplier_rate = object.getString("supplier_rate");
                            String inventory_date = object.getString("inventory_date");
                            String update_date = object.getString("update_date");

                            arrayList.add(new getstocksdata(stock_id, stockproduct_name, supplier_id, category_id, quantity, sale_rate, supplier_rate, inventory_date, update_date));
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

    public void onResume() {
        super.onResume();
        ((MainActivity2) getActivity()).setActionBarTitle(getString(R.string.stock));
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
                ft.detach(StockFragment.this).attach(StockFragment.this).commit();
                return true;
        }
        return true;
    }
}
