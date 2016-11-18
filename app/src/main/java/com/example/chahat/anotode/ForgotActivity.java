package com.example.chahat.anotode;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotActivity extends AppCompatActivity {

    EditText etemail;
    Snackbar snackbar;

    TextInputLayout inputLayoutemail;

    String SERVER_ADDRESS = "http://anotode.herokuapp.com/api/login/forgot_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Forgot Password");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etemail = (EditText) findViewById(R.id.et_email);
        inputLayoutemail = (TextInputLayout) findViewById(R.id.input_layout_email);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==R.id.action_ok)
        {
            if (etemail.getText().toString().isEmpty())
            {
                inputLayoutemail.setErrorEnabled(true);
                inputLayoutemail.setError("enter email");
            }
            else {
                if (isNetworkAvailable(getApplicationContext())) {

                    resetpassword();
                }
                else {
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

    public void resetpassword()
    {
        JSONObject params = new JSONObject();

        try
        {
            params.put("email",etemail.getText().toString());
        }
        catch (JSONException e)
        {
            Log.v("JSON",e.toString());
        }


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST,SERVER_ADDRESS, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        Toast.makeText(getBaseContext(),"New password is sent to your email", Toast.LENGTH_LONG).show();

                        finish();


                        Log.d("res",response+"");

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            snackbar.make(findViewById(android.R.id.content), "Slow Internet", Snackbar.LENGTH_LONG)
                                    .setAction("Retry", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    }).show();
                        }
                            else{
                                inputLayoutemail.setErrorEnabled(true);
                                inputLayoutemail.setError("enter a valid email");

                            }

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
        requestQueue.add(jsObjRequest);


    }


}
