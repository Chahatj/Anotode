package com.example.chahat.anotode;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chahat on 15/12/15.
 */
public class NotesDataHandler extends SQLiteOpenHelper
{
    private static final String databasename = "mynotes.db";
    private static final int databaseversion = 1;

    SharedPreferences sharedPreferences;



    public NotesDataHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context,databasename,factory,databaseversion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL("CREATE TABLE Highlights(id TEXT,time TEXT,url TEXT,title TEXT,note TEXT,comment TEXT,tag1 TEXT,tag2 TEXT,tag3 TEXT,tag4 TEXT,category TEXT);");
        db.execSQL("CREATE TABLE Notes(id TEXT,time TEXT,url TEXT,title TEXT,note TEXT,comment TEXT,tag1 TEXT,tag2 TEXT,tag3 TEXT,tag4 TEXT,category TEXT);");
    }




   public void insertmemo(Highlight highlight)
    {

            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("id",highlight.getId());
            values.put("time", highlight.getTime());
            values.put("url",highlight.getUrl());
            values.put("title",highlight.getTitle());
            values.put("note",highlight.getNotedtext());
            values.put("comment",highlight.getComment());
            values.put("tag1",highlight.getTag1());
            values.put("tag2",highlight.getTag2());
        values.put("tag3",highlight.getTag3());
        values.put("tag4",highlight.getTag4());
            values.put("category",highlight.getCategory());
            db.insert("Notes", null, values);
            db.close();

    }

    public void insertHighlights(Highlight highlight)
    {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",highlight.getId());
        values.put("time", highlight.getTime());
        values.put("url",highlight.getUrl());
        values.put("title",highlight.getTitle());
        values.put("note",highlight.getNotedtext());
        values.put("comment",highlight.getComment());
        values.put("tag1",highlight.getTag1());
        values.put("tag2",highlight.getTag2());
        values.put("tag3",highlight.getTag2());
        values.put("tag4",highlight.getTag2());
        values.put("category",highlight.getCategory());
        db.insert("Highlights", null, values);
        db.close();

    }

    public void deletePreviousofUpdated(String id)
    {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("delete from Highlights where id='"+id+"'");
        db.execSQL("delete from Notes where id='"+id+"'");
    }

    public void updateNotes(String id,String title,String note,String comment,String tag1,String tag2,String tag3,String tag4,String category)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues data=new ContentValues();

        data.put("title",title);
        data.put("note",note);
        data.put("comment",comment);
        data.put("tag1",tag1);
        data.put("tag2",tag2);
        data.put("tag3",tag3);
        data.put("tag4",tag4);
        data.put("category",category);
        db.update("Notes", data, "id = ?", new String[]{id});
        db.update("Highlights", data, "id = ?", new String[]{id});


    }




  /*  public void editmemo(String st, memo me)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("memo",me.getMemo());

        db.update("Memory", values, "memo=?", new String[]{st});
        db.close();
    }

    public void deletememo(memo memo)
    {
        SQLiteDatabase db = getWritableDatabase();

        db.delete("Memory", "memo=?", new String[]{memo.getMemo()});
        db.close();
    }

*/

    Cursor cursor;


    public List<Highlight> getNotes()
    {
        String dbstring = "";

        SQLiteDatabase db=getWritableDatabase();
        List<Highlight> list = new ArrayList<>();
        cursor=db.rawQuery( " SELECT * FROM Notes " , null);

        if(cursor!=null&&cursor.getCount()>0)
        {

            cursor.moveToFirst();
            do
            {
                if(cursor.getString(cursor.getColumnIndex("time"))!=null) {
                    Highlight notes = new Highlight(cursor.getString(cursor.getColumnIndex("id")),cursor.getString(cursor.getColumnIndex("time")),cursor.getString(cursor.getColumnIndex("url")),cursor.getString(cursor.getColumnIndex("title")),cursor.getString(cursor.getColumnIndex("note")),cursor.getString(cursor.getColumnIndex("comment")),cursor.getString(cursor.getColumnIndex("tag1")),cursor.getString(cursor.getColumnIndex("tag2")),cursor.getString(cursor.getColumnIndex("tag3")),cursor.getString(cursor.getColumnIndex("tag4")),cursor.getString(cursor.getColumnIndex("category")));
                    list.add(notes);

                    Log.d("cursor",cursor.getString(cursor.getColumnIndex("time")));
                }

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Highlight> getHighlights()
    {
        String dbstring = "";

        SQLiteDatabase db=getWritableDatabase();
        List<Highlight> list = new ArrayList<>();
        cursor=db.rawQuery( " SELECT * FROM Highlights " , null);

        if(cursor!=null&&cursor.getCount()>0)
        {

            cursor.moveToFirst();
            do
            {
                if(cursor.getString(cursor.getColumnIndex("time"))!=null) {
                    Highlight highlight = new Highlight(cursor.getString(cursor.getColumnIndex("id")),cursor.getString(cursor.getColumnIndex("time")),cursor.getString(cursor.getColumnIndex("url")),cursor.getString(cursor.getColumnIndex("title")),cursor.getString(cursor.getColumnIndex("note")),cursor.getString(cursor.getColumnIndex("comment")),cursor.getString(cursor.getColumnIndex("tag1")),cursor.getString(cursor.getColumnIndex("tag2")),cursor.getString(cursor.getColumnIndex("tag3")),cursor.getString(cursor.getColumnIndex("tag4")),cursor.getString(cursor.getColumnIndex("category")));
                    list.add(highlight);

                    Log.d("cursor",cursor.getString(cursor.getColumnIndex("time")));
                }

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }



    int  i ;

    public int checkNotesExist(Highlight highlight)
    {
        Log.d("checktime",highlight.getTime());


        String dbstring = "";

        SQLiteDatabase db=getWritableDatabase();
        cursor=db.rawQuery( " SELECT * FROM Notes " , null);

        if(cursor!=null&&cursor.getCount()>0)
        {

            cursor.moveToFirst();
            do
            {
                if(cursor.getString(cursor.getColumnIndex("id"))!=null) {
                    Log.d("cursortime",cursor.getString(cursor.getColumnIndex("time")));

                    if ((cursor.getString(cursor.getColumnIndex("id"))).equals(highlight.getId()))
                    {
                       i=1;
                        break;
                    }
                    else{
                        i=0;
                    }


                }

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

       return i;
    }


    public int checkHighlightsExist(Highlight highlight)
    {
        Log.d("checktime",highlight.getTime());


        String dbstring = "";

        SQLiteDatabase db=getWritableDatabase();
        cursor=db.rawQuery( " SELECT * FROM Highlights " , null);

        if(cursor!=null&&cursor.getCount()>0)
        {

            cursor.moveToFirst();
            do
            {
                if(cursor.getString(cursor.getColumnIndex("id"))!=null) {
                    Log.d("cursortime",cursor.getString(cursor.getColumnIndex("time")));

                    if ((cursor.getString(cursor.getColumnIndex("id"))).equals(highlight.getId()))
                    {
                        i=1;
                        break;
                    }
                    else{
                        i=0;
                    }


                }

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return i;
    }

    public List<Highlight> searchDatabase(String query,int i)
    {


        SQLiteDatabase db=getWritableDatabase();
        List<Highlight> list = new ArrayList<>();

        String[] selectionargs = new String[]{null};
        selectionargs[0] ='%' + query + '%';

       if (i==2)
       {
           cursor=db.rawQuery( " SELECT * FROM Notes Where title LIKE ?",selectionargs);
       }
        else if (i==1)
       {
           cursor=db.rawQuery( " SELECT * FROM Highlights Where title LIKE ?",selectionargs);

       }

        if(cursor!=null&&cursor.getCount()>0)
        {

            cursor.moveToFirst();
            do
            {
                if(cursor.getString(cursor.getColumnIndex("time"))!=null) {
                    Highlight notes = new Highlight(cursor.getString(cursor.getColumnIndex("id")),cursor.getString(cursor.getColumnIndex("time")),cursor.getString(cursor.getColumnIndex("url")),cursor.getString(cursor.getColumnIndex("title")),cursor.getString(cursor.getColumnIndex("note")),cursor.getString(cursor.getColumnIndex("comment")),cursor.getString(cursor.getColumnIndex("tag1")),cursor.getString(cursor.getColumnIndex("tag2")),cursor.getString(cursor.getColumnIndex("tag3")),cursor.getString(cursor.getColumnIndex("tag4")),cursor.getString(cursor.getColumnIndex("category")));
                    list.add(notes);

                    Log.d("cursor",cursor.getString(cursor.getColumnIndex("time")));
                }

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }


    /*public List<memo> getfirstline()
    {
        String dbstring = "";
        SQLiteDatabase db=getWritableDatabase();
        List<memo> list = new ArrayList<>();
        c=db.rawQuery( " SELECT substr(1,4) FROM Memory " ,null);

        if(c!=null&& c.getCount()>0)
        {

            c.moveToFirst();
            do
            {
                if(c.getString(c.getColumnIndex("memo"))!=null)
                {
                    memo memo = new memo(c.getString(c.getColumnIndex("memo")));
                    list.add(memo);

                }


            }while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }*/


}

