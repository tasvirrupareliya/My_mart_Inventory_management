package com.project.mymart.ui.profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.project.mymart.MainActivity2;
import com.project.mymart.R;
import com.project.mymart.ui.category.CategoryFragment;
import com.project.mymart.ui.product.ProductFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.project.mymart.MainActivity2.fab;

public class ProfileFragment extends Fragment {

    private ProfileViewModel feedbackFragment;
    EditText adminfirst_name, adminlast_name, admin_contact, admin_email;
    TextView user_name, active_status, admin_id;
    CircleImageView admin_img;
    Button btn_updateprofile, btn_changepassprofile;
    FloatingActionButton updatepimage;
    Bitmap bitmap;
    String url_retrive_admin = "https://mymartproject.000webhostapp.com/app/retriveadmin.php", url_pupdate = "https://mymartproject.000webhostapp.com/app/updateprofileinfo.php";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        feedbackFragment =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        admin_id = (TextView) root.findViewById(R.id.admin_id);
        adminfirst_name = (EditText) root.findViewById(R.id.adminfirst_name);
        adminlast_name = (EditText) root.findViewById(R.id.adminlast_name);
        admin_contact = (EditText) root.findViewById(R.id.admin_contact);
        admin_email = (EditText) root.findViewById(R.id.admin_email);
        admin_img = (CircleImageView) root.findViewById(R.id.admin_img);
        user_name = (TextView) root.findViewById(R.id.user_name);
        active_status = (TextView) root.findViewById(R.id.active_status);
        btn_updateprofile = (Button) root.findViewById(R.id.btn_updateprofile);
        btn_changepassprofile = (Button) root.findViewById(R.id.btn_changepassprofile);
        updatepimage = (FloatingActionButton) root.findViewById(R.id.updatepimage);

        setHasOptionsMenu(true);
        retrive_profile();
        fab.setVisibility(View.GONE);

        btn_changepassprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Change_Password.class);
                startActivity(intent);
            }
        });

        btn_updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_profile();
            }
        });

        updatepimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileupload();
            }
        });
        return root;
    }

    private void profileupload() {
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
                admin_img.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String imagestore(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);

        byte[] imagebyte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imagebyte, Base64.DEFAULT);
    }

    private void update_profile() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, url_pupdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                if (response.equalsIgnoreCase("Profile Updated")) {
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

                SharedPreferences sharedPreference = getActivity().getSharedPreferences("Userdata", MODE_PRIVATE);
                String sharusername = sharedPreference.getString("username", null);

                params.put("username", sharusername);
                params.put("first_name", adminfirst_name.getText().toString());
                params.put("last_name", adminlast_name.getText().toString());
                params.put("contact", admin_contact.getText().toString());
                params.put("email", admin_email.getText().toString());
                params.put("profile_picture", imagestore(bitmap));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void retrive_profile() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, url_retrive_admin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("administration");
                    if (success.equals("1")) {

                        SharedPreferences sharedPreference = getActivity().getSharedPreferences("Userdata", MODE_PRIVATE);
                        String sharusername = sharedPreference.getString("username", null);

                        if (sharusername.equals(getString(R.string.makusername))) {
                            JSONObject object = jsonArray.getJSONObject(0);
                            String admin_id = object.getString("admin_id");
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String contact = object.getString("contact");
                            String email = object.getString("email");
                            String username = object.getString("username");
                            String profile_picture = object.getString("profile_picture");
                            String account_status = object.getString("account_status");

                            adminfirst_name.setText(first_name);
                            adminlast_name.setText(last_name);
                            admin_contact.setText(contact);
                            admin_email.setText(email);
                            Glide.with(getContext())
                                    .load(profile_picture)
                                    .into(admin_img);
                            user_name.setText(username);
                            active_status.setText(account_status);

                        } else if (sharusername.equals(getString(R.string.karmausername))) {

                            JSONObject object = jsonArray.getJSONObject(1);
                            String adminid = object.getString("admin_id");
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String contact = object.getString("contact");
                            String email = object.getString("email");
                            String username = object.getString("username");
                            String profile_picture = object.getString("profile_picture");
                            String account_status = object.getString("account_status");

                            adminfirst_name.setText(first_name);
                            adminlast_name.setText(last_name);
                            admin_contact.setText(contact);
                            admin_email.setText(email);
                            Glide.with(getContext())
                                    .load(profile_picture)
                                    .into(admin_img);
                            user_name.setText(username);
                            active_status.setText(account_status);
                        }
                    }
                } catch (
                        JSONException e) {
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
        ((MainActivity2) getActivity()).setActionBarTitle(getString(R.string.profile));
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
                ft.detach(ProfileFragment.this).attach(ProfileFragment.this).commit();
                return true;
        }
        return true;
    }
}
