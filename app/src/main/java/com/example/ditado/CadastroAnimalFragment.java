package com.example.ditado;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ditado.database.AppDatabase;
import com.example.ditado.databinding.FragmentCadastroAnimalBinding;
import com.example.ditado.entities.Animal;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CadastroAnimalFragment extends Fragment {

    private FragmentCadastroAnimalBinding binding;

    private int idAnimalAtual = -1;
    private byte[] imagemSelecionadaBytes = null;
    private ActivityResultLauncher<String> abrirGaleriaLauncher;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCadastroAnimalBinding.inflate(inflater, container, false);

        abrirGaleriaLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        try {
                            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imgFoto.setImageBitmap(bitmap);
                            imagemSelecionadaBytes = bitmapToByteArray(bitmap);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Erro ao carregar imagem", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ArrayAdapter<CharSequence> adaptadorSpinner = ArrayAdapter.createFromResource(
                getContext(), R.array.filo, R.layout.meu_item_spinner);
        adaptadorSpinner.setDropDownViewResource(R.layout.meu_item_spinner);
        binding.spinFilo.setAdapter(adaptadorSpinner);


        if (getArguments() != null && getArguments().containsKey("id_animal_editar")) {

            int idParaEditar = getArguments().getInt("id_animal_editar");
            idAnimalAtual = idParaEditar;

            new Thread(() -> {
                AppDatabase db = AppDatabase.getDatabase(requireContext());
                Animal animalSelect = db.animalDao().getById(idParaEditar);

                if (animalSelect != null) {
                    requireActivity().runOnUiThread(() -> {


                        binding.edtNomeAnimal.setText(animalSelect.getNome_animal());


                        imagemSelecionadaBytes = animalSelect.getImagem_animal();


                        String filoDoBanco = animalSelect.getFilo_animal();
                        if (filoDoBanco != null) {
                            int posicaoSpinner = adaptadorSpinner.getPosition(filoDoBanco);
                            if (posicaoSpinner >= 0) {
                                binding.spinFilo.setSelection(posicaoSpinner);
                            }
                        }


                        if (imagemSelecionadaBytes != null && imagemSelecionadaBytes.length > 0) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(imagemSelecionadaBytes, 0, imagemSelecionadaBytes.length);
                            binding.imgFoto.setImageBitmap(bitmap);
                        }


                        binding.txtCadastro.setText("Edite as Informações");
                        binding.btnCadastrarAnimal.setText("Atualizar");


                        binding.btnExcluir.setVisibility(View.VISIBLE);
                        binding.btnExcluir.setOnClickListener(v -> confirmarExclusao(animalSelect));
                    });
                }
            }).start();

        } else {
            binding.btnExcluir.setVisibility(View.GONE);
        }


        binding.imgFoto.setOnClickListener(v -> {
            abrirGaleriaLauncher.launch("image/*");
        });


        binding.btnCadastrarAnimal.setOnClickListener(v -> {

            String nomeDigitado = binding.edtNomeAnimal.getText().toString().trim();
            String filoSelecionado = binding.spinFilo.getSelectedItem().toString();


            if (nomeDigitado.isEmpty()) {
                Toast.makeText(getContext(), "Digite o nome do animal!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (imagemSelecionadaBytes == null) {
                Toast.makeText(getContext(), "Selecione uma imagem para o animal!", Toast.LENGTH_SHORT).show();
                return;
            }


            new Thread(() -> {
                AppDatabase db = AppDatabase.getDatabase(requireContext());


                Animal animal = new Animal(nomeDigitado,  imagemSelecionadaBytes, filoSelecionado);

                if (idAnimalAtual == -1) {

                    db.animalDao().insert(animal);
                } else {

                    animal.setId_animal(idAnimalAtual);
                    db.animalDao().update(animal);
                }


                requireActivity().runOnUiThread(() -> {
                    String mensagem = (idAnimalAtual == -1) ? "Cadastrado com sucesso!" : "Atualizado com sucesso!";
                    Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(this).popBackStack();
                });

            }).start();
        });
    }


    private void confirmarExclusao(Animal animal) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Excluir Animal")
                .setMessage("Tem certeza que deseja remover o " + animal.getNome_animal() + "? Esta ação não pode ser desfeita.")
                .setPositiveButton("Sim, Excluir", (dialog, which) -> {

                    new Thread(() -> {
                        AppDatabase db = AppDatabase.getDatabase(requireContext());
                        db.animalDao().delete(animal);

                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Animal removido do aplicativo!", Toast.LENGTH_SHORT).show();
                            NavHostFragment.findNavController(this).popBackStack();
                        });
                    }).start();

                })
                .setNegativeButton("Cancelar", null)
                .show();
    }


    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}