package com.example.ditado;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.ditado.databinding.FragmentFirstBinding;
import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private List<Animal> aves = new ArrayList<>();
    private List<Animal> repteis = new ArrayList<>();
    private List<Animal> peixes = new ArrayList<>();
    private List<Animal> anfibios = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarDados();

        configurarGrid(binding.gvAves, aves);
        configurarGrid(binding.gvPeixes, peixes);
        configurarGrid(binding.gvAnfibios, anfibios);
        configurarGrid(binding.gvRepteis, repteis);

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
    }

    private void inicializarDados() {
        aves.clear();
        aves.add(new Animal("Arara", R.drawable.arara, R.raw.arara));
        aves.add(new Animal("Tucano", R.drawable.tucano, R.raw.tucano));
        aves.add(new Animal("Beija-Flor", R.drawable.beijaflor, R.raw.beijaflor));
        aves.add(new Animal("Papagaio", R.drawable.papagaio, R.raw.papagaio));
        aves.add(new Animal("Rolinha", R.drawable.rolinha, R.raw.rolinha));

        repteis.clear();
        repteis.add(new Animal("Cobra Coral", R.drawable.coral, R.raw.cobracoral));
        repteis.add(new Animal("Iguana", R.drawable.iguana, R.raw.iguana));
        repteis.add(new Animal("Sucuri", R.drawable.sucuri, R.raw.sucuri));
        repteis.add(new Animal("Tartaruga", R.drawable.tartaruga, R.raw.tartaruga));
        repteis.add(new Animal("Lagartixa", R.drawable.lagartixa, R.raw.lagartixa));

        peixes.clear();
        peixes.add(new Animal("Pirarucu", R.drawable.pirarucu, R.raw.pirarucu));
        peixes.add(new Animal("Piau", R.drawable.piau, R.raw.piau));
        peixes.add(new Animal("Dourado", R.drawable.dourado, R.raw.dourado));
        peixes.add(new Animal("Piraputanga", R.drawable.piraputanga, R.raw.piraputanga));
        peixes.add(new Animal("Bagre", R.drawable.bagre, R.raw.bagre));

        anfibios.clear();
        anfibios.add(new Animal("Sapo", R.drawable.sapo, R.raw.sapo));
        anfibios.add(new Animal("Axolote", R.drawable.axolote, R.raw.axolote));
        anfibios.add(new Animal("Perereca", R.drawable.perereca, R.raw.perereca));
        anfibios.add(new Animal("Salamandra", R.drawable.salamandra, R.raw.salamandra));
    }

    private void configurarGrid(GridView grid, List<Animal> lista) {
        grid.setAdapter(new AdaptadorGridView(getContext(), lista.subList(0,4)));
        grid.setOnItemClickListener((parent, view, position, id) -> {

            Bundle bundle = new Bundle();
            bundle.putSerializable("lista_animais", (ArrayList<Animal>) lista);
            bundle.putInt("indice_atual", 0);
            
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
