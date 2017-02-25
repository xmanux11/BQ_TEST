package com.example.manumadrid.bqtest;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.TException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ManuMadrid on 22/02/2017.
 */

public class MainActivity extends FragmentActivity {
    ArrayList<NoteBQ> listaNotas=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (!EvernoteSession.getInstance().isLoggedIn()) {
            finish();
        }
        FrameLayout fragment= (FrameLayout)findViewById(R.id.framelayout_contenedor_detalle);

        final ListView lista= (ListView) findViewById(R.id.lista_notas);

        final EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        noteStoreClient.listNotebooksAsync(new EvernoteCallback<List<Notebook>>() {
            @Override
            public void onSuccess(List<Notebook> result) {
                for (Notebook notebook : result) {
                    String guid= notebook.getGuid();
                    try {
                        Note nota= noteStoreClient.getNote(guid,true,false,false,false);
                        String titulo=nota.getTitle();
                        String cuerpo=nota.getContent();
                        Long fecha=nota.getCreated();
                        NoteBQ notaBQ = new NoteBQ(titulo,cuerpo,fecha);
                        listaNotas.add(notaBQ);
                    } catch (EDAMUserException e) {
                        e.printStackTrace();
                    } catch (EDAMSystemException e) {
                        e.printStackTrace();
                    } catch (EDAMNotFoundException e) {
                        e.printStackTrace();
                    } catch (TException e) {
                        e.printStackTrace();
                    }

                }

//                Snackbar.make(findViewById(),"notebooks have been retrieved", Snackbar.LENGTH_LONG)
//                        .show();
            }

            @Override
            public void onException(Exception exception) {
                Log.e("MainActivity", "Error retrieving notebooks", exception);
            }
        });
        lista.setAdapter(new NoteAdapter(this,listaNotas));

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapter, View view, int position, long arg) {
                NoteBQ item = (NoteBQ) lista.getAdapter().getItem(position);
                String title = item.getTitle();
                String body = item.getBody();

                Bundle arguments = new Bundle();
                arguments.putString("title", title);
                arguments.putString("body",body);
                NoteFragment fragment = new NoteFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_contenedor_detalle, fragment).commit();

//                Bundle arguments = new Bundle();
//                arguments.putString("title", title);
//                arguments.putString("body",body);
//                NoteFragment fragment = NoteFragment.newInstance(arguments);
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(android.R.id.content, fragment, NoteFragment.TAG);
//                ft.commit();
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

    public Note makeNote(NoteStoreClient noteStore, String noteTitle, String noteBody, Notebook parentNotebook) {

        String nBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        nBody += "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">";
        nBody += "<en-note>" + noteBody + "</en-note>";

        // Create note object
        Note ourNote = new Note();
        ourNote.setTitle(noteTitle);
        ourNote.setContent(nBody);

        // parentNotebook is optional; if omitted, default notebook is used
        if (parentNotebook != null && parentNotebook.isSetGuid()) {
            ourNote.setNotebookGuid(parentNotebook.getGuid());
        }

        // Attempt to create note in Evernote account
        Note note = null;
        try {
            note = noteStore.createNote(ourNote);
        } catch (EDAMUserException edue) {
            // Something was wrong with the note data
            // See EDAMErrorCode enumeration for error code explanation
            // http://dev.evernote.com/documentation/reference/Errors.html#Enum_EDAMErrorCode
            System.out.println("EDAMUserException: " + edue);
        } catch (EDAMNotFoundException ednfe) {
            // Parent Notebook GUID doesn't correspond to an actual notebook
            System.out.println("EDAMNotFoundException: Invalid parent notebook GUID");
        } catch (Exception e) {
            // Other unexpected exceptions
            e.printStackTrace();
        }

        // Return created note object
        return note;

    }

}
