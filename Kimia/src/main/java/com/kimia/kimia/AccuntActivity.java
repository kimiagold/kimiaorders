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

//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.view.ViewPager;

public class AccuntActivity extends Activity {

    boolean isSinglePane;
    TextView id;
    //  DBAdapter db = new DBAdapter(this);

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
     /*   String languageToLoad  = "fa";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;*/

        Bundle bundle = new Bundle();
        //super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accunt);

        id = (TextView) findViewById(R.id.ID);

      //  View v = findViewById(R.id.phonelist);
        //if(v == null){
            //it's run on tablet
          //  isSinglePane = false;


     //       bundle.putString("singlepane","false");
// set Fragmentclass Arguments
       //     UserActivity fragment = new UserActivity();
         //   fragment.setArguments(bundle);
   /*
    * MyListFragment and MyDetailFragment have been loaded in XML,
    * no need load.
    */

     //   }else{
            //it's run on phone
            //Load MyListFragment programmatically
       //     isSinglePane = true;

         //   if(savedInstanceState == null){
                //if's the first time created

           /*           FragmentManager fragmentManager = getFragmentManager();
                   FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


                UserActivity fragment = new UserActivity();

                  fragmentTransaction.replace(R.id.phonelist, fragment);*/

             // fragmentTransaction.commit();
        //    setContentView(R.layout.activity_accunt);



             /*  FragmentManager fragmentManager = getFragmentManager();
               FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                UserActivity fragment = new UserActivity();
                fragmentTransaction.replace(R.id.phonelist, fragment);
*/
              //  bundle.putString("singlepane","true");
// set Fragmentclass Arguments
             //   fragment.setArguments(bundle);
                //fra
              //  fragmentTransaction.commit();*/

//        }
        //   getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

      /*  FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        WindowManager wm = getWindowManager();
        Display d = wm.getDefaultDisplay();
        if (d.getWidth()>d.getHeight()){
            UsershowActivity fragment11 = new UsershowActivity();
            fragmentTransaction.replace(android.R.id.content,fragment11);

        }*/
        // setContentView(R.layout.activity_accunt);

    }
    /*
    public static class MyDetailFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            View view = inflater.inflate(R.layout.layout_detailfragment, null);
            return view;
        }*/


 //   void setMessage(String msg){
        //   ID = Integer.parseInt(msg);
     //   onStart();
 //   }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

            //   if (null != mAdapter) {
            //   mAdapter.getFilter().filter(s);
            // }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };

    //  @Override
    // protected void onCreate(Bundle savedInstanceState) {

    //  }

    //@Override
    public void onButtonPressed() {
        // TODO Auto-generated method stub
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

    //NEW
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_add:
                startActivity(new Intent("com.kimia.kimia.AddActivity"));
               // invalidateOptionsMenu();
                break;

            case R.id.action_edit:

                Intent i = new Intent(getApplicationContext(), com.kimia.kimia.EditActivity.class);
                i.putExtra("ID",id.getText().toString());
                startActivity(i);


                //  startActivity(new Intent("com.kimia.kimia.EditActivity"));
                break;

            case R.id.action_delete:
                DBAdapter db = new DBAdapter(this);
                db.open();
                boolean c=db.deleteAc(Long.parseLong(id.getText().toString()));

                if (c==true){
                    Toast.makeText(this,getString(R.string.deleted),
                            Toast.LENGTH_SHORT).show();
                    //finish();
                 //        UserActivity testFrag = (UserActivity) .getFragmentById(R.id.activity_user);

                    UserActivity fragment = (UserActivity) getFragmentManager().findFragmentById(R.id.activity_user);
                    fragment.onStart();




                    //  if(testFrag != null && testFrag.isAdded())
                    //   testFrag.onStart();
                }
                else
                    Toast.makeText(this, getString(R.string.error),
                            Toast.LENGTH_SHORT).show();



                db.close();
                //    finish();
                break;


            case R.id.action_settings:
                Toast.makeText(this, "Action Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
       // return true;
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



    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);

        final EditText editText = (EditText) menu.findItem(
                R.id.menu_search).getActionView();
        editText.addTextChangedListener(textWatcher);

        MenuItem menuItem = menu.findItem(R.id.menu_search);
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true; // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                editText.clearFocus();
                return true; // Return true to expand action view
            }
        });
    }*/

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
     //   searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }*/



}