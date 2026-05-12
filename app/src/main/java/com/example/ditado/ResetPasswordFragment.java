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

import com.example.ditado.database.AppDatabase;
import com.example.ditado.databinding.FragmentResetPasswordBinding;
import com.example.ditado.entities.Usuario;

public class ResetPasswordFragment extends Fragment {

    private FragmentResetPasswordBinding binding;
    private CountDownTimer timer;
    private String codigo;
    private String emailRecebido; // <-- Variável global para guardar o e-mail
    private AppDatabase db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Recupera o código E O EMAIL enviados pela tela anterior logo de cara
        if (getArguments() != null) {
            codigo = getArguments().getString("codigoEnviado");
            emailRecebido = getArguments().getString("email");
        }

        binding.btnConfirmar.setOnClickListener(v -> {
            String codigoDigitado = binding.edtCode.getText().toString().trim();
            String novaSenha = binding.editText2.getText().toString().trim();
            String confirmarSenha = binding.editText.getText().toString().trim();

            if (codigoDigitado.isEmpty() || novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
                Toast.makeText(getContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Trava de segurança
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

            // SE CHEGOU AQUI, TUDO ESTÁ PREENCHIDO CORRETAMENTE. VAMOS AO BANCO:
            new Thread(() -> {
                try {
                    db = AppDatabase.getDatabase(requireContext());

                    // Comandamos o banco a mudar a senha diretamente onde o e-mail for igual
                    db.usuarioDao().atualizarSenhaPorEmail(emailRecebido, novaSenha);

                    // Se chegou aqui, o SQL rodou.
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Senha atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigate(R.id.action_ResetPasswordFragment_to_LoginFragment);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Erro técnico: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            }).start();
        });

        binding.btnVoltarR.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_ResetPasswordFragment_to_CodeRequestFragment);
        });
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