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

public class ProductsListFragment extends Fragment {

    ProductViewFragment productViewFragment;
    ListView listView;
    Cursor cursor;
    ListAdapter listAdapter;
    Context context;
    private TextView textSelectedProductID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_product_list,container,false);
    }

    @Override
    public void onStart(){
        super.onStart();
        showList();
    }

    public void showList() {

        textSelectedProductID = (TextView) getActivity().findViewById(R.id.SelectedProductID);
        DBAdapter db = new DBAdapter(getActivity());
        context = getActivity();
        listView = (ListView) getActivity().findViewById(R.id.listView);

        db.open();
        super.onResume();
        cursor = db.getAllProductName();

        if(cursor != null && cursor.moveToFirst()){
            listAdapter = new ListAdapter(getActivity(), cursor);
            listView.setFastScrollAlwaysVisible(true);
            listView.setAdapter(listAdapter);
        }

        db.close();

        listView.setOnCreateContextMenuListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3){
                TextView textViewId=(TextView)v.findViewById(R.id.userid);
                String ID;
                ID=textViewId.getText().toString();
                textSelectedProductID.setText(ID);
                ((ProductsActivity)getActivity()).setView();
                productViewFragment = (ProductViewFragment) getFragmentManager().findFragmentById(R.id.ProductsViewFragment);
                productViewFragment.showProduct();
            }
        });
    }

    @Override
    public void onResume() {
        showList();
        super.onResume();
    }

    @Override
    public void onPause() {
        //db.close();
        super.onPause();
    }
}