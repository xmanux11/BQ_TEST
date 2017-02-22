package com.example.manumadrid.bq_test;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ManuMadrid on 22/02/2017.
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!EvernoteSession.getInstance().isLoggedIn()) {
            finish();
        }
        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        noteStoreClient.listNotebooksAsync(new EvernoteCallback<List<Notebook>>() {
            @Override
            public void onSuccess(List<Notebook> result) {
                List<String> namesList = new ArrayList<>(result.size());
                for (Notebook notebook : result) {
                    namesList.add(notebook.getName());
                }
                String notebookNames = TextUtils.join(", ", namesList);
                Snackbar.make(getCurrentFocus(), notebookNames + "notebooks have been retrieved", Snackbar.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onException(Exception exception) {
                Log.e("MainActivity", "Error retrieving notebooks", exception);
            }
        });
    }

    public void createNote(){
        if (!EvernoteSession.getInstance().isLoggedIn()) {
            Snackbar.make(getCurrentFocus(), "error en la autentificacion", Snackbar.LENGTH_LONG)
                    .show();
            finish();
        }

        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

        Note note = new Note();
        note.setTitle("My title");
        note.setContent(EvernoteUtil.NOTE_PREFIX + "My content" + EvernoteUtil.NOTE_SUFFIX);

        noteStoreClient.createNoteAsync(note, new EvernoteCallback<Note>() {
            @Override
            public void onSuccess(Note result) {
                Snackbar.make(getCurrentFocus(),result.getTitle()+ "has been created", Snackbar.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onException(Exception exception) {
                Snackbar.make(getCurrentFocus(),"no se ha podido crear la nota", Snackbar.LENGTH_LONG)
                        .show();
                Log.e("MainActivity", "Error creating note", exception);
            }
        });
    }

    public void muestraNota(){

    }


}
