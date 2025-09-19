package com.example.listycity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> dataList;
    ArrayAdapter<String> cityAdapter;
    ListView listView;
    String[] cities = {"Edmonton, AB", "Calgary, AB"};
    String selectedCity = null; // select city to remove by clicking on it which sets the value for it

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this creates the buttons and list UI from the xml file so they correlate to
        //the logic being created for them
        listView = findViewById(R.id.city_list);
        Button addButton = findViewById(R.id.addButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button editButton = findViewById(R.id.edit_Button);

        //the container of type ArrayList holding a string array of cities
        dataList = new ArrayList<>();
        dataList.addAll(Arrays.asList(cities));
        // this initializes the view for the cities so the data can be edited through the listyView
        // UI
        cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(cityAdapter);


        // uses a listener method to get the location of a click on the screen so the correct
        // item can be selected.
        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedCity = dataList.get(position);
            //Toast used to make little pop up messages instead of Intent creating a new screen
            Toast.makeText(this, "Selected: " + selectedCity, Toast.LENGTH_SHORT).show();
        });

        // defines the logic for adding a city using toast for a pop up input aas well as

        addButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add City w/ Province");


            final android.view.View dialogView = getLayoutInflater().inflate(R.layout.dialog_city_input, null);
            builder.setView(dialogView);
            //needed to make a custom xml file for the inputs so they can be done back to back
            final EditText inputCity = dialogView.findViewById(R.id.input_city);
            final EditText inputProvince = dialogView.findViewById(R.id.input_province);

            builder.setPositiveButton("CONFIRM", (dialog, which) -> {
                String newCity = inputCity.getText().toString().trim();
                String newProvince = inputProvince.getText().toString().trim();

                if (!newCity.isEmpty() && !newProvince.isEmpty()) {
                    dataList.add(newCity + ", " + newProvince);
                    cityAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
            builder.show();
        });

        editButton.setOnClickListener(v -> {
            
            if (selectedCity == null) {
                Toast.makeText(this, "No city selected", Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Edit City");

            // uses custom text layout to provide inputs for two separate strings 
            final android.view.View dialogView = getLayoutInflater().inflate(R.layout.dialog_city_input, null);
            builder.setView(dialogView);

            final EditText inputCity = dialogView.findViewById(R.id.input_city);
            final EditText inputProvince = dialogView.findViewById(R.id.input_province);

            // shows current selected city and provine in text input 
            String[] parts = selectedCity.split(", ");
            if (parts.length == 2) {
                inputCity.setText(parts[0]);
                inputProvince.setText(parts[1]);
            }

            builder.setPositiveButton("CONFIRM", (dialog, which) -> {
                String newCity = inputCity.getText().toString().trim();
                String newProvince = inputProvince.getText().toString().trim();
                //edits the selected city and province 
                if (!newCity.isEmpty() && !newProvince.isEmpty()) {
                    int index = dataList.indexOf(selectedCity);
                    dataList.set(index, newCity + ", " + newProvince);
                    cityAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Edited: " + selectedCity, Toast.LENGTH_SHORT).show();
                    selectedCity = null; // reset selection
                } else {
                    Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
            builder.show();
        });


        // creates the logic for the delete functionality using the selected city from
        // the listView listener method to delete said city from the list.
        deleteButton.setOnClickListener(v -> {
            if (selectedCity != null) {
                dataList.remove(selectedCity);
                cityAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Deleted: " + selectedCity, Toast.LENGTH_SHORT).show();
                selectedCity = null; // reset selection
            } else {
                Toast.makeText(this, "No city selected", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
