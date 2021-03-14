package com.example.bullscows;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// TODO: store record in SharedPreferences instead of InstanceState
// TODO: add button that renews the record
// TODO: make popup window which appears when ifo is pressed
// TODO: make icon for the application
public class MainActivity extends AppCompatActivity {
   // AndroidViewModel which stores ListView adapter with all data
   Coffer coffer;
   // used views and variables
   ListView attempts;
   Button check;
   EditText input;
   TextView congrats;
   TextView congratsSequel;
   boolean congratsAppeared = false;
   int record = 999;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      setTitle(getResources().getString(R.string.record) + " N/A");

      // initializing views
      attempts = findViewById(R.id.attempts);
      check = findViewById(R.id.check);
      input = findViewById(R.id.input);
      congrats = findViewById(R.id.congrats);
      congratsSequel = findViewById(R.id.congrats_2);

      // initialising AndroidViewModel and setting it to the ListVeiew
      this.coffer = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Coffer.class);
      coffer.adapt(attempts);
      // FIXME: delete this after testing
      Toast.makeText(this, "keyword: " + coffer.getKeyword(), Toast.LENGTH_SHORT).show();

      // When Button is pressed:
      // 1. Adds suggested number in the ListView, which is then processed by the adapter.
      // 2. If adapter tells that the number is successful, hides the interface and shows congratulation message.
      check.setOnClickListener(new View.OnClickListener() {
         @SuppressLint("SetTextI18n")
         @Override
         public void onClick(View v) {
            String inputValue = input.getText().toString().trim();
            try {
               if (inputValue.length() != 4) throw new RuntimeException("Invalid input!");
               else if (coffer.addValue(inputValue)) {
                  input.setVisibility(View.GONE);
                  check.setVisibility(View.GONE);
                  congrats.setVisibility(View.VISIBLE);
                  congratsSequel.setText(getResources().getString(R.string.you_won_with) + " " +
                        coffer.getAdapterCount() + " " + getResources().getString(R.string.attempts));
                  congratsSequel.setVisibility(View.VISIBLE);
                  congratsAppeared = true;
                  if (record > coffer.getAdapterCount()) {
                     Toast.makeText(MainActivity.this, "record should be rewritten!", Toast.LENGTH_SHORT).show();
                     record = coffer.getAdapterCount();
                     MainActivity.this.setTitle(getResources().getString(R.string.record) + " " + record);
                  }
               }
            } catch (RuntimeException e) {
               Toast.makeText(MainActivity.this, "Enter 4 digits, no gaps!", Toast.LENGTH_SHORT).show();
            } finally {
               input.getText().clear();
            }
         }
      });
   }

   @Override
   protected void onSaveInstanceState(@NonNull Bundle outState) {
      super.onSaveInstanceState(outState);
      outState.putInt("record", record);
   }

   @Override
   protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
      record = savedInstanceState.getInt("record");
      if (record < 999)
         setTitle(getResources().getString(R.string.record) + " " + record);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.main_menu, menu);
      return true;
   }

   /**
    * When new_game option selected:
    * 1. Renews the adapter in the ViewModel.
    * 2. If congratulation screen is appeared, hides it and shows back the interface views.
    * When rules option selected:
    * Shows screen which explains the rules.
    */
   @SuppressLint("NonConstantResourceId")
   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
         case R.id.new_game:
            coffer.renewAdapter();
            if (congratsAppeared) {
               congrats.setVisibility(View.GONE);
               congratsSequel.setVisibility(View.GONE);
               input.setVisibility(View.VISIBLE);
               check.setVisibility(View.VISIBLE);
            }
            // FIXME: delete this after testing
            Toast.makeText(this, "keyword: " + coffer.getKeyword(), Toast.LENGTH_SHORT).show();
            break;
         case R.id.rules:
            Toast.makeText(this, "guess the number in minimum tries", Toast.LENGTH_SHORT).show();
            break;
      }
      return super.onOptionsItemSelected(item);
   }

}