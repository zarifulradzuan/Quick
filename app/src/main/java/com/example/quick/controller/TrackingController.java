package com.example.quick.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.quick.MainActivity;
import com.example.quick.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class TrackingController extends SQLiteOpenHelper {
    public static final String dbName = "quickDB";
    public static final String tblNameTracking = "tracking";
    public static final String colPlaceId = "place_id";

    public static final String strCrtTblTracking = "CREATE TABLE "+ tblNameTracking +
            " ("+ colPlaceId + " TEXT PRIMARY KEY )";
    public static final String strDropTblTracking = "DROP TABLE IF EXISTS " + tblNameTracking;
    private Context context;
    public TrackingController(Context context){
        super(context, dbName, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(strCrtTblTracking);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){
        sqLiteDatabase.execSQL(strDropTblTracking);
        onCreate(sqLiteDatabase);
    }

    public float fnInsertTracking(String placeId) {
        FirebaseMessaging.getInstance().subscribeToTopic(placeId)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = context.getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = context.getString(R.string.msg_subscribe_failed);
                        }
                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                    }
                });
        float retResult = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(colPlaceId, placeId);

        retResult = db.insert(tblNameTracking, null, values);
        return retResult;
    }

    public float fnDeleteSubject(String placeId){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(placeId)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = context.getString(R.string.msg_unsubscribed);
                        if (!task.isSuccessful()) {
                            msg = context.getString(R.string.msg_unsubscribe_failed);
                        }
                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                    }
                });
        float retResult;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(colPlaceId, placeId);
        retResult = db.delete(tblNameTracking, colPlaceId+" = ?", new String[]{placeId});
        return retResult;
    }

    public List<String> fnGetTracking(){
        List<String> list = new ArrayList<String>();
        String strSelAll = "Select * from " + tblNameTracking;

        Cursor cursor = this.getReadableDatabase().rawQuery(strSelAll,null);
        if(cursor.moveToFirst()){
            do{
                list.add(cursor.getString(cursor.getColumnIndex(colPlaceId)));
            }while(cursor.moveToNext());
        }
        return list;
    }
}

