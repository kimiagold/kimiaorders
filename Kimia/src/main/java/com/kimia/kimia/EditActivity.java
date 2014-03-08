package com.kimia.kimia;

import android.app.Activity;
import android.database.Cursor;
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


/**
 * Created by behdad on 2/10/14.
 */

public class EditActivity extends Activity implements CompoundButton.OnCheckedChangeListener {


    EditText name;
    EditText tel;
    EditText mob;
    EditText fax;
    EditText cod;
    EditText tip;
    Switch pre;
    Switch visi;
    boolean pr;
    boolean vi;
    int ID;
    int groupI;

    ArrayList<String> group =new ArrayList<String>();

    Spinner groupS;

  /*  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container ,Bundle savedInstanceState){


   /*     Bundle arguments = getArguments();
        if (arguments != null)
        {
            int idd;
            idd = arguments.getInt("IDD");
            // then you have arguments

            ID = idd;
            //   ID=10;
        }
        //  Bundle bundle = this.getArguments();
        //if(getArguments()!=null){
        //ID = bundle.getInt("ID", 20);

        group.add(getString(R.string.customer));
        group.add(getString(R.string.maker));

//        Bundle extras = getActivity().getIntent().getExtras();
        //      if (extras != null) {
        //        ID = extras.getInt("ID");
        //  }
        return inflater.inflate(R.layout.activity_usershow, container, false);
    }*/


   /* void setMessage(String msg){
        ID = Integer.parseInt(msg);
        onStart();
    }*/

    @Override
    //   public void onStart(){

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ID = Integer.parseInt(extras.getString("ID"));
        }
        //     super.onStart();
        //   getActivity().setContentView(R.layout.activity_usershow);

        name = (EditText) findViewById(R.id.editname);
        tel = (EditText) findViewById(R.id.edittel);
        mob = (EditText) findViewById(R.id.editmob);
        fax = (EditText) findViewById(R.id.editfax);
        cod = (EditText) findViewById(R.id.editcod);
        tip = (EditText) findViewById(R.id.edittip);
        visi = (Switch) findViewById(R.id.switchevis);
        pre = (Switch) findViewById(R.id.switchpre);



        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c=db.getAc(ID);
        if(c != null && c.moveToFirst()){


            groupS =(Spinner)findViewById(R.id.spinnergroup);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, group);
            groupS.setAdapter(adapter);
            groupS.setSelection(Integer.parseInt(c.getString(2).toString()));

            name.setText(c.getString(1));
            tel.setText(c.getString(4));
            mob.setText(c.getString(5));
            fax.setText(c.getString(6));
            cod.setText(c.getString(3));
            tip.setText(c.getString(7));

            if(c.getString(8).equals( "1" ))
                pre.setChecked(true);

            if(c.getString(9).equals("1"))
                visi.setChecked(true);




            groupS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    groupI = arg0.getSelectedItemPosition();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });



            pre.setOnCheckedChangeListener(this);
            visi.setOnCheckedChangeListener(this);


        }
        db.close();

    }



    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int temp=buttonView.getId();

        switch (temp){

            case R.id.switchpre:

                if(isChecked) {
                    pr = true;
                }
                else {
                    pr = false;
                }

                break;

            case R.id.switchevis:

                if(isChecked) {
                    vi = true;
                }
                else {
                    vi = false;
                }

                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    //NEW
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_seveE:
                name = (EditText) findViewById(R.id.editname);
                tel = (EditText) findViewById(R.id.edittel);
                mob = (EditText) findViewById(R.id.editmob);
                fax = (EditText) findViewById(R.id.editfax);
                cod = (EditText) findViewById(R.id.editcod);
                tip = (EditText) findViewById(R.id.edittip);

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
                long id = db.updateContact(ID , groupI, co, na, te, mo, fa, pr, ti, vi);
                db.close();

                if (id==1){
                    Toast.makeText(this, getString(R.string.registred),
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                    Toast.makeText(this,getString(R.string.error),
                            Toast.LENGTH_SHORT).show();

                break;

            case R.id.action_cancelE:

                finish();
                break;


            default:
                break;
        }

        return true;
    }

    public void save(View View){

        name = (EditText) findViewById(R.id.editname);
        tel = (EditText) findViewById(R.id.edittel);
        mob = (EditText) findViewById(R.id.editmob);
        fax = (EditText) findViewById(R.id.editfax);
        cod = (EditText) findViewById(R.id.editcod);
        tip = (EditText) findViewById(R.id.edittip);

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
        long id = db.updateContact(ID , groupI, co, na, te, mo, fa, pr, ti, vi);
        db.close();

        if (id==1){
            Toast.makeText(this, getString(R.string.registred),
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        else
            Toast.makeText(this,getString(R.string.error),
                    Toast.LENGTH_SHORT).show();


    }

    public void delete(View View){
        DBAdapter db = new DBAdapter(this);
        db.open();
        boolean c=db.deleteAc(ID);

        if (c==true){
            Toast.makeText(this,getString(R.string.deleted),
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        else
            Toast.makeText(this,getString(R.string.error),
                    Toast.LENGTH_SHORT).show();



        db.close();
        finish();
    }

    public void cancel(View View){
        finish();
    }
}