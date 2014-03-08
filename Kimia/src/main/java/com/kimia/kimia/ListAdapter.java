package com.kimia.kimia;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ListAdapter extends BaseAdapter {

    HashMap<String, Integer> azIndexer;
    String[] sections;
    private Context mContext;
    Cursor cursor;
    TextView textViewName;
    TextView textViewId;

    public ListAdapter(Context context, Cursor cur){
        super();
        mContext=context;
        cursor=cur;
        azIndexer = new HashMap<String, Integer>(); //stores the positions for the start of each letter

        int size = getCount();
        for (int i = size - 1; i >= 0; i--) {

            cursor.moveToPosition(i);
            String element = cursor.getString(1);
            //We store the first letter of the word, and its index.
            azIndexer.put(element.substring(0, 1), i);
        }

        Set<String> keys = azIndexer.keySet(); // set of letters

        Iterator<String> it = keys.iterator();
        ArrayList<String> keyList = new ArrayList<String>();

        while (it.hasNext()) {
            String key = it.next();
            keyList.add(key);
        }
        Collections.sort(keyList);//sort the keylist
        sections = new String[keyList.size()];

    }

    public int getCount(){

        return cursor.getCount();
    }


    public View getView(int position,  View view, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.user_listitem, null);

        cursor.moveToPosition(position);

        String Name=cursor.getString(1);
        String Id=cursor.getString(0);

        textViewName = (TextView) view.findViewById(R.id.username);
        textViewId = (TextView) view.findViewById(R.id.userid);

        textViewName.setText(Name);
        textViewId.setText(Id);

        return view;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public int getPositionForSection(int section) {
        String letter = sections[section];
        return azIndexer.get(letter);
    }

    public int getSectionForPosition(int position) {
        Log.v("getSectionForPosition", "called");
        return 0;
    }

    public Object[] getSections() {
        return sections; // to string will be called to display the letter
    }
}