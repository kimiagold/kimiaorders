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
import java.util.Set;

public class ListAdapter extends BaseAdapter implements SectionIndexer {

    HashMap<String, Integer> azIndexer;
    String[] sections;
    private Context mContext;
    Cursor cursor;
    TextView textViewName;
    private ArrayList<Long> itemID;
    TextView textViewId;
    private int selectedItem;

    public ListAdapter(Context context, Cursor cur) {
        super();
        mContext=context;
        cursor=cur;
        azIndexer = new HashMap<String, Integer>();

        int size = getCount();
        for (int i = size - 1; i >= 0; i--) {
            cursor.moveToPosition(i);
            String element = cursor.getString(1);
            String ch = null;
            if (element != null) {
                ch = element.substring(0, 1);
            }
            if (!azIndexer.containsKey(ch))
                azIndexer.put(ch, i);
        }

        itemID = new ArrayList<Long>();
        Set<String> keys = azIndexer.keySet();
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

        if (view != null){
            textViewName = (TextView) view.findViewById(R.id.username);
            textViewId = (TextView) view.findViewById(R.id.userid);
        }

        itemID.add(Long.parseLong(Id));
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

    public int getItemPosition(long id) {
        for (int position=0; position < itemID.size(); position++)
            if (itemID.get(position) == id)
                return position;
        return 0;
    }

    public void setSelectedItem(int position) {
        selectedItem = position;
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