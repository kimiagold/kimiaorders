package com.kimia.kimia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProductsActivity extends ActionbarAdapter {

    private ProductViewFragment productViewFragment;
    private ProductsListFragment productsListFragment;
   // private TextView textSelectedProductID;
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
      //  textSelectedProductID = (TextView) findViewById(R.id.SelectedProductID);
        db = new DBAdapter(this);
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

        LinearLayout layout= (LinearLayout) findViewById(R.id.ProductsList);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, weight);
        layout.setLayoutParams(params);
    }

    public void setEdit(boolean changeFocus){
        actionbarSetEdit();
        setFragmentWeightEdit(true);
        if (changeFocus) productViewFragment.resetForAdd(false);
        addOrEdit = 2;
        state = 2;
    }

    public void setView (boolean scroll) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v=getCurrentFocus();

        if (v != null) {
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            v.clearFocus();
        }

        productViewFragment.checkFirsItem();
        productsListFragment.showList();
        productsListFragment.setScroll(scroll);

        /*try {
            selectedProductID = Integer.parseInt(textSelectedProductID.getText().toString());
        } catch (Exception e) {
            selectedProductID = 0;
        }*/

        if (selectedProductID > 0) {
            setFragmentWeightEdit(false);
            actionbarSetView();
            //productsListFragment.setSelect(5);
        } else {
            setAdd(true);
        }

        state = 0;
    }

    public void setAdd(boolean firstItem) {
        actionbarSetEdit();
        setFragmentWeightEdit(true);
        if (!firstItem) productViewFragment.resetForAdd(true);
        addOrEdit = 1;
        state = 1;
    }

    private void deleteProduct(){
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
                        setView(false);
                    }
                    else
                        Toast.makeText(activity, getString(R.string.error),Toast.LENGTH_SHORT).show();

                    db.close();

                    deleteDialog.dismiss();
                }
            });

            deleteDialogView.findViewById(R.id.delete_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDialog.dismiss();
                }
            });
        }

        deleteDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem Item) {

        switch (Item.getItemId()) {

            case R.id.ItemAdd:
                setAdd(false);
                break;

            case R.id.ItemEdit:
                setEdit(true);
                break;

            case R.id.ItemDelete:
                deleteProduct();
                break;

            case R.id.ItemAccept:

                switch (addOrEdit) {
                    case 1:
                        if (productViewFragment.addProduct()){
                            setView(true);
                        }
                        break;

                    case 2:
                        if (productViewFragment.editProduct()){
                            setView(true);
                        }
                        break;
                }

                break;

            case R.id.ItemCancel:
                if (selectedProductID == 0)
                    finish();
                else setView(true);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(Item);
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        outState.putInt("state", state);
        //selectedProductID = Integer.parseInt(textSelectedProductID.getText().toString());
        outState.putLong("selectedProductID", selectedProductID);
        outState.putBundle("viewFragment", productViewFragment.saveState());
    }

    @Override
    public void onRestoreInstanceState (Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        selectedProductID = savedInstanceState.getLong("selectedProductID", 0);
        //textSelectedProductID.setText(String.valueOf(selectedProductID));
        state = savedInstanceState.getInt("state", 0);

        switch (state) {
            case 0:
                setView(true);
                break;

            case 1:
                productsListFragment.showList();
                productsListFragment.setScroll(true);
                setAdd(false);
                break;

            case 2:
                productsListFragment.showList();
                productsListFragment.setScroll(true);
                setEdit(false);
                break;

            case 3:
                deleteProduct();
                break;
        }

        productViewFragment.setState(savedInstanceState.getBundle("viewFragment"));
    }
}