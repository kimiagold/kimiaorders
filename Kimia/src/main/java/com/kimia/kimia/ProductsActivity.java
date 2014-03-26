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
    private TextView textSelectedProductID;
    private int selectedProductID;
    private Activity activity;
    private int addOrEdit = 1;
    private DBAdapter db;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        activity = this;
        productViewFragment = (ProductViewFragment) getFragmentManager().findFragmentById(R.id.ProductsViewFragment);
        productsListFragment = (ProductsListFragment) getFragmentManager().findFragmentById(R.id.ProductsListFragment);
        textSelectedProductID = (TextView) findViewById(R.id.SelectedProductID);
        db = new DBAdapter(this);
        /*
        ProductViewFragment fragment1 = new ProductViewFragment();
        getFragmentManager().beginTransaction().add(R.id.ProductsViewFragment, fragment1).commit();

        ProductsListFragment fragment = new ProductsListFragment();
        getFragmentManager().beginTransaction().add(R.id.ProductsListFragment, fragment).commit();
        */
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
    }

    public void setView(boolean scroll){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v=getCurrentFocus();

        if (v != null) {
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            v.clearFocus();
        }

        productViewFragment.checkFirsItem();
        productsListFragment.showList();
        productsListFragment.setScroll(scroll);

        try {
            selectedProductID = Integer.parseInt(textSelectedProductID.getText().toString());
        }
        catch(Exception e){
            selectedProductID = 0;
        }

        if (selectedProductID > 0){
            setFragmentWeightEdit(false);
            actionbarSetView();
            //productsListFragment.setSelect(5);
        }
        else {
            setFragmentWeightEdit(true);
            actionbarSetEdit();
        }
    }

    public void setAdd(){
        actionbarSetEdit();
        setFragmentWeightEdit(true);
        productViewFragment.resetForAdd(true);
        addOrEdit = 1;
    }

    private void deleteProduct(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.delete_dialog, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        TextView textDialog = null;
        if (deleteDialogView != null) {
            textDialog = (TextView) deleteDialogView.findViewById(R.id.txt_dialog);
            String name = null;
            Cursor c;
            db.open();
            c = db.getProduct(Long.parseLong(textSelectedProductID.getText().toString()));

            if(c != null && c.moveToFirst())
                name = c.getString(0);

            db.close();

            textDialog.setText(name + " " + getString(R.string.Deleted_Q));
            deleteDialogView.findViewById(R.id.delete_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.open();
                    boolean c=db.deleteProduct(Long.parseLong(textSelectedProductID.getText().toString()));

                    if (c){
                        Toast.makeText(activity, getString(R.string.deleted),Toast.LENGTH_SHORT).show();
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
                setAdd();
                break;

            case R.id.ItemEdit:
                setEdit(true);
                break;

            case R.id.ItemDelete:
                deleteProduct();
                break;

            case R.id.ItemAccept:

                switch (addOrEdit){
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
                setView(true);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(Item);
    }
}