package com.example.manumadrid.bqtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ManuMadrid on 25/02/2017.
 */

public class NoteFragment extends android.support.v4.app.Fragment {

    public static final String TAG = "NoteFragment";
    private String titulo;
    private String cuerpo;

//    public static NoteFragment newInstance(Bundle arguments){
//        NoteFragment f = new NoteFragment();
//        if(arguments != null){
//            f.setArguments(arguments);
//        }
//        return f;
//    }

    public NoteFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("title")) {
            titulo = getArguments().getString("title");
        }
        if (getArguments().containsKey("body")) {
            cuerpo = getArguments().getString("body");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.note_fragment, container, false);

        //Mostramos el contenido al usuario
        if (titulo != null) {
            ((TextView) rootView.findViewById(R.id.titulo)).setText(titulo);
        }
        if(cuerpo!=null){
            ((TextView) rootView.findViewById(R.id.cuerpo)).setText(cuerpo);
        }

        return rootView;
    }
}