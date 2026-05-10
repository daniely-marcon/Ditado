package com.example.ditado;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ditado.database.AppDatabase;
import com.example.ditado.databinding.FragmentLoginBinding;
import com.example.ditado.entities.Usuario;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private AppDatabase db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getDatabase(requireContext());


        criarUsuarioPadrao();


        binding.btnEntrar.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString().trim();
            String senha = binding.edtSenha.getText().toString().trim();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(getContext(), "Preencha e-mail e senha!", Toast.LENGTH_SHORT).show();
                return;
            }

            fazerLogin(email, senha);
        });

        binding.btnCadastrar.setOnClickListener(v ->
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_LoginFragment_to_CadastroFragment));

        binding.txtResetPass.setOnClickListener(v ->
                        NavHostFragment.findNavController(LoginFragment.this)
                                .navigate(R.id.action_LoginFragment_to_CodeRequestFragment));

    }

    private void criarUsuarioPadrao() {
        new Thread(() -> {
            Usuario userTeste = db.usuarioDao().buscarUsuario("aluno@teste.com","123");

            if (userTeste == null) {
                Usuario novoUser = new Usuario("Teste","aluno@teste.com","123",null,"Aluno");
                db.usuarioDao().insert(novoUser);
            }
        }).start();
    }

    private void fazerLogin(String email, String senha) {
        new Thread(() -> {
            Usuario usuario = db.usuarioDao().buscarUsuario(email, senha);

            requireActivity().runOnUiThread(() -> {
                if (usuario != null) {
                    SharedPreferences pref = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putInt("id_usuario", usuario.getId_usuario());


                    editor.putString("tipo_usuario", usuario.getTipo());

                    editor.apply();

                    Toast.makeText(getContext(), "Bem-vindo!", Toast.LENGTH_SHORT).show();

                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_LoginFragment_to_FirstFragment);
                } else {
                    Toast.makeText(getContext(), "E-mail ou senha incorretos!", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}