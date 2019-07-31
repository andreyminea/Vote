package com.e.meteo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private static final int AUTH_CODE = 112;
    List<AuthUI.IdpConfig> providers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Initialize providers
        providers  = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
         //     new AuthUI.IdpConfig.GoogleBuilder().build()
         //     new AuthUI.IdpConfig.PhoneBuilder().build()
                );

        showSignInOptions();

    }

    private void showSignInOptions()
    {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers).setTheme(R.style.MyTheme).build(),AUTH_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AUTH_CODE)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK )
            {
                // Get user info
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                Intent intent =  new Intent(MainActivity.this, VoteActivity.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(this, ""+response.getError().getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }


    }
}
