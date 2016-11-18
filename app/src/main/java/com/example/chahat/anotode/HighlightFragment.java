package com.example.chahat.anotode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import java.util.zip.Inflater;

/**
 * Created by chahat on 6/10/16.
 */
public class HighlightFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{
    String token;
    String SERVER_ADDRESS = "http://anotode.herokuapp.com/api/highlights?token=";

    SharedPreferences sharedPreferences;
    HighlightRecyclerAdapter highlightRecyclerAdapter;
    List<Highlight> highlightList;
    List<String> taglist;
    RecyclerView recyclerView;
            NotesDataHandler dh;

    private OnFragmentInteractionListener mListener;

    Snackbar snackbar;
    SwipeRefreshLayout swipeRefreshLayout;

    TextView emptyView;
    String tag,tag2,tag3,tag4;

    static HighlightFragment highlightFragment;




    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ((AnotodeStore)getActivity()).getSupportActionBar().setTitle("Highlights");

                View v = inflater.inflate(R.layout.highlight_fragment,container,false);


        highlightFragment = this;

        taglist = new ArrayList<>();


        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.dot_dark_screen2,R.color.colorPrimary);

        emptyView = (TextView) v.findViewById(R.id.empty_view);

        checkNetwork();

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(true);

                fetchHighlights();


            }
        });



        highlightList= new ArrayList<>();
        highlightRecyclerAdapter = new HighlightRecyclerAdapter(highlightList);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        sharedPreferences = getActivity().getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("inNotesfragment",false);
        editor.apply();

        token = sharedPreferences.getString("token",null);

        Log.d("tok",token);

        dh=new NotesDataHandler(getActivity(),null,null,1);

        fetchHighlights();

        highlightList = dh.getHighlights();
        highlightRecyclerAdapter = new HighlightRecyclerAdapter(highlightList);
        recyclerView.setAdapter(highlightRecyclerAdapter);


        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
              /* Highlight highlight  = highlightList.get(position);

                Intent intent = new Intent(getActivity(),highlight_detail.class);
                String[] myStrings = new String[] {highlight.getId(),highlight.getTime(), highlight.getUrl(),highlight.getTitle(),highlight.getNotedtext(),highlight.getComment(),highlight.getTag(),highlight.getCategory()};
                intent.putExtra("strings", myStrings);
                intent.putExtra("updateChecking",786);
                startActivity(intent);*/
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));




        return v;
    }

    public static HighlightFragment getInstance()
    {
        return highlightFragment;
    }

    public  void fetchHighlights()
    {

        swipeRefreshLayout.setRefreshing(true);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(SERVER_ADDRESS+token ,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try
                        {
                            for (int i = 0;i<response.length();i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                if (!(jsonObject.getString("url").equals("customNote")))
                                {

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

                                    String notedtext = jsonObject.getString("text");

                                    if (jsonObject.has("comment"))
                                    {
                                        comment = jsonObject.getString("comment");
                                    }
                                    else {
                                        comment = "nocomment";
                                    }

                                    if (jsonObject.has("category"))
                                    {
                                        category = jsonObject.getString("category");
                                    }
                                    else {
                                        category= "nocategory";
                                    }


                                   // String tags = jsonObject.getString("tags");

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
                                    Log.d("in4", tag1 + tag2 + tag3 + tag4);

                                    Highlight highlight = new Highlight(id, time, url, title, notedtext, comment, tag1,tag2,tag3,tag4, category);

                                    Log.d("i", dh.checkHighlightsExist(highlight) + "");

                                    if (dh.checkHighlightsExist(highlight) == 0) {
                                        dh.insertHighlights(highlight);
                                        Log.d("inserted", "whhoo insert");
                                    } else if (i == 1) {
                                        Log.d("not inserted", "whhoo not insert");
                                    }
                                    //notesList.add(highlight);
                                }

                            }

                            // Log.d("highlightlist",notesList.get(0).getTime());

                            highlightList = dh.getHighlights();
                            highlightRecyclerAdapter = new HighlightRecyclerAdapter(highlightList);
                            recyclerView.setAdapter(highlightRecyclerAdapter);

                            if (highlightRecyclerAdapter.getItemCount()==0)
                            {
                                recyclerView.setVisibility(View.GONE);
                                emptyView.setVisibility(View.VISIBLE);
                            }
                            else {
                                recyclerView.setVisibility(View.VISIBLE);
                                emptyView.setVisibility(View.GONE);
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
                    }
                });/*{
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("token",token);

                Log.v("param",params+"");

                return params;
            }

        }*/



        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.start();
        requestQueue.add(jsonArrayRequest);

        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();

        fetchHighlights();


    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public boolean checkNetwork()
    {
        if (isNetworkAvailable(getActivity()))
        {

            Log.d("connect","connected");
            return true;

        } else
        {
            Log.d("disconnect","disconnected");



            snackbar.make(getActivity().findViewById(android.R.id.content),"No Internet Connection", Snackbar.LENGTH_LONG)
                    .setAction("No Internet Connection", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("snackbar","snackbar clicked");
                        }
                    })
                    .show();

            swipeRefreshLayout.setRefreshing(false);

            return false;

        }
    }

    @Override
    public void onRefresh() {

      if (checkNetwork())
      {
          fetchHighlights();
      }


    }
}
