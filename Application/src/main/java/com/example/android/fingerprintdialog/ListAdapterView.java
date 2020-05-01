package com.example.android.fingerprintdialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mdgarcia.android.utils.manager.CustomFingerprintManager;
import com.mdgarcia.android.utils.model.Fingerprint;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterView extends BaseAdapter {
    private final CustomFingerprintManager fingerprintManager;
    private Context context;
    private int layout;
    private ArrayList<Fingerprint> fingerprints;

    public ListAdapterView(Context context, int layout, List<Fingerprint> fingerprints, CustomFingerprintManager manager) {
        this.context = context;
        this.layout = layout;
        this.fingerprints = new ArrayList<>(fingerprints);
        this.fingerprintManager = manager;
    }

    @Override
    public int getCount() {
        return this.fingerprints.size();
    }

    @Override
    public Object getItem(int position) {
        return this.fingerprints.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        // Copiamos la vista
        View v = convertView;

        //Inflamos la vista con nuestro propio layout
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);

        v = layoutInflater.inflate(R.layout.list_item, null);
        // Valor actual según la posición

        final Fingerprint currentFingerprint = fingerprints.get(position);

        // Referenciamos el elemento a modificar y lo rellenamos
        TextView textView = (TextView) v.findViewById(R.id.md_text_view);
        textView.setText(currentFingerprint.toString());

        Button button = (Button) v.findViewById(R.id.delete_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fingerprintManager.removeFingerprint(currentFingerprint);
                notifyDataSetChanged();
                Toast.makeText(v.getContext(), "Elemento eliminado", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}
