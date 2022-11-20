package com.project.mymart.ui.reports;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.mymart.MainActivity2;
import com.project.mymart.R;
import com.project.mymart.ui.category.CategoryFragment;
import com.project.mymart.ui.category.addcategory_adapter.Category_adapter;
import com.project.mymart.ui.category.addcategory_adapter.get_setmethod;
import com.project.mymart.ui.reports.adapterandget.adapter_sales;
import com.project.mymart.ui.reports.adapterandget.get_salesdata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.project.mymart.MainActivity2.fab;

public class Sales_ReportFragment extends Fragment {

    EditText start_date, end_date;
    TextView total_amount;
    String url_totalretrive = "https://mymartproject.000webhostapp.com/app/salesreport.php", url_totalamount = "https://mymartproject.000webhostapp.com/app/totalamount.php";
    String file_name_path = "";
    int PERMISSION_ALL = 1;
    ArrayList<get_salesdata> arrayList;
    adapter_sales adapter_sales;
    Button btn_downloadrepoert;
    ListView listview_salesreport;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_salesreport, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        start_date = (EditText) view.findViewById(R.id.start_date);
        end_date = (EditText) view.findViewById(R.id.end_date);
        btn_downloadrepoert = (Button) view.findViewById(R.id.btn_downloadrepoert);
        listview_salesreport = (ListView) view.findViewById(R.id.listview_salesreport);
        total_amount = (TextView) view.findViewById(R.id.total_amount);

        arrayList = new ArrayList<get_salesdata>();

        setHasOptionsMenu(true);

        adapter_sales = new adapter_sales(getContext(), R.layout.sales_report_item_design, arrayList);
        listview_salesreport.setAdapter(adapter_sales);

        fab.setVisibility(View.GONE);

      /*  StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (!hasPermissions(getActivity(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }

        File file = new File(getActivity().getExternalFilesDir(null).getAbsolutePath(), "pdfsdcard_location");
        if (!file.exists()) {
            file.mkdir();
        }*/

        btn_downloadrepoert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (start_date.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please Select Start Date", Toast.LENGTH_LONG).show();
                } else if (end_date.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please Select End Date", Toast.LENGTH_LONG).show();
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Please Wait..");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    StringRequest request = new StringRequest(Request.Method.POST, url_totalretrive, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            arrayList.clear();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                if (success.equals("1")) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String order_date = object.getString("order_date");
                                        String client_name = object.getString("client_name");
                                        String client_contact = object.getString("client_contact");
                                        String grand_total = object.getString("grand_total");

                                        arrayList.add(new get_salesdata(order_date, client_name, client_contact, grand_total));
                                        adapter_sales.notifyDataSetChanged();
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
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("startDate", start_date.getText().toString());
                            params.put("endDate", end_date.getText().toString());
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    requestQueue.add(request);

                    totalgrand_total();
                }
            }
        });

        start_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    FDateSelect(start_date);
                    return true;
                }
                return false;
            }
        });

        end_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    FDateSelect(end_date);
                    return true;
                }
                return false;
            }
        });
    }

    private void totalgrand_total() {
        StringRequest request = new StringRequest(Request.Method.POST, url_totalamount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                total_amount.setText(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("startDate", start_date.getText().toString());
                params.put("endDate", end_date.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void FDateSelect(final EditText date_in) {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                date_in.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new DatePickerDialog(getActivity(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

   /* @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void pdfcreate(String response) {
        Rect bounds = new Rect();
        int pageWidth = 300;
        int pageheight = 470;
        int pathHeight = 2;

        final String fileName = "mypdf";
        file_name_path = "/pdfsdcard_location/" + fileName + ".pdf";
        PdfDocument myPdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint paint2 = new Paint();
        Path path = new Path();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageheight, 1).create();
        PdfDocument.Page documentPage = myPdfDocument.startPage(myPageInfo);
        Canvas canvas = documentPage.getCanvas();
        int x = 10;
        int y = 25;

        paint.getTextBounds(response, 0, response.length(), bounds);
        x = (canvas.getWidth() / 2) - (bounds.width() / 2);
        canvas.drawText(response, x, y, paint);
*//*

        paint.getTextBounds(tv_sub_title.getText().toString(), 0, tv_sub_title.getText().toString().length(), bounds);
        x = (canvas.getWidth() / 2) - (bounds.width() / 2);
        y += paint.descent() - paint.ascent();
        canvas.drawText(tv_sub_title.getText().toString(), x, y, paint);
*//*

        y += paint.descent() - paint.ascent();
        canvas.drawText("", x, y, paint);

//horizontal line
        path.lineTo(pageWidth, pathHeight);
        paint2.setColor(Color.GRAY);
        paint2.setStyle(Paint.Style.STROKE);
        path.moveTo(x, y);

        canvas.drawLine(0, y, pageWidth, y, paint2);

//blank space
        y += paint.descent() - paint.ascent();
        canvas.drawText("", x, y, paint);

        y += paint.descent() - paint.ascent();
        x = 10;
        //canvas.drawText(tv_location.getText().toString(), x, y, paint);

        y += paint.descent() - paint.ascent();
        x = 10;
        //canvas.drawText(tv_city.getText().toString(), x, y, paint);

//blank space
        y += paint.descent() - paint.ascent();
        canvas.drawText("", x, y, paint);

//horizontal line
     *//*   path.lineTo(pageWidth, pathHeight);
        paint2.setColor(Color.GRAY);
        paint2.setStyle(Paint.Style.STROKE);
        path.moveTo(x, y);
        canvas.drawLine(0, y, pageWidth, y, paint2);*//*

//blank space
    *//*    y += paint.descent() - paint.ascent();
        canvas.drawText("", x, y, paint);

        Resources res = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.logo);
        Bitmap b = (Bitmap.createScaledBitmap(bitmap, 100, 50, false));
        canvas.drawBitmap(b, x, y, paint);
        y += 25;
        canvas.drawText(getString(R.string.app_name), 120, y, paint);
*//*

        myPdfDocument.finishPage(documentPage);

        File file = new File(getActivity().getExternalFilesDir(null).getAbsolutePath() + file_name_path);
        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        myPdfDocument.close();
        viewPdfFile();
    }

    private void viewPdfFile() {

        File file = new File(getActivity().getExternalFilesDir(null).getAbsolutePath() + file_name_path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        startActivity(intent);
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
*/
   public void onResume() {
       super.onResume();
       ((MainActivity2) getActivity()).setActionBarTitle(getString(R.string.reports));
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
                ft.detach(Sales_ReportFragment.this).attach(Sales_ReportFragment.this).commit();
                return true;
        }
        return true;
    }
}
