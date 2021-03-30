package com.example.bullscows;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
   View rulesView;
   AlertDialog rulesDialog;
   ListView attempts;
   Button check;
   EditText input;
   TextView congrats;
   TextView congratsSequel;
   SharedPreferences sharedData;
   int record;
   // AndroidViewModel which stores ListView adapter with all data
   Coffer coffer;

   @SuppressLint({"InflateParams", "SetTextI18n"})
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      // #RULES initialising AlertDialog for "rules" option in OptionsMenu
      initRulesDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

      // * SharedPreferences
      // #RECORD setting record and adding it to title
      sharedData = getSharedPreferences("data", MODE_PRIVATE);
      record = sharedData.getInt("record", 999);
      if (record < 999)
         setTitle(getResources().getString(R.string.record) + " " + record);
      else
         setTitle(getResources().getString(R.string.record) + " N/A");
      // #RULES are opened at first launch
      if (sharedData.getBoolean("first launch", true))
         rulesDialog.show();
      sharedData.edit().putBoolean("first launch", false).apply();

      // * Initializing views
      attempts = findViewById(R.id.attempts);
      check = findViewById(R.id.check);
      input = findViewById(R.id.input);
      congrats = findViewById(R.id.congrats);
      congratsSequel = findViewById(R.id.congrats_2);
      // Initialising AndroidViewModel and setting it's RowAdapter to the ListView
      this.coffer = new ViewModelProvider(this,
            new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Coffer.class);
      coffer.adapt(attempts);
      // Showing congrats message if the game is won
      if (coffer.won) {
         input.setVisibility(View.GONE);
         check.setVisibility(View.GONE);
         congrats.setVisibility(View.VISIBLE);
         congratsSequel.setText(getResources().getString(R.string.you_won_with) + " " +
               coffer.getAdapterCount() + " " + getResources().getString(R.string.attempts));
         congratsSequel.setVisibility(View.VISIBLE);
      }
      // ~ For #TESTING
//      Toast.makeText(this, "keyword: " + coffer.getKeyword(), Toast.LENGTH_SHORT).show();

      // * When Button is pressed:
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
                  ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(input.getWindowToken(), 0);
                  input.setVisibility(View.GONE);
                  check.setVisibility(View.GONE);
                  congrats.setVisibility(View.VISIBLE);
                  congratsSequel.setText(getResources().getString(R.string.you_won_with) + " " +
                        coffer.getAdapterCount() + " " + getResources().getString(R.string.attempts));
                  congratsSequel.setVisibility(View.VISIBLE);
                  // #RECORD
                  if (record > coffer.getAdapterCount()) {
                     record = coffer.getAdapterCount();
                     sharedData.edit().putInt("record", record).apply();
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
   public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.main_menu, menu);
      return true;
   }

   /**
    * * When new_game option selected:
    * 1. Renews the adapter in the ViewModel.
    * 2. If congratulation screen is appeared, hides it and shows back the interface views.
    * * When rules option selected:
    * Shows screen which explains the rules.
    */
   @SuppressLint("NonConstantResourceId")
   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
         case R.id.new_game:
            coffer.renewAdapter();
            if (coffer.won) {
               congrats.setVisibility(View.GONE);
               congratsSequel.setVisibility(View.GONE);
               input.setVisibility(View.VISIBLE);
               check.setVisibility(View.VISIBLE);
               coffer.won = false;
            }
            // ~ For #TESTING
//            Toast.makeText(this, "keyword: " + coffer.getKeyword(), Toast.LENGTH_SHORT).show();
            break;
         case R.id.rules:
            // #RULES
            rulesDialog.show();
            // ~ The following would give the AlertDialog max size
//            int matchParent = WindowManager.LayoutParams.MATCH_PARENT;
//            rulesDialog.getWindow().setLayout(matchParent, matchParent);
//            rulesDialog.setContentView(rulesView);
            break;
      }
      return super.onOptionsItemSelected(item);
   }

   /**
    * #RULES
    * Initializes AlertDialog for {@link MainActivity#rulesDialog}
    * Used for the rules option of OptionsMenu
    */
   @SuppressLint("InflateParams")
   private AlertDialog initRulesDialog() {
      rulesView = getLayoutInflater().inflate(R.layout.info, null);
      AlertDialog.Builder rulesBuilder = new AlertDialog.Builder(this);
      rulesDialog = rulesBuilder.setView(rulesView).create();
      // setting AlertDialog views preferences
      ((TextView)rulesView.findViewById(R.id.info_text)).setMovementMethod(new ScrollingMovementMethod());
      rulesView.findViewById(R.id.ok).setOnClickListener(v -> rulesDialog.cancel());
      // #RECORD renews the record
      rulesView.findViewById(R.id.renew_record).setOnClickListener(v -> {
         record = 999;
         sharedData.edit().putInt("record", record).apply();
         MainActivity.this.setTitle(getResources().getString(R.string.record) + " N/A");
         rulesDialog.cancel();
      });
      return rulesDialog;
   }

}