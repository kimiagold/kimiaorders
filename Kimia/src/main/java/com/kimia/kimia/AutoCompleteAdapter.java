package com.kimia.kimia;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AutoCompleteAdapter extends ArrayAdapter<String>{

    private HashMap<String, Integer> azIndexer;
    private Context context;
    private Cursor cursor;
    private TextView textViewName;
    private TextView textViewId;
    private ImageView imageView;
    private int size;
    private boolean picture;
    private boolean onlyID;
    private String Name;
    private String Id;

    public AutoCompleteAdapter(Context con, Cursor cur, boolean pic, boolean ID) {
        super(con, -1);
        cursor=cur;
        context=con;
        picture = pic;
        onlyID = ID;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.user_listitem, null);

        cursor.moveToPosition(position);

        if (!onlyID)
            Name = cursor.getString(0);

        else
            Name = cursor.getString(1);
            Id = cursor.getString(0);

        if (view != null) {
            textViewName = (TextView) view.findViewById(R.id.username);
            textViewId = (TextView) view.findViewById(R.id.userid);
            imageView = (ImageView)view.findViewById(R.id.userPic);
        }

        textViewName.setText(Name);
        textViewId.setText(Id);
        if (!picture)
            imageView .setVisibility(View.INVISIBLE);//GONE

        return view;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(final CharSequence constraint) {
                azIndexer = new HashMap<String, Integer>();
                size = cursor.getCount();
                for (int i=0;i< size ;  i++) {
                    cursor.moveToPosition(i);
                    String element = cursor.getString(1);

                    String ch = null;
                    if (element != null) {
                        ch = element.substring(0, 1);
                    }
                    if (!azIndexer.containsKey(ch))
                        azIndexer.put(ch, i);
                }

                Set<String> keys = azIndexer.keySet();
                Iterator<String> it = keys.iterator();
                ArrayList<String> keyList = new ArrayList<String>();

                while (it.hasNext()) {
                    String key = it.next();
                    keyList.add(key);
                }

                Collections.sort(keyList);
                List<String> addressList;

                if (constraint != null) {
                    addressList = keyList;
                }

                else {
                    addressList = new ArrayList<String>();
                }

                final FilterResults filterResults = new FilterResults();
                filterResults.values = addressList;
                filterResults.count = size;

                return filterResults;
            }

            @Override
            protected void publishResults(final CharSequence contraint, final FilterResults results) {
                clear();

              /*  assert results.values != null;
                for (String string : (List<String>) results.values) {
                    add(string);
                }*/

                List<String> string;
                string = (List<String>) results.values;

                for (int i = 0; i <size; i++){
                    add(String.valueOf(string));
                }

                if (results.count > 0) {
                    notifyDataSetChanged();
                }

                else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}