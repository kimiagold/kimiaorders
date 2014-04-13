package com.kimia.kimia;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ActionbarAdapter extends Activity{

    private MenuItem itemAdd;
    private MenuItem itemEdit;
    private MenuItem itemDelete;
    private MenuItem itemSearch;
    private MenuItem itemAccept;
    private MenuItem itemCancel;
    public boolean stateEdit = false;

    @Override
    public boolean onCreateOptionsMenu(Menu Menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, Menu);

        itemAdd = Menu.findItem(R.id.ItemAdd);
        itemEdit = Menu.findItem(R.id.ItemEdit);
        itemDelete = Menu.findItem(R.id.ItemDelete);
        itemSearch = Menu.findItem(R.id.ItemSearch);
        itemAccept = Menu.findItem(R.id.ItemAccept);
        itemCancel = Menu.findItem(R.id.ItemCancel);

        if (stateEdit) actionbarSetEdit();
        else actionbarSetView();

        return true;
    }

    public boolean actionbarSetEdit(){
        stateEdit = true;
        if (itemAccept!=null){
            itemAdd.setVisible(false);
            itemEdit.setVisible(false);
            itemDelete.setVisible(false);
            itemSearch.setVisible(false);
            itemAccept.setVisible(true);
            itemCancel.setVisible(true);
            return true;
        }
        return false;
    }

    public boolean actionbarSetView(){
        stateEdit = false;
        if (itemAccept!=null){
            itemAdd.setVisible(true);
            itemEdit.setVisible(true);
            itemDelete.setVisible(true);
            itemSearch.setVisible(true);
            itemAccept.setVisible(false);
            itemCancel.setVisible(false);
            return true;
        }
        return false;
    }
}
