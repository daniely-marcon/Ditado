package com.example.ditado;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ditado.dao.AudioAnimalDao;
import com.example.ditado.database.AppDatabase;
import com.example.ditado.databinding.FragmentParabensBinding;
import com.example.ditado.entities.AudioAnimal;

import java.util.List;
import java.util.Random;

public class ParabensFragment extends Fragment {
    private FragmentParabensBinding binding;

    private List<AudioAnimal> listaAudios;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentParabensBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listaAudios = AppDatabase.getDatabase(requireContext()).audioAnimalDao().getAll();

        if (listaAudios != null && !listaAudios.isEmpty()) {
            Random random = new Random();
            int indice = random.nextInt(listaAudios.size());

            int idAudio = listaAudios.get(indice).getId_audio();

            MediaPlayer mediaPlayer = MediaPlayer.create(requireContext(),listaAudios.get(indice).getAudioResId());
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
            mediaPlayer.start();
        }

        // Ao clicar no botão, ele limpa a pilha e volta para a tela inicial (FirstFragment)
        binding.btnVoltarInicio.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_ParabensFragment_to_FirstFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}