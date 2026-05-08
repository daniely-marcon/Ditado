package com.example.ditado;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ditado.databinding.FragmentSecondBinding;
import com.example.ditado.entities.Animal;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SecondFragment extends Fragment {
    private FragmentSecondBinding binding;
    private List<Animal> listaAnimais;
    private int indiceAtual = 0;
    private int qtdRespondidos = 0;

    // Classe Java que lê o texto
    private TextToSpeech tts;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Configura o "Leitor de Texto" (TTS)
        configurarTTS();

        // 2. Aplica o tamanho da fonte da MainActivity
        MainActivity main = (MainActivity) getActivity();
        if (main != null) {
            binding.txtResult.setTextSize(main.fonte);
            binding.txtPalavra.setTextSize(main.fonte + 4);
            binding.btnTerminar.setTextSize(main.fonte);
        }

        // 3. Pega a lista de animais vinda da tela anterior
        if (getArguments() != null) {
            listaAnimais = (List<Animal>) getArguments().getSerializable("lista_animais");
            indiceAtual = getArguments().getInt("indice_atual");
        }

        // 4. Mostra o animal e já faz o TTS falar o nome dele
        if (listaAnimais != null && !listaAnimais.isEmpty()) {
            exibirAnimalAtual();
        }

        // 5. Botão para repetir a fala (Ouvir de novo)
        binding.btnFalar.setOnClickListener(v -> {
            if (listaAnimais != null) {
                falarPalavra(listaAnimais.get(indiceAtual).getNome_animal());
            }
        });

        // 6. Botão para validar o que o aluno escreveu
        binding.btnTerminar.setOnClickListener(v -> validarResposta());

        // Botão de voltar (FAB)
        binding.fabF2.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_SecondFragment_to_FirstFragment)
        );
    }

    private void configurarTTS() {
        tts = new TextToSpeech(getContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                // Define o idioma para Português do Brasil
                tts.setLanguage(new Locale("pt", "BR"));
            }
        });
    }

    private void falarPalavra(String texto) {
        if (tts != null) {
            // QUEUE_FLUSH limpa a fila e fala o novo texto na hora
            tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void exibirAnimalAtual() {
        if (listaAnimais != null && indiceAtual < listaAnimais.size()) {
            Animal atual = listaAnimais.get(indiceAtual);


            byte[] imagemBytes = atual.getImagem_animal();

            if (imagemBytes != null && imagemBytes.length > 0) {

                Bitmap bitmap = BitmapFactory.decodeByteArray(imagemBytes, 0, imagemBytes.length);

                binding.imageView.setImageBitmap(bitmap);
            } else {

                binding.imageView.setImageResource(R.drawable.ic_launcher_background);
            }

            binding.txtPalavra.setText(atual.getNome_animal());
            falarPalavra(atual.getNome_animal());
        }
    }

    private void validarResposta() {
        String resposta = binding.edtPalavra.getText().toString().trim();
        Animal atual = listaAnimais.get(indiceAtual);

        if (resposta.equalsIgnoreCase(atual.getNome_animal())) {
            binding.txtResult.setText("Muito bem! Próximo...");
            binding.txtResult.setTextColor(Color.parseColor("#34A43A"));

            indiceAtual++;
            qtdRespondidos++;

            if (qtdRespondidos < listaAnimais.size()) {
                if (indiceAtual >= listaAnimais.size()) indiceAtual = 0;

                binding.edtPalavra.setText("");
                exibirAnimalAtual();
            } else {
                Toast.makeText(getContext(), "Parabéns! Você terminou o grupo!", Toast.LENGTH_LONG).show();
                NavHostFragment.findNavController(this).navigate(R.id.action_SecondFragment_to_TerceiroFragment);
            }
        } else {
            binding.txtResult.setText("Quase lá! Tente de novo.");
            binding.txtResult.setTextColor(Color.RED);
            falarPalavra("Tente novamente");
        }
    }

    @Override
    public void onDestroyView() {

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroyView();
        binding = null;
    }
}