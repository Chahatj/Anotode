package com.example.chahat.anotode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener {

    GoogleSignInOptions gso;

    //google api client
    GoogleApiClient mGoogleApiClient;
    SignInButton signInButton;


    EditText et_username,et_email,et_password;
    Button bt_signup;
    String username,email,password,picture,picture_url;

    String SERVER_ADDRESS = "http://anotode.herokuapp.com/api/users";

    SharedPreferences sharedPreferences;
    de.hdodenhof.circleimageview.CircleImageView imageButton;

    Bitmap bitmap;
    String imageName,image;
    File f;

    ImageButton mNavButtonView;

    Snackbar snackbar;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

      /*  Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Signup");*/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("1036953527322-jpr9phlk6sd97jt6fdn00kd31ohhthi1.apps.googleusercontent.com") //by the web credential
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        signInButton = (SignInButton) findViewById(R.id.google_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent,2);
            }
        });



        et_username = (EditText) findViewById(R.id.input_username);
        et_email = (EditText) findViewById(R.id.input_email);
        et_password = (EditText) findViewById(R.id.input_password);
        bt_signup = (Button) findViewById(R.id.btn_signup);
        imageButton = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.imageButton);
        progressBar = (ProgressBar) findViewById(R.id.loading_spinner);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable(getApplicationContext()))
                {
                    Intent   intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 1);
                }
                else {
                    snackbar.make(findViewById(android.R.id.content), "Image can't upload", Snackbar.LENGTH_LONG)
                            .setAction("No Internet Connection", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d("snackbar", "snackbar clicked");
                                }
                            }).show();
                }




            }
        });

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = et_username.getText().toString();
                email = et_email.getText().toString();
                password = et_password.getText().toString();


    if (username.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty() || f==null)
    {
        Toast.makeText(getApplicationContext(),"Fill empty fields",Toast.LENGTH_SHORT).show();
    }
    else
    {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (isNetworkAvailable(getApplicationContext())) {

                User user = new User(username, email, password, picture_url);
                registerUser(user);
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
        else {
            Toast.makeText(getApplicationContext(),"Not a valid email",Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText("");

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();

            Picasso.with(this).load(selectedImage).noPlaceholder().centerCrop().fit()
                    .into((ImageView) findViewById(R.id.imageButton));

         /* try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            }
            catch (IOException e) {
                e.printStackTrace();
            }*/
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            Log.d("picturepath",picturePath);
            cursor.close();

               // imageButton.setImageBitmap(BitmapFactory.decodeFile(picturePath));
          /*  Bitmap bitmap = decodeFile(new File(picturePath));
            imageButton.setImageBitmap(bitmap);*/


            f = new File(picturePath);

            imageName = f.getName();
            Log.d("imageName",imageName);


            if (f!=null){


                    progressBar.setVisibility(View.VISIBLE);
                    imagesend();


                }



        }

        else if (requestCode ==2) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
      //  Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            picture_url = acct.getPhotoUrl().toString();

            Log.d("idtoken", acct.getIdToken());

            User user = new User(acct.getDisplayName(), acct.getEmail(), acct.getIdToken(),null);
           registerUser(user);



        } else {
            // Signed out, show unauthenticated UI.
           Log.d("error","errorrr");
        }
    }






   /* public Bitmap decodeFile(File f)
    {
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_SIZE=70;

            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }*/

    private void registerUser(final User user) {




        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {




                        Intent intent = new Intent(getBaseContext(),LogInActivity.class);

                        startActivity(intent);
                        finish();

                        Toast.makeText(getBaseContext(),"Successfully register", Toast.LENGTH_LONG).show();
                        Log.d("response",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        NetworkResponse response = error.networkResponse;

                        Log.v("res",response+"");

                        if (response!=null)
                        {
                            if (response.statusCode==400)
                            {
                                Toast.makeText(getBaseContext(),"Email already used",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getBaseContext(),"Slow internet connection...wait",Toast.LENGTH_LONG).show();
                            }
                        }




                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username", user.username);
                Log.d("username",user.username);
                params.put("email", user.email);
                params.put("password", user.password);
                params.put("picture_url",picture_url);





                Log.v("param",params+"");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
        requestQueue.add(stringRequest);

    }

    public void imagesend()
    {
        Ion.with(getBaseContext()).load("POST","http://uploads.im/api").uploadProgressHandler(new ProgressCallback()
        {
            @Override
            public void onProgress(long uploaded, long total)
            {
                System.out.println("uploaded " + (int)uploaded + " Total: "+total);
            }
        }).setMultipartParameter("platform", "android").setMultipartFile("image", f).asString().setCallback(new FutureCallback<String>()
        {
            @Override
            public void onCompleted(Exception e, String result)
            {
               // Log.d("imageresult",result);

                try {

                    JSONObject jsonObject = new JSONObject(result);

                    String data = jsonObject.getString("data");

                    JSONObject inerJson = new JSONObject(data);

                    picture_url = inerJson.getString("img_url");

                    Log.d("picture_url",picture_url);

                    progressBar.setVisibility(View.GONE);
                }
                catch (JSONException ee)
                {
                    Log.d("JSONException",ee+"");
                }


            }
        });
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
