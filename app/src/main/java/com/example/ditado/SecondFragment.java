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

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {
    private FragmentSecondBinding binding;
    private List<Animal> listaAnimais;
    private int indiceAtual = 0;
    private MediaPlayer mediaPlayer;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void
    onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            listaAnimais = (List<Animal>) getArguments().getSerializable("lista_animais");
            indiceAtual = getArguments().getInt("indice_atual");
        }

        if (listaAnimais != null) {
            exibirAnimalAtual();
        }

        // Botão para ouvir o áudio novamente
        binding.btnFalar.setOnClickListener(v -> {
            if (listaAnimais != null) {
                tocarAudio(listaAnimais.get(indiceAtual).getAudioId());
            }
        });

        binding.btnTerminar.setOnClickListener(v -> {
            MainActivity atividadeMae = (MainActivity) getActivity();
            String resposta = binding.edtPalavra.getText().toString().trim();
            if (resposta.equalsIgnoreCase(listaAnimais.get(indiceAtual).getNome())) {
                if (atividadeMae.aprendidos.stream().noneMatch(a -> a.getNome().equals(listaAnimais.get(indiceAtual).getNome()))) {
                    if(atividadeMae!=null){
                        atividadeMae.aprendidos.add(listaAnimais.get(indiceAtual));
                    }

                }
                indiceAtual++;
                if (indiceAtual < listaAnimais.size()) {
                    exibirAnimalAtual();
                    binding.edtPalavra.setText(""); 
                    binding.txtResult.setText("Muito bem! Próximo...");


                } else {
                    binding.txtResult.setText("Parabéns! Você terminou a lista!");

                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_TerceiroFragment);
                }

            } else {
                binding.txtResult.setText("Errou, tente de novo!");
                binding.txtResult.setTextColor(android.graphics.Color.parseColor("#FF0000"));
            }
        });
        binding.fabF2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_TerceiroFragment);
            }
        });

    }

    private void exibirAnimalAtual() {
        if (listaAnimais != null && indiceAtual < listaAnimais.size()) {
            Animal atual = listaAnimais.get(indiceAtual);
            binding.imageView.setImageResource(atual.getImagemId());
            tocarAudio(atual.getAudioId());


        }
    }

    private void tocarAudio(int audioId) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getContext(), audioId);
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
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
