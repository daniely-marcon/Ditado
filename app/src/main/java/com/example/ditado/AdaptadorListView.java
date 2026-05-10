package com.example.ditado;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ditado.entities.Animal;

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
        TextView filo = convertView.findViewById(R.id.txtFilo);

        if (animalAtual != null) {

            filo.setText(animalAtual.getFilo_animal());


            byte[] imagemBytes = animalAtual.getImagem_animal();
            if (imagemBytes != null && imagemBytes.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imagemBytes, 0, imagemBytes.length);
                foto.setImageBitmap(bitmap);
            } else {
                foto.setImageResource(R.drawable.ic_launcher_background);
            }
        }

        return convertView;
    }
}