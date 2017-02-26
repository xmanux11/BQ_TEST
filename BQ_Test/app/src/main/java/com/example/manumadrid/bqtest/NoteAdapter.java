package com.example.manumadrid.bqtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ManuMadrid on 25/02/2017.
 */

public class NoteAdapter extends BaseAdapter {

    private ArrayList<NoteBQ> notas;
    private Context context;

    public NoteAdapter(Context context, ArrayList<NoteBQ> notas) {
        this.context = context;
        this.notas = notas;
    }


    @Override
    public int getCount() {
        return notas.size();
    }

    @Override
    public Object getItem(int position) {
        return notas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.note_list_item, parent, false);
        }

        TextView title = (TextView) rowView.findViewById(R.id.title_note);
        TextView body = (TextView) rowView.findViewById(R.id.title_body);
        TextView date = (TextView) rowView.findViewById(R.id.date);

        NoteBQ nota = this.notas.get(position);
        title.setText(nota.getTitle());
        body.setText(nota.getBody());
        date.setText(nota.getFechaCreacion().toString());

        return rowView;
    }
}
