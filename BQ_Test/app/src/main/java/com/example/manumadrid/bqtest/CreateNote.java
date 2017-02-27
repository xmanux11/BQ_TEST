package com.example.manumadrid.bqtest;

import android.app.Activity;
import android.content.res.AssetManager;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.thrift.TException;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ManuMadrid on 25/02/2017.
 * Clase para crear las notas y mandarlas al servidor
 */

public class CreateNote extends Activity {

    /**
     * titulo de la imagen para ocr
     */
    private Bitmap bmTitle;
    /**
     * body de la imagen para ocr
     */
    private Bitmap bmBody;
    private TessBaseAPI mTess;
    /**
     * ruta al fichero de lenguaje
     */
    private String datapath = "";


    public CreateNote() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_note);
        Button create = (Button) findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText titulo = ((EditText) findViewById(R.id.titulo));
                EditText cuerpo = ((EditText) findViewById(R.id.cuerpo));
                final String tituloText = titulo.getText().toString();
                final String cuerpoText = cuerpo.getText().toString();
                if (tituloText != null && !tituloText.isEmpty()) {
                    NoteBQ note = new NoteBQ(tituloText, cuerpoText, System.currentTimeMillis() / 1000);
                    createNote(note);
                }
            }
        });

        try {

            /**
             * inicializamos Tesseract
             */
            datapath = getFilesDir()+ "/tesseract/";
            checkFile(new File(datapath + "tessdata/"));
            String lang = "spa";
            mTess = new TessBaseAPI();
            mTess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO);
            mTess.init(datapath, lang);


        } catch (Exception e) {

            Log.v("Gestures", e.getMessage());
            e.printStackTrace();
        }


    }


    /**
     * metodo para crear la nota y mandarla al servicio
     * @param noteBQ
     */
    private void createNote(NoteBQ noteBQ) {

        Note note = new Note();
        note.setTitle(noteBQ.getTitle());
        note.setContent(EvernoteUtil.NOTE_PREFIX + noteBQ.getBody() + EvernoteUtil.NOTE_SUFFIX);
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
            Log.d("CreateNote", "Unexpected error adding note using Evernote SDK", e);
            Snackbar.make(findViewById(android.R.id.content), "La sesion ha expirado, vuelva a logearse", Snackbar.LENGTH_LONG)
                    .show();
        }
        finish();
    }

    /**
     * metodo para guardar el gesto de los dos campos de gestos
     * @param view
     */
    public void saveGesto(View view) {
        try {
            GestureOverlayView title = (GestureOverlayView) findViewById(R.id.titleocr);
            GestureOverlayView body = (GestureOverlayView) findViewById(R.id.bodyocr);
            title.setDrawingCacheEnabled(true);
            body.setDrawingCacheEnabled(true);
            bmTitle = Bitmap.createBitmap(title.getDrawingCache()).copy(Bitmap.Config.ARGB_8888, true);
            bmBody = Bitmap.createBitmap(body.getDrawingCache()).copy(Bitmap.Config.ARGB_8888, true);
            processImage();

        } catch (Exception e) {
            Log.v("Gestures", e.getMessage());
            e.printStackTrace();
        }
    }

    private void copyFiles() {
        try {

            String filepath = datapath + "tessdata/spa.traineddata";
            AssetManager assetManager = getAssets();
            InputStream instream = assetManager.open("tessdata/spa.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * chekeamos que el lenguaje.traineddata existe y si no lo copiamos desde el assets
     * @param dir
     */
    private void checkFile(File dir) {

        if (!dir.exists()&& dir.mkdirs()){
            copyFiles();
        }
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/spa.traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles();
            }
        }
    }

    /**
     * procesado de las imagenes guardadas y seteado de los campos
     */
    public void processImage(){
        String OCRresultTitle = "";
        mTess.setImage(bmTitle);
        OCRresultTitle = mTess.getUTF8Text();
        EditText OCRTextViewTitle = (EditText) findViewById(R.id.titulo);
        OCRTextViewTitle.setText("");
        OCRTextViewTitle.setText(OCRresultTitle);

        String OCRresultBody = "";
        mTess.setImage(bmBody);
        OCRresultBody = mTess.getUTF8Text();
        EditText OCRTextViewBody = (EditText) findViewById(R.id.cuerpo);
        OCRTextViewBody.setText("");
        OCRTextViewBody.setText(OCRresultBody);
    }




}