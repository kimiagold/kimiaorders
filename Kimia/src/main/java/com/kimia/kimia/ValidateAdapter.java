package com.kimia.kimia;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;

public class ValidateAdapter extends Activity{

    public boolean validateEditText(Context context, EditText editText){
        String string = editText.getText().toString();
        if (string != null && !string.isEmpty()){
            editText.setError(null);
            return true;
        }
        else {
            editText.setError(context.getString(R.string.is_empty));
            return false;
        }
    }

    public boolean validateString(Context context, EditText editText,String string){
        if (string != null && !string.isEmpty()){
            editText.setError(null);
            return true;
        }
        else {
            editText.setError(context.getString(R.string.is_empty));
            return false;
        }
    }

    public void setDuplicate(Context context, EditText editText){
        editText.setError(context.getString(R.string.error_cod_duplicate));
    }

    public void setDuplicate2(Context context, EditText editText){
        editText.setError("Name & gruops is duplicate");
    }

    public void resetValidate(Context context, EditText editText){
        editText.setError(null);
    }
}
