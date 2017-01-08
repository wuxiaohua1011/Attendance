package example.com.attendance;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingsActivity extends AppCompatActivity {
    private EditText editTextKey, editTextWorksheetName;
    private ToggleButton toggleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        editTextKey = (EditText)findViewById(R.id.setting_activity_editText_spreadsheet_key);
        editTextWorksheetName = (EditText)findViewById(R.id.setting_activity_editText_name_of_the_desired_worksheet);
        toggleButton = (ToggleButton)findViewById(R.id.activity_setting_toggleButton_setting);


    }

    @Override
    public void onBackPressed() {
        final String key = editTextKey.getText().toString();
        final String name = editTextWorksheetName.getText().toString();
        if (toggleButton.isChecked()){
            if (!(key.equals("")||name.equals(""))){
                Toast.makeText(this, "Please check you entered all values", Toast.LENGTH_SHORT).show();
            }
            else{
                new AlertDialog.Builder(this)
                        .setTitle("Really Exit?")
                        .setMessage("Are you sure you are done and want to exit?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SharedPreferences sharedPreference = getPreferences(MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreference.edit();
                                editor.putString("key", key);
                                editor.putString("name",name);
                                editor.commit();
                                SettingsActivity.super.onBackPressed();
                            }
                        }).create().show();
            }
        }
        else{
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you are done and want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            SettingsActivity.super.onBackPressed();
                        }
                    }).create().show();
        }
    }
}
