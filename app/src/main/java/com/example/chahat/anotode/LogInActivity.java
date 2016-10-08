package com.example.chahat.anotode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class LogInActivity extends AppCompatActivity {

    EditText et_email,et_password;
    Button bt_login;
    String email,password;
    String SERVER_ADDRESS = "http://anotode.herokuapp.com/api/login";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        et_email = (EditText) findViewById(R.id.input_email);
        et_password = (EditText) findViewById(R.id.input_password);
        bt_login = (Button) findViewById(R.id.btn_login);



        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = et_email.getText().toString();
                password = et_password.getText().toString();

                User user = new User(email,password);
               loginUser(user);
            }
        });


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
                            editor.apply();

                            Log.d("savetoken",sharedPreferences.getString("token",null));

                            Intent intent = new Intent(getBaseContext(),AnotodeStore.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getBaseContext(),response, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getBaseContext(),error.toString(),Toast.LENGTH_LONG).show();
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
