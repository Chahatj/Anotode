package com.example.chahat.anotode;


import android.app.DownloadManager;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AnotodeStore extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HighlightFragment.OnFragmentInteractionListener,NotesFragment.OnFragmentInteractionListener {

    String username,email,token,picture_url;
    ImageView nav_image;
    TextView nav_email,nav_username;

    SharedPreferences sharedPreferences;
    String SERVER_ADDRESS = "http://anotode.herokuapp.com/api/users/user?token=";

    NotesDataHandler dh;
    NavigationView navigationView;

    Toolbar toolbar;

    Snackbar snackbar;

    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anotode_store);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        if (sharedPreferences.contains("byOutside"))
        {
            if (!(sharedPreferences.getBoolean("byOutside",false)))
            {
                WelcomeActivity.getInstance().finish();
            }
        }
        else {
            WelcomeActivity.getInstance().finish();
        }

        int i = getIntent().getExtras().getInt("AnotherActivity");

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

       /* if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = HighlightFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.framelayout,fragment).commit();
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

     //   username = sharedPreferences.getString("username",null);
     //   email = sharedPreferences.getString("email",null);

     //   Log.d("username,email",username+email);

        dh = new NotesDataHandler(this,null,null,1);

        View headerView = navigationView.getHeaderView(0);

        nav_email = (TextView)  headerView.findViewById(R.id.nav_email);
        nav_username = (TextView) headerView.findViewById(R.id.nav_username);
        nav_image = (ImageView) headerView.findViewById(R.id.imageView);



        fetchUser();



        nav_email.setText(sharedPreferences.getString("email",null));
        nav_username.setText(sharedPreferences.getString("username",null));

        File f =    new File( Environment.getExternalStorageDirectory()+ "/Anotode Profile/profile.jpg");

        Picasso.with(getApplicationContext()).load(f).skipMemoryCache().into(nav_image);


    /*    View view = navigationView.inflateHeaderView(R.layout.nav_header_anotode_store);
        TextView nav_tv = (TextView) view.findViewById(R.id.nav_username);
        TextView nav_tv1 = (TextView) view.findViewById(R.id.nav_email);
        ImageView im = (ImageView) view.findViewById(R.id.imageView);

        nav_tv.setText(username);
        nav_tv1.setText(email);
        im.setImageResource(R.drawable.ic_discount);

        */

        navigationView.setNavigationItemSelectedListener(this);



        if (i==123) {

            if (sharedPreferences.contains("inNotesfragment"))
            {
                if (sharedPreferences.getBoolean("inNotesfragment",false))
                {
                    navigationView.setCheckedItem(R.id.nav_note);
                    NotesFragment notesFragment = new NotesFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,notesFragment).commit();

                }
                else {
                    navigationView.setCheckedItem(R.id.nav_highlight);
                    HighlightFragment highlightFragment = new HighlightFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, highlightFragment).commit();

                }
            }
            else {
                navigationView.setCheckedItem(R.id.nav_highlight);
                HighlightFragment highlightFragment = new HighlightFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, highlightFragment).commit();

            }



        }
        else if (i==1234)
        {
            navigationView.setCheckedItem(R.id.nav_note);
            NotesFragment notesFragment = new NotesFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,notesFragment).commit();

        }



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.anotode_store, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        ComponentName cn = new ComponentName(this, SearchResultActivity.class);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));

        return true;
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(this,SettingActivity.class);
            startActivity(intent);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_highlight)
        {
            HighlightFragment highlightFragment = new HighlightFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, highlightFragment).commit();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("inNotesfragment",false);
            editor.apply();

        }
        else if (id == R.id.nav_note) {

            NotesFragment notesFragment = new NotesFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,notesFragment).commit();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("inNotesfragment",true);
            editor.apply();

        }
        else if (id==R.id.nav_download)
        {
            download();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void fetchUser()
    {
        String token = sharedPreferences.getString("token",null);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, SERVER_ADDRESS+token ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("response",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            username = jsonObject.getString("username");
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username",username);
                            editor.apply();

                            email = jsonObject.getString("email");

                            if (jsonObject.has("picture_url")) {
                                picture_url = jsonObject.getString("picture_url");
                                SharedPreferences.Editor editorr = sharedPreferences.edit();
                                editorr.putString("picture_url",picture_url);
                                editorr.apply();

                             File f =    new File( Environment.getExternalStorageDirectory()+ "/Anotode Profile/profile.jpg");

                                if (!(sharedPreferences.getBoolean("picture_load",false)&& f.isFile()))
                                {
                                    Log.d("hiiiiiii","hiiiiiiiiiiii");
                                    loadPicture(picture_url);
                                }

                            }

                            nav_username.setText(username);
                            nav_email.setText(email);



                            Log.d("userdetail",username+"  " +email+"  "+picture_url);

                        }catch (JSONException e)
                        {
                            Log.d("JSONException",e+"");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(),"Slow network connection",Toast.LENGTH_LONG).show();
                    }
                }){

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
        requestQueue.add(stringRequest);


    }

    public void loadPicture(String picture_url)
    {
        Picasso.with(this)
                .load(picture_url)
                .into(nav_image);

        Picasso.with(this).load(picture_url)
                .into(target);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("picture_load",true);
        editor.apply();

    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                  /*  File file = new File(Environment.getExternalStorageDirectory().getPath() +"/profile.jpg");*/

                    File filepath = Environment.getExternalStorageDirectory();

                    // Create a new folder in SD Card
                    File dir = new File(filepath.getAbsolutePath()
                            + "/Anotode Profile/");
                    dir.mkdirs();

                    // Create a name for the saved image
                    File file1 = new File(dir, "profile.jpg");

                    try
                    {
                        file1.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file1);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                        ostream.close();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        nav_username.setText(sharedPreferences.getString("username",null));

        File f =    new File( Environment.getExternalStorageDirectory()+ "/Anotode Profile/profile.jpg");

        if (!(f.isFile()))
        {
            loadPicture(sharedPreferences.getString("picture_url",null));
        }
    }


    public void download()
    {
        String token = sharedPreferences.getString("token",null);


        String url = "http://anotode.herokuapp.com/api/highlights/export?token="+token;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
   //     request.setDescription("Some descrition");
      //  request.setTitle("Some title");
// in order for this if to run, you must use the android 3.2 to compile your app

            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "AnotodeData.txt");

// get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }


}
