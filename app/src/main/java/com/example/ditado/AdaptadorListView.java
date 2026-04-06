package com.example.ditado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;


public class AdaptadorListView extends ArrayAdapter<Animal> {

    public AdaptadorListView(Context context, List<Animal> listaAnimais) {
                super(context, R.layout.item_lista_animal, listaAnimais);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lista_animal, parent, false);
        }


        Animal animalAtual = getItem(position);


        ImageView foto = convertView.findViewById(R.id.imgFotoLinha);
        TextView nome = convertView.findViewById(R.id.txtNomeLinha);

        if (animalAtual != null) {
            foto.setImageResource(animalAtual.getImagemId()); // Ajuste se seu getter for diferente
            nome.setText(animalAtual.getNome());
        }

        return convertView;
    }
}