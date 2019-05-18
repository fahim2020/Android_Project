// source : scanning Beacons: https://github.com/joelwass/Android-BLE-Scan-Example
    package de.uniweimar.beacon;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.orm.SugarDb;

import java.util.Date;

import de.uniweimar.beacon.entities.User;
import de.uniweimar.beacon.services.DataHolder;
import de.uniweimar.beacon.services.Manager;

import static android.provider.Settings.Secure.ANDROID_ID;

public class MainActivity extends AppCompatActivity {
    String deviceId="";
    EditText editTextName;
    EditText editTextBD;
    EditText textEmail;
    String numberToPass ="";
    RadioGroup rg ;
    RadioButton male , female;
    SharedPreferences prefs = null;
    Manager manager=new Manager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         // get device ID
         deviceId= Settings.Secure.getString( getContentResolver(), ANDROID_ID );

         //interaction with SQLite database
        SugarDb db = new SugarDb(this);
        db.onCreate(db.getDB());
        if(isFirstTime(getSharedPreferences("firstTime", Context.MODE_PRIVATE)))
        {
            manager.addsection("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0".toLowerCase(), "Entrance");
            manager.addsection("B9407F30-F5F8-466E-AFF9-25556B57FE6D".toLowerCase(),"First floor: North direction");
            manager.addsection("e2c56db5-dffb-48d2-b060-d0f5a71096e2".toLowerCase(),"First floor: South direction");
            manager.addsection("E2C56DB5-DFFB-48D2-B060-D0F5A71096E1".toLowerCase(),"Ground floor");
        }
        //check if device id is already exist
        User user=manager.getUserByDeviceId(deviceId);
        if(user!=null)
        {
            DataHolder.getInstance().setUser(user);
            Intent intent = new Intent(MainActivity.this,StepCounterActivity.class);
            startActivity(intent);
        }

        Button btnSave = (Button) findViewById(R.id.buttonSave);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextBD = (EditText) findViewById(R.id.editTextBD);
        textEmail = (EditText)findViewById( R.id.textEmail );
        rg = (RadioGroup) findViewById( R.id.RGroup );
        male = (RadioButton)findViewById( R.id.Mal );
        female = (RadioButton)findViewById( R.id.Fem );

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String gender = numberToPass.toString();

                manager.addUser( editTextName.getText().toString(),deviceId,gender,textEmail.getText().toString(),editTextBD.getText().toString());
                DataHolder.getInstance().setUser(manager.getUserByDeviceId(deviceId));
                Intent intent = new Intent(MainActivity.this,StepCounterActivity.class);
                startActivity(intent);
                // int userLen =(int) User.count(User.class,"userName=?",null);

            }
        });

    }


    private static Boolean firstTime = null;
    /**
     * Checks if the user is opening the app for the first time.
     * Note that this method should be placed inside an activity and it can be called multiple times.
     * @return boolean
     */

    private static boolean isFirstTime(SharedPreferences mPreferences) {
        if (firstTime == null) {
            firstTime = mPreferences.getBoolean("firstTime", true);
            if (firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
            }
        }
        return firstTime;
    }

    public void onRadioButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.Mal:
                numberToPass = "Male";
                break;
            case R.id.Fem:
                numberToPass = "Female";
                break;
        }

    }
}
