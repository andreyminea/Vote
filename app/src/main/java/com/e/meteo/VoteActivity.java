package com.e.meteo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class VoteActivity extends AppCompatActivity {

    Boolean ok = false;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button vote;
    private final static int SEND_CODE = 5316;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        radioGroup = findViewById(R.id.radiogroup);
        vote = findViewById(R.id.vote_btn);

        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ok)
                {
                    if(checkPermission(Manifest.permission.SEND_SMS))
                    {
                        String msg = radioButton.getText().toString();
                        String phoneNumeber = "0751014390";
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumeber, null, msg, null, null);
                        Toast.makeText(VoteActivity.this, "Vote submitted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VoteActivity.this, SubmittedActivity.class);
                        startActivity(intent);
                        finish();

                    }
                    else
                    {
                        ActivityCompat.requestPermissions(VoteActivity.this, new String[]{Manifest.permission.SEND_SMS}, SEND_CODE);
                    }
                }
            }
        });

    }
    private boolean checkPermission(String sendSms)
    {
        int checkPerm = ContextCompat.checkSelfPermission(this, sendSms);
        return checkPerm == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case SEND_CODE :
                if(grantResults.length>0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
                {
                    Toast.makeText(VoteActivity.this, "SMS Permission granted", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void voteSelected(View v)
    {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        ok = true;
        Toast.makeText(this, "Selected: "+radioButton.getText().toString(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.signOutOption:
                AuthUI.getInstance()
                        .signOut(VoteActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                Toast.makeText(VoteActivity.this, "You have logged out", Toast.LENGTH_SHORT).show();
                            }
                        });
                Intent Intent = new Intent (VoteActivity.this, MainActivity.class);
                startActivity(Intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
