package com.example.bullscows;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

// TODO: actually, I think it'd be better to leave bulls and cows handling to getView method only
// TODO: and clear all excessive overridings
public class RowsAdapter extends ArrayAdapter<String> {
   private ArrayList<Integer> bullsList = new ArrayList<Integer>();
   private ArrayList<Integer> cowsList = new ArrayList<Integer>();
   private String keyword;
   private int count = 0;

   public RowsAdapter(@NonNull Context context, String keyword) {
      super(context, R.layout.row, R.id.value);
      this.keyword = keyword;
   }

   @Override
   public int getCount() {
      return count;
   }

   @Override
   public void clear() {
      setNotifyOnChange(false);
      super.clear();
      bullsList.clear();
      cowsList.clear();
      count = 0;
      notifyDataSetChanged();
   }

   @Override
   public void remove(@Nullable String object) {
      int i = getPosition(object);
      setNotifyOnChange(false);
      super.remove(object);
      bullsList.remove(i);
      cowsList.remove(i);
      count--;
      notifyDataSetChanged();
   }

   // TODO: add bulls and cows values with methods which calculate them
   @Override
   public void add(@Nullable String object) {
      setNotifyOnChange(false);
      super.add(object);
      count++;
   }

   public void renew(String keyword) {
      clear();
      this.keyword = keyword;
   }

   // TODO: check how findBulls and findCows methods work in the terminal
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
