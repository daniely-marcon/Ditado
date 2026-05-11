package com.example.ditado;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ditado.database.AppDatabase;
import com.example.ditado.databinding.FragmentCadastroBinding;
import com.example.ditado.entities.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class CadastroFragment extends Fragment {

    private FragmentCadastroBinding binding;
    private Bitmap fotoBitmap;
    private AppDatabase db;
    private Uri cameraUri;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCadastroBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.imgFoto.setOnClickListener(v -> abrirCamera());

        binding.btnCadastrarUser.setOnClickListener(v -> {
            String nome = binding.edtNomeUser.getText().toString().trim();
            String email = binding.edtEmailUser.getText().toString().trim();
            String senha = binding.edtSenhaUser.getText().toString().trim();
            String tipo = binding.radioProfessor.isChecked() ? "Professor" : "Aluno";

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || fotoBitmap == null) {
                Toast.makeText(getContext(), "Preencha todos os campos e tire uma foto!", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    fotoBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
                    byte[] fotoBytes = stream.toByteArray();

                    db = AppDatabase.getDatabase(requireContext());
                    Usuario novoUser = new Usuario(nome, email, senha, fotoBytes, tipo);
                    db.usuarioDao().insert(novoUser);

                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigateUp();
                    });
                } catch (Exception e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Erro ao cadastrar usuário!", Toast.LENGTH_SHORT).show()
                    );
                }
            }).start();
        });

        binding.btnVoltar.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigateUp();
        });
    }

    private void carregarImagem(Uri uri) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.Source source = ImageDecoder.createSource(requireContext().getContentResolver(), uri);
                fotoBitmap = ImageDecoder.decodeBitmap(source);
            } else {
                fotoBitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
            }
            binding.imgFoto.setImageBitmap(fotoBitmap);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Erro ao carregar imagem", Toast.LENGTH_SHORT).show();
        }
    }

    private final ActivityResultLauncher<Uri> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            success -> {
                if (success && cameraUri != null) {
                    carregarImagem(cameraUri);
                }
            }
    );

    private void abrirCamera() {
        try {
            File imageFile = new File(requireContext().getFilesDir(), "foto_usuario.jpg");
            cameraUri = FileProvider.getUriForFile(requireContext(),
                    "com.example.ditado.fileprovider", imageFile);
            cameraLauncher.launch(cameraUri);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erro ao abrir a câmera", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}