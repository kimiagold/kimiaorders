package com.kimia.kimia;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.logging.Logger;

public class ProductsListFragment extends Fragment {

    ListView listView;
    Cursor cursor;
    ListAdapter listAdapter;
    Context context;
    private TextView textSelectedProductID;
    private ArrayList<Long> itemID;
    DBAdapter db;
    int i=0;
    private int firstScroll;
    private int itemPosition;
    View c;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_product_list,container,false);
    }

    @Override
    public void onStart(){
        super.onStart();
        textSelectedProductID = (TextView) getActivity().findViewById(R.id.SelectedProductID);
        db = new DBAdapter(getActivity());
        context = getActivity();
        listView = (ListView) getActivity().findViewById(R.id.listView);

        db.open();
        super.onResume();
        cursor = db.getAllProductName();

        if(cursor != null && cursor.moveToFirst()){
            textSelectedProductID.setText(cursor.getString(0));
        } else {
            textSelectedProductID.setText("0");
            ((ProductsActivity)getActivity()).setFragmentWeightEdit(true);
        }

        showList();
    }

    public void showList() {

        db.open();
        super.onResume();
        cursor = db.getAllProductName();
        itemID = new ArrayList<Long>();
        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            itemID.add(Long.parseLong(cursor.getString(0)));
        }

        listAdapter = new ListAdapter(getActivity(), cursor);
       // listView.setFastScrollAlwaysVisible(true);
        listView.setAdapter(listAdapter);
        listView.setSmoothScrollbarEnabled(true);

        db.close();

        listView.setOnCreateContextMenuListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3){
                TextView textViewId=(TextView)v.findViewById(R.id.userid);
                String ID;
                ID=textViewId.getText().toString();
                textSelectedProductID.setText(ID);
                setSelect(position);
                firstScroll = listView.getFirstVisiblePosition();
                c = listView.getChildAt(1);
                ((ProductsActivity)getActivity()).setView(false);
            }
        });
    }

    public void setNextItem(){
        if (itemPosition == itemID.size()-1)
            itemPosition--;

        else if (itemPosition < itemID.size()-1)
            itemPosition = itemPosition;

        else itemPosition = 0;

        db.open();
        cursor = db.getAllProductName();
        cursor.moveToPosition(itemPosition);
        textSelectedProductID.setText(cursor.getString(0));
        db.close();
    }

    public void setSelect(int i) {
        listAdapter.setSelectedItem(i);
    }

    public int getItemPosition(long id) {
        for (int position=0; position<itemID.size(); position++)
            if (itemID.get(position) == id)
                return position;
        return 0;
    }

    public void setScroll(boolean scroll) {
        listAdapter.notifyDataSetChanged();
        String a = textSelectedProductID.getText().toString();
        long i = Long.parseLong(a);
        itemPosition = getItemPosition(i);
        setSelect(itemPosition);
        int scrollY = 0;

        if (!scroll) {
            int h = c != null ? c.getHeight() + 1 : 0;
            int t = c != null ? c.getTop() : 0;
            scrollY = (itemPosition - firstScroll-1)*h + t;
        }

        listView.setSelectionFromTop(itemPosition, scrollY);
    }

    @Override
    public void onResume() {
        //showList();
        super.onResume();
    }

    @Override
    public void onPause() {
        //db.close();
        super.onPause();
    }
}