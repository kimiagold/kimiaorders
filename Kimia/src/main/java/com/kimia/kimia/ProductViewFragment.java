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

import java.util.Random;

public class ProductViewFragment extends Fragment implements View.OnFocusChangeListener {

    private Bundle savedState = null;


    private static final String ALLOWED_CHARACTERS ="ضصثقفغعهخحجچچگکمنتالبیسشظطزرذدپو.۱۲۳۴۵۶۷۸۹۰";

    private AutoCompleteTextView textGroup;
    private AutoCompleteTextView textMaker;
    private AutoCompleteTextView textName;

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
    private Cursor cursor;
    //private ListAdapter listAdapter;
    //private Context context;
    private DBAdapter db;
    //private boolean visible;
    private Activity activity;

    private long selectedProductID;
    //private TextView textSelectedProductID;
    private ProductsActivity productsActivity;
    private ValidateAdapter validateAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container ,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_product_view, null);

       // return inflater.inflate(R.layout.fragment_product_view, container, false);
        return view;
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
    public void onStart() {

        super.onStart();
        activity = getActivity();

        if (activity != null) {
            textGroup = (AutoCompleteTextView) activity.findViewById(R.id.ProductEditGroupID);
            textMaker = (AutoCompleteTextView) activity.findViewById(R.id.ProductEditMaker);
            textName = (AutoCompleteTextView) activity.findViewById(R.id.ProductEditName);

            textCod = (EditText) activity.findViewById(R.id.ProductEditCod);
            textTip = (EditText) activity.findViewById(R.id.ProductEditTip);
            //textSelectedProductID = (TextView) activity.findViewById(R.id.SelectedProductID);

            db = new DBAdapter(activity);
            //context = activity;

            validateAdapter = new ValidateAdapter();
        }

        textGroup.setOnFocusChangeListener(this);
        textName.setOnFocusChangeListener(this);
        textMaker.setOnFocusChangeListener(this);
        textCod.setOnFocusChangeListener(this);
        textTip.setOnFocusChangeListener(this);

        checkFirsItem();

        /************************************TextChangedListener***********************************/

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

        textName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isEdit){
                    showNameList();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /********************************************Check Edit****************************************/

    public void checkFirsItem() {
        selectedProductID = productsActivity.getSelectedProductID();

        if (selectedProductID > 0) {
            showProduct();
            onFocusForEdit = true;
        }

        else {
            resetForAdd(true);
        }
    }

    /******************************************On focus Change*************************************/

    @Override
    public void onFocusChange(View view, boolean b) {
        if (onFocusForEdit) {
            productsActivity.setEdit(false);
            isEdit = true;
        }

        if (view == textGroup && b)
            showProductsGroupList();

        if (view == textMaker && b)
            showMakersList();

        if (view == textName && b)
            showNameList();

        if (view == textGroup && b && textGroup.getText().toString().trim().length() > 0)
            textGroup.dismissDropDown();

        if (view == textMaker && b && textMaker.getText().toString().trim().length() > 0)
            textMaker.dismissDropDown();

        if (view == textName && b && textName.getText().toString().trim().length() > 0)
            textName.dismissDropDown();

        if (view == textGroup && !b)
            validateAdapter.validateEditText(activity, textGroup);

        if (view == textName && !b)
            validateAdapter.validateEditText(activity, textName);

        if (view == textMaker && !b)
            validateAdapter.validateEditText(activity, textMaker);

        if (view == textCod && !b){
            long longCod;
            cod = textCod.getText().toString();

            if (validateAdapter.validateString(activity, textCod, cod)){
                longCod = Long.parseLong(cod);

                if (selectedProductCod != longCod) {
                    db.open();
                    if (!db.checkProductCod(longCod))
                        validateAdapter.setDuplicate(activity, textCod);

                    db.close();
                }
            }
        }
    }

    /**************************************************Show Product********************************/

    private void showProduct() {
        isEdit = false;
        resetAllValidate();
        selectedProductID = productsActivity.getSelectedProductID();

        db.open();
        Cursor c=db.getProduct(selectedProductID);

        if (c != null && c.moveToFirst()) {
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
        longCod = Long.parseLong(cod);

    /*    if (validateAdapter.validateString(activity, textCod, cod)){
            longCod = Long.parseLong(cod);
            db.open();

            if (db.checkProductCod(longCod))*/
                status++;
           /* else {
                validateAdapter.setDuplicate(activity, textCod);
                setFocus(textCod);
            }

            db.close();
        }
        else setFocus(textCod);*/

        if (validateAdapter.validateString(activity, textMaker, maker)){
            status++;
        }
        else setFocus(textMaker);

        if (validateAdapter.validateString(activity, textName, name)){
            status++;
        }
        else setFocus(textName);

        if (validateAdapter.validateString(activity, textGroup, groups)){
            status++;
        }
        else setFocus(textGroup);

        if ( status == 4 ) {


            /*db.open();
            for (int i = 0 ; i<20 ; i++){
            groups = getRandomString(10);
            maker = getRandomString(10);
            name = getRandomString(10);
            tip = getRandomString(10);
            longCod++;*/



            db.open();
            groupsID = db.getProductsGroupID(groups);
            makerID = db.getAccountsNameID(maker);

            if (groupsID < 0) {
                groupsID = db.insertProductsGroup(groups);
            }

            if (makerID < 0 ){
                long makerCod = db.MaxCod();
                makerID = db.insertAc( 1, makerCod+1 , maker, "", "", "", false, "", true, 0);
            }

            long id;
            id = db.insertProduct(longCod, groupsID, name, makerID, true, tip);
            db.plusMaker(makerID);
            db.plusGroup(groupsID);
            db.close();
            //}
            //db.close();

            if (id > 0){
                Toast.makeText(activity, name + ' ' + getString(R.string.registred), Toast.LENGTH_SHORT).show();
                productsActivity.setSelectedProductID(id);
                resetAllValidate();
                return true;

            } else if (id == -2) {
                validateAdapter.setDuplicate(activity, textCod);
                return false;

            } else if (id == -3) {
                validateAdapter.setDuplicate2(activity, textName);
                setFocus(textName);
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

        if (validateAdapter.validateString(activity, textCod, cod)){
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

        if (validateAdapter.validateString(activity, textMaker, maker)){
            status++;
        }
        else setFocus(textMaker);

        if (validateAdapter.validateString(activity, textName, name)){
            status++;
        }
        else setFocus(textName);

        if (validateAdapter.validateString(activity, textGroup, groups)){
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
                makerID = db.insertAc( 1, makerCod+1 , maker, "", "", "", false, "", true, 0);
            }

            long gID1 = 0;
            long mID1 = 0;
            long mID = 0;
            long gID = 0;

            Cursor c = db.getDeleteProduct(selectedProductID);
            if (c != null && c.moveToFirst()) {
                gID = c.getLong(0);
                mID = c.getLong(1);
            }

            long id;
            id = db.updateProduct(selectedProductID, longCod, groupsID, name, makerID, true, tip);

            Cursor c1 = db.getDeleteProduct(selectedProductID);
            if (c1 != null && c1.moveToFirst()) {
                gID1 = c1.getLong(0);
                mID1 = c1.getLong(1);
            }
            db.close();

            if (id > 0){
                Toast.makeText(activity, name + ' ' + getString(R.string.Edited), Toast.LENGTH_SHORT).show();

                db.open();
                if (gID != gID1) {
                    db.minusGroup(gID);
                    db.plusGroup(gID1);
                }

                if (mID != mID1) {
                    db.minusMaker(mID);
                    db.plusMaker(mID1);
                }
                db.close();
                //textSelectedProductID.setText(Long.toString(id));
                resetAllValidate();
                return true;

            } else if (id == -2) {
                validateAdapter.setDuplicate(activity, textCod);
                return false;

            } else if (id == -3) {
                validateAdapter.setDuplicate2(activity, textName);
                setFocus(textName);
                return false;
            }
        }
        return false;
    }

    /**************************************************Reset for Add*******************************/

    public void resetForAdd(boolean edit) {
        onFocusForEdit = false;

        if (edit) {

            textName.setText("");
            textTip.setText("");

            db.open();

            long max=  db.ProductMaxCod();
            textCod.setText(Long.toString(max + 1));

            cursor = db.lastProduct();

            if (cursor!=null && cursor.moveToFirst()) {
                textGroup.setText(cursor.getString(0));
                textMaker.setText(cursor.getString(1));
                setFocus(textName);
            } else {
                textGroup.setText("");
                textMaker.setText("");
                setFocus(textGroup);
            }

            db.close();
        }

        isEdit = true;
    }

    /******************************************Set Focus & show keyboard***************************/

    public void setFocus(EditText editText) {
        if(editText.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**********************************************************Reset Validate**********************/

    public void resetAllValidate() {
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
        cursor.moveToFirst();
        textMaker.setAdapter(new AutoCompleteAdapter(activity, cursor, true));
        db.close();
        textMaker.setOnCreateContextMenuListener(this);
        textMaker.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3){
                TextView textViewName = (TextView)v.findViewById(R.id.username);
                String name;
                name = textViewName.getText().toString();
                textMaker.setText(name);
                textMaker.setSelection(textMaker.getText().length());
            }
        });
    }

    /**************************************************Show Products Group List********************/

    private void showProductsGroupList() {
        db.open();
        super.onResume();
        String filter;
        filter = textGroup.getText().toString();
        cursor = db.filterProductsGroup(filter);
        cursor.moveToFirst();
        textGroup.setAdapter(new AutoCompleteAdapter(activity, cursor , false));
        db.close();
        textGroup.setOnCreateContextMenuListener(this);
        textGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                TextView textViewName = (TextView) v.findViewById(R.id.username);
                String name;
                name = textViewName.getText().toString();
                textGroup.setText(name);
                textGroup.setSelection(textGroup.getText().length());
            }
        });
    }

    /*************************************************Show Name List*******************************/

    private void showNameList() {
        db.open();
        super.onResume();
        String filter;
        filter = textName.getText().toString();
        cursor = db.filterProductsName(filter);
        cursor.moveToFirst();
        textName.setAdapter(new AutoCompleteAdapter(activity, cursor , false));
        db.close();
        textName.setOnCreateContextMenuListener(this);
        textName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                TextView textViewName = (TextView) v.findViewById(R.id.username);
                String name;
                name = textViewName.getText().toString();
                textName.setText(name);
                textName.setSelection(textName.getText().length());
            }
        });
    }

    /*************************************************Save state***********************************/

    public Bundle saveState() { /* called either from onDestroyView() or onSaveInstanceState() */
        Bundle outState = new Bundle();
        outState.putString("textGroup", textGroup.getText().toString());
        outState.putString("textName", textName.getText().toString());
        outState.putString("textMaker", textMaker.getText().toString());
        outState.putString("textCod", textCod.getText().toString());
        outState.putString("textTip", textTip.getText().toString());

        int viewId = activity.getCurrentFocus().getId();
        outState.putInt("hasFocus", viewId);

        int end;
        int start;
        try {
            EditText editText = (EditText) activity.findViewById(viewId);
            end = editText.getSelectionEnd();
            start = editText.getSelectionStart();
        } catch (Exception e) {
            end = 0;
            start = 0;
        }

        outState.putInt("selectionStart", start);
        outState.putInt("selectionEnd", end);
        return outState;
    }

    /*************************************************Restore state********************************/

    public void setState(Bundle state) {
        savedState = state;
        if(savedState != null){
            textGroup.setText(savedState.getString("textGroup"));
            textName.setText(savedState.getString("textName"));
            textMaker.setText(savedState.getString("textMaker"));
            textCod.setText(savedState.getString("textCod"));
            textTip.setText(savedState.getString("textTip"));
            int viewId = savedState.getInt("hasFocus");
            int start = savedState.getInt("selectionStart");
            int end = savedState.getInt("selectionEnd");

            try {
                EditText editText = (EditText) activity.findViewById(viewId);
                setFocus(editText);
                editText.setSelection(start, end);
            } catch (Exception e) {
                View view = activity.findViewById(viewId);
                view.requestFocus();
            }
        }
        savedState = null;
    }

    private static String getRandomString(final int sizeOfRandomString){
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder();
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }
}

