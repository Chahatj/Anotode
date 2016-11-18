package com.example.chahat.anotode;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    NotesDataHandler dh;

    List<Highlight> highlightList,notesList;
    List<String> taglist;

    RecyclerView recyclerView;

    NotesRecyclerAdapter notesRecyclerAdapter;
    HighlightRecyclerAdapter highlightRecyclerAdapter;

    TextView tv_noResult;

    SharedPreferences sharedPreferences;

    String tag,tag2,tag3,tag4;

    String SERVER_ADDRESS = "http://anotode.herokuapp.com/api/highlights?token=";

    Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        dh=new NotesDataHandler(this,null,null,1);

        tv_noResult = (TextView) findViewById(R.id.tv_noResult);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        highlightList = new ArrayList<>();
        notesList = new ArrayList<>();
        taglist = new ArrayList<>();

        if (sharedPreferences.getBoolean("inNotesfragment",false))
        {
            notesRecyclerAdapter = new NotesRecyclerAdapter(notesList);
        }
        else
        {
            highlightRecyclerAdapter = new HighlightRecyclerAdapter(highlightList);
        }


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        handleIntent(getIntent());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
               /* Highlight highlight  = list.get(position);
                Intent intent = new Intent(getApplicationContext(),note_detail.class);
                String[] myStrings = new String[] {highlight.getId(),highlight.getTime(), highlight.getUrl(),highlight.getTitle(),highlight.getNotedtext(),highlight.getComment(),highlight.getTag1(),highlight.getTag2(),highlight.getCategory()};
                intent.putExtra("strings", myStrings);
                intent.putExtra("updatenotechecking",788);
                startActivity(intent);
*/
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
            if (isNetworkAvailable(getApplicationContext()))
            {
                search(query);
            }
            else
            {
                finish();

               Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_SHORT).show();


            }


            /*if (sharedPreferences.getBoolean("inNotesfragment",false))
            {
                list = dh.searchDatabase(query,2);
            }
            else {
                list = dh.searchDatabase(query,1);
            }


            if (list.isEmpty())
            {
                tv_noResult.setVisibility(View.VISIBLE);
            }
            else {
                notesRecyclerAdapter = new NotesRecyclerAdapter(list);
                recyclerView.setAdapter(notesRecyclerAdapter);

            }*/





        }
    }

    public void search(String query)
    {
        sharedPreferences = getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(SERVER_ADDRESS+token+"&contains="+query,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("responsenotes",response+"");

                        try
                        {
                            for (int i = 0;i<response.length();i++) {

                                taglist = new ArrayList<>();

                                JSONObject jsonObject = response.getJSONObject(i);
                                // String userid = jsonObject.getString("user_id");

                                    String id = jsonObject.getString("_id");
                                    String time = jsonObject.getString("time");
                                    String url = jsonObject.getString("url");

                                String title,comment,category,tag1;

                                if(jsonObject.has("title")){
                                    title = jsonObject.getString("title");
                                }
                                else {
                                    title = "notitle";
                                }

                                if (jsonObject.has("comment"))
                                {
                                    comment = jsonObject.getString("comment");
                                }
                                else {
                                    comment = "nocomment";
                                }

                                    String notedtext = jsonObject.getString("text");

                                if (jsonObject.has("category"))
                                {
                                    category = jsonObject.getString("category");
                                }
                                else {
                                    category= "nocategory";
                                }


                                if (jsonObject.has("tags")&& jsonObject.getJSONArray("tags").length()!=0) {

                                    JSONArray tagsArray = jsonObject.getJSONArray("tags");

                                    for (int j = 0; j < tagsArray.length(); j++) {
                                        tag = tagsArray.getString(j);

                                        taglist.add(tag);

                                        Log.d("tag", tag);
                                    }

                                    tag1 = taglist.get(0);


                                    if (taglist.size() == 2) {

                                        tag2 = taglist.get(1);
                                        tag3 = "notag3";
                                        tag4 = "notag4";

                                    } else if (taglist.size() == 3) {
                                        tag2 = taglist.get(1);
                                        tag3 = taglist.get(2);
                                        tag4 = "notag4";

                                    } else if (taglist.size() == 4) {
                                        tag2 = taglist.get(1);
                                        tag3 = taglist.get(2);
                                        tag4 = taglist.get(3);
                                    } else {
                                        tag2 = "notag2";
                                        tag3 = "notag3";
                                        tag4 = "notag4";
                                    }

                                    taglist.clear();

                                }
                                else {
                                    tag1 = "notag1";
                                    tag2 = "notag2";
                                    tag3 = "notag3";
                                    tag4 = "notag4";
                                }

                                    Log.d("time", time);

                                    Highlight highlight = new Highlight(id, time,url, title,notedtext,comment,tag1,tag2,tag3,tag4, category);

                                    if (url.equals("customNote"))
                                    {
                                        notesList.add(highlight);
                                    }
                                else {
                                        highlightList.add(highlight);
                                    }


                            }


                            if (sharedPreferences.getBoolean("inNotesfragment",false))
                            {
                                if (notesList.isEmpty())
                                {
                                    tv_noResult.setVisibility(View.VISIBLE);
                                }
                                else {
                                    notesRecyclerAdapter = new NotesRecyclerAdapter(notesList);
                                    recyclerView.setAdapter(notesRecyclerAdapter);
                                }
                            }
                            else
                            {
                                if (highlightList.isEmpty())
                                {
                                    tv_noResult.setVisibility(View.VISIBLE);
                                }
                                else {
                                    highlightRecyclerAdapter = new HighlightRecyclerAdapter(highlightList);
                                    recyclerView.setAdapter(highlightRecyclerAdapter);
                                }


                            }





                        }
                        catch (JSONException e)
                        {
                            Log.d("jsonexception",e.toString());
                        }





                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();

                        snackbar.make(findViewById(android.R.id.content), "Slow Connection", Snackbar.LENGTH_LONG)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Log.d("snackbar", "snackbar clicked");
                                    }
                                }).show();
                    }
                }){
           /* @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("url","customNote");

                Log.v("param",params+"");

                return params;
            }*/

        };



        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
        requestQueue.add(jsonArrayRequest);

    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
