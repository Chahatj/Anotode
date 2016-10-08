package com.example.chahat.anotode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class NotesFragment extends Fragment
{

    SharedPreferences sharedPreferences;

    String SERVER_ADDRESS = "http://anotode.herokuapp.com/api/highlights?token=";


    NotesRecyclerAdapter notesRecyclerAdapter;
    List<Highlight> notesList;
    RecyclerView  recyclerView;



    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.notes_fragment,container,false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        notesList = new ArrayList<>();



        NotesDataHandler dh=new NotesDataHandler(getActivity(),null,null,1);

      //  notesList = dh.getNotes();
        notesRecyclerAdapter = new NotesRecyclerAdapter(notesList);

        fetch_CustomNotes();

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(notesRecyclerAdapter);





        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Highlight highlight  = notesList.get(position);
                Toast.makeText(getActivity(), highlight.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),AddNotesActivity.class));
            }
        });

        return v;
    }

    public  void fetch_CustomNotes()
    {

        sharedPreferences = getActivity().getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(SERVER_ADDRESS+token,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("responsenotes",response+"");

                        try
                        {
                            for (int i = 0;i<response.length();i++) {



                                JSONObject jsonObject = response.getJSONObject(i);
                                // String userid = jsonObject.getString("user_id");

                                /*if (jsonObject.getString("url").equals("customNote")) {*/

                                    String time = jsonObject.getString("time");

                                    //String url = jsonObject.getString("url");
                                    //String title = jsonObject.getString("title");
                                    //String notedtext = jsonObject.getString("text");
                                    //String comment = jsonObject.getString("comment");
                                    //String category = jsonObject.getString("category");
                                    String tags = jsonObject.getString("tags");


                                    Log.d("time", time);

                                    Highlight highlight = new Highlight(time, null, null, null, null, tags, null);

                                    notesList.add(highlight);

                            }

                            Log.d("highlightlist",notesList.get(0).getTime());

                            notesRecyclerAdapter = new NotesRecyclerAdapter(notesList);
                            recyclerView.setAdapter(notesRecyclerAdapter);
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
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
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

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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


}
