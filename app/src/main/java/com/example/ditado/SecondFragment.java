package com.example.ditado;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.ditado.database.AppDatabase;
import com.example.ditado.databinding.FragmentSecondBinding;
import com.example.ditado.entities.Animal;
import com.example.ditado.entities.PalavrasAprendidas;

import java.util.List;
import java.util.Locale;

public class SecondFragment extends Fragment {
    private FragmentSecondBinding binding;
    private List<Animal> listaAnimais;
    private int indiceAtual = 0;
    private int qtdRespondidos = 0;

    private TextToSpeech tts;
    private boolean ttsPronto = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (getArguments() != null) {
            listaAnimais = (List<Animal>) getArguments().getSerializable("lista_animais");
            indiceAtual = getArguments().getInt("indice_atual");
        }


        configurarTTS();


        MainActivity main = (MainActivity) getActivity();
        if (main != null) {
            binding.txtResult.setTextSize(main.fonte);
            binding.txtPalavra.setTextSize(main.fonte*2f-10f);
            binding.btnTerminar.setTextSize(main.fonte);
        }


        if (listaAnimais != null && !listaAnimais.isEmpty()) {
            exibirAnimalAtual(false);
        }

        binding.btnFalar.setOnClickListener(v -> {
            if (listaAnimais != null && ttsPronto) {
                falarPalavra(listaAnimais.get(indiceAtual).getNome_animal());
            }
        });

        binding.btnTerminar.setOnClickListener(v -> validarResposta());

        binding.fabF2.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_SecondFragment_to_TerceiroFragment)
        );
    }

    private void configurarTTS() {
        tts = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("pt", "BR"));
                ttsPronto = true;


                if (listaAnimais != null && !listaAnimais.isEmpty()) {
                    falarPalavra(listaAnimais.get(indiceAtual).getNome_animal());
                }
            }
        });
    }

    private void falarPalavra(String texto) {
        if (tts != null && ttsPronto) {
            tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }


    private void exibirAnimalAtual(boolean deveFalar) {
        if (listaAnimais != null && indiceAtual < listaAnimais.size()) {
            Animal atual = listaAnimais.get(indiceAtual);

            byte[] imagemBytes = atual.getImagem_animal();

            if (imagemBytes != null && imagemBytes.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imagemBytes, 0, imagemBytes.length);
                binding.imageView.setImageBitmap(bitmap);
            } else {
                binding.imageView.setImageResource(R.drawable.ic_launcher_background);
            }

            if (deveFalar) {
                falarPalavra(atual.getNome_animal());
            }
        }
    }

    private void validarResposta() {
        String resposta = binding.edtPalavra.getText().toString().trim();
        Animal atual = listaAnimais.get(indiceAtual);

        if (resposta.equalsIgnoreCase(atual.getNome_animal())) {


            salvarPalavraAprendida(atual);


            binding.txtResult.setText("Muito bem! Próximo...");
            binding.txtResult.setTextColor(Color.parseColor("#34A43A"));

            indiceAtual++;
            qtdRespondidos++;

            if (qtdRespondidos < listaAnimais.size()) {
                if (indiceAtual >= listaAnimais.size()) indiceAtual = 0;

                binding.edtPalavra.setText("");


                exibirAnimalAtual(true);
            } else {
                Toast.makeText(getContext(), "Parabéns! Você terminou o grupo!", Toast.LENGTH_LONG).show();
                NavHostFragment.findNavController(this).navigate(R.id.action_SecondFragment_to_TerceiroFragment);
            }
        } else {
            binding.txtResult.setText("Quase lá! Tente de novo.");
            binding.txtResult.setTextColor(Color.RED);
                    }
    }

    private void salvarPalavraAprendida(Animal animalAcertado) {

        SharedPreferences pref = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        int idUsuario = pref.getInt("id_usuario", -1);

        if (idUsuario != -1) {

            PalavrasAprendidas novaConquista = new PalavrasAprendidas(idUsuario, animalAcertado.getId_animal());

            AppDatabase db = AppDatabase.getDatabase(requireContext());

            new Thread(() -> {
                try {
                    db.palavrasAprendidasDao().insert(novaConquista);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
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