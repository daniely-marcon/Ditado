package com.example.ditado;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ditado.database.AppDatabase;
import com.example.ditado.databinding.FragmentFirstBinding;
import com.example.ditado.entities.Animal;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    private List<Animal> aves = new ArrayList<>();
    private List<Animal> repteis = new ArrayList<>();
    private List<Animal> peixes = new ArrayList<>();
    private List<Animal> anfibios = new ArrayList<>();
    private List<Animal> mamiferos = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        carregarDadosDoBanco();

        binding.fabF1.setOnClickListener(v ->
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_TerceiroFragment)
        );

        configurarCliquesCategorias();
        configurarSpinnerFonte();
    }

    private void carregarDadosDoBanco() {
        new Thread(() -> {
            try {
                AppDatabase db = AppDatabase.getDatabase(requireContext());

                List<Animal> tempAves = db.animalDao().buscarPorFilo("Aves");
                List<Animal> tempPeixes = db.animalDao().buscarPorFilo("Peixes");
                List<Animal> tempAnfibios = db.animalDao().buscarPorFilo("Anfíbios");
                List<Animal> tempRepteis = db.animalDao().buscarPorFilo("Répteis");
                List<Animal> tempMamiferos = db.animalDao().buscarPorFilo("Mamíferos");

                requireActivity().runOnUiThread(() -> {
                    if (tempAves != null) aves = tempAves;
                    if (tempPeixes != null) peixes = tempPeixes;
                    if (tempAnfibios != null) anfibios = tempAnfibios;
                    if (tempRepteis != null) repteis = tempRepteis;
                    if (tempMamiferos != null) mamiferos = tempMamiferos;

                    if (!aves.isEmpty()) configurarGrid(binding.gvAves, aves);
                    if (!peixes.isEmpty()) configurarGrid(binding.gvPeixes, peixes);
                    if (!anfibios.isEmpty()) configurarGrid(binding.gvAnfibios, anfibios);
                    if (!repteis.isEmpty()) configurarGrid(binding.gvRepteis, repteis);
                    if(!mamiferos.isEmpty()) configurarGrid(binding.gvMamiferos, mamiferos);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void configurarGrid(GridView grid, List<Animal> lista) {
        if (lista == null || lista.isEmpty()) {
            grid.setVisibility(View.GONE);
            return;
        }

        int limite = Math.min(lista.size(), 4);

        grid.setAdapter(new AdaptadorGridView(getContext(), new ArrayList<>(lista.subList(0, limite))));

        grid.setOnItemClickListener((parent, v, position, id) -> {
            irParaJogo(lista, position);
        });
    }

    private void configurarCliquesCategorias() {
        binding.txtPeixes.setOnClickListener(v -> irParaJogo(peixes, 0));
        binding.txtAves.setOnClickListener(v -> irParaJogo(aves, 0));
        binding.txtRepteis.setOnClickListener(v -> irParaJogo(repteis, 0));
        binding.txtAnfibios.setOnClickListener(v -> irParaJogo(anfibios, 0));
        binding.txtMamiferos.setOnClickListener(v->irParaJogo(mamiferos,0));
    }

    private void irParaJogo(List<Animal> lista, int indice) {
        if (lista == null || lista.isEmpty()) {
            Toast.makeText(getContext(), "Nenhum animal nesta categoria!", Toast.LENGTH_SHORT).show();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("lista_animais", new ArrayList<>(lista));
        bundle.putInt("indice_atual", indice);

        NavHostFragment.findNavController(this).navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
    }

    private void configurarSpinnerFonte() {
        ArrayAdapter<CharSequence> adaptadorSpinner = ArrayAdapter.createFromResource(
                getContext(), R.array.fonte, R.layout.meu_item_spinner);
        adaptadorSpinner.setDropDownViewResource(R.layout.meu_item_spinner);
        binding.spinner.setAdapter(adaptadorSpinner);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MainActivity main = (MainActivity) getActivity();

                if (main != null) {
                    switch (position) {
                        case 0: main.fonte = 18f; break;
                        case 1: main.fonte = 14f; break;
                        case 2: main.fonte = 22f; break;
                    }

                    binding.txtPeixes.setTextSize(main.fonte);
                    binding.txtAves.setTextSize(main.fonte);
                    binding.txtRepteis.setTextSize(main.fonte);
                    binding.txtAnfibios.setTextSize(main.fonte);
                    binding.txtTitulo.setTextSize(main.fonte*2f);
                    binding.txtMamiferos.setTextSize(main.fonte);

                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}