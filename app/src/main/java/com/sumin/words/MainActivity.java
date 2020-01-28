package com.sumin.words;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.support.annotation.NonNull;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewWord;
    private EditText editTextEng;
    private EditText editTextRus;
    private final ArrayList<Word> words = new ArrayList<>();
    private WordAdapter adapter;
    private WordDBhelper dBhelper;
    private SQLiteDatabase database;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        recyclerViewWord = findViewById(R.id.recyclerViewWord);
        editTextEng = findViewById(R.id.editTextEng);
        editTextRus = findViewById(R.id.editTextRus);
        dBhelper = new WordDBhelper(this);
        database = dBhelper.getWritableDatabase();
        getData();


        adapter = new WordAdapter(words);
        recyclerViewWord.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
        recyclerViewWord.setAdapter(adapter);

        adapter.setOnWordClickListerner(new WordAdapter.OnWordClickListerner() {
            @Override
            public void onWordClick(int position) {
                Toast.makeText(MainActivity.this, "pos " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onWordLongClick(int position) {

                updateWord(position);
                remove(position);
                //TODO сделать заполнение едитов выбраного елемента
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                remove(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewWord);
    }

    private void remove(int position) {
        int id = words.get(position).getId();
        String where = WordDBhelper.WordsEntry._ID + " = ?";
        String[] whereArgs = new String[]{Integer.toString(id)};
        database.delete(WordDBhelper.WordsEntry.TABLE_NAME, where, whereArgs);
        getData();
        //words.remove(position);
        adapter.notifyDataSetChanged();
        //Toast.makeText(MainActivity.this, "nom pos"+position, Toast.LENGTH_SHORT).show();
    }

    public void onClickAddWord(View view) {
        database = dBhelper.getWritableDatabase();
        dBhelper = new WordDBhelper(this);
        String eng = editTextEng.getText().toString().trim();
        String rus = editTextRus.getText().toString().trim();

        if (isFilled(eng, rus)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(WordDBhelper.WordsEntry.COLUMN_ENG, eng);
            contentValues.put(WordDBhelper.WordsEntry.COLUMN_RUS, rus);
            contentValues.put(WordDBhelper.WordsEntry.COLUMN_HEALTH, 3);
            database.insert(WordDBhelper.WordsEntry.TABLE_NAME, null, contentValues);
            getData();
            adapter.notifyDataSetChanged();
            editTextEng.setText("");
            editTextRus.setText("");
        } else
            Toast.makeText(this, R.string.warning_fill_fileds, Toast.LENGTH_SHORT).show();


    }

    private void updateWord(int position) {

        int id = words.get(position).getId();
        String idSelect = "_id == '" + id + "'";
        Cursor cursor = database.query(WordDBhelper.WordsEntry.TABLE_NAME, null, idSelect, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String eng = cursor.getString(cursor.getColumnIndex(WordDBhelper.WordsEntry.COLUMN_ENG));
            String rus = cursor.getString(cursor.getColumnIndex(WordDBhelper.WordsEntry.COLUMN_RUS));
            editTextEng.setText(eng);
            editTextRus.setText(rus);

            break;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        adapter.notifyDataSetChanged();
    }

    private boolean isFilled(String eng, String rus) {
        return !eng.isEmpty() && !rus.isEmpty();
    }

    private void getData() {
        words.clear();

        Cursor cursor = database.query(WordDBhelper.WordsEntry.TABLE_NAME, null, null, null, null, null, WordDBhelper.WordsEntry.COLUMN_HEALTH, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(WordDBhelper.WordsEntry._ID));
            String eng = cursor.getString(cursor.getColumnIndex(WordDBhelper.WordsEntry.COLUMN_ENG));
            String rus = cursor.getString(cursor.getColumnIndex(WordDBhelper.WordsEntry.COLUMN_RUS));
            int health = cursor.getInt(cursor.getColumnIndex(WordDBhelper.WordsEntry.COLUMN_HEALTH));
            Word word = new Word(id, eng, rus, health);
            words.add(word);
        }
        cursor.close();
        if (words.isEmpty()) {
            words.add(new Word(0, "gem", "жемчюжина", 5));
            words.add(new Word(1, "slug", "слизняк", 5));
            words.add(new Word(2, "dripping", "просачивание", 5));
            words.add(new Word(3, "kudos", "почет", 5));
            words.add(new Word(4, "idle", "бездействие", 5));
            words.add(new Word(5, "persue", "преследовать", 5));
            words.add(new Word(6, "blurt", "сболтнуть", 5));
            words.add(new Word(7, "blimey", "иди ты!", 5));
            words.add(new Word(8, "gem", "драгоценый камень", 5));
            words.add(new Word(9, "moss", "мох", 5));
            words.add(new Word(10, "ferry", "паром", 5));
            words.add(new Word(11, "quaint", "причудливый", 5));
            words.add(new Word(12, "hike", "длительная прогулка", 5));
            words.add(new Word(13, "participate", "принимать участие", 5));
            words.add(new Word(14, "aptly", "легко", 5));
            words.add(new Word(15, "scenic", "живописный", 5));
            words.add(new Word(16, "knack", "умение", 5));
            words.add(new Word(17, "melt", "плавить", 5));
            words.add(new Word(18, "handly", "удобный", 5));
            words.add(new Word(19, "treatment", "лечение", 2));
            words.add(new Word(20, "flounder", "барахтаться", 2));
            words.add(new Word(21, "incentive", "стимул", 2));
            words.add(new Word(22, "diversity", "разнообразие", 2));
            words.add(new Word(23, "roaches", "тараканы", 2));
            words.add(new Word(24, "pine", "хвоя", 2));
            words.add(new Word(24, "apear", "обращение", 2));
            words.add(new Word(24, "lance", "копье", 2));
            words.add(new Word(24, "the buzz", "кайф", 2));
            words.add(new Word(24, "pine", "хвоя", 2));
            words.add(new Word(24, "fuse", "предохранитель", 2));
            words.add(new Word(24, "offense", "преступление", 2));
            words.add(new Word(24, "resemble", "походить", 2));
            words.add(new Word(24, "outlet", "вых. отверстие", 2));
            words.add(new Word(24, "moderate", "умеренный", 2));
            words.add(new Word(24, "liability", "ответственность", 2));
            words.add(new Word(24, "palates", "нёбо", 2));
            words.add(new Word(24, "ingenuity", "изобретательность", 2));
            words.add(new Word(24, "frail", "хилый", 2));
            words.add(new Word(24, "nether", "нижний", 2));
            words.add(new Word(24, "composure", "хладнокровие", 2));
            words.add(new Word(24, "significant", "значительное", 2));
            words.add(new Word(24, "thigh", "бедро", 2));
            words.add(new Word(24, "wedges", "клинья", 2));
            words.add(new Word(24, "warp", "деформироваться", 2));

            words.add(new Word(24, "conducive", "способствующий", 2));
            words.add(new Word(24, "villain", "злодей", 2));
            words.add(new Word(24, "entitle", "давать право", 2));
            words.add(new Word(24, "adore", "обожать", 2));
            words.add(new Word(24, "composure", "хладнокровие", 2));
            words.add(new Word(24, "despite", "несмотря на", 2));
            words.add(new Word(24, "greedy", "жадный", 2));
            words.add(new Word(24, "cringe", "съеживаться", 2));
            words.add(new Word(24, "resurrection", "воскрешение", 2));
            words.add(new Word(24, "clarity", "ясность", 2));
            words.add(new Word(24, "redeem", "выкупать", 2));
            words.add(new Word(24, "putrid", "гнилой", 2));
            words.add(new Word(24, "smirks", "ухмыляется", 2));
            words.add(new Word(24, "vapor", "пар", 2));
            words.add(new Word(24, "sting", "ужалить", 2));
            words.add(new Word(24, "draft", "призыв в армию", 2));
            words.add(new Word(24, "festoon", "гирлянда", 2));
            words.add(new Word(24, "mutton", "баранина", 2));
            words.add(new Word(24, "minority", "меньшенство", 2));
            words.add(new Word(24, "stag", "олень", 2));
            words.add(new Word(24, "sack", "Мешок", 2));
            words.add(new Word(24, "conundrum", "головоломка", 2));
            words.add(new Word(24, "damnation", "проклятие", 2));
            words.add(new Word(24, "misgivings", "опасения", 2));
            words.add(new Word(24, "notches", "выемки", 2));
            words.add(new Word(24, "loin", "поясница", 2));
            words.add(new Word(24, "tenderize", "развивать", 2));
            words.add(new Word(24, "noble", "благородный", 2));
            words.add(new Word(24, "crumbling", "рушатся", 2));
            words.add(new Word(24, "spared", "избавлены", 2));
            words.add(new Word(24, "pierce", "проколоть", 2));
            words.add(new Word(24, "guts", "кишки", 2));
            words.add(new Word(24, "ignominious", "постыдный", 2));
            words.add(new Word(24, "legacy", "наследие", 2));
            words.add(new Word(24, "give a darn", "проклясть", 2));
            words.add(new Word(24, "darn", "штопать", 2));
            words.add(new Word(24, "Simp", "простофиля", 2));
            words.add(new Word(24, "outer", "внешний", 2));
            words.add(new Word(24, "graze", "клевок", 2));
            words.add(new Word(24, "Misled", "введеный в заблуждение", 2));

            words.add(new Word(24, "Sewage", "сточная вода", 2));
            words.add(new Word(24, "redolent", "благоухающий", 2));
            words.add(new Word(24, "relent", "смягчаться", 2));
            words.add(new Word(24, "tick", "поставить галочку", 2));
            words.add(new Word(24, "warden", "смотритель", 2));
            words.add(new Word(24, "Overseer ", "смотритель", 2));
            words.add(new Word(24, "lurking", "скрываясь", 2));

            words.add(new Word(24, "above", "над", 2));
            words.add(new Word(24, "profanity", "ненормативная лексика", 2));
            words.add(new Word(24, "Pimple", "прыщ", 2));
            words.add(new Word(24, "Stroll", "прогулка", 2));
            words.add(new Word(24, "affection", "привязаность", 2));
            words.add(new Word(24, "crave", "жаждать", 2));
            words.add(new Word(24, "hiccup", "икота", 2));
            words.add(new Word(24, "Soil", "почва", 2));
            words.add(new Word(24, "berk", "болван", 2));
            words.add(new Word(24, "pun", "каламбур", 2));
            words.add(new Word(24, "singling", "выделение", 2));
            words.add(new Word(24, "plunged", "погрузилась", 2));

            words.add(new Word(24, "outwitted", "перехетрил", 3));
            words.add(new Word(24, "lured", "заманил", 3));
            words.add(new Word(24, "Sullied", "пятналась", 3));
            words.add(new Word(24, "rode", "ухал", 3));
            words.add(new Word(24, "defy", "игнорировать", 3));
            words.add(new Word(24, "summon", "вызывать", 3));
            words.add(new Word(24, "solemn", "торжественный", 3));
            words.add(new Word(24, "agenda", "повестка дня", 3));
            words.add(new Word(24, "possession", "владение", 3));
            words.add(new Word(24, "riddles", "загадки", 3));
            words.add(new Word(24, "toss", "жеребьевка", 3));
            words.add(new Word(24, "furl", "свертивать", 3));
            words.add(new Word(24, "snide", "ехидный", 3));
            words.add(new Word(24, "flea", "блоха", 3));
            words.add(new Word(24, "envy", "зависть", 3));
            words.add(new Word(24, "sobbing", "рыдание", 3));

            words.add(new Word(24, "snide", "ехидный", 3));
            words.add(new Word(24, "snide", "ехидный", 3));
            words.add(new Word(24, "snide", "ехидный", 3));
            words.add(new Word(24, "snide", "ехидный", 3));
            words.add(new Word(24, "snide", "ехидный", 3));


            for (Word word : words) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(WordDBhelper.WordsEntry.COLUMN_ENG, word.getEng());
                contentValues.put(WordDBhelper.WordsEntry.COLUMN_RUS, word.getRus());
                contentValues.put(WordDBhelper.WordsEntry.COLUMN_HEALTH, word.getHealth());
                //    database.delete(WordDBhelper.WordsEntry.TABLE_NAME,null,null);
                database.insert(WordDBhelper.WordsEntry.TABLE_NAME, null, contentValues);
            }

        }
    }

    public void onClickTest(View view) {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }

    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }
}
