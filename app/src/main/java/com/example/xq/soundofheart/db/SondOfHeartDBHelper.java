package com.example.xq.soundofheart.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rocky on 2018/6/8 0008.
 */

public class SondOfHeartDBHelper extends SQLiteOpenHelper {
    public static final int DB_VERSIOIN=1;
    public  static final String DB_NAME="sondOfHeart.db";
    public static final String TABLE_NAME="resultReport";
    private  static Context mcontext;

    private SondOfHeartDBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSIOIN);
    }
    private static class LazyHolder{
        private static final SondOfHeartDBHelper INSTANCE=new SondOfHeartDBHelper(mcontext);
    }
    public static final SondOfHeartDBHelper getInstance(Context context){
        mcontext=context.getApplicationContext();
        return LazyHolder.INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create table if not exists resultReport (id integer primary key,correctRate real,createTime timestamp not null default (datetime('now','localtime')))
        String sql="create table if not exists " + TABLE_NAME + " (id integer primary key,correctRate real,createTime timestamp not null default (datetime('now','localtime')))";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql="drop table if exists"+TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
