package com.example.ditado;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.ditado.entities.Animal;

import java.util.List;

public class AdaptadorGridView extends BaseAdapter {
    private Context ctx;
    private List<Animal> list;

    public AdaptadorGridView(Context ctx, List<Animal> list){
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
 /*
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
        int fotoDoAnimal = animalAtual.getImagemId();
        iv.setImageResource(fotoDoAnimal);

        return iv;
    }*/


    }
