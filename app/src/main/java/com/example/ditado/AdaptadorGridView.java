package com.example.ditado;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.ditado.entities.Animal;

import java.util.List;

public class AdaptadorGridView extends BaseAdapter {
    private Context ctx;
    private List<Animal> list;

    public AdaptadorGridView(Context ctx, List<Animal> list){
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list != null ? list.get(position) : null;
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



            int alturaEmPixels = (int) (82 * ctx.getResources().getDisplayMetrics().density);


            iv.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, alturaEmPixels));


            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);


        } else {
            iv = (ImageView) convertView;
        }

        Animal animalAtual = list.get(position);

        if (animalAtual != null) {
            byte[] imagemBytes = animalAtual.getImagem_animal();

            if (imagemBytes != null && imagemBytes.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imagemBytes, 0, imagemBytes.length);
                iv.setImageBitmap(bitmap);
            } else {
                iv.setImageResource(R.drawable.ic_launcher_background);
            }
        }

        return iv;
    }
}