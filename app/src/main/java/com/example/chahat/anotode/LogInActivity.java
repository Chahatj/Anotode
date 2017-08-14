package com.example.chahat.anotode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.drm.DrmStore;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {

    EditText et_email,et_password;
    Button bt_login;
    String email,password;
    String SERVER_ADDRESS = "http://anotode.herokuapp.com/api/login";
    SharedPreferences sharedPreferences;
    Snackbar snackbar;
    TextView forgot;

    TextInputLayout inputLayoutemail,inputLayoutpassword;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

*/

        et_email = (EditText) findViewById(R.id.input_email);
        et_password = (EditText) findViewById(R.id.input_password);
        bt_login = (Button) findViewById(R.id.btn_login);
        forgot = (TextView) findViewById(R.id.forgot);

        inputLayoutemail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutpassword = (TextInputLayout) findViewById(R.id.input_layout_password);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),ForgotActivity.class);
                startActivity(intent);
            }
        });


        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = et_email.getText().toString();
                password = et_password.getText().toString();

                if (email.trim().isEmpty() || password.trim().isEmpty())
                {
                    if (email.trim().isEmpty())
                    {
                       inputLayoutpassword.setErrorEnabled(false);
                       inputLayoutemail.setErrorEnabled(true);
                        inputLayoutemail.setError("Enter your email");
                    }
                    else
                    {

                            inputLayoutemail.setErrorEnabled(false);
                            inputLayoutpassword.setErrorEnabled(true);
                            inputLayoutpassword.setError("Enter your password");
                    }

                }
                else if (password.length()<6)
                {
                    inputLayoutemail.setErrorEnabled(false);
                    inputLayoutpassword.setErrorEnabled(true);
                    inputLayoutpassword.setError("Min length is 6");
                }

                else {

                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                        User user = new User(email, password);

                        if (isNetworkAvailable(getApplicationContext())) {

                            Log.d("connect", "connected");
                            loginUser(user);

                        } else {
                            try {
                                //To hide keyBoard

                                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                            Log.d("disconnect", "disconnected");


                            snackbar.make(findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_LONG)
                                    .setAction("No Internet Connection", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Log.d("snackbar", "snackbar clicked");
                                        }
                                    }).show();

                        }
                    }
                    else{

                        inputLayoutemail.setErrorEnabled(true);
                        inputLayoutemail.setError("Email is invalid");
                        inputLayoutpassword.setErrorEnabled(false);
                    }


                }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }



    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void loginUser(final User user) {



        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String token = jsonObject.getString("token");

                            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token",token);
                            editor.putString("email",email);
                            editor.putString("password",password);
                            editor.putBoolean("hasLoggedin",true);
                            editor.putBoolean("picture_load",false);
                            editor.apply();

                            Log.d("savetoken",sharedPreferences.getString("token",null));

                            Toast.makeText(getApplicationContext(),"Successfully logged in",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getBaseContext(),AnotodeStore.class);
                            intent.putExtra("AnotherActivity",123);
                            startActivity(intent);
                            finish();

                          //  Toast.makeText(getBaseContext(),response, Toast.LENGTH_LONG).show();
                            Log.d("response",response);

                        }catch (JSONException e)
                        {
                            Log.e("JSONerror",e+"");
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        NetworkResponse response = error.networkResponse;

                        String news = new String(response.data);



                        Log.v("dkd",news);

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            snackbar.make(findViewById(android.R.id.content), "Slow Internet", Snackbar.LENGTH_LONG)
                                    .setAction("Retry", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            bt_login.performClick();
                                        }
                                    }).show();
                        } else if (error instanceof AuthFailureError) {

                            inputLayoutemail.setErrorEnabled(true);
                            inputLayoutemail.setError("Recheck your email");

                            inputLayoutpassword.setErrorEnabled(true);
                            inputLayoutpassword.setError("Recheck your password");
                        }
                        else {
                            inputLayoutemail.setErrorEnabled(true);
                            inputLayoutemail.setError("Recheck your email");

                            inputLayoutpassword.setErrorEnabled(true);
                            inputLayoutpassword.setError("Recheck your password");
                        }

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", user.email);
                params.put("password", user.password);

                Log.v("param",params+"");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
