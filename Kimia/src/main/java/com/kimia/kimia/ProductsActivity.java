package com.kimia.kimia;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProductsActivity extends ActionbarAdapter {

    private ProductViewFragment productViewFragment;
    private ProductsListFragment productsListFragment;
    private long selectedProductID = 0;
    private Activity activity;
    private int addOrEdit = 1;
    private int state = 0;
    private DBAdapter db;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        activity = this;
        productViewFragment = (ProductViewFragment) getFragmentManager().findFragmentById(R.id.ProductsViewFragment);
        productsListFragment = (ProductsListFragment) getFragmentManager().findFragmentById(R.id.ProductsListFragment);
        db = new DBAdapter(this);

        ActionBar actionBar = getActionBar();

       // ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        /*
        ProductViewFragment fragment1 = new ProductViewFragment();
        getFragmentManager().beginTransaction().add(R.id.ProductsViewFragment, fragment1).commit();

        ProductsListFragment fragment = new ProductsListFragment();
        getFragmentManager().beginTransaction().add(R.id.ProductsListFragment, fragment).commit();
        */
    }

    public void setSelectedProductID(long id){
        selectedProductID = id;
    }

    public long getSelectedProductID(){
        return selectedProductID;
    }

    public void setFragmentWeightEdit(boolean b){
        float weight;

        if (b)
            weight = 7;
        else weight = 5;

        LinearLayout layout = (LinearLayout) findViewById(R.id.ProductsList);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, weight);
        layout.setLayoutParams(params);

    }

    public void setEdit(boolean changeFocus){
        actionbarSetEdit();
        setFragmentWeightEdit(true);
        if (changeFocus) productViewFragment.resetForAdd(false);
        addOrEdit = 2;
        state = 2;
        productViewFragment.setAddOrEdit(true);
    }

    public void clearFocus(){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v=getCurrentFocus();
        if (v != null) {
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            v.clearFocus();
        }
    }

    public void setView (boolean scroll) {
        productViewFragment.checkFirsItem();
        productsListFragment.showList(false, "");
        clearFocus();
        productsListFragment.setScroll(scroll);
        state = 0;

        if (selectedProductID > 0) {
            setFragmentWeightEdit(false);
            actionbarSetView();
            //productsListFragment.setSelect(5);
        } else {
            setAdd(true);
        }
    }

    public void setViewORSearch(boolean scroll, boolean clearFocus){
        if (searchState) {
            setSearch(scroll, false, clearFocus);
        }
        else setView(scroll);
    }

    public void setSearch (boolean scroll, boolean view, boolean clearFocus) {
        if (view) {
            productViewFragment.checkFirsItem();
        } else
            productsListFragment.showList(true, search);

        if (clearFocus) clearFocus();

        setFragmentWeightEdit(false);
        actionbarSetView();
        productsListFragment.setScroll(scroll);
    }

    public void setAdd(boolean firstItem) {
        actionbarSetEdit();
        setFragmentWeightEdit(true);
        if (!firstItem) productViewFragment.resetForAdd(true);
        addOrEdit = 1;
        state = 1;
        productViewFragment.setAddOrEdit(false);
    }

    private void deleteProduct() {
        String name = null;
        long groupID = 0;
        long makerID = 0;
        state = 3;
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.delete_dialog, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        TextView textDialog;
        if (deleteDialogView != null) {
            textDialog = (TextView) deleteDialogView.findViewById(R.id.txt_dialog);
            Cursor c;
            db.open();
            c = db.getDeleteProduct(selectedProductID);

            if(c != null && c.moveToFirst()){
                groupID = c.getLong(0);
                makerID = c.getLong(1);
                name = c.getString(2);
            }

            db.close();

            textDialog.setText(name + " " + getString(R.string.Deleted_Q));
            final long finalGroupID = groupID;
            final long finalMakerID = makerID;
            deleteDialogView.findViewById(R.id.delete_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.open();
                    boolean c=db.deleteProduct(selectedProductID);

                    if (c){
                        Toast.makeText(activity, getString(R.string.deleted),Toast.LENGTH_SHORT).show();
                        db.minusGroup(finalGroupID);
                        db.minusMaker(finalMakerID);
                        productsListFragment.setNextItem();
                        setViewORSearch(false, true);
                    }
                    else
                        Toast.makeText(activity, getString(R.string.error),Toast.LENGTH_SHORT).show();

                    db.close();
                    deleteDialog.dismiss();
                    state = 0;
                }
            });

            deleteDialogView.findViewById(R.id.delete_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDialog.dismiss();
                    state = 0;
                }
            });
        }

        deleteDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem Item) {

        switch (Item.getItemId()) {

            case R.id.ItemAdd:
                resetSearch();
                searchState = false;
                setViewFragmentVisible(true);
                setAdd(false);
                break;

            case R.id.ItemEdit:
                //searchState = false;
                productsListFragment.selectSearch = false;
                setEdit(true);
                break;

            case R.id.ItemDelete:
                //searchState = false;
                productsListFragment.selectSearch = false;
                deleteProduct();
                break;

            case R.id.ItemAccept:
                //searchState = false;
                productsListFragment.selectSearch = false;

                switch (addOrEdit) {
                    case 1:
                        if (productViewFragment.addProduct()){
                            setViewORSearch(true, true);
                        }
                        break;

                    case 2:
                        if (productViewFragment.editProduct()){
                            setViewORSearch(true, true);
                        }
                        break;
                }

                break;

            case R.id.ItemCancel:
                //searchState = false;
                productsListFragment.selectSearch = false;
                if (selectedProductID == 0)
                    finish();
                else setViewORSearch(true, true);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(Item);
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        outState.putBundle("search", getRotate());
        outState.putInt("state", state);
        //selectedProductID = Integer.parseInt(textSelectedProductID.getText().toString());
        outState.putLong("selectedProductID", selectedProductID);
        outState.putBundle("viewFragment", productViewFragment.saveState());
    }

    @Override
    public void onRestoreInstanceState (Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        selectedProductID = savedInstanceState.getLong("selectedProductID", 0);
        state = savedInstanceState.getInt("state", 0);
        setRotate(savedInstanceState.getBundle("search"));
        productsListFragment.selectSearch = false;

        switch (state) {
            case 0:
                setViewORSearch(true, true);
                break;

            case 1:
                productsListFragment.showList(false, "");
                productsListFragment.setScroll(true);
                setAdd(false);
                break;

            case 2:
                productsListFragment.showList(false, "");
                productsListFragment.setScroll(true);
                setEdit(false);
                break;

            case 3:
                deleteProduct();
                break;

            default:
                break;
        }

        productViewFragment.setState(savedInstanceState.getBundle("viewFragment"));
    }

    @Override
    public void onBackPressed() {
        if (state == 0)
            finish();
        else setViewORSearch(true, true);
    }

    public void setViewFragmentVisible(boolean b) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.ProductsView);
        if (b) {
            actionbarSetView();
            layout.setVisibility(View.VISIBLE);
        }
        else {
            actionbarSetView1();
            layout.setVisibility(View.INVISIBLE);//GONE  INVISIBLE
        }
    }
}