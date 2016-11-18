package com.example.chahat.anotode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ChangeProfileActivity extends AppCompatActivity {

    de.hdodenhof.circleimageview.CircleImageView imageButton;

    TextView textView;

    File f;

    String picture_url;

    Snackbar snackbar;

    SharedPreferences sharedPreferences;

    String SERVER_ADDRESS = "http://anotode.herokuapp.com/api/users/user?token=";

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setTitle("Change profile");

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        imageButton = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.imageButton);
        progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        textView = (TextView) findViewById(R.id.textView);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable(getApplicationContext()))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 1);
                }
                else{
                    snackbar.make(findViewById(android.R.id.content), "No Internet", Snackbar.LENGTH_LONG)
                            .setAction("Connect", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }).show();
                }





            }
        });
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

            if (f!=null){

                progressBar.setVisibility(View.VISIBLE);

                imagesend();}

        }
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
                Log.d("imageresult",result);

                try {

                    JSONObject jsonObject = new JSONObject(result);

                    String data = jsonObject.getString("data");

                    JSONObject inerJson = new JSONObject(data);

                    picture_url = inerJson.getString("img_url");

                    progressBar.setVisibility(View.GONE);

                    Log.d("picture_url",picture_url);
                }
                catch (JSONException ee)
                {
                    Log.d("JSONException",ee+"");
                }


            }
        });
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

            if (f==null)
            {
             textView.setTextColor(Color.RED);
            }
            else{
                if (isNetworkAvailable(getApplicationContext())) {
                    updateProfile();
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

    public void updateProfile()
    {
        String token = sharedPreferences.getString("token",null);

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, SERVER_ADDRESS+token ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       try {
                           JSONObject jsonObject = new JSONObject(response);
                           picture_url = jsonObject.getString("picture_url");
                       }
                       catch (JSONException ee)
                       {
                           Log.d("jsonException",ee.toString());
                       }

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("picture_url",picture_url);
                        editor.putBoolean("picture_load",false);
                        editor.apply();

                        File filepath = Environment.getExternalStorageDirectory();

                        // Create a new folder in SD Card
                        File dir = new File(filepath.getAbsolutePath()
                                + "/Anotode Profile/");

                        DeleteRecursive(dir);

                        Toast.makeText(getBaseContext(),"Profilepic updated", Toast.LENGTH_LONG).show();
                        Log.d("response",response);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        snackbar.make(findViewById(android.R.id.content), "Slow Connection", Snackbar.LENGTH_LONG)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                }).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("picture_url",picture_url);

                Log.v("param",params+"");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
        requestQueue.add(stringRequest);

    }

    private void DeleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles()) {
                child.delete();
                DeleteRecursive(child);
            }

        fileOrDirectory.delete();
    }

}
