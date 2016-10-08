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

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText et_username,et_email,et_password;
    Button bt_signup;
    String username,email,password;
    String SERVER_ADDRESS = "http://anotode.herokuapp.com/api/users";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_username = (EditText) findViewById(R.id.input_username);
        et_email = (EditText) findViewById(R.id.input_email);
        et_password = (EditText) findViewById(R.id.input_password);
        bt_signup = (Button) findViewById(R.id.btn_signup);


        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = et_username.getText().toString();
                email = et_email.getText().toString();
                password = et_password.getText().toString();

                User user = new User(username,email, password);
                registerUser(user);

            }
        });

    }

    private void registerUser(final User user) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("username",user.username);
                        editor.putString("email",user.email);
                        editor.putString("password",user.password);
                        editor.apply();

                        Intent intent = new Intent(getBaseContext(),LogInActivity.class);
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
                params.put("username", user.username);
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


}
