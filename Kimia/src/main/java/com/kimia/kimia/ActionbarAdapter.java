package com.kimia.kimia;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

////
public class ActionbarAdapter extends Activity{

    private MenuItem itemAdd;
    private MenuItem itemEdit;
    private MenuItem itemDelete;
    private MenuItem itemAccept;
    private MenuItem itemCancel;

    private TextView textSelectedProductID;
    private int selectedProductID;

    @Override
    public boolean onCreateOptionsMenu(Menu Menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, Menu);

        itemAdd = Menu.findItem(R.id.ItemAdd);
        itemEdit = Menu.findItem(R.id.ItemEdit);
        itemDelete = Menu.findItem(R.id.ItemDelete);
        itemAccept = Menu.findItem(R.id.ItemAccept);
        itemCancel = Menu.findItem(R.id.ItemCancel);

        textSelectedProductID = (TextView) findViewById(R.id.SelectedProductID);

        try {
            selectedProductID = Integer.parseInt(textSelectedProductID.getText().toString());
        }
        catch(Exception e){
            selectedProductID = 0;
        }

        if (selectedProductID > 0)
            actionbarSetView();
        else actionbarSetEdit();

        return true;
    }

    public boolean actionbarSetEdit(){

        if (itemAccept!=null){
            itemAdd.setVisible(false);
            itemEdit.setVisible(false);
            itemDelete.setVisible(false);
            itemAccept.setVisible(true);
            itemCancel.setVisible(true);
        }

        return true;
    }

    public boolean actionbarSetView(){

        if (itemAccept!=null){
            itemAdd.setVisible(true);
            itemEdit.setVisible(true);
            itemDelete.setVisible(true);
            itemAccept.setVisible(false);
            itemCancel.setVisible(false);
        }

        return true;
    }
}
