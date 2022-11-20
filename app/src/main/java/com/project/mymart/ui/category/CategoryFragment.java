package com.project.mymart.ui.category;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

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
import com.project.mymart.MainActivity2;
import com.project.mymart.R;
import com.project.mymart.ui.category.addcategory_adapter.Category_adapter;
import com.project.mymart.ui.category.addcategory_adapter.get_setmethod;
import com.project.mymart.ui.dashboard.HomeFragment;
import com.project.mymart.ui.supplier.SupplierFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.project.mymart.MainActivity2.fab;

public class CategoryFragment extends Fragment {

    private CategoryViewModel galleryViewModel;
    SwipeMenuListView category_listView;
    public static ArrayList<get_setmethod> ar;
    Category_adapter adapter;
    String c_name_str, c_status_str, url_update = "https://mymartproject.000webhostapp.com/app/categoryupdate.php", url_delete = "https://mymartproject.000webhostapp.com/app/categorydelete.php", url_addcategory = "https://mymartproject.000webhostapp.com/app/addcategory.php", url_retrive = "https://mymartproject.000webhostapp.com/app/categoryretrive.php";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(CategoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_category, container, false);

        category_listView = (SwipeMenuListView) root.findViewById(R.id.category_listview);
        ar = new ArrayList<get_setmethod>();

        setHasOptionsMenu(true);

        adapter = new Category_adapter(getContext(), R.layout.design_category_item, ar);
        category_listView.setAdapter(adapter);

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
        category_listView.setMenuCreator(creator);
        category_listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
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
                                deletedata(ar.get(position).getCid());
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

        retrivedata();

        fab.setVisibility(View.VISIBLE);
        fab.setImageResource(R.drawable.ic_baseline_add_24);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view1 = getLayoutInflater().inflate(R.layout.design_addcategory_alertbox, null);

                Spinner c_status = (Spinner) view1.findViewById(R.id.status_spinner);
                final EditText c_name = (EditText) view1.findViewById(R.id.c_name);
                Button btn_addcategory = (Button) view1.findViewById(R.id.btn_addcategory);
                Button btn_close_category = (Button) view1.findViewById(R.id.btn_close_category);

                builder.setView(view1);
                final AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);

                final ArrayList<String> arrayList = new ArrayList<>();

                arrayList.add("Available");
                arrayList.add("Unavailable");

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, arrayList);
                c_status.setAdapter(arrayAdapter);

                btn_close_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btn_addcategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (c_name.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "Enter Category Name", Toast.LENGTH_LONG).show();
                        } else {

                            final ProgressDialog progressDialog = new ProgressDialog(getContext());
                            progressDialog.setMessage("Please Wait..");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            c_name_str = c_name.getText().toString().trim();
                            c_status_str = c_status.getSelectedItem().toString();

                            StringRequest request = new StringRequest(Request.Method.POST, url_addcategory, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    alertDialog.dismiss();

                                    if (response.equalsIgnoreCase("Category Added")) {

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
                                    params.put("category_name", c_name_str);
                                    params.put("category_status", c_status_str);
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
        return root;
    }

    private void updatedata(int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view1 = getLayoutInflater().inflate(R.layout.designfor_edititem, null);

        Spinner c_status = (Spinner) view1.findViewById(R.id.ed_spinner);
        EditText c_name = (EditText) view1.findViewById(R.id.c_edname);
        EditText c_id = (EditText) view1.findViewById(R.id.c_edid);
        Button btn_addcategory = (Button) view1.findViewById(R.id.btn_edupdatecategory);
        Button btn_close_category = (Button) view1.findViewById(R.id.btn_close_edcategory);

        builder.setView(view1);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        final ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Available");
        arrayList.add("Unavailable");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, arrayList);
        c_status.setAdapter(arrayAdapter);

        c_id.setText(ar.get(position).getCid());
        c_name.setText(ar.get(position).getCname());

        btn_close_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_addcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Please Wait..");
                progressDialog.setCancelable(false);
                progressDialog.show();

                StringRequest request = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        alertDialog.dismiss();

                        if (response.equalsIgnoreCase("Category Updated")) {

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
                        params.put("category_id", c_id.getText().toString());
                        params.put("category_name", c_name.getText().toString());
                        params.put("category_status", c_status.getSelectedItem().toString());
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(request);
            }
        });
        alertDialog.show();
    }

    private void deletedata(final String position) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                if (response.equalsIgnoreCase("Category Deleted")) {
                    ar.remove(position);
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
                params.put("category_id", position);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void retrivedata() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, url_retrive, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                ar.clear();
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

                            ar.add(new get_setmethod(category_id, category_name, category_status));
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

        ((MainActivity2) getActivity()).setActionBarTitle("Category List");
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
                ft.detach(CategoryFragment.this).attach(CategoryFragment.this).commit();
                return true;
        }
        return true;
    }
}