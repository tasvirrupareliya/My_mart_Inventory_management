package com.project.mymart.ui.supplier;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.mymart.MainActivity2;
import com.project.mymart.R;
import com.project.mymart.ui.category.CategoryFragment;
import com.project.mymart.ui.supplier.sup_adapter.sup_adapter;
import com.project.mymart.ui.supplier.sup_adapter.sup_getdata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.project.mymart.MainActivity2.fab;

public class SupplierFragment extends Fragment {

    ListView s_listView;
    private SupplierViewmodel supplierViewmodel;
    public static ArrayList<sup_getdata> arrayList;
    sup_adapter adapter;
    String url_suppretrive = "https://mymartproject.000webhostapp.com/app/supplierretrive.php";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        supplierViewmodel =
                new ViewModelProvider(this).get(SupplierViewmodel.class);
        View root = inflater.inflate(R.layout.fragment_supplier, container, false);

        s_listView = (ListView) root.findViewById(R.id.suppler_list);
        arrayList = new ArrayList<sup_getdata>();

        setHasOptionsMenu(true);

        adapter = new sup_adapter(getContext(), R.layout.supplier_item_design, arrayList);
        s_listView.setAdapter(adapter);

        fab.setVisibility(View.GONE);

        s_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewdetail(position);
            }
        });

        retrivedata();

        return root;
    }

    private void viewdetail(int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view1 = getLayoutInflater().inflate(R.layout.design_alertbox_supplierdeatils, null);

        TextView supplier_id, companyname, website, first_name, last_name, contact, email, address_line1, address_line2, state, city, pincode, username;

        supplier_id = (TextView) view1.findViewById(R.id.tvsup_id);
        companyname = (TextView) view1.findViewById(R.id.tvsup_cname);
        website = (TextView) view1.findViewById(R.id.tvsup_website);
        first_name = (TextView) view1.findViewById(R.id.tvsup_fname);
        last_name = (TextView) view1.findViewById(R.id.tvsup_lname);
        contact = (TextView) view1.findViewById(R.id.tvsup_contact);
        email = (TextView) view1.findViewById(R.id.tvsup_email);
        address_line1 = (TextView) view1.findViewById(R.id.tvsup_add1);
        address_line2 = (TextView) view1.findViewById(R.id.tvsup_add2);
        state = (TextView) view1.findViewById(R.id.tvsup_state);
        city = (TextView) view1.findViewById(R.id.tvsup_city);
        pincode = (TextView) view1.findViewById(R.id.tvsup_pincode);
        username = (TextView) view1.findViewById(R.id.tvsup_username);
        ImageView imageView = (ImageView) view1.findViewById(R.id.cancel_button);


        supplier_id.setText(getString(R.string.supplierid) + " " + arrayList.get(position).getSupplier_id());
        companyname.setText(getString(R.string.comname) + " " + arrayList.get(position).getCompanyname());
        website.setText(getString(R.string.website) + " " + arrayList.get(position).getWebsite());
        first_name.setText(getString(R.string.fname) + " " + arrayList.get(position).getFirst_name());
        last_name.setText(getString(R.string.lname) + " " + arrayList.get(position).getLast_name());
        contact.setText(getString(R.string.contact) + " " + arrayList.get(position).getContact());
        email.setText(getString(R.string.email) + " " + arrayList.get(position).getEmail());
        address_line1.setText(getString(R.string.add1) + " " + arrayList.get(position).getAddress_line1());
        address_line2.setText(getString(R.string.add2) + " " + arrayList.get(position).getAddress_line2());
        state.setText(getString(R.string.state) + " " + arrayList.get(position).getState());
        city.setText(getString(R.string.city) + " " + arrayList.get(position).getCity());
        pincode.setText(getString(R.string.pincode) + " " + arrayList.get(position).getPincode());
        username.setText(getString(R.string.username) + " " + arrayList.get(position).getUsername());

        builder.setView(view1);
        final AlertDialog alertDialog = builder.create();
        //alertDialog.setTitle("Supplier Details");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void retrivedata() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, url_suppretrive, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                arrayList.clear();
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

                            arrayList.add(new sup_getdata(supplier_id, companyname, website, first_name, last_name, contact, email, address_line1, address_line2, state, city, pincode, username));
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
        ((MainActivity2) getActivity()).setActionBarTitle(getString(R.string.supplier));
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
                ft.detach(SupplierFragment.this).attach(SupplierFragment.this).commit();
                return true;
        }
        return true;
    }
}
