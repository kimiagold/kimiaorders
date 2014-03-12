package com.kimia.kimia;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;

public class ValidateAdapter extends Activity{

    public boolean validate(Context context, EditText editText,String string, int n){
        boolean status;
        switch (n){
            case 0:
                string = editText.getText().toString();
                if (string != null && !string.isEmpty()){
                    editText.setError(null);
                    status = true;
                }
                else {
                    editText.setError(context.getString(R.string.is_empty));
                    status = false;
                }
                break;

            case 1:
                if (string != null && !string.isEmpty()){
                    editText.setError(null);
                    status = true;
                }
                else {
                    editText.setError(context.getString(R.string.is_empty));
                    status = false;
                }
                break;

            default:
                status = false;
                break;
            }

        return status;
    }

    public void setDuplicate(Context context, EditText editText){
        editText.setError(context.getString(R.string.error_cod_duplicate));
    }

    public void resetValidate(Context context, EditText editText){
        editText.setError(null);
    }
}
