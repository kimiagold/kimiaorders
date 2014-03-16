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
import android.view.inputmethod.InputMethodManager;
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
    private long selectedProductCod;

    private boolean isEdit = false;
    private boolean onFocusForEdit;

    private String cod;
    private long groupsID;
    private long makerID;
    //private ListView listViewProductGroup;
    //private ListView listViewMaker;
    private Cursor cursor;
    //private ListAdapter listAdapter;
    //private Context context;
    private DBAdapter db;
    //private boolean visible;
    private Activity activity;

    private long selectedProductID;
    private TextView textSelectedProductID;
    private ProductsActivity productsActivity;
    private ValidateAdapter validateAdapter;

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
        activity = getActivity();

        if (activity != null) {
            textGroup = (AutoCompleteTextView) activity.findViewById(R.id.ProductEditGroupID);
            textMaker = (AutoCompleteTextView) activity.findViewById(R.id.ProductEditMaker);

            textName = (EditText) activity.findViewById(R.id.ProductEditName);
            textCod = (EditText) activity.findViewById(R.id.ProductEditCod);
            textTip = (EditText) activity.findViewById(R.id.ProductEditTip);
            textSelectedProductID = (TextView) activity.findViewById(R.id.SelectedProductID);

            db = new DBAdapter(activity);
            //context = activity;

            validateAdapter = new ValidateAdapter();
        }

        textGroup.setOnFocusChangeListener(this);
        textName.setOnFocusChangeListener(this);
        textMaker.setOnFocusChangeListener(this);
        textCod.setOnFocusChangeListener(this);
        textTip.setOnFocusChangeListener(this);

        checkEdit();
        //listViewProductGroup = (ListView) getActivity().findViewById(R.id.Product_ListGroup);
        //listViewMaker = (ListView) getActivity().findViewById(R.id.Product_ListMaker);

        textMaker.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isEdit){
                    showMakersList();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        textGroup.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isEdit){
                    showProductsGroupList();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /********************************************Check Edit****************************************/

    public void checkEdit(){
        try {
            selectedProductID = Integer.parseInt(textSelectedProductID.getText().toString());
        }
        catch(Exception e) {
            selectedProductID = 0;
        }

        if (selectedProductID > 0) {
            showProduct();
            onFocusForEdit = true;
        }

        else {
            resetForAdd();
        }
    }

    /******************************************On focus Change*************************************/

    @Override
    public void onFocusChange(View view, boolean b) {
        if (onFocusForEdit) productsActivity.setEdit(2);

        if (view == textGroup && !b)
            validateAdapter.validate(activity, textGroup, "", 0);

        if (view == textName && !b)
            validateAdapter.validate(activity, textName, "", 0);

        if (view == textMaker && !b)
            validateAdapter.validate(activity, textMaker, "", 0);

        if (view == textCod && !b){
            if (validateAdapter.validate(activity, textCod, "", 0)){

                db.open();
                if (!db.checkProductCod(Long.parseLong(textCod.getText().toString())))
                    validateAdapter.setDuplicate(activity, textCod);
                db.close();
            }
        }
    }

      /*  if (view == getActivity().findViewById(R.id.ProductEditMaker) && b == true){
           // listViewMaker.setVisibility(View.VISIBLE);
           // showAccountsList();
        }
    }*/

    /**************************************************Show Product********************************/

    private void showProduct(){
        isEdit = false;
        resetAllValidate();

        try{
            selectedProductID = Integer.parseInt(textSelectedProductID.getText().toString());
        }
        catch(Exception e) {
            selectedProductID = 0;
        }

        db.open();
        Cursor c=db.getProduct(selectedProductID);

        if(c != null && c.moveToFirst()){
            selectedProductCod = Long.parseLong(c.getString(1));
            textGroup.setText(c.getString(5));
            textName.setText(c.getString(0));
            textMaker.setText(c.getString(4));
            textCod.setText(c.getString(1));
            textTip.setText(c.getString(2));
        }

        db.close();
    }

    /**************************************************Add product*********************************/

    public boolean addProduct(){
        isEdit = false;
        int status = 0;

        name = textName.getText().toString();
        tip = textTip.getText().toString();
        cod = textCod.getText().toString();
        groups = textGroup.getText().toString();
        maker = textMaker.getText().toString();
        long longCod = 0;

        if (validateAdapter.validate(activity, textCod, cod, 1)){
            longCod = Long.parseLong(cod);
            db.open();

            if (db.checkProductCod(longCod))
                status++;
            else {
                validateAdapter.setDuplicate(activity, textCod);
                setFocus(textCod);
            }

            db.close();
        }
        else setFocus(textCod);

        if (validateAdapter.validate(activity, textMaker, maker, 1)){
            status++;
        }
        else setFocus(textMaker);

        if (validateAdapter.validate(activity, textName, name, 1)){
            status++;
        }
        else setFocus(textName);

        if (validateAdapter.validate(activity, textGroup, groups, 1)){
            status++;
        }
        else setFocus(textGroup);

        if ( status == 4 ) {
            db.open();
            groupsID = db.getProductsGroupID(groups);
            makerID = db.getAccountsNameID(maker);

            if (groupsID < 0) {
                groupsID = db.insertProductsGroup(groups);
            }

            if (makerID < 0 ){
                long makerCod = db.MaxCod();
                makerID = db.insertAc( 1, makerCod+1 , maker, "", "", "", false, "", true);
            }

            long id;
            id = db.insertProduct(longCod, groupsID, name, makerID, true, tip);
            db.close();

            if (id > 0){
                Toast.makeText(activity, name + ' ' + getString(R.string.registred) + ' ' + id, Toast.LENGTH_SHORT).show();
                textSelectedProductID.setText(Long.toString(id));
                resetAllValidate();
                return true;

            } else if (id == -2) {
                validateAdapter.setDuplicate(activity, textCod);
                return false;

            } else if (id == -3) {
                 Toast.makeText(activity, getString(R.string.error), Toast.LENGTH_SHORT).show();
                 return false;
            }
        }
        return false;
    }

    /*********************************************Edit Product*************************************/

    public boolean editProduct() {
        isEdit = false;
        int status = 0;

        name = textName.getText().toString();
        tip = textTip.getText().toString();
        cod = textCod.getText().toString();
        groups = textGroup.getText().toString();
        maker = textMaker.getText().toString();
        long longCod = 0;

        if (validateAdapter.validate(activity, textCod, cod, 1)){
            longCod = Long.parseLong(cod);
            db.open();

            if (selectedProductCod != longCod) {
                if (db.checkProductCod(longCod))
                    status++;
                else {
                    validateAdapter.setDuplicate(activity, textCod);
                    setFocus(textCod);
                }
            }
            else status ++;
            db.close();
        }
        else setFocus(textCod);

        if (validateAdapter.validate(activity, textMaker, maker, 1)){
            status++;
        }
        else setFocus(textMaker);

        if (validateAdapter.validate(activity, textName, name, 1)){
            status++;
        }
        else setFocus(textName);

        if (validateAdapter.validate(activity, textGroup, groups, 1)){
            status++;
        }
        else setFocus(textGroup);


        if ( status == 4 ) {
            db.open();
            groupsID = db.getProductsGroupID(groups);
            makerID = db.getAccountsNameID(maker);

            if (groupsID < 0) {
                groupsID = db.insertProductsGroup(groups);
            }

            if (makerID < 0 ){
                long makerCod = db.MaxCod();
                makerID = db.insertAc( 1, makerCod+1 , maker, "", "", "", false, "", true);
            }

            long id;
            id = db.updateProduct(selectedProductID, longCod, groupsID, name, makerID, true, tip);
            db.close();

            if (id==1){
                Toast.makeText(activity, getString(R.string.registred),
                        Toast.LENGTH_SHORT).show();
                return true;
            }
            else{
                Toast.makeText(activity,getString(R.string.error),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return false;
    }

    /**************************************************Reset for Add*******************************/

    public void resetForAdd(){
        onFocusForEdit = false;
        textGroup.setText("");
        textName.setText("");
        textMaker.setText("");
        textTip.setText("");

        db.open();
        long max=  db.ProductMaxCod();

        textCod.setText (Long.toString(max+1));
        db.close();

        setFocus(textGroup);
        isEdit = true;
    }

    /******************************************Set Focus & show keyboard***************************/

    public void setFocus(EditText editText){
        if(editText.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**********************************************************Reset Validate**********************/

    public void resetAllValidate(){
        validateAdapter.resetValidate(activity, textCod);
        validateAdapter.resetValidate(activity, textMaker);
        validateAdapter.resetValidate(activity, textGroup);
        validateAdapter.resetValidate(activity, textName);
    }

    /**************************************************Show Accounts List*****8********************/

    private void showMakersList() {
        db.open();
        super.onResume();
        String filter;
        filter = textMaker.getText().toString();
        cursor = db.filterAccounts(filter);

        if(cursor != null && cursor.moveToFirst()){
            textMaker.setAdapter(new AutoCompleteAdapter(activity, cursor));
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
            textGroup.setAdapter(new AutoCompleteAdapter(activity, cursor));
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