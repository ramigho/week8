package com.example.week8;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> viewArray = new ArrayList<String>();
    ArrayList<String> bottleArray = new ArrayList<String>();
    ArrayList<String> sizeArray = new ArrayList<String>();

    BottleDispenser dispenser = BottleDispenser.getInstance();
    ListView listview;
    Button addMoney;
    Button returnMoney;
    Button purchase;
    Button receipt;
    TextView activity;
    //EditText input;
    ArrayAdapter<String> listviewAA;
    ArrayAdapter<String> bottleNameAA;
    ArrayAdapter<String> bottleSizeAA;
    SeekBar money_bar;
    Spinner bottleSpinner;
    Spinner sizeSpinner;
    Context context = null;
    int progress_value;
    int position_value;
    int size_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Elements on screen */
        listview = (ListView) findViewById(R.id.listview);
        addMoney = (Button) findViewById(R.id.addMoney);
        returnMoney = (Button) findViewById(R.id.returnMoney);
        purchase = (Button) findViewById(R.id.purchase);
        receipt = (Button) findViewById(R.id.receipt);
        activity = (TextView) findViewById(R.id.activity);
        //input = (EditText) findViewById(R.id.input);
        money_bar = (SeekBar) findViewById(R.id.money_bar);
        bottleSpinner = (Spinner) findViewById(R.id.bottleSpinner);
        sizeSpinner = (Spinner) findViewById(R.id.sizeSpinner);
        context = MainActivity.this;

        /* ArrayAdapters */
        listviewAA = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, viewArray);
        listview.setAdapter(listviewAA);

        bottleNameAA = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bottleArray);
        bottleNameAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bottleSizeAA = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sizeArray);
        bottleSizeAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        /* Initialize arrays, listviews and spinners */
        for (int i=0; i<dispenser.bottleList.size(); i++) {
            viewArray.add(i + 1 + ". " + dispenser.bottleList.get(i).getName() + "\n"+"\t"+"\tPrice: " + dispenser.bottleList.get(i).getPrize());
            activity.setText("\n"+defaultLine());
        }
        // Array for two spinners.
        bottleArray.add("Pepsi Max");
        bottleArray.add("Coca-Cola Zero");
        bottleArray.add("Fanta Zero");

        sizeArray.add("0.5");
        sizeArray.add("1.5");

        seekbar();

        bottlespinner();
        sizespinner();
    }

    public void bottlespinner(){
        bottleSpinner.setAdapter(bottleNameAA);

        bottleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position_value = position;
                String bottle = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + bottle, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void sizespinner(){
        sizeSpinner.setAdapter(bottleSizeAA);

        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                size_value = position;
                String size = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(),"Selected: "+size, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void seekbar(){
        money_bar.setProgress(0);
        money_bar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    String chosen;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;

                        if (progress_value == 1) {
                            chosen = "0.05€";
                        } else if (progress_value == 2) {
                            chosen = "0.10€";
                        } else if (progress_value == 3) {
                            chosen = "0.20€";
                        } else if (progress_value == 4) {
                            chosen = "0.50€";
                        } else if (progress_value == 5) {
                            chosen = "1.00€";
                        } else if (progress_value == 6){
                            chosen = "2.00€";
                        }
                        activity.setText("\nAdd " + chosen + " to dispenser. Press 'ADD MONEY'.");

                        if (progress_value == 0){
                            activity.setText("\n"+defaultLine());
                        }

                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // Nothing
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // Nothing
                    }
                }
        );
    }

    public void returnMoney(View v){
        if (dispenser.money == 0) {
            activity.setText("Klink klink!! All money gone!");
        } else {
            String change = String.format("%.02f", dispenser.money);
            activity.setText("Klink klink. Money came out! You got "+change+"€ back");
            dispenser.emptyMoney();
        }
    }

    public void buyBottle(View v) {
        int index;
        Boolean enoughMoney;
        int correction = 0;

        index = dispenser.getAvailability(position_value, size_value);
        if (index == -1){
            activity.setText("\nBottle not found!");
        } else {
            enoughMoney = dispenser.checkMoney(index);
            if (enoughMoney == false){
                activity.setText("\nAdd money first!");
            } else {

                if (position_value == 0 & size_value == 0){
                    System.out.println(dispenser.bottleList.get(0).getName());
                    activity.setText("KACHUNK! " + dispenser.bottleList.get(0).getName() + " came out of the dispenser!\n\n" + defaultLine());
                } else if (position_value == 0 & size_value == 1){
                    while (true) {
                        try {
                            System.out.println(dispenser.bottleList.get(1 - correction).getName());
                            activity.setText("KACHUNK! " + dispenser.bottleList.get(1 - correction).getName() + " came out of the dispenser!\n\n" + defaultLine());
                            break;
                        } catch (IndexOutOfBoundsException ex) {
                            correction++;
                            continue;
                        }
                    }
                } else if (position_value == 1 & size_value == 0){

                    while (true) {
                        try {
                            System.out.println(dispenser.bottleList.get(2 - correction).getName());
                            activity.setText("KACHUNK! " + dispenser.bottleList.get(2 - correction).getName() + " came out of the dispenser!\n\n" + defaultLine());
                            break;
                        } catch (IndexOutOfBoundsException ex) {
                            correction++;
                            continue;
                        }
                    }
                } else if (position_value == 1 & size_value == 1){
                    while (true) {
                        try {
                            System.out.println(dispenser.bottleList.get(3 - correction).getName());
                            activity.setText("KACHUNK! " + dispenser.bottleList.get(3 - correction).getName() + " came out of the dispenser!\n\n" + defaultLine());
                            break;
                        } catch (IndexOutOfBoundsException ex) {
                            correction++;
                            continue;
                        }
                    }
                } else if (position_value == 2){
                    while (true) {
                        try {
                            System.out.println(dispenser.bottleList.get(4 - correction).getName());
                            activity.setText("KACHUNK! " + dispenser.bottleList.get(4 - correction).getName() + " came out of the dispenser!\n\n" + defaultLine());
                            break;
                        } catch (IndexOutOfBoundsException ex) {
                            correction++;
                            continue;
                        }
                    }
                }
                dispenser.purchase(index);
                setArray();
            }
        }
    }

    public void addMoney(View v) {
        double moneyAdded = 0;

        if (progress_value == 0){
            // Tulosta, että rahaa ei laitettu.
        }
        if (progress_value == 1) {
            moneyAdded = 0.05;
        } else if (progress_value == 2) {
            moneyAdded = 0.10;
        } else if (progress_value == 3) {
            moneyAdded = 0.20;
        } else if (progress_value == 4) {
            moneyAdded = 0.5;
        } else if (progress_value == 5) {
            moneyAdded = 1.00;
        } else if (progress_value == 6){
            moneyAdded = 2.00;
        }

        dispenser.addMoney(moneyAdded);
        money_bar.setProgress(0);
        activity.setText("\nKlink! Added "+moneyAdded+" to dispenser!\nMoney: "+dispenser.money);

    }

    public void setArray(){
        int i;
        viewArray.clear();
        for (i=0; i<dispenser.bottleList.size(); i++){
            viewArray.add(i + 1 + ". " + dispenser.bottleList.get(i).getName() + "\n"+"\t"+"\tPrice: " + dispenser.bottleList.get(i).getPrize());
            listviewAA.notifyDataSetChanged();
        }
    }

    public String defaultLine(){
        return ("Move the slider to select the amount of money.\n\nSelect bottle 1-"+dispenser.bottleList.size());
    }

    public void writeReceipt(View v){
        String filename = "receipt.txt";

        String text = "BOTTLEDISPENSER RECEIPT\n\n"+dispenser.getLastBottle()+"\t\t\t\t"+dispenser.getLastSize()+"\nCost: "+dispenser.getLastPrice()+"\n\nThank you and enjoy your day!";

        try {
            OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput(filename, context.MODE_PRIVATE));
            writer.write(text);
            writer.close();

        } catch (IOException e){
            Log.e("IOException:", "Check your input." );

        } finally {
            activity.setText("\nReceipt printed.");

        }

    }
}
