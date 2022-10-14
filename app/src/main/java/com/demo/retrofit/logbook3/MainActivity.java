package com.demo.retrofit.logbook3;


import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView img;
    TextInputLayout urlEnter;
    TextInputEditText urlEnterEdit;

    urlDatabase DB;
    ArrayList<String> link ;

    CustomAdapter customAdapter;
    int i = 0,check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlEnter = findViewById(R.id.textInputLayout);

        img = findViewById(R.id.imageView);


        DB = new  urlDatabase(MainActivity.this);
        link = new ArrayList<>();

        storeDataInArrays();
        check = link.size();
        extracted();

    }

    //set img
    private void extracted() {
        Glide.with(MainActivity.this)
                .load(link.get(i))
                .centerCrop()
                .into(img);
    }

    public void nextImg(View view){
        i++;
        if (i >=link.size())
            i = 0;
        extracted();
    }

    public void previousImg(View view){
        i--;
        if (i <0)
            i = link.size() -1;
        extracted();
    }
    public void addLink(View view){
        String urlStr = urlEnter.getEditText().getText().toString();

        if (isValidURL(urlStr) == true){
            DB.insertUrl(urlStr);
            storeDataInArrays();
            i = link.size() -1;
            extracted();
        } else {
            Toast.makeText(this, "Url is not vail Please enter again", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isValidURL(String urlStr) {
        try{
            new URL(urlStr).toURI();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }

    void storeDataInArrays(){
        Cursor cursor = DB.readAllData();
        if(cursor.getCount() == 0){
            urlEnterEdit = findViewById(R.id.urlEnter);
            urlEnterEdit.setText("No photos yet, please add a new one");
        }else{
            while (cursor.moveToNext()){
                link.add(cursor.getString(1));
            }
        }
    }
}