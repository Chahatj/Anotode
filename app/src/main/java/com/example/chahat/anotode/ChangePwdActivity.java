package com.example.chahat.anotode;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePwdActivity extends AppCompatActivity {

    Snackbar snackbar;

    EditText et_oldpwd,et_newpwd;
    TextInputLayout inputLayoutpasswordold,inputLayoutpasswordnew;

    SharedPreferences sharedPreferences;

    String SERVER_ADDRESS = "http://anotode.herokuapp.com/api/users/user?token=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Change password");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_oldpwd = (EditText) findViewById(R.id.et_oldpwd);
        et_newpwd = (EditText) findViewById(R.id.et_newpwd);
        inputLayoutpasswordold = (TextInputLayout) findViewById(R.id.input_layout_passwordold);
        inputLayoutpasswordnew = (TextInputLayout) findViewById(R.id.input_layout_passwordnew);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==R.id.action_ok) {

            if (et_oldpwd.getText().toString().isEmpty() || et_newpwd.getText().toString().isEmpty()) {

                if (et_oldpwd.getText().toString().isEmpty())
                {
                    inputLayoutpasswordold.setErrorEnabled(true);
                    inputLayoutpasswordold.setError("enter old password");
                    inputLayoutpasswordnew.setErrorEnabled(false);
                }
                else {
                    inputLayoutpasswordold.setErrorEnabled(false);
                    inputLayoutpasswordnew.setErrorEnabled(true);
                    inputLayoutpasswordnew.setError("enter new password");
                }

            } else {

                if (et_oldpwd.getText().toString().equals(sharedPreferences.getString("password", null))) {
                    if (isNetworkAvailable(getApplicationContext())) {
                        updatePassword();
                    } else {
                        snackbar.make(findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_LONG)
                                .setAction("No Internet Connection", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Log.d("snackbar", "snackbar clicked");
                                    }
                                }).show();
                    }
                } else {
                    inputLayoutpasswordold.setErrorEnabled(true);
                    inputLayoutpasswordold.setError("enter your previous password");
                }
            }
        }

        return true;
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void updatePassword()
    {
        String token = sharedPreferences.getString("token",null);

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, SERVER_ADDRESS+token ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("response",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("password",et_newpwd.getText().toString());
                            editor.apply();

                            Toast.makeText(getApplicationContext(),"Password changed",Toast.LENGTH_SHORT).show();

                            finish();


                        }catch (JSONException e)
                        {
                            Log.d("JSONException",e+"");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        snackbar.make(findViewById(android.R.id.content), "Slow Connection", Snackbar.LENGTH_LONG)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Log.d("snackbar", "snackbar clicked");
                                    }
                                }).show();
                    }
                }){


            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("password", et_newpwd.getText().toString());


                Log.v("param",params+"");

                return params;
            }


        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
        requestQueue.add(stringRequest);

    }
}
