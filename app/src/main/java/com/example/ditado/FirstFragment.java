package com.example.ditado;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ditado.database.AppDatabase;
import com.example.ditado.databinding.FragmentFirstBinding;
import com.example.ditado.entities.Animal;
import com.example.ditado.entities.Usuario;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private List<Animal> aves = new ArrayList<>();
    private List<Animal> repteis = new ArrayList<>();
    private List<Animal> peixes = new ArrayList<>();
    private List<Animal> anfibios = new ArrayList<>();
    private List<Animal> mamiferos = new ArrayList<>();
    private List<Animal> todos = new ArrayList<>();
    private Usuario usuarioLogado;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        carregarDadosDoBanco();
        recuperarDadosUsuario();

        binding.fabF1.setOnClickListener(v -> {


            if (usuarioLogado != null && usuarioLogado.getTipo() != null) {

                if (usuarioLogado.getTipo().equals("Aluno")) {
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_TerceiroFragment);

                } else {
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_CadastroAnimalFragment);
                }

            } else {

                Toast.makeText(getContext(), "Carregando dados, aguarde um segundo...", Toast.LENGTH_SHORT).show();
            }
        });

        configurarCliquesCategorias();
        configurarSpinnerFonte();

        binding.swc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                if(binding.swc.isChecked()){
                    binding.swc.setText("Agrupado");
                    binding.gvAnfibios.setVisibility(getView().VISIBLE);
                    binding.gvAves.setVisibility(getView().VISIBLE);
                    binding.gvPeixes.setVisibility(getView().VISIBLE);
                    binding.gvRepteis.setVisibility(getView().VISIBLE);
                    binding.gvMamiferos.setVisibility(getView().VISIBLE);
                    binding.txtPeixes.setVisibility(getView().VISIBLE);
                    binding.txtAves.setVisibility(getView().VISIBLE);
                    binding.txtRepteis.setVisibility(getView().VISIBLE);
                    binding.txtAnfibios.setVisibility(getView().VISIBLE);
                    binding.txtMamiferos.setVisibility(getView().VISIBLE);
                    binding.lvAnimais.setVisibility(getView().GONE);
                }else{
                    binding.swc.setText("Listado");
                    binding.gvAnfibios.setVisibility(getView().GONE);
                    binding.gvAves.setVisibility(getView().GONE);
                    binding.gvPeixes.setVisibility(getView().GONE);
                    binding.gvRepteis.setVisibility(getView().GONE);
                    binding.gvMamiferos.setVisibility(getView().GONE);
                    binding.txtPeixes.setVisibility(getView().GONE);
                    binding.txtAves.setVisibility(getView().GONE);
                    binding.txtRepteis.setVisibility(getView().GONE);
                    binding.txtAnfibios.setVisibility(getView().GONE);
                    binding.txtMamiferos.setVisibility(getView().GONE);
                    binding.lvAnimais.setVisibility(getView().VISIBLE);
                }
            }
        });


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
                List<Animal> tempTodos = db.animalDao().getAll();


                requireActivity().runOnUiThread(() -> {
                    if (tempAves != null) aves = tempAves;
                    if (tempPeixes != null) peixes = tempPeixes;
                    if (tempAnfibios != null) anfibios = tempAnfibios;
                    if (tempRepteis != null) repteis = tempRepteis;
                    if (tempMamiferos != null) mamiferos = tempMamiferos;
                    if(tempTodos!=null)  todos = tempTodos;

                    if (!aves.isEmpty()) configurarGrid(binding.gvAves, aves);
                    if (!peixes.isEmpty()) configurarGrid(binding.gvPeixes, peixes);
                    if (!anfibios.isEmpty()) configurarGrid(binding.gvAnfibios, anfibios);
                    if (!repteis.isEmpty()) configurarGrid(binding.gvRepteis, repteis);
                    if(!mamiferos.isEmpty()) configurarGrid(binding.gvMamiferos, mamiferos);
                    if(!todos.isEmpty()) configurarList(binding.lvAnimais, todos);
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void configurarList(ListView list, List<Animal> todos){
        if (todos == null || todos.isEmpty()) {
            list.setVisibility(View.GONE);
            return;
        }
        list.setAdapter(new AdaptadorListView(getContext(), todos, false));
        binding.lvAnimais.setOnItemClickListener((parent, v, position, id) -> {
            irParaJogo(todos, position);
        });
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
        binding.txtPeixes.setOnClickListener(v->irParaJogo(peixes,0));
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


        if (usuarioLogado == null) {
            Toast.makeText(getContext(), "Aguarde um segundo, carregando permissões...", Toast.LENGTH_SHORT).show();
            return;
        }


        if (usuarioLogado.getTipo().equals("Aluno")) {

            Bundle bundleJogo = new Bundle();
            bundleJogo.putSerializable("lista_animais", new ArrayList<>(lista));
            bundleJogo.putInt("indice_atual", indice);

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_FirstFragment_to_SecondFragment, bundleJogo);

        }

        else {
            Animal animalSelecionado = lista.get(indice);
            Bundle bundleEdicao = new Bundle();

            bundleEdicao.putInt("id_animal_editar", animalSelecionado.getId_animal());

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_FirstFragment_to_CadastroAnimalFragment, bundleEdicao);
        }
    }

    private void recuperarDadosUsuario() {
        SharedPreferences pref = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        int idUsuario = pref.getInt("id_usuario", -1);

        if (idUsuario != -1) {
            new Thread(() -> {
                AppDatabase db = AppDatabase.getDatabase(requireContext());
                usuarioLogado = db.usuarioDao().getUsuarioById(idUsuario);

                if (usuarioLogado != null) {

                    requireActivity().runOnUiThread(() -> {

                        if (usuarioLogado.getTipo().equals("Aluno")) {

                            binding.fabF1.setImageResource(R.drawable.icons8_trophy_32);

                        } else {

                            binding.fabF1.setImageResource(android.R.drawable.ic_input_add);

                        }

                    });
                }
            }).start();
        }
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