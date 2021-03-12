package com.example.bullscows;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RowsAdapter extends ArrayAdapter<String> {
   private ArrayList<Integer> bullsList = new ArrayList<Integer>();
   private ArrayList<Integer> cowsList = new ArrayList<Integer>();
   private int count = 0;

   public RowsAdapter(@NonNull Context context) {
      super(context, R.layout.row, R.id.value);
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
}
