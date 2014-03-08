package com.kimia.kimia;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class UserActivity extends Fragment{

    ListView listac;
    Cursor cur;
    ListAdapter userlistAdapter;
    Context context;
    boolean isphone=false;
    TextView id;
    Bundle args = new Bundle();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_user,container,false);
    }

    AccuntActivity buttonListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            buttonListener = (AccuntActivity) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFileSelectedListener");
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        showlist();
        buttonListener.onButtonPressed();
    }

    public void showlist() {

        id = (TextView) getActivity().findViewById(R.id.ID);
        DBAdapter db = new DBAdapter(getActivity());
        context=getActivity();
        listac=(ListView)getActivity().findViewById(R.id.listViewU);

        db.open();
        super.onResume();
        cur = db.getALLacname();

        if(cur != null && cur.moveToFirst()){
            id.setText(cur.getString(0));
            userlistAdapter=new ListAdapter(getActivity(), cur);
            listac.setFastScrollAlwaysVisible(true);
            listac.setAdapter(userlistAdapter);
        }
        db.close();

        listac.setOnCreateContextMenuListener(this);
        listac.setOnItemClickListener(new OnItemClickListener(){

            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3){
                TextView textViewId=(TextView)v.findViewById(R.id.userid);
                String ID;
                ID=textViewId.getText().toString();
                id.setText(ID);
                buttonListener.onButtonPressed();
            }
        });
    }

    @Override
    public void onResume() {
        showlist();
        super.onResume();
    }

    @Override
    public void onPause() {
        //db.close();
        super.onPause();
    }
}