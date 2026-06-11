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
import com.example.ditado.dao.UsuarioDao;
import com.example.ditado.databinding.FragmentLoginBinding;
import com.example.ditado.entities.Usuario;
import com.example.ditado.security.SecurityUtils; // Importa a classe de segurança da sua professora

import org.mindrot.jbcrypt.BCrypt;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private UserManager userManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        userManager = new UserManager(requireContext());

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

    private void fazerLogin(String email, String senhaDigitada) {

        userManager.buscarUsuarioPorEmail(email, usuario -> {


            requireActivity().runOnUiThread(() -> {


                if (usuario != null) {

                    boolean senhaValida = SecurityUtils.verifyPassword(senhaDigitada, usuario.getSenha());

                    if (senhaValida) {

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

                } else {

                    Toast.makeText(getContext(), "E-mail ou senha incorretos!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}