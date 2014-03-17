package com.kimia.kimia;

import android.content.Context;
import android.os.Bundle;
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
    private int addOrEdit = 1;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        productViewFragment = (ProductViewFragment) getFragmentManager().findFragmentById(R.id.ProductsViewFragment);
        productsListFragment = (ProductsListFragment) getFragmentManager().findFragmentById(R.id.ProductsListFragment);
        textSelectedProductID = (TextView) findViewById(R.id.SelectedProductID);
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

    public void setEdit(int a){
        actionbarSetEdit();
        setFragmentWeightEdit(true);
        addOrEdit = a;
    }

    public void setView(boolean scroll){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v=getCurrentFocus();

        if (v != null) {
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            v.clearFocus();
        }

        productViewFragment.checkEdit();
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
        setEdit(1);
        productViewFragment.resetForAdd();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem Item) {

        switch (Item.getItemId()) {

            case R.id.ItemAdd:
                setAdd();
                break;

            case R.id.ItemEdit:
                setEdit(2);
                break;

            case R.id.ItemDelete:
                DBAdapter db = new DBAdapter(this);
                db.open();

                boolean c=db.deleteProduct(Long.parseLong(textSelectedProductID.getText().toString()));

                if (c){
                    Toast.makeText(this, getString(R.string.deleted),Toast.LENGTH_SHORT).show();
                    productsListFragment.setNextItem();
                    setView(true);
                }
                else
                    Toast.makeText(this, getString(R.string.error),Toast.LENGTH_SHORT).show();

                db.close();
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