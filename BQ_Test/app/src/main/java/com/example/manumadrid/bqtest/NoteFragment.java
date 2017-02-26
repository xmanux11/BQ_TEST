package com.example.manumadrid.bqtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.thrift.TException;

/**
 * Created by ManuMadrid on 25/02/2017.
 */

public class NoteFragment extends Activity {

    private String titulo;
    private String cuerpo;

    public NoteFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_fragment);

        if (getIntent().getExtras()!=null&&getIntent().getExtras().containsKey("title")) {
            titulo = getIntent().getExtras().getString("title");
        }
        if (getIntent().getExtras()!=null&&getIntent().getExtras().containsKey("body")) {
            cuerpo = getIntent().getExtras().getString("body");
        }

        if (titulo != null) {
            ((TextView) findViewById(R.id.titulo)).setText(titulo);
        }
        if(cuerpo!=null){
            ((TextView) findViewById(R.id.cuerpo)).setText(cuerpo);
        }
    }

}