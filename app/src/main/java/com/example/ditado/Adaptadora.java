package com.example.ditado;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.recyclerview.widget.ListAdapter;

import java.util.List;

public class Adaptadora extends BaseAdapter {
    private Context ctx;
    private List<Animal> list;

    public Adaptadora(Context ctx, List<Animal> list){
        this.ctx=ctx;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv;
        if (convertView == null) {
            iv = new ImageView(ctx);
            iv.setLayoutParams(new ViewGroup.LayoutParams(250, 250));
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            iv = (ImageView) convertView;
        }


        Animal animalAtual = list.get(position);

        // 2. Extraímos a imagem de dentro desse objeto usando o getter
        int fotoDoAnimal = animalAtual.getImagemId();

        // 3. Colocamos a imagem na View
        iv.setImageResource(fotoDoAnimal);

        return iv;
    }


    }
