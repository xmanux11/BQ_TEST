package com.example.manumadrid.bqtest;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.client.android.EvernoteOAuthActivity;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.login.EvernoteLoginActivity;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.userstore.UserStore;


import java.util.List;
import java.util.Locale;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "bq", "bqpass"};

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private static final String CONSUMER_KEY = "manu-ramos-1";
    private static final String CONSUMER_SECRET = "d48315a7bdf66b59";
    private static final String EXTRA_SUPPORT_APP_LINKED_NOTEBOOKS = "EXTRA_SUPPORT_APP_LINKED_NOTEBOOKS";
    private static final String EXTRA_LOCALE = "EXTRA_LOCALE";
    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;
    public static EvernoteSession mEvernoteSession = null;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        context = this;
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        //Para capturar las excepciones que se dan dentro de la api de evernote a la hora de loguear
//        Thread.currentThread().setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
//                closeLogin();
//                Snackbar.make(findViewById(android.R.id.content), "Fallo en los datos, compruebelos y pruebe de nuevo", Snackbar.LENGTH_LONG)
//                        .show();
//
//            }
//        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if (email.equals(DUMMY_CREDENTIALS[0])) {
                email = CONSUMER_KEY;
            }
            if (password.equals(DUMMY_CREDENTIALS[1])) {
                password = CONSUMER_SECRET;
            }
            mEvernoteSession=new EvernoteSession.Builder(this)
                    .setEvernoteService(EVERNOTE_SERVICE)
                    .setSupportAppLinkedNotebooks(true)
                    .setForceAuthenticationInThirdPartyApp(true)
                    .build(email, password)
                    .asSingleton();
            this.startActivityForResult(EvernoteLoginActivity.createIntent(getApplicationContext(), email, password, true, Locale.getDefault()), 14390);


        }
    }

    private boolean isEmailValid(String email) {
        return email.length() > 1;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 2;
    }

    /**
     * metodo para hacer dismiss del login cuando el usuario no se puede loguear debido a fallo en credenciales
     */
    public  void closeLogin() {
        List<Fragment> fragments = this.getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof DialogFragment) {
                    ((DialogFragment) fragment).dismiss();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 14390:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        this.startActivity(intent);
                        finishAffinity();
                    }catch (Error e){
                        Log.d("Loginactivity","error: "+e.getMessage());
                    }
                } else {

                    Snackbar.make(findViewById(android.R.id.content), "Fallo en los datos, compruebelos y pruebe de nuevo", Snackbar.LENGTH_LONG)
                            .show();

                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}

