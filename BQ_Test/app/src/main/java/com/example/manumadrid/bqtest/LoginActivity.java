package com.example.manumadrid.bqtest;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import com.evernote.client.android.EvernoteSession;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    private static final String CONSUMER_KEY = "manu-ramos-1";
    private static final String CONSUMER_SECRET = "d48315a7bdf66b59";
    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;
    public static EvernoteSession mEvernoteSession = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        
//        Para capturar las excepciones que se dan dentro de la api de evernote a la hora de loguear
        Thread.currentThread().setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                closeLogin();
                Snackbar.make(findViewById(android.R.id.content), "Fallo en los datos, compruebelos y pruebe de nuevo", Snackbar.LENGTH_LONG)
                        .show();

            }
        });
    }

    /**
     * Inicializa la actividad de login de la api de Evernote con la API key suministrada por evernote para esta APP
     */
    private void attemptLogin() {


        mEvernoteSession = new EvernoteSession.Builder(this)
                .setEvernoteService(EVERNOTE_SERVICE)
                .setSupportAppLinkedNotebooks(true)
                .setForceAuthenticationInThirdPartyApp(true)
                .build(CONSUMER_KEY, CONSUMER_SECRET)
                .asSingleton();
        mEvernoteSession.authenticate(this);

    }

    /**
     * metodo para hacer dismiss del login cuando el usuario no se puede loguear debido a fallo en credenciales
     */
    public void closeLogin() {
        List<Fragment> fragments = this.getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof DialogFragment) {
                    ((DialogFragment) fragment).dismiss();
                }
            }
        }
    }

    /**
     * Segun el resultado del loguin avanzamos hacia la clase principal o no
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                this.startActivity(intent);
                finishAffinity();
            } catch (Error e) {
                Log.d("Loginactivity", "error: " + e.getMessage());
            }
        } else {

            Snackbar.make(findViewById(android.R.id.content), "Fallo en los datos, compruebelos y pruebe de nuevo", Snackbar.LENGTH_LONG)
                    .show();
        }
    }
}

