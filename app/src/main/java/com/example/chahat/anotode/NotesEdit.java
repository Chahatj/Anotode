package com.example.chahat.anotode;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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

public class NotesEdit extends AppCompatActivity {

    String id,time,url,title,notedtext,comment,tag1,tag2,tag3,tag4,category;

    TextView tvtime;

    EditText ettitle,etnotedtext,ettag1,ettag2,ettag3,ettag4,etcategory;
    TextInputLayout inputLayoutnotes;

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
        setContentView(R.layout.activity_notes_edit);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        dh = new NotesDataHandler(getApplicationContext(),null,null,1);

        tvtime = (TextView) findViewById(R.id.tv_time);

        ettitle = (EditText) findViewById(R.id.et_title);
        etnotedtext = (EditText) findViewById(R.id.et_note);
        ettag1 = (EditText) findViewById(R.id.et_tag1);
        ettag2 = (EditText) findViewById(R.id.et_tag2);
        ettag3 = (EditText) findViewById(R.id.et_tag3);
        ettag4 = (EditText) findViewById(R.id.et_tag4);
        spcategory = (Spinner) findViewById(R.id.sp_category);
        etcategory = (EditText) findViewById(R.id.et_category);

        inputLayoutnotes = (TextInputLayout) findViewById(R.id.input_layout_notes);

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

        getSupportActionBar().setTitle("Update Note");

        tvtime.setText(time);

        if (!(title.equals("notitle")))
        {
            ettitle.setText(title);
        }

        etnotedtext.setText(notedtext);

        if (!(tag1.equals("notag1")))
        {
            ettag1.setText(tag1);
        }

        if (!(tag2.equals("notag2")))
        {
            ettag2.setText(tag2);
        }

        if (!(tag3.equals("notag3")))
        {
            ettag3.setText(tag3);
        }

        if (!(tag4.equals("notag4")))
        {
            ettag4.setText(tag4);
        }

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
                if (etcategory.getText().toString().trim().isEmpty())
                {
                    category = "nocategory";
                }
                else {
                    category = etcategory.getText().toString();
                }

            }


            if (etnotedtext.getText().toString().trim().isEmpty())
            {

                inputLayoutnotes.setErrorEnabled(true);
                inputLayoutnotes.setError("enter notes");
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

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void updateHighlight()
    {

        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);

        /*StringRequest stringRequest = new StringRequest(Request.Method.PUT, SERVER_ADDRESS+id+"?token="+token ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        dh.updateNotes(id,ettitle.getText().toString(),etnotedtext.getText().toString(),comment,ettag1.getText().toString(),ettag2.getText().toString(),category);

                        Intent intent = new Intent(getApplicationContext(),note_detail.class);
                        intent.putExtra("updatenotechecking",789);
                        intent.putExtra("stringss",new String[] {ettitle.getText().toString(),etnotedtext.getText().toString(),ettag1.getText().toString(),ettag2.getText().toString(),category,id,time,url,comment});
                        startActivity(intent);
                        finish();

                        note_detail.getInstance().finish();

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
                params.put("text",etnotedtext.getText().toString());
                params.put("tags", ettag.getText().toString());
                params.put("category",category);

                Log.v("param",params+"");

                return params;
            }

        };*/

        title = ettitle.getText().toString();
        tag1 = ettag1.getText().toString();
        tag2 = ettag2.getText().toString();
        tag3 = ettag3.getText().toString();
        tag4  = ettag4.getText().toString();

        ArrayList<String> numbers = new ArrayList<String>();

        if (!(tag1.trim().isEmpty()))
        {
            numbers.add(tag1);
        }
        else {
            tag1 = "notag1";
        }

        if (!(tag2.trim().isEmpty()))
        {
            numbers.add(tag2);
        }
        else {
            tag2 = "notag2";
        }

        if (!(tag3.trim().isEmpty()))
        {
            numbers.add(tag3);
        }
        else {
            tag3 = "notag3";
        }

        if (!(tag4.trim().isEmpty()))
        {
            numbers.add(tag4);
        }
        else {
            tag4 = "notag4";
        }

        JSONObject params = new JSONObject();

        try {

            if (!(title.trim().isEmpty()))
            {
                params.put("title", ettitle.getText().toString());
            }
            else {
                title = "notitle";
            }

            params.put("text",etnotedtext.getText().toString());

            if (!(category.equals("nocategory")))
            {
                params.put("category",category);
            }


            Log.v("param",params+"");

            if (!(numbers.isEmpty()))
            {
                JSONArray jsArray = new JSONArray(numbers);
                params.put("tags", jsArray);
            }
            else {
                tag1 = "notag1";
                tag2 = "notag2";
                tag3 = "notag3";
                tag4 = "notag4";
            }



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

                        Log.v("tag",tag1+" "+tag2+" "+tag3+" "+tag4);

                        dh.updateNotes(id,title,etnotedtext.getText().toString(),comment,tag1,tag2,tag3,tag4,category);

                        Intent intent = new Intent(getApplicationContext(),note_detail.class);
                        intent.putExtra("updatenotechecking",789);
                        intent.putExtra("stringss",new String[] {title,etnotedtext.getText().toString(),tag1,tag2,tag3,tag4,category,id,time,url,comment});
                        startActivity(intent);
                        finish();

                        note_detail.getInstance().finish();

                        Toast.makeText(getBaseContext(),"Updated", Toast.LENGTH_LONG).show();
                        Log.d("response",response+"");

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        snackbar.make(findViewById(android.R.id.content), "Slow Connection", Snackbar.LENGTH_LONG)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Log.d("snackbar", "snackbar clicked");
                                    }
                                }).show();

                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
        requestQueue.add(jsObjRequest);

    }

}
