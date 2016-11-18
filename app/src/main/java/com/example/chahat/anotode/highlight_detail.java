package com.example.chahat.anotode;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class highlight_detail extends AppCompatActivity {

    Toolbar toolbar;
    TextView tv_title,tv_url,tv_highlightdetail,tv_commentdetail;
    Button bt_tag1,bt_tag2,bt_tag3,bt_tag4,bt_category;

    String id,time,url,title,notedtext,comment,tag1,tag2,tag3,tag4,category;
    Dialog dialog;

    ImageView categoryicon,tagicon;

    NotesDataHandler dh;
    String[] myStrings=new String[]{null,null,null,null,null,null,null,null,null,null,null};

    String SERVER_ADDRESS = "http://anotode.herokuapp.com/api/highlights/";

   static highlight_detail highlight_details;

    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlight_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        highlight_details = this;

        dh = new NotesDataHandler(getApplicationContext(),null,null,1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        tv_title = (TextView) findViewById(R.id.detail_title);
        tv_url = (TextView) findViewById(R.id.detail_url);
        tv_highlightdetail = (TextView) findViewById(R.id.detail_highlight);
        tv_commentdetail = (TextView) findViewById(R.id.detail_comment);

        bt_tag1 = (Button) findViewById(R.id.tag_button1);
        bt_tag2 = (Button) findViewById(R.id.tag_button2);
        bt_tag3 = (Button) findViewById(R.id.tag_button3);
        bt_tag4 = (Button) findViewById(R.id.tag_button4);
        bt_category = (Button) findViewById(R.id.category_button);

        categoryicon = (ImageView) findViewById(R.id.category_icon);
        tagicon = (ImageView) findViewById(R.id.tag_icon);

        int i = getIntent().getExtras().getInt("updateChecking");

        if (i==786) {

            myStrings = getIntent().getStringArrayExtra("strings");
        }
        else if (i==787)
        {
            String[] updatedString = getIntent().getStringArrayExtra("stringss");

            myStrings[3] = updatedString[0];
            myStrings[5] = updatedString[1];
            myStrings[6] = updatedString[2];
            myStrings[7] = updatedString[3];
            myStrings[8] = updatedString[4];
            myStrings[9] =updatedString[5];
            myStrings[10] = updatedString[6];

            myStrings[0] = updatedString[7];
            myStrings[1] = updatedString[8];
            myStrings[2] = updatedString[9];
            myStrings[4] = updatedString[10];
        }

        id = myStrings[0];
        time = myStrings[1];
        url = myStrings[2];
        notedtext = myStrings[4];

        title = myStrings[3];
        comment = myStrings[5];
        tag1 = myStrings[6];
        tag2 = myStrings[7];
        tag3  = myStrings[8];
        tag4 = myStrings[9];
        category = myStrings[10];


        getSupportActionBar().setTitle(time);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));

        tv_title.setText(title);
        tv_url.setText(url);

        if (tag1.equals("notag1"))
        {
          bt_tag1.setVisibility(View.GONE);
        }
        else
        {
            bt_tag1.setText(tag1);
        }



        if (tag2.equals("notag2"))
        {
            bt_tag2.setVisibility(View.GONE);
        }
        else {
            bt_tag2.setVisibility(View.VISIBLE);
            bt_tag2.setText(tag2);
        }
        if (tag3.equals("notag3"))
        {
            bt_tag3.setVisibility(View.GONE);
        }
        else {
            bt_tag3.setVisibility(View.VISIBLE);
            bt_tag3.setText(tag3);
        }
        if (tag4.equals("notag4"))
        {
            bt_tag4.setVisibility(View.GONE);
        }
        else {
            bt_tag4.setVisibility(View.VISIBLE);
            bt_tag4.setText(tag4);
        }

        if (tag1.equals("notag1")&&tag2.equals("notag2")&&tag3.equals("notag3")&&tag4.equals("notag4"))
        {
            tagicon.setVisibility(View.GONE);
        }


        if (category.equals("nocategory"))
        {
            bt_category.setVisibility(View.GONE);
            categoryicon.setVisibility(View.GONE);
        }
        else{
            bt_category.setText(category);
        }


        tv_highlightdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                readHighlight();
            }
        });

        tv_commentdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                readComment();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog();
            }
        });


    }

    public static highlight_detail getInstance()
    {
        return highlight_details;
    }


    public void readHighlight() {

        dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        // it remove the dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set the laytout in the dialog
        dialog.setContentView(R.layout.dialogreader);
        // set the background partial transparent
       TextView tvhighlight = (TextView) dialog.findViewById(R.id.readtext);
        tvhighlight.setText(notedtext);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.color.black);
        window.setUiOptions(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // it dismiss the dialog when click outside the dialog frame
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();


    }

    public void readComment() {

        dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        // it remove the dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set the laytout in the dialog
        dialog.setContentView(R.layout.dialogreader);
        // set the background partial transparent
       TextView tvhighlight = (TextView) dialog.findViewById(R.id.readtext);
       tvhighlight.setText(comment);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.color.black);
        window.setUiOptions(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // it dismiss the dialog when click outside the dialog frame
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();


    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }

    public void customDialog(){

        dialog = new Dialog(this);
        // it remove the dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set the laytout in the dialog
        dialog.setContentView(R.layout.dialogbox);
        // set the background partial transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        // set the layout at right bottom
        param.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        // it dismiss the dialog when click outside the dialog frame
        dialog.setCanceledOnTouchOutside(true);
        // initialize the item of the dialog box, whose id is demo1
        View demodialog =(View) dialog.findViewById(R.id.cross);
        // it call when click on the item whose id is demo1.
        demodialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // diss miss the dialog
                dialog.dismiss();
            }
        });

        View updatedialog = (View) dialog.findViewById(R.id.demo1);

        updatedialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),HighlightEdit.class);
                intent.putExtra("strings", myStrings);
                startActivity(intent);

                dialog.dismiss();
            }
        });

        View deletedialog = (View) dialog.findViewById(R.id.demo2);

        deletedialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                if (isNetworkAvailable(getApplicationContext())) {
                    openAlertdialog();
                }
                else {
                    snackbar.make(findViewById(android.R.id.content),"No Internet Connection", Snackbar.LENGTH_LONG)
                            .setAction("No Internet Connection", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d("snackbar","snackbar clicked");
                                }
                            }).show();
                }
            }
        });

        View sharedialog = (View) dialog.findViewById(R.id.demo3);

        sharedialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,notedtext);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });


        // it show the dialog box
        dialog.show();

    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    public void openAlertdialog()
    {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this);

// Setting Dialog Title
        alertDialog2.setTitle("Confirm Delete");

// Setting Dialog Message
        alertDialog2.setMessage("Are you sure you want to delete this Highlight?");

// Setting Icon to Dialog
        alertDialog2.setIcon(R.drawable.garbage);

// Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        deleteHighlight();
                        Toast.makeText(getApplicationContext(),"Highlight is Deleted",Toast.LENGTH_SHORT).show();
                    }
                });

// Setting Negative "NO" Btn
        alertDialog2.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });

// Showing Alert Dialog
        alertDialog2.show();
    }

    public void deleteHighlight()
    {
        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, SERVER_ADDRESS+id+"?token="+token ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        dh.deletePreviousofUpdated(id);

                        Toast.makeText(getBaseContext(),"Deleted", Toast.LENGTH_LONG).show();
                        Log.d("response",response);
                        finish();
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

}
