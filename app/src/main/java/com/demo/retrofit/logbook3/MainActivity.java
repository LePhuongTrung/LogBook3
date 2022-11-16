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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView img;
    TextInputLayout urlEnter;
    TextView numberPage;

    urlDatabase DB;
    ArrayList<String> url ;


    int i = 0,current = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlEnter = findViewById(R.id.textInputLayout);
        img = findViewById(R.id.imageView);
        numberPage = findViewById(R.id.textView);

        DB = new  urlDatabase(MainActivity.this);
        url = new ArrayList<>();

        storeDataInArrays();
        if (url.size() == 0){
            url.add("https://as2.ftcdn.net/v2/jpg/04/75/01/23/1000_F_475012363_aNqXx8CrsoTfJP5KCf1rERd6G50K0hXw.jpg");
            extracted();
        } else {
            extracted();
        }

    }

    //set img
    private void extracted() {
        numberPage.setText((i+1)+"/"+ url.size());
        Picasso.get()
//                .load("https://as2.ftcdn.net/v2/jpg/04/75/01/23/1000_F_475012363_aNqXx8CrsoTfJP5KCf1rERd6G50K0hXw.jpg")
                .load(url.get(i))
                .into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.i("numberPage:", String.valueOf(i));
                    }

                    @Override
                    public void onError(Exception e) {
                        DB.delete((i + 1));
                        storeDataInArrays();
                        i = current;
                        urlEnter.getEditText().setText("");
                        Toast.makeText(MainActivity.this, "Url not return Img", Toast.LENGTH_SHORT).show();
                        extracted();
                    }
                });
    }

    public void nextImg(View view){
        i++;
        if (i >=url.size())
            i = 0;
        current = i;
        extracted();
    }

    public void previousImg(View view){
        i--;
        if (i <0)
            i = url.size() -1;
        current = i;
        extracted();
    }
    public void addLink(View view){
        String urlStr = urlEnter.getEditText().getText().toString();
        DB.insertUrl(urlStr);
        storeDataInArrays();
        i = url.size() - 1;
        extracted();
    }


    void storeDataInArrays(){
        url.clear();
        Cursor cursor = DB.readAllData();
        if(cursor.getCount() == 0){
            numberPage.setText("No photos yet, please add a new one");
        }else{
            while (cursor.moveToNext()){
                url.add(cursor.getString(1));
            }
        }
    }
}