package com.kimia.kimia;

import android.content.Context;
import android.database.Cursor;
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
import java.util.Set;

public class ListAdapter extends BaseAdapter implements SectionIndexer {

    Cursor cursor;
    TextView textViewName;
    TextView textViewId;
    private int selectedItem;
    boolean group;
    Context ctx;
    HashMap<String,Integer> alphaIndexer;
    String[] sections;

    public ListAdapter(Context context, Cursor cur, boolean showGroup) {
        cursor = cur;
        ctx = context;
        group = showGroup;

        alphaIndexer = new HashMap<String,Integer>();
        int size = getCount();

        if( cursor != null && size > 0 ) {
            for (int x = size-1; x >= 0; x--) {
                cursor.moveToPosition(x);
                String s = cursor.getString(1);
                assert s != null;
                String ch = s.substring(0, 1);
                ch = ch.toUpperCase();
             //   if (!alphaIndexer.containsKey(ch))
                    alphaIndexer.put(ch, x);
            }

            Set<String> sectionLetters = alphaIndexer.keySet();
            ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
            Collections.sort(sectionList);
            sections = new String[sectionList.size()];
            sectionList.toArray(sections);
        }
    }

    @Override
    public int getCount() {
        return cursor.getCount();

    }

    @Override
    public Object getItem(int position) {
        //return items[position];
        cursor.moveToPosition(position);
        return cursor;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position,  View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.listitem, null);

        if (view != null){
            textViewName = (TextView) view.findViewById(R.id.name);
            textViewId = (TextView) view.findViewById(R.id.id);
            //textViewGroup = (TextView) view.findViewById(R.id.group);
        }

        cursor.moveToPosition(position);
        String Name=cursor.getString(1);
        String Id=cursor.getString(0);


        if (group) {
            String Group = cursor.getString(2);
            textViewName.setText(Name + " . " + Group);
        } else textViewName.setText(Name);

        textViewId.setText(Id);

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

    @Override
    public int getPositionForSection(int section) {
        try {
            return alphaIndexer.get(sections[section]);
        } catch (Exception e) {
            return 1;
        }
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        return sections;
    }

    public void setSelectedItem(int position) {
        selectedItem = position;
    }
}