package com.example.chahat.anotode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chahat on 6/10/16.
 */
public class NotesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{

    SharedPreferences sharedPreferences;

    String SERVER_ADDRESS = "http://anotode.herokuapp.com/api/highlights?token=";


    NotesRecyclerAdapter notesRecyclerAdapter;
    List<Highlight> notesList;

    RecyclerView  recyclerView;
    TextView emptyView;

    NotesDataHandler dh;

    Snackbar snackbar;

    SwipeRefreshLayout swipeRefreshLayout;

    String id; String tag2,tag3,tag4;


    List<String> taglist;

    static NotesFragment notesFragment;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ((AnotodeStore)getActivity()).getSupportActionBar().setTitle("Notes");

        notesFragment =  this;

        View v = inflater.inflate(R.layout.notes_fragment,container,false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        emptyView = (TextView) v.findViewById(R.id.empty_view);



        notesList = new ArrayList<>();


        notesRecyclerAdapter=new NotesRecyclerAdapter(notesList);

        sharedPreferences = getActivity().getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("inNotesfragment",true);
        editor.apply();

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.dot_dark_screen2,R.color.colorPrimary);
        checkNetwork();

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(true);

                fetch_CustomNotes();
            }
        });



        dh=new NotesDataHandler(getActivity(),null,null,1);

        fetch_CustomNotes();

        notesList = dh.getNotes();
        notesRecyclerAdapter = new NotesRecyclerAdapter(notesList);
        recyclerView.setAdapter(notesRecyclerAdapter);

        //notesRecyclerAdapter = new NotesRecyclerAdapter(notesList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                /*Highlight highlight  = notesList.get(position);
                Intent intent = new Intent(getActivity(),note_detail.class);
                String[] myStrings = new String[] {highlight.getId(),highlight.getTime(), highlight.getUrl(),highlight.getTitle(),highlight.getNotedtext(),highlight.getComment(),highlight.getTag(),highlight.getCategory()};
                intent.putExtra("strings", myStrings);
                intent.putExtra("updatenotechecking",788);
                startActivity(intent);*/
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),AddNotesActivity.class);
                intent.putExtra("_id",id);
                startActivity(intent);


            }
        });



        return v;
    }



    public static NotesFragment getInstance()  //this is created because i want to finish this activity from addnotesactivity because notefragment is opened after addnoteactivity
    {
        return notesFragment;
    }

    public  void fetch_CustomNotes()
    {

        sharedPreferences = getActivity().getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);

        swipeRefreshLayout.setRefreshing(true);

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(SERVER_ADDRESS+token,
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

                                if (jsonObject.getString("url").equals("customNote")) {
                                    id = jsonObject.getString("_id");
                                    String time = jsonObject.getString("time");
                                    String url = jsonObject.getString("url");
                                    String title = jsonObject.getString("title");
                                    String notedtext = jsonObject.getString("text");
                                    String comment = jsonObject.getString("comment");
                                    String category = jsonObject.getString("category");

                                    JSONArray tagsArray = jsonObject.getJSONArray("tags");



                                    for (int j=0;j<tagsArray.length();j++)
                                    {
                                       String  tag = tagsArray.getString(j);

                                       taglist.add(tag);

                                        Log.d("tag",tag);
                                    }

                                    String tag1 = taglist.get(0);

                            Log.d("tagsize",taglist.size()+"");

                                    if (taglist.size()==2){

                                    tag2 = taglist.get(1);

                                        tag3="";
                                        tag4="";

                                        Log.d("in2",tag1+tag2+tag3+tag4);

                                    }
                                    else if (taglist.size()==3)
                                    {
                                        tag2 = taglist.get(1);
                                        tag3 = taglist.get(2);

                                        tag4="";

                                        Log.d("in3",tag1+tag2+tag3+tag4);
                                    }
                                    else if(taglist.size()==4)
                                    {
                                        tag2 = taglist.get(1);
                                        tag3 = taglist.get(2);
                                        tag4 = taglist.get(3);

                                        Log.d("in4",tag1+tag2+tag3+tag4);
                                    }

                                    else {
                                        tag2=  "";
                                        tag3 = "";
                                        tag4 = "";
                                    }

                                    taglist.clear();




                                    Log.d("time", time);

                                   Highlight highlight = new Highlight(id, time,url, title,notedtext,comment,tag1,tag2,tag3,tag4, category);

                                    Log.d("i", dh.checkNotesExist(highlight) + "");

                                   if (dh.checkNotesExist(highlight) == 0) {
                                        dh.insertmemo(highlight);
                                        Log.d("inserted", "whhoo insert");
                                    } else if (i == 1) {
                                        Log.d("not inserted", "whhoo not insert");
                                    }
                                    //notesList.add(highlight);

                                }

                            }

                           // Log.d("highlightlist",notesList.get(0).getTime());

                            notesList = dh.getNotes();
                            notesRecyclerAdapter = new NotesRecyclerAdapter(notesList);
                            recyclerView.setAdapter(notesRecyclerAdapter);

                            if (notesRecyclerAdapter.getItemCount()==0)
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
                }){
           /* @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("url","customNote");

                Log.v("param",params+"");

                return params;
            }*/

        };



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

       fetch_CustomNotes();

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
            fetch_CustomNotes();
        }


    }



}
