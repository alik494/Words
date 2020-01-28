package com.sumin.words;


//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordsViewHolder>  {
public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordsViewHolder>  {

    private ArrayList<Word> words;
    private OnWordClickListerner onWordClickListerner;


    public WordAdapter(ArrayList<Word> words) {
        this.words = words;
    }

    public void setOnWordClickListerner(OnWordClickListerner onWordClickListerner) {
        this.onWordClickListerner = onWordClickListerner;
    }

    interface OnWordClickListerner{
        void onWordClick(int position);
        void onWordLongClick(int position);
    }

    @NonNull
    @Override
    public WordsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.words_item,viewGroup,false);
        return new WordsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordsViewHolder wordsViewHolder, int i) {
        Word word = words.get(i);
        wordsViewHolder.textViewEng.setText(word.getEng());
        wordsViewHolder.textViewRus.setText(word.getRus());
        wordsViewHolder.textViewHealth.setText(String.format("%s",word.getHealth()));

    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    class WordsViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewEng;
        private TextView textViewRus;
        private TextView textViewHealth;


        public WordsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEng=itemView.findViewById(R.id.textViewEng);
            textViewRus=itemView.findViewById(R.id.textViewRus);
            textViewHealth=itemView.findViewById(R.id.textViewHealth);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onWordClickListerner!=null){
                        onWordClickListerner.onWordClick(getAdapterPosition());
                }
            }
        });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onWordClickListerner!=null){
                        onWordClickListerner.onWordLongClick(getAdapterPosition());
                    }
                    return true;
                }
            });


        }
    }



}
