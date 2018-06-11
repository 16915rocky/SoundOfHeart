package com.example.xq.soundofheart.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.xq.soundofheart.bean.PracticeResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rocky on 2018/6/8 0008.
 */

public class ResultReportDao {
    private Context mContext;
    private SondOfHeartDBHelper sondOfHeartDBHelper;


    public ResultReportDao(Context context) {
        this.mContext=context;
        sondOfHeartDBHelper=SondOfHeartDBHelper.getInstance(context);
    }

    public void addPracticeResult(double correctRate ){
         SQLiteDatabase db = sondOfHeartDBHelper.getWritableDatabase();
         db.beginTransaction();
         //insert into resultReport (correctRate) values(0.01)
         db.execSQL("insert into "+ SondOfHeartDBHelper.TABLE_NAME +"(correctRate) values ("+ correctRate +")");
         db.setTransactionSuccessful();
    }

    public List<PracticeResultBean> queryPracticeResult(){
        SQLiteDatabase db = sondOfHeartDBHelper.getReadableDatabase();

        Cursor cursor = db.query(SondOfHeartDBHelper.TABLE_NAME, new String[]{"id","correctRate"}, null, null, null, null, null);
        List<PracticeResultBean> mList=new ArrayList<PracticeResultBean>(cursor.getCount());
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                PracticeResultBean practiceResultBean = parsePRBean(cursor);
                mList.add(practiceResultBean);
            }
        }
        return mList;
    }
    /**
     * 将查找到的数据转换成Order类
     */
    private PracticeResultBean parsePRBean(Cursor cursor){
        PracticeResultBean prBean = new PracticeResultBean();
        prBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
        prBean.setCorrectRate((cursor.getFloat(cursor.getColumnIndex("correctRate"))));
        return prBean;
    }
}
