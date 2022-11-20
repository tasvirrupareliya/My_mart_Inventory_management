package com.project.mymart.ui.dashboard;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.project.mymart.ui.category.CategoryFragment;
import com.project.mymart.ui.dashboard.adapterandgetset.todo_adapter;
import com.project.mymart.ui.dashboard.adapterandgetset.todo_textget;
import com.project.mymart.ui.product.ProductFragment;
import com.project.mymart.ui.stock.StockFragment;
import com.project.mymart.ui.supplier.SupplierFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.project.mymart.MainActivity2.fab;
import static com.project.mymart.MainActivity2.navigationView;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    SwipeMenuListView todo_listview;
    ArrayList<todo_textget> todo_textgets;
    todo_adapter adapter;
    View root;
    RelativeLayout product_box, category_box, stock_box, supplier_box;
    String urlstocklowcount = "https://mymartproject.000webhostapp.com/app/countstocklow.php", urlproductcount = "https://mymartproject.000webhostapp.com/app/countproducts.php", urlcategorycount = "https://mymartproject.000webhostapp.com/app/countcategory.php", urlsuppliercount = "https://mymartproject.000webhostapp.com/app/countsuppliers.php", url_deletetodo = "https://mymartproject.000webhostapp.com/app/tododelete.php", url_todoinsert = "https://mymartproject.000webhostapp.com/app/todoinsert.php", url_retrivetodo = "https://mymartproject.000webhostapp.com/app/retrivetodolist.php";
    TextView dash_productcount, dash_categorycount, dash_suppliercount, dash_stockcount;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        todo_listview = (SwipeMenuListView) root.findViewById(R.id.todo_listview);
        dash_productcount = (TextView) root.findViewById(R.id.dash_productcount);
        dash_categorycount = (TextView) root.findViewById(R.id.dash_categorycount);
        dash_suppliercount = (TextView) root.findViewById(R.id.dash_suppliercount);
        dash_stockcount = (TextView) root.findViewById(R.id.dash_ordercount);
        stock_box = (RelativeLayout) root.findViewById(R.id.order_box);
        supplier_box = (RelativeLayout) root.findViewById(R.id.supplier_box);
        category_box = (RelativeLayout) root.findViewById(R.id.category_box);
        product_box = (RelativeLayout) root.findViewById(R.id.product_box);
        todo_textgets = new ArrayList<todo_textget>();

        adapter = new todo_adapter(getContext(), R.layout.todo_design, todo_textgets);
        todo_listview.setAdapter(adapter);

        setHasOptionsMenu(true);

        supplier_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment, new SupplierFragment());
                fr.commit();

                navigationView.getMenu().getItem(2).setChecked(true);
            }
        });

        stock_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment, new StockFragment());
                fr.commit();

                navigationView.getMenu().getItem(5).setChecked(true);
            }
        });

        category_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment, new CategoryFragment());
                fr.commit();

                navigationView.getMenu().getItem(3).setChecked(true);
            }
        });

        product_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment, new ProductFragment());
                fr.commit();

                navigationView.getMenu().getItem(4).setChecked(true);
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
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
        todo_listview.setMenuCreator(creator);
        todo_listview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        deletetodo_data(todo_textgets.get(position).getTodo_id());
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
                View view1 = getLayoutInflater().inflate(R.layout.todo_insertdesign, null);

                final EditText todo_entertext = (EditText) view1.findViewById(R.id.todo_entertext);
                Button btn_addtodo = (Button) view1.findViewById(R.id.btn_addtodo);
                Button btn_closetodo = (Button) view1.findViewById(R.id.btn_closetodo);

                builder.setView(view1);
                final AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);

                btn_closetodo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btn_addtodo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (todo_entertext.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "Enter Task", Toast.LENGTH_LONG).show();
                        } else {

                            final ProgressDialog progressDialog = new ProgressDialog(getContext());
                            progressDialog.setMessage("Please Wait..");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            StringRequest request = new StringRequest(Request.Method.POST, url_todoinsert, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    alertDialog.dismiss();

                                    if (response.equalsIgnoreCase("TODO Added")) {
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
                                    params.put("title", todo_entertext.getText().toString());
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

        retrive_todolist();
        count_suppliers();
        count_category();
        count_product();
        count_stock_low();

        return root;
    }

    private void count_suppliers() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, urlsuppliercount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                dash_suppliercount.setText(response);
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

    private void count_category() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, urlcategorycount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                dash_categorycount.setText(response);
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

    private void count_product() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, urlproductcount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                dash_productcount.setText(response);
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

    private void count_stock_low() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, urlstocklowcount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                dash_stockcount.setText(response);
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

    private void deletetodo_data(String position) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url_deletetodo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                if (response.equalsIgnoreCase("Task Deleted")) {
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
                params.put("task_id", position);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void retrive_todolist() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, url_retrivetodo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                todo_textgets.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String task_id = object.getString("task_id");
                            String title = object.getString("title");

                            todo_textgets.add(new todo_textget(task_id, title));
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
                ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
                return true;
        }
        return true;
    }

    public void onResume() {
        super.onResume();
        ((MainActivity2) getActivity()).setActionBarTitle("Dashboard");
    }
}