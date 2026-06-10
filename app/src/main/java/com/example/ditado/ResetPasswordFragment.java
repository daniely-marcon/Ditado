package com.example.ditado;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ditado.databinding.FragmentResetPasswordBinding;
import com.example.ditado.entities.Usuario;

public class ResetPasswordFragment extends Fragment {

    private FragmentResetPasswordBinding binding;
    private CountDownTimer timer;
    private String codigo;
    private String emailRecebido;
    private UserManager userManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userManager = new UserManager(requireContext());


        if (getArguments() != null) {
            codigo = getArguments().getString("codigoEnviado");
            emailRecebido = getArguments().getString("email");
        }

        binding.btnConfirmar.setOnClickListener(v -> {
            String codigoDigitado = binding.edtCode.getText().toString().trim();
            String novaSenha = binding.edtNovaSenha.getText().toString().trim();
            String confirmarSenha = binding.edtConfirmaSenha.getText().toString().trim();

            if (codigoDigitado.isEmpty() || novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
                Toast.makeText(getContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }


            if (emailRecebido == null || emailRecebido.isEmpty()) {
                Toast.makeText(getContext(), "Erro Crítico: E-mail não recebido da tela anterior!", Toast.LENGTH_LONG).show();
                return;
            }

            if (!codigoDigitado.equals(codigo)) {
                Toast.makeText(getContext(), "Código de verificação incorreto!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!novaSenha.equals(confirmarSenha)) {
                Toast.makeText(getContext(), "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isSenhaForte(confirmarSenha)) {
                binding.edtNovaSenha.setError("A senha deve conter pelo menos 8 caracteres, incluindo letras maiúsculas, minúsculas, números e caracteres especiais (@#$%^&+=!).");
                binding.edtConfirmaSenha.requestFocus();
                return;
            }


            userManager.redefinirSenhaPorEmail(emailRecebido, novaSenha, (sucesso, erroMensagem) -> {


                requireActivity().runOnUiThread(() -> {
                    if (sucesso) {
                        Toast.makeText(getContext(), "Senha atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigate(R.id.action_ResetPasswordFragment_to_LoginFragment);
                    } else {
                        Toast.makeText(getContext(), "Erro técnico: " + erroMensagem, Toast.LENGTH_LONG).show();
                    }
                });
            });
        });

        binding.btnVoltarR.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_ResetPasswordFragment_to_CodeRequestFragment);
        });
    }
    private boolean isSenhaForte(String senha) {

        String regexSenha = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return senha.matches(regexSenha);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.cancel();
        }
        binding = null;
    }
}