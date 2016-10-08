package com.example.chahat.anotode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNotesActivity extends AppCompatActivity {

    EditText title,notes,tag;
    Spinner spinner;
    NotesDataHandler notesDataHandler;
    String category,tit,not,ta;

    String SERVER_ADDRESS = "http://anotode.herokuapp.com/api/highlights?token=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        notesDataHandler = new NotesDataHandler(this,null,null,1);

        title = (EditText) findViewById(R.id.input_title);
        notes = (EditText) findViewById(R.id.input_notes);
        tag = (EditText) findViewById(R.id.input_tag);
        spinner = (Spinner) findViewById(R.id.spinner);



                List<String> categories = new ArrayList<String>();
        categories.add("Stay Out Overnight");
        categories.add("Organise an Event");
        categories.add("Leave");
        categories.add("Others");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                category = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                save_send_Notes();

            }
        });

    }

    private void save_send_Notes() {

        tit = title.getText().toString();
        not = notes.getText().toString();
        ta = tag.getText().toString();

        final Highlight highlight = new Highlight(tit,not,ta,category);

        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS+token ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       // notesDataHandler.insertmemo(highlight);

                        Intent intent = new Intent(getBaseContext(),AnotodeStore.class);
                        startActivity(intent);
                        finish();

                        Toast.makeText(getBaseContext(),response, Toast.LENGTH_LONG).show();
                        Log.d("response",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("url","customNote");
                params.put("title", highlight.getTitle());
                params.put("text", highlight.getNotedtext());
                params.put("tags", highlight.getTag());
                params.put("category", highlight.getCategory());

                Log.v("param",params+"");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
        requestQueue.add(stringRequest);

    }

}
