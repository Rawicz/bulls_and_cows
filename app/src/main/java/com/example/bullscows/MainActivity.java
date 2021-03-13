package com.example.bullscows;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Random;

// TODO: use ViewModel to save UI state on the screen turning
public class MainActivity extends AppCompatActivity {
   private String keyword;
   RowsAdapter adapter;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      ListView attempts = findViewById(R.id.attempts);
      Button check = findViewById(R.id.check);
      EditText input = findViewById(R.id.edit_input);

      adapter = new RowsAdapter(this, makeKeyword());
      attempts.setAdapter(adapter);

      check.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            String inputValue = input.getText().toString().trim();
            try {
               if (inputValue.length() != 4) throw new RuntimeException("Invalid input!");
               else adapter.add(inputValue);
            } catch (RuntimeException e) {
               Toast.makeText(MainActivity.this, "Enter 4 digits, no gaps!", Toast.LENGTH_SHORT).show();
            } finally {
               input.getText().clear();
            }
         }
      });
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.main_menu, menu);
      return true;
   }

   @SuppressLint("NonConstantResourceId")
   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
         case R.id.new_game:
            adapter.renew(makeKeyword());
            break;
         case R.id.rules:
            Toast.makeText(this, "guess the number in minimum tries", Toast.LENGTH_SHORT).show();
            break;
      }
      return super.onOptionsItemSelected(item);
   }

   protected String makeKeyword() {
      int keynum = new Random().nextInt(10000);
      if (keynum < 10)
         keyword = "000".concat(String.valueOf(keynum));
      else if (keynum < 100)
         keyword = "00".concat(String.valueOf(keynum));
      else if (keynum < 1000)
         keyword = "0".concat(String.valueOf(keynum));
      else
         keyword = String.valueOf(keynum);
      Toast.makeText(this, "keyword: " + keyword, Toast.LENGTH_SHORT).show();
      return keyword;
   }
}