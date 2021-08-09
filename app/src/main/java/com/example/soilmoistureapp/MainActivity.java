package com.example.soilmoistureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ekn.gruzer.gaugelibrary.ArcGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.graphics.Color.BLACK;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    ArcGauge arcGauge;
    ListView listView;
    ArrayList<String> arrayListV;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    ArrayList<String> arrayListData = new ArrayList<String>(10);
    double data = 0.0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("currentValue");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        arcGauge = (ArcGauge) findViewById(R.id.sm);
        listView = (ListView) findViewById(R.id.listview);

        textView.setText("Độ ẩm trong đất");
        displayGauge();
        addListHistory();
        final ArrayList<String> arrayList1 = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList1.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    arrayList1.add(dataSnapshot.getValue().toString());
                }
                if(arrayListData.size()==10){
                    arrayListData.remove(0);}
                arrayListData.add("Thời gian "+sdf.format(new Date())+" - Độ ẩm: "+ arrayList1.get(0)+" %");
                addListHistory();
                data = (double) Double.parseDouble(arrayList1.get(0));
                displayGauge();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Bo","error");
            }
        });
    }
    void displayGauge(){
        Range range = new Range();
        Range range1 = new Range();
        Range range2 = new Range();

        range.setColor(Color.parseColor("#E3E500"));
        range.setFrom(0.0);
        range.setTo(50.0);

        range1.setColor(Color.parseColor("#00b20b"));
        range1.setFrom(50.0);
        range1.setTo(100.0);

        range2.setColor(Color.parseColor("#ce0000"));
        range2.setFrom(100.0);
        range2.setTo(150.0);

        arcGauge.addRange(range);
        arcGauge.addRange(range1);
        arcGauge.addRange(range2);
        arcGauge.setUseRangeBGColor(true);
        arcGauge.setValueColor(BLACK);

        arcGauge.setMinValue(0.0);
        arcGauge.setMaxValue(150.0);
        arcGauge.setValue(data);

    }
    private void addListHistory(){
        Log.e("bo","test");
        arrayListV = new ArrayList<>();
        for(int i =0; i<arrayListData.size();i++){
            arrayListV.add(arrayListData.get(i));
        }

//        histories.add("bo");
        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this,
                android.R.layout.simple_list_item_1, arrayListV);
        listView.setAdapter(arrayAdapter);
    }
}