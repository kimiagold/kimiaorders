package com.kimia.kimia;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by behdad on 2/10/14.
 */

public class AddActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    EditText name;
    EditText tel;
    EditText mob;
    EditText fax;
    EditText cod;
    EditText tip;
    boolean visi=true;
    boolean pre=false;
    int ID;
    int groupI;
    Switch s1;
    Switch s2;


    ArrayList<String> group =new ArrayList<String>();
    Spinner groupS;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        name = (EditText) findViewById(R.id.editname);
        tel = (EditText) findViewById(R.id.edittel);
        mob = (EditText) findViewById(R.id.editmob);
        fax = (EditText) findViewById(R.id.editfax);
        cod = (EditText) findViewById(R.id.editcod);
        tip = (EditText) findViewById(R.id.edittip);
        s1 = (Switch) findViewById(R.id.switchpre);
        s2 = (Switch) findViewById(R.id.switchevis);
        group.add(getString(R.string.customer));
        group.add(getString(R.string.maker));



        DBAdapter db = new DBAdapter(this);
        db.open();

        int m=db.MaxCod();
        int g=db.lastUser();

        groupS =(Spinner)findViewById(R.id.spinnergroup);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, group);
        groupS.setAdapter(adapter);
        groupS.setSelection(g);

        groupS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                groupI = arg0.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });



        cod.setText(Integer.toString(m+1));
        db.close();


        if (s1 != null) {
            s1.setOnCheckedChangeListener(this);
        }

        s2.setChecked(true);

        if (s2 != null) {
            s2.setOnCheckedChangeListener(this);
        }
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int temp=buttonView.getId();

        switch (temp){

            case R.id.switchpre:

                if(isChecked) {
                    pre = true;
                }
                else {
                    pre = false;
                }

                break;

            case R.id.switchevis:

                if(isChecked) {
                    visi = true;
                }
                else {
                    visi = false;
                }

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }








 //   private static final String ALLOWED_CHARACTERS ="ضصثقفغعهخحجچگکمنتالبیسشظطزرذدپو.qwertyuiopasdfghjklzxcvbnm";


   /* public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){

            randomStringBuilder.append(ALLOWED_CHARACTERS.charAt(generator.nextInt(ALLOWED_CHARACTERS.length())));
        }
        return randomStringBuilder.toString();
    }
*/



  //  private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

   /* private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder();
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }


*/




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_save:


                String na=name.getText().toString();
                String te=tel.getText().toString();
                String mo=mob.getText().toString();
                String fa=fax.getText().toString();
                String ti=tip.getText().toString();



                int co;

                try{
                    co=Integer.parseInt(cod.getText().toString());
                }
                catch (NumberFormatException nfe){
                    co=-1;
                }





                DBAdapter db = new DBAdapter(this);
                db.open();

                long id;



                if (na != null && !na.isEmpty()){

                    if (co!=-1){
                        id = db.insertAc(groupI,co , na,te,mo,fa,pre,ti,visi);
                        db.close();

                        if (id>0){
                            Toast.makeText(this,na+' '+ getString(R.string.registred),Toast.LENGTH_SHORT).show();
                          //  reset();
                        }

                        else if (id==-2)
                            Toast.makeText(this, getString(R.string.error_cod_duplicate), Toast.LENGTH_SHORT).show();

                        else
                            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();

                    }

                    else
                        Toast.makeText(this, getString(R.string.error_cod_number), Toast.LENGTH_SHORT).show();
                }

                else
                    Toast.makeText(this, getString(R.string.error_name), Toast.LENGTH_SHORT).show();





                break;
















            case R.id.action_cancel:
                finish();
                break;


            default:
                break;
        }

        return true;
    }

    public void save(View View){



        String na=name.getText().toString();
        String te=tel.getText().toString();
        String mo=mob.getText().toString();
        String fa=fax.getText().toString();
        String ti=tip.getText().toString();

        int co;

        try{
            co=Integer.parseInt(cod.getText().toString());
        }
        catch (NumberFormatException nfe){
            co=-1;
        }

        DBAdapter db = new DBAdapter(this);
        db.open();

        long id;

        if (na != null && !na.isEmpty()){

            if (co!=-1){
                id = db.insertAc(groupI,co,na,te,mo,fa,pre,ti,visi);
                db.close();

                if (id>0){
                    Toast.makeText(this,na+' '+ getString(R.string.registred),Toast.LENGTH_SHORT).show();
                    reset();
                }

                else if (id==-2)
                    Toast.makeText(this, getString(R.string.error_cod_duplicate), Toast.LENGTH_SHORT).show();

                else
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();

            }

            else
                Toast.makeText(this, getString(R.string.error_cod_number), Toast.LENGTH_SHORT).show();
        }

        else
            Toast.makeText(this, getString(R.string.error_name), Toast.LENGTH_SHORT).show();
    }




    public void reset(){
        name.setText("");
        tel.setText("");
        mob.setText("");
        fax.setText("");
        tip.setText("");
        s1.setChecked(false);
        s2.setChecked(true);
        visi=true;
        pre=false;

        DBAdapter db = new DBAdapter(this);
        db.open();
        int m=db.MaxCod();
        cod.setText(Integer.toString(m+1));
        db.close();

    }

    public void cancel(View View){
        finish();
    }
}