package com.example.ditado;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ditado.database.AppDatabase;
import com.example.ditado.databinding.FragmentCodeRequestBinding;
import com.example.ditado.entities.Usuario;

import java.util.Random;

public class CodeRequestFragment extends Fragment {

    private FragmentCodeRequestBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentCodeRequestBinding.inflate(inflater, container, false);
       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnEnviarCODE.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, insira seu e-mail", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                AppDatabase db = AppDatabase.getDatabase(requireContext());
                Usuario usuario = db.usuarioDao().buscarUsuario(email);

                requireActivity().runOnUiThread(() -> {
                    if (usuario == null) {
                        Toast.makeText(getContext(), "E-mail não encontrado", Toast.LENGTH_SHORT).show();
                    } else {
                        // Geração do código (6 dígitos)
                        String codigoGerado = String.valueOf(new Random().nextInt(899999) + 100000);

                        // Abre o app de e-mail
                        sendEmailSingleRecipient(email, "Código de Verificação", "Seu código é: " + codigoGerado);

                        // Passa o código para a próxima tela via bundle
                        Bundle args = new Bundle();
                        args.putString("codigoEnviado", codigoGerado);
                        args.putString("email", email);

                        NavHostFragment.findNavController(this)
                                .navigate(R.id.action_CodeRequestFragment_to_ResetPasswordFragment, args);
                    }
                });
            }).start();
        });

        binding.btnVoltarCODE.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_CodeRequestFragment_to_LoginFragment);
        });
    }

    // Metodo para enviar email
    public void sendEmailSingleRecipient(String recipient, String subject, String htmlBody) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient}); // Single recipient
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

        // Adding HTML body
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(htmlBody));
        emailIntent.setType("text/html");

        try {
            startActivity(emailIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Não há aplicativos de e-mail instalados.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
