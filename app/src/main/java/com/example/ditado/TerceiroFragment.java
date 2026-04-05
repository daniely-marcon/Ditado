package com.example.ditado;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.example.ditado.databinding.FragmentFirstBinding;
import com.example.ditado.databinding.FragmentSecondBinding;
import com.example.ditado.databinding.FragmentTerceiroBinding;

import java.util.ArrayList;


public class TerceiroFragment extends Fragment {
    private FragmentTerceiroBinding binding;
    private MediaPlayer mediaPlayer;

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTerceiroBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<Animal> aprendidos = (ArrayList<Animal>) getArguments().getSerializable("aprendidos");
        AdaptadorListView meuAdaptador = new AdaptadorListView(getContext(), aprendidos);
        binding.lvEstatisticas.setAdapter(meuAdaptador);


        binding.lvEstatisticas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mediaPlayer = MediaPlayer.create(getContext(), aprendidos.get(position).getAudioId());
                mediaPlayer.start();
            }
        });
        binding.fabF3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aprendidos.clear();
                meuAdaptador.notifyDataSetChanged();
            }
        });
    }
}