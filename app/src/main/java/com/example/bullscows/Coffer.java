package com.example.bullscows;

import android.app.Application;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Coffer extends AndroidViewModel {
   private String keyword;
   private final RowsAdapter adapter;

   public Coffer(@NonNull Application application) {
      super(application);
      adapter = new RowsAdapter(getApplication(), makeKeyword());
   }

   /**
    * Sets nested adapter to passed ListView
    */
   public void adapt(@NotNull ListView v) {
      v.setAdapter(adapter);
   }

   public String getKeyword() {
      return keyword;
   }

   public int getAdapterCount() {
      return adapter.getCount();
   }

   public boolean addValue(String value) {
      adapter.add(value);
      return value.equals(keyword);
   }

   public void renewAdapter() {
      adapter.renew(makeKeyword());
   }

   private String makeKeyword() {
      int number = new Random().nextInt(10000);
      if (number < 10)
         keyword = "000" + number;
      else if (number < 100)
         keyword = "00" + number;
      else if (number < 1000)
         keyword = "0" + number;
      else
         keyword = String.valueOf(number);
      return keyword;
   }
}
