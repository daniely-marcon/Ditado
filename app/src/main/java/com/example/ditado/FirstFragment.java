package com.example.ditado;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
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



        configurarGrid(binding.gvAves, aves);
        configurarGrid(binding.gvPeixes, peixes);
        configurarGrid(binding.gvAnfibios, anfibios);
        configurarGrid(binding.gvRepteis, repteis);
        configurarGrid(binding.gvMamiferos, mamiferos);


        binding.fabF1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_TerceiroFragment);
            }
        });

        ArrayAdapter<CharSequence> adaptadorSpinner =  ArrayAdapter.createFromResource(getContext(),R.array.fonte, R.layout.meu_item_spinner);
        adaptadorSpinner.setDropDownViewResource(R.layout.meu_item_spinner);
        binding.spinner.setAdapter(adaptadorSpinner);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                MainActivity main = (MainActivity) getActivity();
                if(main != null) {
                    switch(position) {
                        case 0:
                            main.fonte = 18f;
                            break;
                        case 1:
                            main.fonte = 14f;
                            break;
                        case 2:
                            main.fonte = 22f;
                            break;
                    }

                    binding.txtPeixes.setTextSize(main.fonte);
                    binding.txtAves.setTextSize(main.fonte);
                    binding.txtRepteis.setTextSize(main.fonte);
                    binding.txtAnfibios.setTextSize(main.fonte);
                    binding.txtSpinner.setTextSize(main.fonte);
                    binding.txtTitulo.setTextSize(main.fonte + 12);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.txtPeixes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("lista_animais", (ArrayList<Animal>) peixes);
                bundle.putInt("indice_atual", 0);

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
            }
        });
        binding.txtAves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("lista_animais", (ArrayList<Animal>) aves);
                bundle.putInt("indice_atual", 0);

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
            }
        });
        binding.txtRepteis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("lista_animais", (ArrayList<Animal>) repteis);
                bundle.putInt("indice_atual", 0);

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
            }
        });
        binding.txtAnfibios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("lista_animais", (ArrayList<Animal>) anfibios);
                bundle.putInt("indice_atual", 0);

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
            }
        });

    }



    private void configurarGrid(GridView grid, List<Animal> lista) {
        grid.setAdapter(new AdaptadorGridView(getContext(), lista.subList(0,4)));
        grid.setOnItemClickListener((parent, view, position, id) -> {

            Bundle bundle = new Bundle();
            bundle.putSerializable("lista_animais", (ArrayList<Animal>) lista);
            bundle.putInt("indice_atual", position);
            
            NavHostFragment.findNavController(FirstFragment.this)
                    .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
