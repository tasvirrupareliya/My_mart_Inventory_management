package com.project.mymart.ui.logout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.mymart.R;

import static android.content.Context.MODE_PRIVATE;

public class LogoutFragment extends Fragment {

    private LogoutViewModel settingViewModel;
    SharedPreferences sharedPreference;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingViewModel =
                new ViewModelProvider(this).get(LogoutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_setting, container, false);

        sharedPreference = getActivity().getSharedPreferences("Userdata", MODE_PRIVATE);
        String username = sharedPreference.getString("username", null);
        String password = sharedPreference.getString("password", null);

        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.clear();
        editor.commit();
        getActivity().finish();
        Toast.makeText(getContext(), "Logout Successfully", Toast.LENGTH_SHORT).show();
        return root;
    }

  /*  @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                View view1 = getLayoutInflater().inflate(R.layout.custom_dialog, null);

                final EditText change_cpass = (EditText) view1.findViewById(R.id.change_cpass);
                final EditText change_newpass = (EditText) view1.findViewById(R.id.change_newpass);
                final EditText change_renewpass = (EditText) view1.findViewById(R.id.change_renewpass);
                final Button btn_changepass = (Button) view1.findViewById(R.id.btn_changepass);
                final Button btn_cancel = (Button) view1.findViewById(R.id.btn_cancel);

                alertDialog.setView(view1);

                final AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.setCanceledOnTouchOutside(false);

                ccpass = change_cpass.toString().trim();
                cnpass = change_newpass.toString().trim();
                crnpass = change_renewpass.toString().trim();

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog1.dismiss();
                    }
                });

                btn_changepass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (ccpass.equals("")) {
                            Toast.makeText(getContext(), "Enter Current Password", Toast.LENGTH_LONG).show();
                        } else if (cnpass.equals("")) {
                            Toast.makeText(getContext(), "Enter New Password", Toast.LENGTH_LONG).show();
                        } else if (crnpass.equals("")) {
                            Toast.makeText(getContext(), "Re-Enter New Password", Toast.LENGTH_LONG).show();
                        } else {
                            if (cnpass.equals(crnpass)) {
                                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("Please Wait..");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                StringRequest request = new StringRequest(Request.Method.POST, urlchangepass, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressDialog.dismiss();

                                        if (response.equalsIgnoreCase("Password Updated")) {

                                            change_cpass.setText("");
                                            change_newpass.setText("");
                                            change_renewpass.setText("");
                                            alertDialog1.dismiss();

                                            Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                                    }
                                }

                                ) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("oldpass", ccpass);
                                        params.put("newpass", cnpass);
                                        params.put("newcpass", crnpass);
                                        return params;

                                    }
                                };

                                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                                requestQueue.add(request);
                            } else {
                                Toast.makeText(getContext(), "Please Verify Your New Password & Re-type Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }*/
}