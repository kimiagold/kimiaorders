package com.kimia.kimia;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

public class ActionbarAdapter extends Activity{

    private MenuItem itemAdd;
    private MenuItem itemEdit;
    private MenuItem itemDelete;
    private MenuItem itemSearch;
    private MenuItem itemAccept;
    private MenuItem itemCancel;
    private ProductsListFragment productsListFragment;
    public boolean stateEdit = false;
    SearchView searchView;
    //private boolean rotate = false;
    public String search = "";
    public boolean searchState = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        productsListFragment = (ProductsListFragment) getFragmentManager().findFragmentById(R.id.ProductsListFragment);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);

        itemAdd = menu.findItem(R.id.ItemAdd);
        itemEdit = menu.findItem(R.id.ItemEdit);
        itemDelete = menu.findItem(R.id.ItemDelete);
        itemSearch = menu.findItem(R.id.ItemSearch);
        itemAccept = menu.findItem(R.id.ItemAccept);
        itemCancel = menu.findItem(R.id.ItemCancel);
        searchView = (SearchView) menu.findItem(R.id.ItemSearch).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);


        if (stateEdit) actionbarSetEdit();
        else actionbarSetView();

        if (searchView != null) {

            if (searchState) {
                searchView.setQuery(search, true);
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
                //productsListFragment.showList(true, search);
            }
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                search = newText;
                productsListFragment.showList(true, newText);
                searchState = true;
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                search = query;
                productsListFragment.showList(true, search);
                searchState = true;
                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    //    return true;
    }

    public void setRotate(Bundle state) {
        search = state.getString("searchString");
        searchState = state.getBoolean("searchState");
    }

    public Bundle getRotate() {
        Bundle outState = new Bundle();
        outState.putBoolean("searchState", searchState);
        outState.putString("searchString", search);
        return outState;
    }

    public boolean actionbarSetEdit() {
        stateEdit = true;
        if (itemAccept!=null) {
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

    public boolean actionbarSetView() {
        stateEdit = false;
        if (itemAccept!=null) {
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
