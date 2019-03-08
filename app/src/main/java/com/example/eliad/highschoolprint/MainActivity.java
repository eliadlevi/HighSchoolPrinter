package com.example.eliad.highschoolprint;

import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.ValueEventListener;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements ValueEventListener {


    ListView listViewUsers;
    DatabaseReference databaseUsers;

    String t = "";
    String t2 = "";

    private DatabaseReference myRef;
    private FirebaseDatabase database;

    //String [] foods;
    boolean vis = false;

    ArrayList<String> foods = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    AlertDialog.Builder adb;
    String tdb;//text data base
    ArrayList<String> alist = new ArrayList<String>();// keep all the names thet un the data base



    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference mRootRef = db.getReference("req");
    private DatabaseReference mdatabase;
    private DatabaseReference mStringRef = mRootRef.push();

    RadioButton reject;
    RadioButton standby;
    RadioButton approv;
    Button button;
    TextView tv1;//name
    TextView tv2;//reqwast


    String gm = "@gmail.com";
    String rdState = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListAdapter usersAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, foods);
        ListView usersListView = (ListView) findViewById(R.id.listView);
        usersListView.setAdapter(usersAdapter);

        mdatabase = FirebaseDatabase.getInstance().getReference();

        button = (Button) findViewById(R.id.button);
        tv1 = (TextView) findViewById(R.id.name);
        tv2 = (TextView) findViewById(R.id.reqState);
        reject = (RadioButton) findViewById(R.id.reject);
        standby = (RadioButton) findViewById(R.id.standby);
        approv = (RadioButton) findViewById(R.id.approv);
        reject.setVisibility(View.GONE);
        standby.setVisibility(View.GONE);
        approv.setVisibility(View.GONE);
        button.setVisibility(View.GONE);


        usersListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        vis = true;
                        final String food = valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(getApplicationContext(), food, Toast.LENGTH_LONG).show();

                        button.setVisibility(View.VISIBLE);
                        reject.setVisibility(View.VISIBLE);
                        standby.setVisibility(View.VISIBLE);
                        approv.setVisibility(View.VISIBLE);
                        tv1.setText(food);

                        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                tv2.setText(dataSnapshot.child(delTen(food)).getValue(String.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
        );
    }

//    public void enterdb(View view) {
//        EditText edb = (EditText) findViewById(R.id.enterdb);
//        tdb = edb.getText().toString();
//
//        adb = new AlertDialog.Builder(this);
//        if (tdb.equals(null)||tdb.equals(""))
//        {
//            AlertDialog ab = adb.create();
//            adb.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            });
//            adb.show();
//            adb.setMessage("data did not inserted");
//            return;
///*            ListView lv = (ListView) findViewById(R.id.listView);
//            alist.add(tdb);
//            alist.add("shon");
//            alist.add("eliad the gay");
//
//            ArrayAdapter<String> test = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alist);
//            lv.setAdapter(test);
//            */
//
//        }
//        else
//        {
//            mStringRef = mRootRef.push();
//            mStringRef.setValue(tdb);
//        }
//
//    }

    public void updateList() {
        ListView lv = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> test = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alist);
        lv.setAdapter(test);
    }

    public void next(View view) {
        Intent t = new Intent(MainActivity.this, Main2Activity.class);
        t.putExtra("alist", alist);
        startActivity(t);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        if (dataSnapshot.getChildren() != null) {
            alist.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                String key = snapshot.getKey();
                alist.add(key + gm);
                // alist.add(snapshot.getValue(String.class));
            }
            updateList();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mRootRef.addValueEventListener(this);
    }

    public String delTen(String a) {
        int i = a.length();
        a = a.substring(0, i - 10);
        Toast.makeText(getApplicationContext(), a, Toast.LENGTH_LONG).show();
        return (a);

    }

    public void finish(View view)
    {
        adb = new AlertDialog.Builder(this);
        AlertDialog ab;
        if(vis == true)
        {
            if (rdState.isEmpty())
            {
                Toast.makeText(getApplicationContext(), "you did not chose a state", Toast.LENGTH_LONG).show();

            }else {

                t = valueOf(tv1.getText());
                t2 = valueOf(tv2.getText());

                if(rdState.equals("reject"))
                {
                    ab = adb.create();
                    adb.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                            mdatabase.child("req").child(delTen(t)).removeValue();

                            /////////////remove the user ///////////



                        }
                    });
                    adb.setMessage("you choosed to reject this user");
                    adb.show();
                    return;

                }else if(rdState.equals("approv"))
                {
                    ab = adb.create();
                    adb.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            mdatabase.child("users").child(delTen(t)).setValue(t2);
                            mdatabase.child("req").child(delTen(t)).removeValue();

                        }
                    });
                    adb.setMessage("you choosed to approv this user");
                    adb.show();

                    return;

                }else if(rdState.equals("standby"))
                {

                    ab = adb.create();
                    adb.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
                    adb.setMessage("you choosed to put this user reqwast on stand by ");
                    adb.show();
                    return;

                }
            }

        }else {
            Toast.makeText(getApplicationContext(), "you did not chose a user", Toast.LENGTH_LONG).show();
        }

    }

    public void RadioChanger(View view)
    {
        if (reject.isChecked())
        {
            rdState ="reject";
        } else {
            if (standby.isChecked())
            {
                rdState ="standby";
            } else {
                if (approv.isChecked())
                {
                    rdState ="approv";
                }

            }
        }
    }


    public void backToMain(View view)
    {
        startActivity(new Intent(MainActivity.this, Main4Activity.class));

    }
}
