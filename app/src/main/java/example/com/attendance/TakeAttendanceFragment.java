package example.com.attendance;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.Calendar;

/**
 * Created by micha on 1/5/2017.
 */

public class TakeAttendanceFragment extends Fragment {

    Spinner playerName,playerStatus;
    Button pickADateButton,postButton;
    private int year,month,day;
    private String playerNameString,statusString;
    private TextView playerNameTextView,statusTextView,dateTextView;
    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.take_attendance,container,false);
        wireWidget();
        addListener();
        return myView;
    }

    private void wireWidget() {

        //wire spinners and import designated string resources
        playerName = (Spinner)myView.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.player_names, android.R.layout.simple_spinner_item); // Creating adapter for spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //specify drop-down style
        playerName.setAdapter(adapter); //  attaching data adapter to spinner
        playerStatus=(Spinner)myView.findViewById(R.id.spinner2);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.status, android.R.layout.simple_spinner_item);// Creating adapter for spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//specify drop-down style
        playerStatus.setAdapter(adapter);//  attaching data adapter to spinner
        postButton=(Button)myView.findViewById(R.id.button_post);
        //wire String values
        playerNameTextView = (TextView)myView.findViewById(R.id.textView);
        statusTextView=(TextView)myView.findViewById(R.id.textView2);
        dateTextView = (TextView)myView.findViewById(R.id.textView3);

        pickADateButton = (Button)myView.findViewById(R.id.button_datePicker);
    }

    private void addListener() {
        playerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                playerNameString = parent.getItemAtPosition(position).toString();
                playerNameTextView.setText(playerNameString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        playerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusString=parent.getItemAtPosition(position).toString();
                statusTextView.setText(statusString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        pickADateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Process to get Current Date
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog fragment
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        dateTextView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, year,month,day);
                dpd.show();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        postData();

                    }
                    HttpRequest mReq = new HttpRequest();
                    private void postData() {
                        String fullUrl = "https://docs.google.com/forms/d/e/1FAIpQLSfO8UrwSla43P1EenzVLxMuHdFo9r6p-e6c5WT_CL-C_4R_Mg/formResponse";

                        String data = "entry.1298782818=" + URLEncoder.encode(playerNameString) + "&" +
                                "entry.1176356327_year=" + URLEncoder.encode(""+year)+ "&" +
                                "entry.1176356327_month=" + URLEncoder.encode(""+(month+1)) + "&" +
                                "entry.1176356327_day=" + URLEncoder.encode(""+day) + "&" +
                                "entry.1532322145=" + URLEncoder.encode(changeStatusString());

                        mReq.sendPost(fullUrl, data);
                    }
                });
                t.start();
                Toast.makeText(getActivity(), "Data Posted", Toast.LENGTH_SHORT).show();
                clear();
            }
        });
    }

    private void clear() {
        playerNameString="";
        playerNameTextView.setText("");
        playerName.setPrompt("");

        statusString="";
        statusTextView.setText("");
        playerStatus.setPrompt("");

        dateTextView.setText("");
    }


    private String changeStatusString() {
        if (statusString.equals("Present")){statusString = "p";}
        if (statusString.equals("Absent")){statusString = "e";}
        if (statusString.equals("Excused")){statusString = "a";}
        return statusString;
    }
}
