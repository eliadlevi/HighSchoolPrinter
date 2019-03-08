package com.example.eliad.highschoolprint;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main4Activity extends AppCompatActivity {

    String email = "";
    String stat = "the firsst value thet entered";

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference mRootRef = db.getReference("users");
    private DatabaseReference mRootRef2 = db.getReference("req");


    private Button mLogOutBtn;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    boolean pa = false;//print approval
    boolean ua = false;//user approval
    private GoogleApiClient mGoogleApiClient;

    TextView mainStat;
    TextView mainName;

    Button userAp;
    Button printAp;
    Button printReq;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        userAp = (Button) findViewById(R.id.userApproval);
        printAp = (Button) findViewById(R.id.printApproval);
        printReq = (Button) findViewById(R.id.printReq);
        mainStat = (TextView) findViewById(R.id.mainStat);
        mainName = (TextView) findViewById(R.id.mainName);
        mainStat.setText("");
        mainName.setText("");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {

                if(firebaseAuth.getCurrentUser() == null)
                {
                    startActivity(new Intent(Main4Activity.this, Main2Activity.class));
                }
            }
        };

        getEmail();
        mainName.setText(email);
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                stat = dataSnapshot.child(delTen(email)).getValue(String.class);
                if(stat== null)
                {
                    mRootRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            stat = dataSnapshot.child(delTen(email)).getValue(String.class);
                            if(stat== null)
                            {
                                mainStat.setText("you'r reqwast was terned down");
                            }
                            else
                            {
                                mainStat.setText("you'r reqwast still on standby");

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    if(stat.equals("norm"))
                    {
                        printReq.setVisibility(View.VISIBLE);
                        mainStat.setText("normal");
                    }
                    else if(stat.equals("spmg"))
                    {
                        mainStat.setText("super manager");
                        printReq.setVisibility(View.VISIBLE);
                        userAp.setVisibility(View.VISIBLE);
                        printAp.setVisibility(View.VISIBLE);
                    }
                    else if(stat.equals("manag"))
                    {
                        mainStat.setText("manager");
                        printReq.setVisibility(View.VISIBLE);
                        printAp.setVisibility(View.VISIBLE);
                    }
                    else if(stat.equals("printmg"))
                    {
                        mainStat.setText("print manager");
                        printReq.setVisibility(View.VISIBLE);
                        //still not sure in the difference between print manager to normal user
                    }
                    else
                    {
                        mainStat.setText("problem");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mLogOutBtn = (Button) findViewById(R.id.logout);

        mLogOutBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mAuth.signOut();

            }
        });



        if(stat.equals("norm"))
        {
            printReq.setVisibility(View.VISIBLE);
            mainStat.setText("normal");
        }
        else if(stat.equals("spmg"))
        {
            mainStat.setText("super manager");
            printReq.setVisibility(View.VISIBLE);
            userAp.setVisibility(View.VISIBLE);
            printAp.setVisibility(View.VISIBLE);
        }
        else if(stat.equals("manag"))
        {
            mainStat.setText("manager");
            printReq.setVisibility(View.VISIBLE);
            printAp.setVisibility(View.VISIBLE);
        }
        else if(stat.equals("printmg"))
        {
            mainStat.setText("print manager");
            printReq.setVisibility(View.VISIBLE);
            //still not sure in the difference between print manager to normal user
        }
        else
        {
            mainStat.setText("problem");
        }



    }

    @Override
    protected void onStart()
    {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    public void printApproval(View view)
    {
        //startActivity(new Intent(Main4Activity.this, Main2Activity.class));
        //still not exist
    }

    public void userApproval(View view)
    {
        startActivity(new Intent(Main4Activity.this, MainActivity.class));
    }


    public void  getEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
            email = user.getEmail();
        }
    }

    public String delTen(String a)
    {

        String[] parts = a.split("@");
        String part1 = parts[0]; // 004
        String part2 = parts[1];
//        int i=a.length();
//        a=a.substring(0 ,i-10);
        return (part1);

    }

}
