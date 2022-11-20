package com.project.mymart.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.mymart.R;

import java.util.HashMap;
import java.util.Map;

public class Change_Password extends AppCompatActivity {

    String urlchangepass = "https://mymartproject.000webhostapp.com/app/appchangepassword.php", ccpass, cnpass, crnpass;
    EditText change_cpass, change_newpass, change_renewpass;
    Button btn_changepass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);

        change_cpass = (EditText) findViewById(R.id.change_cpass);
        change_newpass = (EditText) findViewById(R.id.change_newpass);
        change_renewpass = (EditText) findViewById(R.id.change_renewpass);
        btn_changepass = (Button) findViewById(R.id.btn_changepass);

        ccpass = change_cpass.getText().toString();
        cnpass = change_newpass.getText().toString();
        crnpass = change_renewpass.getText().toString();

        SharedPreferences sharedPreference = getSharedPreferences("Userdata", MODE_PRIVATE);
        String sharusername = sharedPreference.getString("username", null);
        String sharpassword = sharedPreference.getString("password", null);

        btn_changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (change_cpass.getText().toString().equals("")) {
                    Toast.makeText(Change_Password.this, "Enter Current Password", Toast.LENGTH_LONG).show();
                } else if (change_newpass.getText().toString().equals("")) {
                    Toast.makeText(Change_Password.this, "Enter New Password", Toast.LENGTH_LONG).show();
                } else if (change_renewpass.getText().toString().equals("")) {
                    Toast.makeText(Change_Password.this, "Re-Enter New Password", Toast.LENGTH_LONG).show();
                } else {
                    if (cnpass.equals(crnpass)) {
                        final ProgressDialog progressDialog = new ProgressDialog(Change_Password.this);
                        progressDialog.setMessage("Please Wait..");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        StringRequest request = new StringRequest(Request.Method.POST, urlchangepass, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();

                                if (response.equalsIgnoreCase("Password Change Successfully")) {

                                    change_cpass.setText("");
                                    change_newpass.setText("");
                                    change_renewpass.setText("");

                                    Toast.makeText(Change_Password.this, response, Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Change_Password.this, response, Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(Change_Password.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                        ) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                /*params.put("oldpass", ccpass);
                                params.put("newpass", cnpass);*/

                                if (ccpass.equals(sharpassword)){
                                    params.put("newpass", cnpass);
                                    params.put("username", sharusername);
                                }else{
                                    Toast.makeText(Change_Password.this, "Old Password Did Not Match", Toast.LENGTH_SHORT).show();
                                }
                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(Change_Password.this);
                        requestQueue.add(request);
                    } else {
                        Toast.makeText(Change_Password.this, "Please Verify Your New Password & Re-type Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}