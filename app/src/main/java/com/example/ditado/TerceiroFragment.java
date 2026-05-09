package com.example.ditado;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.ditado.databinding.FragmentTerceiroBinding;
import com.example.ditado.entities.Animal;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TerceiroFragment extends Fragment {
    private FragmentTerceiroBinding binding;
    private TextToSpeech tts;
    private List<Animal> listaConcluidos = new ArrayList<>();
    private AdaptadorListView meuAdaptador;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTerceiroBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity main = (MainActivity) getActivity();

        configurarTTS();

        if (main != null) {
            binding.txtEstatisticas.setTextSize(main.fonte + 12);
            binding.btnLimpar.setTextSize(main.fonte);

            carregarDadosDoBanco();


            binding.lvEstatisticas.setOnItemClickListener((adapterView, v, position, l) -> {
                String nome = listaConcluidos.get(position).getNome_animal();
                falarPalavra(nome);
            });

            binding.fabF3.setOnClickListener(v ->
                    NavHostFragment.findNavController(TerceiroFragment.this)
                            .navigate(R.id.action_TerceiroFragment_to_FirstFragment)
            );


            binding.btnLimpar.setOnClickListener(v -> limparHistoricoDoBanco());
        }
    }

    private void carregarDadosDoBanco() {
        // 1. Pega o ID do aluno logado
        SharedPreferences pref = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        int idUsuario = pref.getInt("id_usuario", -1);

        if (idUsuario != -1) {
            new Thread(() -> {
                AppDatabase db = AppDatabase.getDatabase(requireContext());


                List<Animal> concluidosDb = db.palavrasAprendidasDao().getPalavrasPorUsuario(idUsuario);


                requireActivity().runOnUiThread(() -> {
                    listaConcluidos.clear();
                    if (concluidosDb != null) {
                        listaConcluidos.addAll(concluidosDb);
                    }
                    meuAdaptador = new AdaptadorListView(getContext(), listaConcluidos);
                    binding.lvEstatisticas.setAdapter(meuAdaptador);
                });
            }).start();
        }
    }

    private void limparHistoricoDoBanco() {
        SharedPreferences pref = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        int idUsuario = pref.getInt("id_usuario", -1);

        if (idUsuario != -1) {
            new Thread(() -> {
                AppDatabase db = AppDatabase.getDatabase(requireContext());

                db.palavrasAprendidasDao().ApagarPalavrasUsuario(idUsuario);

                requireActivity().runOnUiThread(() -> {
                    listaConcluidos.clear();
                    if (meuAdaptador != null) {
                        meuAdaptador.notifyDataSetChanged();
                    }
                    Toast.makeText(getContext(), "Histórico apagado com sucesso!", Toast.LENGTH_SHORT).show();
                });
            }).start();
        }
    }

    private void configurarTTS() {
        tts = new TextToSpeech(getContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(new Locale("pt", "BR"));
            }
        });
    }

    private void falarPalavra(String texto) {
        if (tts != null) {
            tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null, null);
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