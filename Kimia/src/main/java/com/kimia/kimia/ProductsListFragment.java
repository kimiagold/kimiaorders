package com.kimia.kimia;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class ProductsListFragment extends Fragment {

    ListView listView;
    Cursor cursor;
    ListAdapter listAdapter;
    Context context;
    private ArrayList<Long> itemID;
    DBAdapter db;
    public boolean selectSearch = true;
    private int firstScroll;
    private int itemPosition;
    private ProductsActivity productsActivity;
    boolean search;
    String filter;
    View c;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_list,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        db = new DBAdapter(getActivity());
        context = getActivity();
        listView = (ListView) getActivity().findViewById(R.id.listView);
        productsActivity = (ProductsActivity)getActivity();

        db.open();
        super.onResume();
        cursor = db.getAllProductName();

        if(cursor != null && cursor.moveToFirst()) {
            productsActivity.setSelectedProductID(Long.parseLong(cursor.getString(0)));
        } else {
            productsActivity.setSelectedProductID(0);
            //productsActivity.setFragmentWeightEdit(true);
            //productsActivity.actionbarSetEdit();
            productsActivity.setAdd(true);
        }
        showList(false, "");
        //productsActivity.setView(false);
    }

    public void showList(boolean sea, String fil) {
        super.onResume();
        filter = fil;
        search = sea;
        db.open();

        if (search) {
            cursor = db.getSearchProductName(filter);

            if(cursor != null && cursor.moveToFirst()) {
                if (selectSearch) {
                    productsActivity.setSelectedProductID(Long.parseLong(cursor.getString(0)));
                //    selectSearch = false;
                }
                selectSearch = true;

                productsActivity.setSearch(false, true, false);
                productsActivity.setViewFragmentVisible(true);
            } else {
                productsActivity.setViewFragmentVisible(false);
            }
        }
        else cursor = db.getAllProductName();

        itemID = new ArrayList<Long>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            itemID.add(Long.parseLong(cursor.getString(0)));
        }

        listAdapter = new ListAdapter(getActivity(), cursor, true);
        // listView.setFastScrollAlwaysVisible(true);
        // listView.setFastScrollEnabled(true);
        listView.setAdapter(listAdapter);
        listView.setSmoothScrollbarEnabled(true);

        db.close();


        listView.setOnCreateContextMenuListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3) {
                selectSearch = false;
                TextView textViewId=(TextView)v.findViewById(R.id.id);
                String ID;
                ID = textViewId.getText().toString();
                productsActivity.setSelectedProductID(Long.parseLong(ID));
                setSelect(position);
                firstScroll = listView.getFirstVisiblePosition();
                c = listView.getChildAt(1);
                productsActivity.setViewORSearch(false, true);
                productsActivity.clearFocus();
            }
        });
    }

    public void setNextItem() {

        if (itemPosition == itemID.size()-1)
            itemPosition--;

        else if (itemPosition < itemID.size()-1)
            itemPosition = itemPosition;

        else itemPosition = 0;

        db.open();
        if (search) {
            cursor = db.getSearchProductName(filter);
        }
        else cursor = db.getAllProductName();

        if (cursor.moveToPosition(itemPosition))
            productsActivity.setSelectedProductID(Long.parseLong(cursor.getString(0)));
        else {
            productsActivity.setSelectedProductID(0);
          //  productsActivity.setAdd(true);
        }
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
        long i = productsActivity.getSelectedProductID();
        itemPosition = getItemPosition(i);
        setSelect(itemPosition);
        int scrollY = 0;

        if (!scroll) {
            int h = c != null ? c.getHeight() + 1 : 0;
            int t = c != null ? c.getTop() : 0;
            scrollY = (itemPosition - firstScroll-1) * h + t;
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