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
    private String codigo; // Codigo enviado para o email do usuário
    private AppDatabase db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Recupera o código e email enviados pela tela anterior
        if (getArguments() != null) {
            codigo = getArguments().getString("codigoEnviado");
        }

        binding.btnConfirmar.setOnClickListener(v -> {
            String codigoDigitado = binding.edtCode.getText().toString().trim();
            String novaSenha = binding.editText2.getText().toString().trim();
            String confirmarSenha = binding.editText.getText().toString().trim();

            if (codigoDigitado.isEmpty() || novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
                Toast.makeText(getContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (codigoDigitado.equals(codigo)) {
                if (novaSenha.equals(confirmarSenha)) {


                    new Thread(() -> {
                        String email = getArguments().getString("email");
                        db = AppDatabase.getDatabase(requireContext());

                        Usuario usuario = db.usuarioDao().buscarUsuario(email);

                        if (usuario != null) {
                            usuario.setSenha(novaSenha);
                            db.usuarioDao().update(usuario, novaSenha);

                            // Volta para a Main Thread para mostrar o Toast e navegar
                            requireActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), "Senha atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                                NavHostFragment.findNavController(this).navigate(R.id.action_ResetPasswordFragment_to_LoginFragment);
                            });
                        }
                    }).start();

                } else {
                    Toast.makeText(getContext(), "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getContext(), "Código de verificação incorreto!", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).navigate(R.id.action_ResetPasswordFragment_to_CodeRequestFragment);
            }
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