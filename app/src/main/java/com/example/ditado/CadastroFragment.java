package com.example.ditado;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ditado.database.AppDatabase;
import com.example.ditado.databinding.FragmentCodeRequestBinding;
import com.example.ditado.entities.Usuario;


public class CadastroFragment extends Fragment {

    private FragmentCodeRequestBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCodeRequestBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }/*

    private static final int CAMERA_PERMISSION_CODE=1;
    ActivityResultLauncher<Uri> takePictureLauncher;
    Uri imageUri;
    private EditText edtNome, edtEmail, edtSenha;
    private ImageView imgFoto;
    private Bitmap fotoBitmap;
    private AppDatabase db;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnCadastrar_User.setOnClickListener(v -> {
            String nome = binding.edtNome_User.getText().toString().trim();
            String email = binding.edtEmail_User.getText().toString().trim();
            String senha = binding.edtSenha_User.getText().toString().trim();
            ImageView foto = binding.imgFoto;
            String tipo = binding.groupTipo.getCheckedRadioButtonId() == R.id.radioProfessor.isChecked() ? "Professor" : "Aluno";

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) || tipo.isEmpty()){
                Toast.makeText(getContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                AppDatabase db = AppDatabase.getDatabase(requireContext());
                Usuario novoUser = new Usuario(nome, email, senha, null, tipo);
                db.usuarioDao().insert(novoUser);

            }
        });
    }*/
}