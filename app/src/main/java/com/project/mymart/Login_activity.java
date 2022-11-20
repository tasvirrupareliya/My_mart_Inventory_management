package com.project.mymart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class Login_activity extends AppCompatActivity {

    /* TextView register_already, login_already;*/
    //View fit_login, fit_register;
    EditText /*r_name, r_mobileno, r_email, r_password,*/ l_email, l_password;
    Button login_btn/*, register_btn*/;
    public static String lstr_email;
    String urllogin = "https://mymartproject.000webhostapp.com/app/applogin.php", /*rstr_name, rstr_email, rstr_password, rstr_mobileno,*/
            lstr_password;
    //SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       /* login_already = (TextView) findViewById(R.id.login_already);
        register_already = (TextView) findViewById(R.id.register_already);
        fit_login = (View) findViewById(R.id.fit_login);*/
        // fit_register = (View) findViewById(R.id.fit_register);
        /*register_btn = (Button) findViewById(R.id.register_btn);*/
        login_btn = (Button) findViewById(R.id.login_btn);/*
        r_name = (EditText) findViewById(R.id.r_name);
        r_mobileno = (EditText) findViewById(R.id.r_mobileno);
        r_email = (EditText) findViewById(R.id.r_email);
        r_password = (EditText) findViewById(R.id.r_password);
       */
        l_email = (EditText) findViewById(R.id.l_email);
        l_password = (EditText) findViewById(R.id.l_password);
        // sessionManager=new SessionManager(this);

       /* register_already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fit_register.setVisibility(View.GONE);
                fit_login.setVisibility(View.VISIBLE);
            }
        });

        login_already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fit_register.setVisibility(View.VISIBLE);
                fit_login.setVisibility(View.GONE);
            }
        });
*/
        SharedPreferences sharedPreference = getSharedPreferences("Userdata", MODE_PRIVATE);
        String sharusername = sharedPreference.getString("username", null);
        String password = sharedPreference.getString("password", null);

        if (sharusername != null && password != null) {
            Intent intent = new Intent(Login_activity.this, MainActivity2.class);
            startActivity(intent);
        }

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l_email.getText().toString().equals("")) {
                    Toast.makeText(Login_activity.this, "Enter Email", Toast.LENGTH_LONG).show();
                } else if (l_password.getText().toString().equals("")) {
                    Toast.makeText(Login_activity.this, "Enter Password", Toast.LENGTH_LONG).show();
                } else  {
                    final ProgressDialog progressDialog = new ProgressDialog(Login_activity.this);
                    progressDialog.setMessage("Please Wait..");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    lstr_email = l_email.getText().toString().trim();
                    lstr_password = l_password.getText().toString().trim();

                    StringRequest request = new StringRequest(Request.Method.POST, urllogin, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            if (response.equalsIgnoreCase("Login Successfully")) {

                                SharedPreferences sharedPreferences = getSharedPreferences("Userdata", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString("username", lstr_email);
                                editor.putString("password", lstr_password);

                                editor.apply();

                                Toast.makeText(Login_activity.this, response, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();
                                startActivity(intent);
                            } else {
                                Toast.makeText(Login_activity.this, response, Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(Login_activity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("username", lstr_email);
                            params.put("password", lstr_password);

                            return params;

                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(Login_activity.this);
                    requestQueue.add(request);
                }/* else {
                    Toast.makeText(Login_activity.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
                }*/
            }

            /*public void moveToRegistration(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                finish();
            }*/
        });

       /* register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(Login_activity.this);
                progressDialog.setMessage("Please Wait..");

                if (r_name.getText().toString().equals("")) {
                    Toast.makeText(Login_activity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                } else if (r_mobileno.getText().toString().equals("")) {
                    Toast.makeText(Login_activity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (r_email.getText().toString().equals("")) {
                    Toast.makeText(Login_activity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                } else if (r_password.getText().toString().equals("")) {
                    Toast.makeText(Login_activity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    rstr_name = r_name.getText().toString().trim();
                    rstr_email = r_email.getText().toString().trim();
                    rstr_password = r_password.getText().toString().trim();
                    rstr_mobileno = r_mobileno.getText().toString().trim();

                    StringRequest request = new StringRequest(Request.Method.POST, urlregister, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            r_name.setText("");
                            r_email.setText("");
                            r_password.setText("");
                            r_mobileno.setText("");
                            Toast.makeText(Login_activity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(Login_activity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();

                            params.put("name",str_name);
                            params.put("mobileno",str_mobileno);
                            params.put("email",str_email);
                            params.put("password",str_password);
                            *//*
                            return params;

                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(Login_activity.this);
                    requestQueue.add(request);
                }
            }
        });*/
    }
}