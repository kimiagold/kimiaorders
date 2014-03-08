package com.kimia.kimia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

       /* forceRTLIfSupported();
        super.onCreate(savedInstanceState);
        String languageToLoad  = "fa";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
*/
        setContentView(R.layout.activity_main);
    }

    public void AccountsClick(View view){
        startActivity(new Intent("com.kimia.kimia.AccuntActivity"));
    }


    public void ProductsClick(View view){
        startActivity(new Intent("com.kimia.kimia.ProductsActivity"));
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
*/
}