package com.kimia.kimia;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

//import android.support.v4.app.FragmentActivity;

public class ProductsActivity extends ActionbarAdapter {

    ProductViewFragment productViewFragment;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        productViewFragment = (ProductViewFragment) getFragmentManager().findFragmentById(R.id.ProductsViewFragment);

        /*
        ProductViewFragment fragment1 = new ProductViewFragment();
        getFragmentManager().beginTransaction().add(R.id.ProductsViewFragment, fragment1).commit();

        ProductsListFragment fragment = new ProductsListFragment();
        getFragmentManager().beginTransaction().add(R.id.ProductsListFragment, fragment).commit();
        */
    }

    public void SetFragmentWeight(float weight){

        LinearLayout layout= (LinearLayout) findViewById(R.id.ProductsList);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, weight );
        layout.setLayoutParams(params);
    }

    public void setEdit(){
        actionbarSetEdit();
        SetFragmentWeight(7);
    }

    public void setView(){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v=getCurrentFocus();
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        v.clearFocus();
        SetFragmentWeight(5);
        actionbarSetView();
    }

    public void setAdd(){
        setEdit();
        productViewFragment.resetForAdd();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem Item) {

        switch (Item.getItemId()) {

            case R.id.ItemAdd:
                setAdd();
                break;

            case R.id.ItemEdit:
                setEdit();
                break;

            case R.id.ItemDelete:
                break;

            case R.id.ItemAccept:
                productViewFragment.addProduct();
                setView();
                break;

            case R.id.ItemCancel:
                setView();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(Item);
    }

}