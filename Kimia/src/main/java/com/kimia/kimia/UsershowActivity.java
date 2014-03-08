package com.kimia.kimia;

/**
 * Created by behdad on 2/10/14.
 */

import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.*;

import java.util.ArrayList;

/**
 * Created by behdad on 1/23/14.
 */
public class UsershowActivity extends Fragment implements CompoundButton.OnCheckedChangeListener {


    TextView name;
    TextView tel;
    EditText mob;
    TextView fax;
    TextView cod;
    TextView tip;
    Switch pre;
    Switch visi;
    boolean pr;
    boolean vi;
    int ID;
    int groupI;
    String ddd;

    TextView id;

    ArrayList<String> group =new ArrayList<String>();

    Spinner groupS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container ,Bundle savedInstanceState){

        // id = (TextView) getActivity().findViewById(R.id.ID);


//ID = 24;
        //  ddd = id.getText().toString();

  /*

        Bundle arguments = getArguments();
        if (arguments != null)
        {
int idd;
             idd = arguments.getInt("IDD");
            // then you have arguments

            ID = idd;
         //   ID=10;
        }*/
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
    }

    void setMessage(){
        //   getActivity().setContentView(R.layout.activity_usershow);

        name = (TextView) getActivity().findViewById(R.id.editname);
        tel = (TextView) getActivity().findViewById(R.id.edittel);
        mob = (EditText) getActivity().findViewById(R.id.editmob);
        fax = (TextView) getActivity().findViewById(R.id.editfax);
        cod = (TextView) getActivity().findViewById(R.id.editcod);
        tip = (TextView) getActivity().findViewById(R.id.edittip);
        visi = (Switch) getActivity().findViewById(R.id.switchevis);
        pre = (Switch) getActivity().findViewById(R.id.switchpre);


        id = (TextView) getActivity().findViewById(R.id.ID);


        //   ID = 24;
        try{
        ID = Integer.parseInt(id.getText().toString());
        }
        catch(NumberFormatException e)
        {
           ID = 0;
        }


        DBAdapter db = new DBAdapter(getActivity());
        db.open();
        Cursor c=db.getAc(ID);
        if(c != null && c.moveToFirst()){


            groupS =(Spinner)getActivity().findViewById(R.id.spinnergroup);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, group);
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

    @Override
    public void onStart(){


        super.onStart();


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

  /*  public void save(View View){

        name = (EditText) getActivity().findViewById(R.id.editname);
        tel = (EditText) getActivity().findViewById(R.id.edittel);
        mob = (EditText) getActivity().findViewById(R.id.editmob);
        fax = (EditText) getActivity().findViewById(R.id.editfax);
        cod = (EditText) getActivity().findViewById(R.id.editcod);
        tip = (EditText) getActivity().findViewById(R.id.edittip);

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

        DBAdapter db = new DBAdapter(getActivity());
        db.open();
       long id = db.updateContact(ID , groupI, co, na, te, mo, fa, pr, ti, vi);
        db.close();

        if (id==1){
            Toast.makeText(getActivity(),getString(R.string.registred),
                    Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        else
            Toast.makeText(getActivity(),getString(R.string.error),
                    Toast.LENGTH_SHORT).show();


    }

    public void delete(View View){
        DBAdapter db = new DBAdapter(getActivity());
        db.open();
        boolean c=db.deleteAc(ID);

        if (c==true){
            Toast.makeText(getActivity(),getString(R.string.deleted),
                    Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        else
            Toast.makeText(getActivity(),getString(R.string.error),
                    Toast.LENGTH_SHORT).show();



    db.close();
        getActivity().finish();
    }*/

    public void cancel(View View){
        getActivity().finish();
    }
}
