package com.example.eliad.highschoolprint;


import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;


public class Main3Activity extends AppCompatActivity {
    ArrayList<String> alist = new ArrayList<String>();// keep all the names thet un the data base
    String Stxt;// string to convert to txt file
    String filename;
    String str;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
    }

    public void next3(View view) {

        Intent t = new Intent(this, Main2Activity.class);
        startActivity(t);
    }

    public void back2(View view) {
        Intent t = new Intent(this, Main2Activity.class);
        startActivity(t);
    }

    public void enter_storge(View view) throws IOException {
        String filename = UUID.randomUUID().toString() + ".txt";
        EditText edtxt = (EditText) findViewById(R.id.etStorge);
        Stxt = edtxt.getText().toString();
        FileOutputStream outputStream = null;
        File file = new File(filename);
        try {
            outputStream = openFileOutput(filename, MODE_PRIVATE);
            outputStream.write(Stxt.getBytes());
        } catch (IOException e) {
            System.out.println("worked!");
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FirebaseStorage storage;
        StorageReference storageReference;

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        String path="file:///data/data/com.example.eliad.highschoolprint/files/"+filename;
        filePath=Uri.parse(path);

        StorageReference ref = storageReference.child("files/"+ filename);
        ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Main3Activity.this, "Uploaded", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Main3Activity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                    }
                });;

    }

}
