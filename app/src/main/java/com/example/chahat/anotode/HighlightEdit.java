package com.example.chahat.anotode;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HighlightEdit extends AppCompatActivity {

   String id,time,url,title,notedtext,comment,tag1,tag2,tag3,tag4,category;

    TextView tvurl,tvnotedText,tvtime;
    EditText ettitle,etcomment,ettag1,ettag2,ettag3,ettag4,etcategory;
    Spinner spcategory;



    Toolbar toolbar;
    Dialog dialog;

    String SERVER_ADDRESS = "http://anotode.herokuapp.com/api/highlights/";

    NotesDataHandler dh;
    Snackbar snackbar;

    List<String> categories;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlight_edit);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        dh = new NotesDataHandler(getApplicationContext(),null,null,1);


        tvurl = (TextView) findViewById(R.id.tv_url);
        tvnotedText = (TextView) findViewById(R.id.tv_detailHighlight);
        tvtime = (TextView) findViewById(R.id.tv_time);

        ettitle = (EditText) findViewById(R.id.et_title);
        etcomment = (EditText) findViewById(R.id.et_comment);
        ettag1 = (EditText) findViewById(R.id.et_tag1);
        ettag2 = (EditText) findViewById(R.id.et_tag2);
        spcategory = (Spinner) findViewById(R.id.sp_category);
        etcategory = (EditText) findViewById(R.id.et_category);

        String[] myStrings = getIntent().getStringArrayExtra("strings");

        id = myStrings[0];
        time = myStrings[1];
        url = myStrings[2];
        title = myStrings[3];
        notedtext = myStrings[4];
        comment = myStrings[5];
        tag1 = myStrings[6];
        tag2 = myStrings[7];
        tag3 = myStrings[8];
        tag4 = myStrings[9];
        category = myStrings[10];

        getSupportActionBar().setTitle("Update Highlight");

        tvurl.setText(url);
        tvtime.setText(time);

        ettitle.setText(title);
        etcomment.setText(comment);
        ettag1.setText(tag1);
        ettag2.setText(tag2);
        ettag3.setText(tag3);
        ettag4.setText(tag4);


        tvnotedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ReadDialog();
            }
        });

         categories = new ArrayList<String>();
        categories.add("Others");

        getCategory();


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spcategory.setAdapter(dataAdapter);

        spcategory.setSelection(dataAdapter.getPosition(category));

        spcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                category = parent.getItemAtPosition(position).toString();

                if (category.equals("Others"))
                {
                    etcategory.setVisibility(View.VISIBLE);
                }
                else {
                    etcategory.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    public void getCategory()
    {
        sharedPreferences = getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);

        String add = "http://anotode.herokuapp.com/api/highlights/categories?token=";

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(add+token,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("responsenotes",response+"");

                        try
                        {


                            for (int i = 0;i<response.length();i++)
                            {
                                categories.add(response.getString(i));
                            }

                        }
                        catch (JSONException e)
                        {
                            Log.d("jsonexception",e.toString());
                        }





                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
           /* @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("url","customNote");

                Log.v("param",params+"");

                return params;
            }*/

        };



        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==R.id.action_ok)
        {
            if (category.equals("Others"))
            {
                category = etcategory.getText().toString();
            }

            if (ettitle.getText().toString().isEmpty() || etcomment.getText().toString().isEmpty() || ettag1.getText().toString().isEmpty()||category.isEmpty())
            {
                if (ettag1.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Tag1 is required",Toast.LENGTH_SHORT).show();
                }else{

                Toast.makeText(getApplicationContext(),"Fill empty fields",Toast.LENGTH_SHORT).show();}
            }
            else {

                if (isNetworkAvailable(getApplicationContext())) {
                    updateHighlight();
                } else {
                    snackbar.make(findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_LONG)
                            .setAction("No Internet Connection", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d("snackbar", "snackbar clicked");
                                }
                            }).show();
                }
            }
        }

        return true;
    }



    public void updateHighlight()
    {

            SharedPreferences sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);

       /* StringRequest stringRequest = new StringRequest(Request.Method.PUT, SERVER_ADDRESS+id+"?token="+token ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        dh.updateNotes(id,ettitle.getText().toString(),tvnotedText.getText().toString(),etcomment.getText().toString(),ettag.getText().toString(),category);

                        Intent intent = new Intent(getApplicationContext(),highlight_detail.class);
                        intent.putExtra("updateChecking",787);
                        intent.putExtra("stringss",new String[] {ettitle.getText().toString(),etcomment.getText().toString(),ettag.getText().toString(),category,id,time,url,notedtext});
                        startActivity(intent);
                        finish();

                        highlight_detail.getInstance().finish();

                        Toast.makeText(getBaseContext(),"Updated", Toast.LENGTH_LONG).show();
                        Log.d("response",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(),"Slow network connection",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("title", ettitle.getText().toString());
                params.put("comment",etcomment.getText().toString());
                params.put("tags", ettag.getText().toString());
                params.put("category",category);

                Log.v("param",params+"");

                return params;
            }

        };*/

        ArrayList<String> numbers = new ArrayList<String>();

        numbers.add(ettag1.getText().toString());
        numbers.add(ettag2.getText().toString());
        numbers.add(ettag3.getText().toString());
        numbers.add(ettag4.getText().toString());

        JSONObject params = new JSONObject();

        try {

            params.put("title", ettitle.getText().toString());
            params.put("comment",etcomment.getText().toString());
            params.put("category",category);

            Log.v("param",params+"");

            JSONArray jsArray = new JSONArray(numbers);
            params.put("tags", jsArray);

            Log.v("param", params + "");
        }
        catch (JSONException eeee)
        {
            Log.d("json","jsonerrr");
        }


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.PUT,SERVER_ADDRESS+id+"?token="+token , params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        dh.updateNotes(id,ettitle.getText().toString(),tvnotedText.getText().toString(),etcomment.getText().toString(),ettag1.getText().toString(),ettag2.getText().toString(),ettag3.getText().toString(),ettag4.getText().toString(),category);

                        Intent intent = new Intent(getApplicationContext(),highlight_detail.class);
                        intent.putExtra("updateChecking",787);
                        intent.putExtra("stringss",new String[] {ettitle.getText().toString(),etcomment.getText().toString(),ettag1.getText().toString(),ettag2.getText().toString(),ettag3.getText().toString(),ettag4.getText().toString(),category,id,time,url,notedtext});
                        startActivity(intent);
                        finish();

                        highlight_detail.getInstance().finish();

                        Toast.makeText(getBaseContext(),"Updated", Toast.LENGTH_LONG).show();
                        Log.d("response",response+"");

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        Toast.makeText(getBaseContext(),"Slow network connection",Toast.LENGTH_LONG).show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
        requestQueue.add(jsObjRequest);

    }

    public void ReadDialog() {

        dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        // it remove the dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set the laytout in the dialog
        dialog.setContentView(R.layout.dialogreader);

        TextView tvHighlight = (TextView) dialog.findViewById(R.id.readtext);
        tvHighlight.setText(notedtext);
        // set the background partial transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.color.black);
        window.setUiOptions(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // it dismiss the dialog when click outside the dialog frame
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();


    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
