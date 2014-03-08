package com.kimia.kimia;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
////
public class ActionbarAdapter extends Activity{

    private MenuItem itemAdd;
    private MenuItem itemEdit;
    private MenuItem itemDelete;
    private MenuItem itemAccept;
    private MenuItem itemCancel;

    @Override
    public boolean onCreateOptionsMenu(Menu Menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, Menu);

        itemAdd = Menu.findItem(R.id.ItemAdd);
        itemEdit = Menu.findItem(R.id.ItemEdit);
        itemDelete = Menu.findItem(R.id.ItemDelete);
        itemAccept = Menu.findItem(R.id.ItemAccept);
        itemCancel = Menu.findItem(R.id.ItemCancel);

        itemAccept.setVisible(false);
        itemCancel.setVisible(false);

        return true;
    }

    public boolean actionbarSetEdit(){

        itemAdd.setVisible(false);
        itemEdit.setVisible(false);
        itemDelete.setVisible(false);
        itemAccept.setVisible(true);
        itemCancel.setVisible(true);

        return true;
    }

    public boolean actionbarSetView(){

        itemAdd.setVisible(true);
        itemEdit.setVisible(true);
        itemDelete.setVisible(true);
        itemAccept.setVisible(false);
        itemCancel.setVisible(false);

        return true;
    }
}
