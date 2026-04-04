package com.example.ditado;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.ditado.databinding.FragmentSecondBinding;

import com.example.ditado.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private String nomeCorreto;
    private int audioResId;
    private MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();



    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 1. Recuperar os dados passados pelo FirstFragment
        if (getArguments() != null) {
            nomeCorreto = getArguments().getString("nome");
            int fotoResId = getArguments().getInt("foto");
            audioResId = getArguments().getInt("audio");

            binding.imageView.setImageResource(fotoResId);
            tocarAudio();
        }

        // 2. Botão para ouvir o áudio de novo
        binding.btnFalar.setOnClickListener(v -> tocarAudio());

        // 3. Lógica de Verificação
        binding.btnVerificar.setOnClickListener(v -> {
            String resposta = binding.edtPalavra.getText().toString().trim();
            if (resposta.equalsIgnoreCase(nomeCorreto)) {
                binding.txtResult.setText("Correto! Parabéns!");
                binding.txtResult.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else {
                binding.txtResult.setText("Incorreto. Tente novamente!");
                binding.txtResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
        });

        // 4. Botão Voltar
        binding.btnVoltarF2.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp()
        );
    }

    private void tocarAudio() {
        if (mediaPlayer != null) mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(getContext(), audioResId);
        mediaPlayer.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        binding = null;
    }
}



