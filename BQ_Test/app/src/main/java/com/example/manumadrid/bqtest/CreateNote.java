package com.example.manumadrid.bqtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.thrift.TException;

/**
 * Created by ManuMadrid on 25/02/2017.
 */

public class CreateNote extends Activity {



    public CreateNote(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_note_fragment);
        Button create= (Button) findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText titulo=((EditText) findViewById(R.id.titulo));
                EditText cuerpo=((EditText) findViewById(R.id.cuerpo));
                final String tituloText= titulo.getText().toString();
                final String cuerpoText=cuerpo.getText().toString();
                if(tituloText!=null&&!tituloText.isEmpty()){
                    NoteBQ note= new NoteBQ(tituloText,cuerpoText, System.currentTimeMillis()/1000);
                    createNote(note);
                }
            }
        });

    }


    public void createNote(NoteBQ noteBQ) {

        Note note = new Note();
        note.setTitle(noteBQ.getTitle());
        note.setContent(EvernoteUtil.NOTE_PREFIX + noteBQ.getBody() + EvernoteUtil.NOTE_SUFFIX);
//        String developerToken = "S=s1:U=9366f:E=161d300a51a:C=15a7b4f77d0:P=1cd:A=en-devtoken:V=2:H=a7010c31118a826bb0326d24108057b3";
        EvernoteAuth evernoteAuth = new EvernoteAuth(EvernoteService.SANDBOX, EvernoteSession.getInstance().getAuthToken());
        ClientFactory factory = new ClientFactory(evernoteAuth);
        NoteStoreClient noteStoreClient;
        try {
            noteStoreClient = factory.createNoteStoreClient();
            noteStoreClient.createNote(note);
        } catch (EDAMUserException e) {
            e.printStackTrace();
        } catch (EDAMSystemException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        } catch (EDAMNotFoundException e) {
            Log.d("CreateNote","Unexpected error adding note using Evernote SDK", e);
        }
//        setResult(Activity.RESULT_OK);
        finish();
    }
}