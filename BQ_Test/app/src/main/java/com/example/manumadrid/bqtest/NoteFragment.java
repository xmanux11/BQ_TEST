package com.example.manumadrid.bqtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by ManuMadrid on 25/02/2017.
 * Clase para visualizar el contenido de una NoteBQ
 */

public class NoteFragment extends Activity {

    /**
     * titulo de la nota
     */
    private String titulo;
    /**
     * body de la nota
     */
    private String cuerpo;

    public NoteFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_details);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("title")) {
            titulo = getIntent().getExtras().getString("title");
        }
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("body")) {
            cuerpo = getIntent().getExtras().getString("body");
        }

        if (titulo != null) {
            ((TextView) findViewById(R.id.titulo)).setText(titulo);
        }
        if (cuerpo != null) {
            ((TextView) findViewById(R.id.cuerpo)).setText(cuerpo);
        }
    }

}