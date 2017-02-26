package com.example.manumadrid.bqtest;


import android.app.Activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.client.android.EvernoteSession;

import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.type.Note;

import com.evernote.thrift.TException;



import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by ManuMadrid on 22/02/2017.
 */

public class MainActivity extends Activity {
    ArrayList<NoteBQ> listaNotas = new ArrayList<>();
    private NoteStoreClient noteStoreClient;
    ListView lista;
    Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (!EvernoteSession.getInstance().isLoggedIn()) {
            finish();
        }
        FloatingActionButton fab_add_note = (FloatingActionButton) findViewById(R.id.fab_add_note);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        context=this;
        lista = (ListView) findViewById(R.id.lista_notas);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapter, View view, int position, long arg) {
                NoteBQ item = (NoteBQ) lista.getAdapter().getItem(position);
                String title = item.getTitle();
                String body = item.getBody();
                Intent mIntent = new Intent(getApplicationContext(), NoteFragment.class);
                Bundle extras = new Bundle();
                extras.putString("title", title);
                extras.putString("body", body);
                mIntent.putExtras(extras);
                startActivity(mIntent);


            }
        });
        fab_add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), CreateNote.class), 1);

            }
        });
        final Spinner dropdown = (Spinner) findViewById(R.id.desplegable);
        String[] items = new String[]{"Fecha", "Titulo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<NoteBQ> listaNotasAux = listaNotas;
                switch (position) {
                    case 0:
                        listaNotas = new ArrayList<NoteBQ>();
                        Collections.sort(listaNotasAux, new Comparator<NoteBQ>() {
                            @Override
                            public int compare(NoteBQ o1, NoteBQ o2) {
                                int res = 0;
                                if (o1.getFechaCreacion() > o2.getFechaCreacion()) {
                                    res = -1;
                                }
                                if (o1.getFechaCreacion() < o2.getFechaCreacion()) {
                                    res = 1;
                                }
                                return res;
                            }
                        });
                        listaNotas = listaNotasAux;
                        lista.invalidateViews();
                        lista.setAdapter(new NoteAdapter(context, listaNotas));
                        break;
                    case 1:
                        listaNotas = new ArrayList<NoteBQ>();
                        Collections.sort(listaNotasAux, new Comparator<NoteBQ>() {
                            @Override
                            public int compare(NoteBQ o1, NoteBQ o2) {
                                return o1.getTitle().compareToIgnoreCase(o2.getTitle());
                            }
                        });
                        listaNotas = listaNotasAux;
                        lista.invalidateViews();
                        lista.setAdapter(new NoteAdapter(context, listaNotas));
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void updateList() {

        EvernoteAuth evernoteAuth = new EvernoteAuth(EvernoteService.SANDBOX, EvernoteSession.getInstance().getAuthToken());
        ClientFactory factory = new ClientFactory(evernoteAuth);
        try {
            noteStoreClient = factory.createNoteStoreClient();
        } catch (EDAMUserException e) {
            e.printStackTrace();
        } catch (EDAMSystemException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }

        try {
            listaNotas = new ArrayList<>();
            List<Note> notes = noteStoreClient.findNotes(new NoteFilter(), 0, 1000).getNotes();
            List<Note> notesWithContent = new ArrayList<>();
            for (Note note : notes) {
                notesWithContent.add(noteStoreClient.getNote(note.getGuid(), true, false, false, false));
            }
            for (Note nota : notesWithContent) {
                String content = nota.getContent();
                String contentParse = content.substring(content.indexOf("<en-note>") + 9, content.indexOf("</en-note>"));
                NoteBQ note = new NoteBQ(nota.getTitle(), contentParse, nota.getCreated());
                listaNotas.add(note);
            }
        } catch (EDAMUserException | EDAMSystemException | EDAMNotFoundException | TException e) {
            Log.d("Main activity", "Unexpected error obtaining notes using Evernote SDK", e);

        }
        lista.setAdapter(new NoteAdapter(this, listaNotas));
    }

    @Override
    protected void onResume() {
        super.onResume();
        lista.invalidateViews();
        updateList();
    }
}
