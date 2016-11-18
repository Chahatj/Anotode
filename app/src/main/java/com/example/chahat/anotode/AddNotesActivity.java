package com.example.chahat.anotode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNotesActivity extends AppCompatActivity {

    EditText title,notes,tag1,tag2,tag3,tag4,etcategory;
    Spinner spinner;
    NotesDataHandler notesDataHandler;
    String id,category,tit,not,ta,ta2,ta3,ta4;
    List<String> categories;

    ImageView icontitle,icontag,iconcategory,iconnote;
    Snackbar snackbar;

    SharedPreferences sharedPreferences;

    String SERVER_ADDRESS = "http://anotode.herokuapp.com/api/highlights?token=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Add Note");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        notesDataHandler = new NotesDataHandler(this,null,null,1);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("byOutside",false);
        editor.apply();

        id= getIntent().getExtras().getString("_id");

        title = (EditText) findViewById(R.id.input_title);
        notes = (EditText) findViewById(R.id.input_notes);
        tag1 = (EditText) findViewById(R.id.input_tag1);
        tag2 = (EditText) findViewById(R.id.input_tag2);
        tag3 = (EditText) findViewById(R.id.input_tag3);
        tag4 = (EditText) findViewById(R.id.input_tag4);
        spinner = (Spinner) findViewById(R.id.spinner);
        etcategory = (EditText) findViewById(R.id.et_category);

        icontitle = (ImageView) findViewById(R.id.icon_title);
        icontag = (ImageView) findViewById(R.id.icon_tag);
        iconcategory = (ImageView) findViewById(R.id.icon_category);
        iconnote = (ImageView) findViewById(R.id.icon_note);

        icontitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.requestFocus();
            }
        });

        icontag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag1.requestFocus();
            }
        });

        iconcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });

        iconnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notes.requestFocus();
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
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

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

        if (sharedPreferences.getBoolean("hasLoggedin",false)) {
            Intent intent = getIntent();
            String action = intent.getAction();
            String type = intent.getType();

            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if ("text/plain".equals(type)) {
                    handleSendText(intent);
                    SharedPreferences.Editor editor1 = sharedPreferences.edit();
                    editor1.putBoolean("byOutside",true);
                    editor1.apply();
                    // Handle text being sent
                }

            }
        }
        else {

            finish();
            Toast.makeText(this,"Login to Anotode",Toast.LENGTH_SHORT).show();
        }


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

    public void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            notes.setText(sharedText);
        }
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
                if (etcategory.getText().toString().isEmpty())
                {
                    category = "nocategory";
                }
                else{
                    category = etcategory.getText().toString();
                }

            }

            if (notes.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Fill Notes",Toast.LENGTH_SHORT).show();}

            else {

                if (isNetworkAvailable(getApplicationContext())) {
                    save_send_Notes();
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


    private void save_send_Notes() {

        if (title.getText().toString().isEmpty())
        {
            tit = "notitle";
        }
        else {
            tit = title.getText().toString();
        }


        not = notes.getText().toString();

       if (tag1.getText().toString().isEmpty())
       {
           ta = "notag1";
       }
        else {
           ta = tag1.getText().toString();
       }
        if (tag2.getText().toString().isEmpty())
        {
            ta2 = "notag2";
        }
        else {
            ta2 = tag2.getText().toString();
        }
        if (tag3.getText().toString().isEmpty())
        {
            ta3 = "notag3";
        }
        else {
            ta3 = tag3.getText().toString();
        }
        if (tag4.getText().toString().isEmpty())
        {
            ta4 = "notag4";
        }
        else {
            ta4 = tag4.getText().toString();
        }





        final Highlight highlight = new Highlight(id,null,"customNote",tit,not,"NoComment",ta,ta2,ta3,ta4,category);

        final SharedPreferences sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);



        ArrayList<String> numbers = new ArrayList<String>();

        if (!(ta.equals("notag1"))) {
            numbers.add(highlight.getTag1());
        }

        if (!(ta2.equals("notag2")))
        {
            numbers.add(highlight.getTag2());
        }

        if (!(ta3.equals("notag3")))
        {
            numbers.add(highlight.getTag3());
        }

        if (!(ta4.equals("notag4")))
        {
            numbers.add(highlight.getTag4());
        }



        JSONObject params = new JSONObject();

        try {

            params.put("url", "customNote");
            if (!(highlight.getTitle().equals("notitle")))
            {
                params.put("title", highlight.getTitle());
            }

            params.put("text", highlight.getNotedtext());

           if (!(highlight.getCategory().equals("nocategory")))
           {
               params.put("category", highlight.getCategory());
           }

            if (!(numbers.isEmpty()))
            {
                JSONArray jsArray = new JSONArray(numbers);
                params.put("tags", jsArray);
            }




            Log.v("param", params + "");
        }
        catch (JSONException eeee)
        {
            Log.d("json","jsonerrr");
        }


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST,SERVER_ADDRESS+token, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Intent intent = new Intent(getBaseContext(),AnotodeStore.class);
                        intent.putExtra("AnotherActivity",1234);
                        startActivity(intent);
                        finish();

                        if (!(sharedPreferences.getBoolean("byOutside",false)))
                        {
                            NotesFragment.getInstance().getActivity().finish();
                        }


                        Toast.makeText(getBaseContext(),"Note is successfully added", Toast.LENGTH_LONG).show();


                        Log.d("res",response+"");

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

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


}
