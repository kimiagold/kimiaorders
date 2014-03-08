package com.kimia.kimia;

/**
 * Created by behdad on 2/10/14.
 */
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

/**
 * Created by behdad on 1/29/14.
 */
public class ListAdapter extends BaseAdapter {

    // public static abstract class Row {}

    ArrayList<String> myElements;
    HashMap<String, Integer> azIndexer;
    String[] sections;

    private Context mContext;
    Cursor cursor;
    TextView textViewName;
    TextView textViewId;

    public ListAdapter(Context context, Cursor cur)
    {
        super();
        mContext=context;
        cursor=cur;


        //   myElements = (ArrayList<String>) objects;
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

/*


        alphaIndexer = new HashMap<String, Integer>();
        int size = getCount();

        for (int x = 0; x < size; x++) {
            String s = cursor.getString(x);

            // get the first letter of the store
            String ch = s.substring(0, 1);
            // convert to uppercase otherwise lowercase a -z will be sorted
            // after upper A-Z
            ch = ch.toUpperCase();
            // put only if the key does not exist
            if (!alphaIndexer.containsKey(ch))
                alphaIndexer.put(ch, x);



        }


        Set<String> sectionLetters = alphaIndexer.keySet();

        // create a list from the set to sort
        ArrayList<String> sectionList = new ArrayList<String>(
                sectionLetters);

        Collections.sort(sectionList);

        sections = new String[sectionList.size()];

        sectionList.toArray(sections);
        */
    }

    public int getCount()
    {
        return cursor.getCount();
    }


    public View getView(int position,  View view, ViewGroup parent)
    {
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
        // TODO Auto-generated method stub
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
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


 /*   public int getPositionForSection(int section) {
        return alphaIndexer.get(sections[section]);
    }

    public int getSectionForPosition(int position) {
        return 0;
    }

    public Object[] getSections() {
        return sections;
    }

    */

}