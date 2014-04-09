package com.kimia.kimia;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

public class ListAdapter extends BaseAdapter implements SectionIndexer {

    //HashMap<String, Integer> azIndexer;
    String[] sections;
    private Context mContext;
    Cursor cursor;
    TextView textViewName;
    TextView textViewId;
    private int selectedItem;
    View view1;
    ViewGroup parent1;

    public LinkedHashMap<String, Integer> sectionList;
    public HashMap<Integer,Integer> sectionPositions;
    public HashMap<Integer,Integer> positionsForSection;

    public ListAdapter(Context context, Cursor cur) {
        super();
        mContext=context;
        cursor=cur;
        sectionList = new LinkedHashMap<String, Integer>();
        sectionPositions = new HashMap<Integer, Integer>();
        positionsForSection = new HashMap<Integer, Integer>();
        //azIndexer = new HashMap<String, Integer>();

   /*     int size = getCount();
        for (int i = size - 1; i >= 0; i--) {
            cursor.moveToPosition(i);
            String element = cursor.getString(1);
            String ch = null;
            if (element != null) {
                ch = element.substring(0, 1);
            }
            //if (!azIndexer.containsKey(ch))
                azIndexer.put(ch, i);
        }*/


        if( cur != null && getCount() > 0 ) {
            //Iterate through the contacts, take the first letter, uppercase it, and use that as a key to reference the alphabetised list constructed above.
            for( int i = getCount()-1; i >= 0; i-- ) {

                cursor.moveToPosition(i);
                String element = cursor.getString(1);
                String ch = null;
                if (element != null)
                    ch =element.substring(0, 1);

                //if( ch != null ) {
                    //if(!sectionList.containsValue(ch)) {
                        sectionList.put(ch, i);
                        positionsForSection.put(sectionList.size(), i);
                    //}
                //}
                sectionPositions.put(i-1, 1);
            }
        }

        Set<String> keys = sectionList.keySet();
        Iterator<String> it = keys.iterator();
        ArrayList<String> keyList = new ArrayList<String>(keys);

        while (it.hasNext()) {
            String key = it.next();
            keyList.add(key);
        }

        Collections.sort(keyList);
        sections = new String[keyList.size()];
        keyList.toArray(sections);
    }

    public int getCount() {
        return cursor.getCount();
    }

    public View getView(int position,  View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.user_listitem, null);
        cursor.moveToPosition(position);
        String Name=cursor.getString(1);
        String Id=cursor.getString(0);
        view1 = view;
        parent1 = parent;

        if (view != null){
            textViewName = (TextView) view.findViewById(R.id.username);
            textViewId = (TextView) view.findViewById(R.id.userid);
        }

        textViewName.setText(Name);
        textViewId.setText(Id);

        // set selected item
        LinearLayout ActiveItem = (LinearLayout) view;
        if (position == selectedItem) {
            if (ActiveItem != null) {
                ActiveItem.setBackgroundResource(R.drawable.item_background_select);
            }

            // for focus on it
            int top = (ActiveItem == null) ? 0 : ActiveItem.getTop();
            ((ListView) parent).setSelectionFromTop(position, top);
        } else {
            if (ActiveItem != null) {
                ActiveItem.setBackgroundResource(R.drawable.item_background);
            }
        }

        return view;
    }

    public void setSelectedItem(int position) {
        selectedItem = position;
        // for focus on it
  //      int top = (ActiveItem == null) ? 0 : ActiveItem.getTop();
//        ((ListView) parent1).setSelectionFromTop(position, top);
    }


    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getPositionForSection(int section) {
        String letter = sections[section];
        //return azIndexer.get(letter);
        return sectionList.get(letter);
        //return positionsForSection.get(section);
    }

    @Override
    public int getSectionForPosition(int position) {
        Log.v("getSectionForPosition", "called");
        String letter = sections[position];
        //return azIndexer.get(letter);
        //return sectionList.get(letter);
        return sectionPositions.get(position);
        //return 0;
    }

    @Override
    public Object getItem(int position) {
        if( getCount() > position ) {
            //return locations.get(position);
            return position;
        }

        return null;
    }

    public Object[] getSections() {
        return sections; // to string will be called to display the letter
    }
}