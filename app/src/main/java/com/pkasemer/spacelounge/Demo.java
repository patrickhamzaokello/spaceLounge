package com.pkasemer.spacelounge;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.pkasemer.spacelounge.Adapters.NameAdapter;
import com.pkasemer.spacelounge.HttpRequests.RequestHandler;
import com.pkasemer.spacelounge.HttpRequests.URLs;
import com.pkasemer.spacelounge.Models.Name;
import com.pkasemer.spacelounge.Utils.NetworkStateChecker;
import com.pkasemer.spacelounge.localDatabase.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Demo extends  AppCompatActivity implements View.OnClickListener {

    /*
     * this is the url to our webservice
     * make sure you are using the ip instead of localhost
     * it will not work if you are using localhost
     * */

    //database helper object
    private DatabaseHelper db;

    //View objects
    private Button buttonSave;
    private EditText editTextName;
    private ListView listViewNames;

    //List to store all the names
    private List<Name> names;

    //1 means data is synced and 0 means data is not synced
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;

    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";

    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;

    //adapterobject for list view
    private NameAdapter nameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        //initializing views and objects
        db = new DatabaseHelper(this);
        names = new ArrayList<>();

        buttonSave = findViewById(R.id.buttonSave);
        editTextName = findViewById(R.id.editTextName);
        listViewNames = findViewById(R.id.listViewNames);

        //adding click listener to button
        buttonSave.setOnClickListener(this);

        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        //calling the method to load all the stored names
        loadNames();

        //the broadcast receiver to update sync status
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //loading the names again
                loadNames();
            }
        };

        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));
    }

    /*
     * this method will
     * load the names from the database
     * with updated sync status
     * */
    private void loadNames() {
        names.clear();
        Cursor cursor = db.getNames();
        if (cursor.moveToFirst()) {
            do {
                Name name = new Name(
                        cursor.getString(1),
                        Integer.parseInt(cursor.getString(2))
                );
                names.add(name);
            } while (cursor.moveToNext());
        }

        nameAdapter = new NameAdapter(this, R.layout.names, names);
        listViewNames.setAdapter(nameAdapter);
    }

    /*
     * this method will simply refresh the list
     * */
    private void refreshList() {
        nameAdapter.notifyDataSetChanged();
    }

    /*
     * this method is saving the name to ther server
     * */

    private void saveNameToServer(){

        final ProgressDialog progressDialog = new ProgressDialog(this);


        final String name = editTextName.getText().toString().trim();


        class NamePostRequest extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setMessage("Saving Name...");
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                if(s.isEmpty()){
                    //show network error
                    progressDialog.dismiss();
                    //on error storing the name to sqlite with status unsynced
                    saveNameToLocalStorage(name, NAME_NOT_SYNCED_WITH_SERVER);
                    return;
                }

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        saveNameToLocalStorage(name, NAME_SYNCED_WITH_SERVER);
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                    } else {
                        //Invalid Username or Password
                        saveNameToLocalStorage(name, NAME_NOT_SYNCED_WITH_SERVER);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("name", name);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_SEND_ORDER, params);
            }
        }

        NamePostRequest ul = new NamePostRequest();
        ul.execute();
    }



    //saving the name to local storage
    private void saveNameToLocalStorage(String name, int status) {
        editTextName.setText("");
        db.addName(name, status);
        Name n = new Name(name, status);
        names.add(n);
        refreshList();
    }


    @Override
    public void onClick(View view) {
        saveNameToServer();
    }
}