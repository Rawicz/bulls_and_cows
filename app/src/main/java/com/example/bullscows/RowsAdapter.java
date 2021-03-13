package com.example.bullscows;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RowsAdapter extends ArrayAdapter<String> {
   private Context context;
   private ArrayList<Integer> bullsList = new ArrayList<>();
   private ArrayList<Integer> cowsList = new ArrayList<>();
   private String keyword;
   private int count = 0;

   public RowsAdapter(@NonNull Context context, String keyword) {
      super(context, R.layout.row, R.id.value);
      this.context = context;
      this.keyword = keyword;
   }

   @NonNull
   @Override
   public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
      LayoutInflater inflater = LayoutInflater.from(context);
      View row;
      if (convertView == null)
         row = inflater.inflate(R.layout.row, parent, false);
      else
         row = convertView;

      TextView index = row.findViewById(R.id.index);
      TextView value = row.findViewById(R.id.value);
      TextView bulls = row.findViewById(R.id.bulls);
      TextView cows = row.findViewById(R.id.cows);

      String item = getItem(position);
      index.setText(String.valueOf(position + 1));
      value.setText(item);
      bulls.setText(String.valueOf(findBulls(item)));
      cows.setText(String.valueOf(findCows(item)));
      return row;
   }

   public void renew(String keyword) {
      clear();
      this.keyword = keyword;
   }

   public int findBulls(String object) {
      int bulls = 0;
      for (int i = 0; i < 4; i++) {
         if (object.charAt(i) == keyword.charAt(i)) bulls++;
      }
      return bulls;
   }

   public int findCows(String object) {
      int cows = 0;
      char[] keywordCopy = keyword.toCharArray();
      char shot;
      for (int i = 0; i < 4; i++) {
         shot = object.charAt(i);
         for (int j = 0; j < 4; j++) {
            if (j != i && keywordCopy[j] == shot){
               cows++;
               keywordCopy[j] = '*';
            }
         }
      }
      return cows;
   }
}
