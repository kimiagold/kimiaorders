package com.kimia.kimia;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProductViewFragment extends Fragment implements View.OnFocusChangeListener {

    private AutoCompleteTextView textGroup;
    private AutoCompleteTextView textMaker;

    private EditText textName;
    private EditText textCod;
    private EditText textTip;

    private String groups;
    private String name;
    private String tip;
    private String maker;

    private long cod;
    private long groupsID;
    private long makerID;
    //private ListView listViewProductGroup;
    //private ListView listViewMaker;
    private Cursor cursor;
    private ListAdapter listAdapter;
    private Context context;
    private DBAdapter db;
    private boolean visible;

    private int selectedProductID;
    private TextView textSelectedProductID;
    private ProductsActivity productsActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container ,Bundle savedInstanceState){

        return inflater.inflate(R.layout.fragment_product_view, container, false);
    }

    /*******************************************On Attach******************************************/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            productsActivity = (ProductsActivity) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFileSelectedListener");
        }
    }

    /*********************************************On Start*****************************************/

    @Override
    public void onStart(){

        super.onStart();

        if (getActivity() != null) {
            textGroup = (AutoCompleteTextView) getActivity().findViewById(R.id.ProductEditGroupID);
            textMaker = (AutoCompleteTextView) getActivity().findViewById(R.id.ProductEditMaker);

            textName = (EditText) getActivity().findViewById(R.id.ProductEditName);
            textCod = (EditText) getActivity().findViewById(R.id.ProductEditCod);
            textTip = (EditText) getActivity().findViewById(R.id.ProductEditTip);
            textSelectedProductID = (TextView) getActivity().findViewById(R.id.SelectedProductID);
        }

        textGroup.setOnFocusChangeListener(this);
        textName.setOnFocusChangeListener(this);
        textMaker.setOnFocusChangeListener(this);
        textCod.setOnFocusChangeListener(this);
        textTip.setOnFocusChangeListener(this);

        db = new DBAdapter(getActivity());
        context = getActivity();

        checkEdit();
        //listViewProductGroup = (ListView) getActivity().findViewById(R.id.Product_ListGroup);
        //listViewMaker = (ListView) getActivity().findViewById(R.id.Product_ListMaker);

        textMaker.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showMakersList();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        textGroup.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showProductsGroupList();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



   /* public void setstart(){
        if (getActivity() != null) {
            textGroup = (AutoCompleteTextView) getActivity().findViewById(R.id.ProductEditGroupID);
            textMaker = (AutoCompleteTextView) getActivity().findViewById(R.id.ProductEditMaker);

            textName = (EditText) getActivity().findViewById(R.id.ProductEditName);
            textCod = (EditText) getActivity().findViewById(R.id.ProductEditCod);
            textTip = (EditText) getActivity().findViewById(R.id.ProductEditTip);
            textSelectedProductID = (TextView) getActivity().findViewById(R.id.SelectedProductID);
        }

        textGroup.setOnFocusChangeListener(this);
        textName.setOnFocusChangeListener(this);
        textMaker.setOnFocusChangeListener(this);
        textCod.setOnFocusChangeListener(this);
        textTip.setOnFocusChangeListener(this);

        db = new DBAdapter(getActivity());
        context = getActivity();
    }*/

    /********************************************Check Edit****************************************/

    public void checkEdit(){
        try {
            selectedProductID = Integer.parseInt(textSelectedProductID.getText().toString());
        }
        catch(Exception e) {
            selectedProductID = 0;
        }

        if (selectedProductID > 0)
            showProduct();

        else resetForAdd();
    }

    /******************************************On focus Change*************************************/

    @Override
    public void onFocusChange(View view, boolean b) {
//        productsActivity.setEdit();

        /*
        switch(v.getId()){
            case r.id.editText1:
                break;

        if (view == getActivity().findViewById(R.id.ProductEditMaker) && b == true){
            listViewMaker.setVisibility(View.VISIBLE);
            showAccountsList();
        }

        if (view == getActivity().findViewById(R.id.ProductEditGroupID) && b == true){
            listViewProductGroup.setVisibility(View.VISIBLE);
            showProductsGroupList();
        }

        if (view == getActivity().findViewById(R.id.ProductEditGroupID) && b == false){
            listViewProductGroup.setVisibility(View.GONE);
        }

        if (view == getActivity().findViewById(R.id.ProductEditMaker) && b == false){
            listViewMaker.setVisibility(View.GONE);
        }*/
    }

    /**************************************************Show Product********************************/

    private void showProduct(){

        try{
            selectedProductID = Integer.parseInt(textSelectedProductID.getText().toString());
        }
        catch(Exception e)
        {
            selectedProductID = 0;
        }

        db.open();
        Cursor c=db.getProduct(selectedProductID);

        if(c != null && c.moveToFirst()){
            textGroup.setText(c.getString(5));
            textName.setText(c.getString(0));
            textMaker.setText(c.getString(4));
            textCod.setText(c.getString(1));
            textTip.setText(c.getString(2));
        }

        db.close();
    }

    /**************************************************Add product*********************************/

    public void addProduct(){

        name = textName.getText().toString();
        tip = textTip.getText().toString();
        cod = Integer.parseInt(textCod.getText().toString());
        groups = textGroup.getText().toString();
        maker = (textMaker.getText().toString());

        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        groupsID = db.getProductsGroupID(groups);
        makerID = db.getAccountsNameID(maker);
long groupsIDfff = 0;
        if (groupsID < 0){
            groupsIDfff = db.insertProductsGroup(groups);
        }

        if (makerID < 0 ){
            int makerCod = db.MaxCod();
            makerID = db.insertAc( 1, makerCod+1 , maker, "", "", "", false, "", true);
        }

        long id;
        id = db.insertProduct(cod, groupsIDfff, name, makerID, true, tip);
        db.close();

        if (id>0){
            Toast.makeText(getActivity(), name + ' ' + getString(R.string.registred) + id, Toast.LENGTH_SHORT).show();
        }
    }

    /**************************************************Reset for Add********************/

    public void resetForAdd(){

        textGroup.setText("");
        textName.setText("");
        textMaker.setText("");
        textTip.setText("");

        db.open();
        int max=db.ProductMaxCod();
        textCod.setText(Integer.toString(max+1));
        db.close();
    }

    /**************************************************Show Accounts List*****8********************/

    private void showMakersList() {

        db.open();
        super.onResume();
        String filter;
        filter = textMaker.getText().toString();
        cursor = db.filterAccounts(filter);

        if(cursor != null && cursor.moveToFirst()){
            textMaker.setAdapter(new AutoCompleteAdapter(getActivity(), cursor));
        }

        db.close();
        textMaker.setOnCreateContextMenuListener(this);
        textMaker.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3){
                TextView textViewId=(TextView)v.findViewById(R.id.username);
                String ID;
                ID=textViewId.getText().toString();
                textMaker.setText(ID);
            }
        });
    }

    /**************************************************Show Products Group List********************/

    private void showProductsGroupList(){

        db.open();
        super.onResume();
        String filter;
        filter = textGroup.getText().toString();
        cursor = db.filterProductsGroup(filter);

        if(cursor != null && cursor.moveToFirst()){
            textGroup.setAdapter(new AutoCompleteAdapter(getActivity(), cursor));
        }

        db.close();
        textGroup.setOnCreateContextMenuListener(this);
        textGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                TextView textViewId = (TextView) v.findViewById(R.id.username);
                String ID;
                ID = textViewId.getText().toString();
                textGroup.setText(ID);
            }
        });
    }

    /**********************************************************************************************/
}

/***********************************************Get Height*************************************/

    /*public int getHeight() {
        Rect rect = new Rect();
        Window win = getActivity().getWindow();
        win.getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.height();
        int contentViewTop = win.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentViewTop - statusBarHeight;
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenHeight = metrics.heightPixels;
        return screenHeight - (titleBarHeight + statusBarHeight);
    }*/

/*********************************************Set List View Height*****************************/

    /*public void setListViewHeight(int h){
        LayoutParams list = (LayoutParams) listViewMaker.getLayoutParams();

        if (h>getHeight())
            list.height = getHeight()-240;

        else
            list.height = h;

        listViewMaker.setLayoutParams(list);
    }*/