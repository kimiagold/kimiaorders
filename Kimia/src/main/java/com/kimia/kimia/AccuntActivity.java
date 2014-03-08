package com.kimia.kimia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class AccuntActivity extends Activity {

    boolean isSinglePane;
    TextView id;

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
     /*   String languageToLoad  = "fa";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;*/

        Bundle bundle = new Bundle();
        setContentView(R.layout.activity_accunt);

        id = (TextView) findViewById(R.id.ID);
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    //@Override
    public void onButtonPressed() {
        UsershowActivity Obj=(UsershowActivity) getFragmentManager().findFragmentById(R.id.activity_usershow);
        if (Obj != null) {
            Obj.setMessage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.accunt_menu, menu);
        return true;
    }

    boolean canAddItem = true;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_add:
                startActivity(new Intent("com.kimia.kimia.AddActivity"));
                break;

            case R.id.action_edit:

                Intent i = new Intent(getApplicationContext(), com.kimia.kimia.EditActivity.class);
                i.putExtra("ID",id.getText().toString());
                startActivity(i);
                break;

            case R.id.action_delete:
                DBAdapter db = new DBAdapter(this);
                db.open();
                boolean c=db.deleteAc(Long.parseLong(id.getText().toString()));

                if (c==true){
                    Toast.makeText(this,getString(R.string.deleted),
                            Toast.LENGTH_SHORT).show();

                    UserActivity fragment = (UserActivity) getFragmentManager().findFragmentById(R.id.activity_user);
                    fragment.onStart();
                }
                else
                    Toast.makeText(this, getString(R.string.error),
                            Toast.LENGTH_SHORT).show();

                db.close();
                break;

            case R.id.action_settings:
                Toast.makeText(this, "Action Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(canAddItem){
            menu.getItem(0).setIcon(R.drawable.ic_action_search);
            MenuItem mi = menu.add("New Item");
            mi.setIcon(R.drawable.ic_pic);
            mi.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            canAddItem = false;
        }
        else{
            menu.getItem(0).setIcon(R.drawable.ic_action_accept);
            canAddItem = true;
        }

        return super.onPrepareOptionsMenu(menu);
    }
}