package com.example.ditado;

import android.os.Bundle;
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
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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


            Toast.makeText(getContext(), "Buscando usuário e enviando e-mail...", Toast.LENGTH_SHORT).show();

            new Thread(() -> {
                AppDatabase db = AppDatabase.getDatabase(requireContext());
                Usuario usuario = db.usuarioDao().buscarUsuario(email);

                if (usuario == null) {

                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "E-mail não encontrado no sistema.", Toast.LENGTH_SHORT).show();
                    });
                } else {

                    String codigoGerado = String.valueOf(new Random().nextInt(899999) + 100000);


                    enviarEmailBackground(email, "Código de Recuperação - App Ditado",
                            "<h2>Recuperação de Senha</h2><p>Seu código de verificação é: <b>" + codigoGerado + "</b></p><br><p>Se você não solicitou isso, ignore este e-mail.</p>");


                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Código enviado para o e-mail!", Toast.LENGTH_LONG).show();


                        Bundle args = new Bundle();
                        args.putString("codigoEnviado", codigoGerado);
                        args.putString("email", email);

                        NavHostFragment.findNavController(this)
                                .navigate(R.id.action_CodeRequestFragment_to_ResetPasswordFragment, args);
                    });
                }
            }).start();
        });

        binding.btnVoltarCODE.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_CodeRequestFragment_to_LoginFragment);
        });
    }


    private void enviarEmailBackground(String destinatario, String assunto, String mensagemHtml) {


        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());

        final String emailRemetente = "max0724sh@gmail.com";
        final String senhaRemetente = "gsldnkvkygruvyvq";


        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");


        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailRemetente, senhaRemetente);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailRemetente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(assunto);
            message.setContent(mensagemHtml, "text/html; charset=utf-8");

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();


            requireActivity().runOnUiThread(() -> {
                String erroDetalhado = (e.getCause() != null) ? e.getCause().toString() : e.getMessage();
                Toast.makeText(getContext(), "Erro SMTP: " + erroDetalhado, Toast.LENGTH_LONG).show();
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}