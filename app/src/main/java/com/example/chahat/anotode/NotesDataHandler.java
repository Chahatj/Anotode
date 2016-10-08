package com.example.chahat.anotode;

import android.content.ContentValues;
import android.content.Context;
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
        db.execSQL("CREATE TABLE Notes(time TEXT, title TEXT,notes TEXT,tag TEXT,category TEXT);");
    }

  /*  public void insertmemo(Highlight highlight)
    {

            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("time", highlight.getTime());
        Log.d("time",highlight.getTime());
            values.put("title",highlight.getTitle());
            values.put("notes",highlight.getNotedtext());
            values.put("tag",highlight.getTag());
            values.put("category",highlight.getCategory());
            db.insert("Notes", null, values);
            db.close();

    }

    */

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

   /* Cursor cursor;


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
                if(cursor.getString(cursor.getColumnIndex("notes"))!=null) {
                    Highlight notes = new Highlight(cursor.getString(cursor.getColumnIndex("title")),cursor.getString(cursor.getColumnIndex("notes")),cursor.getString(cursor.getColumnIndex("tag")),cursor.getString(cursor.getColumnIndex("category")));
                    list.add(notes);

                    Log.d("cursor",cursor.getString(cursor.getColumnIndex("time")));
                }

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    */
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

