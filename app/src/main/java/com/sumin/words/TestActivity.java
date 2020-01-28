package com.sumin.words;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {
    private TextView textViewEng;
    private TextView textViewRus;

    private WordDBhelper dBhelper;
    private SQLiteDatabase database;
    private  ArrayList<Word> words= new ArrayList<>();
    private String[] selectionArgs = null;
    private  Word word;

    private Word wordCheck;
    private Word wordCheck2=new Word(0,"null1","frg",43);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        textViewEng=findViewById(R.id.textViewEng);
        textViewRus=findViewById(R.id.textViewRus);

        dBhelper=new WordDBhelper(this);
        database =dBhelper.getWritableDatabase();
        getData();

        generateWord();

    }
    private int genIn(){
        int size=words.size();

        int indexArr =(int) (Math.random() *size);
        if (!words.isEmpty()) {
            wordCheck = words.get(indexArr);
            String wordCheckS = wordCheck.getEng();
            String wordCheck2S = wordCheck2.getEng();
            while (wordCheckS.equals(wordCheck2S)) {
                size = words.size();
                indexArr = (int) (Math.random() * size);
                wordCheck = words.get(indexArr);
                wordCheckS = wordCheck.getEng();
            }
            wordCheck2 = wordCheck;
        }
        return indexArr;
    }

    private void updateQue(){
        getData();


        generateWord();
    }

    private void generateWord(){
        if (!words.isEmpty()) {
            word = words.get(genIn());
            textViewEng.setText(word.getEng());
        }

    }
    private void getData(){
        words.clear();
        String selection = "health > ?";
        selectionArgs = new String[] { "0" };
        Cursor cursor=database.query(WordDBhelper.WordsEntry.TABLE_NAME,null,selection,selectionArgs,null,null,null);
        while (cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex(WordDBhelper.WordsEntry._ID));
            String eng = cursor.getString(cursor.getColumnIndex(WordDBhelper.WordsEntry.COLUMN_ENG));
            String rus=cursor.getString(cursor.getColumnIndex(WordDBhelper.WordsEntry.COLUMN_RUS));
            int health=cursor.getInt(cursor.getColumnIndex(WordDBhelper.WordsEntry.COLUMN_HEALTH));
            Word word= new Word(id,eng,rus,health);
            words.add(word);

        }
        cursor.close();
        if (words.size()<2||words.isEmpty()){
            finish();
            Toast.makeText(this, "too small dict", Toast.LENGTH_SHORT).show();
        }
    }
    public void onClickShow(View view) {

        textViewRus.setText(word.getRus());
    }


    public void onClickYes(View view) {

        int id =word.getId();

        ContentValues contentValues=new ContentValues();
        contentValues.put(WordDBhelper.WordsEntry.COLUMN_ENG,word.getEng());
        contentValues.put(WordDBhelper.WordsEntry.COLUMN_RUS,word.getRus());
        contentValues.put(WordDBhelper.WordsEntry.COLUMN_HEALTH,word.getHealth()-1);

        String where = WordDBhelper.WordsEntry._ID+" = ?";
        String[] whereArgs=new   String[]{Integer.toString(id)};
        database.update(WordDBhelper.WordsEntry.TABLE_NAME,contentValues,where,whereArgs);
        updateQue();
        textViewRus.setText(R.string.show_translate);
    }

    public void onClickNo(View view) {
        int id =word.getId();

        ContentValues contentValues=new ContentValues();
        contentValues.put(WordDBhelper.WordsEntry.COLUMN_ENG,word.getEng());
        contentValues.put(WordDBhelper.WordsEntry.COLUMN_RUS,word.getRus());
        contentValues.put(WordDBhelper.WordsEntry.COLUMN_HEALTH,word.getHealth()+1);

        String where = WordDBhelper.WordsEntry._ID+" = ?";
        String[] whereArgs=new   String[]{Integer.toString(id)};
        database.update(WordDBhelper.WordsEntry.TABLE_NAME,contentValues,where,whereArgs);

        updateQue();
        textViewRus.setText(R.string.show_translate);
    }
}
