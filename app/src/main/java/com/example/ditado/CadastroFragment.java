package com.example.ditado;
import static android.view.View.GONE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import org.mindrot.jbcrypt.BCrypt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CadastroFragment extends Fragment {

    private FragmentCadastroBinding binding;
    private Bitmap fotoBitmap;
    private AppDatabase db;
    private Uri cameraUri;


    private int idUsuarioLogado = -1;
    private Usuario usuarioLogado;
    private boolean isModoEdicao = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCadastroBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getDatabase(requireContext());

        if (getArguments() != null) {
            idUsuarioLogado = getArguments().getInt("id_usuario_edicao", -1);
        }

        if (idUsuarioLogado != -1) {
            isModoEdicao = true;
            configurarLayoutEdicao();
            carregarDadosDoUsuario();
        }
// Essa condição  está repetida ?
        if (idUsuarioLogado != -1) {
            isModoEdicao = true;
            configurarLayoutEdicao();
            carregarDadosDoUsuario();
        }

        binding.imgFoto.setOnClickListener(v -> abrirCamera());


        binding.edtEmailUser.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String email = binding.edtEmailUser.getText().toString().trim();
                if (!email.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.edtEmailUser.setError("Formato de e-mail inválido");
                }
            }
        });


        binding.btnCadastrarUser.setOnClickListener(v -> {
            String nome = binding.edtNomeUser.getText().toString().trim();
            String email = binding.edtEmailUser.getText().toString().trim();
            String senha = binding.edtSenhaUser.getText().toString().trim();
            String tipo = binding.radioProfessor.isChecked() ? "Professor" : "Aluno";


            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || (!isModoEdicao && fotoBitmap == null)) {
                Toast.makeText(getContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmailUser.setError("Digite um e-mail válido!");
                binding.edtEmailUser.requestFocus();
                return;
            }

            if (!isSenhaForte(senha)) {
                binding.edtSenhaUser.setError("A senha deve conter pelo menos 8 caracteres, incluindo letras maiúsculas, minúsculas, números e caracteres especiais (@#$%^&+=!).");
                binding.edtSenhaUser.requestFocus();
                return;
            }


            byte[] fotoBytes = null;
            if (fotoBitmap != null) {
                Bitmap fotoRedimensionada = redimensionarBitmap(fotoBitmap, 500);
                java.io.ByteArrayOutputStream stream = new java.io.ByteArrayOutputStream();
                fotoRedimensionada.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                fotoBytes = stream.toByteArray();
            } else if (isModoEdicao && usuarioLogado != null) {
                fotoBytes = usuarioLogado.getImagem_usuario();
            }


            UserManager userManager = new UserManager(requireContext());

            if (isModoEdicao) {

                userManager.updateUser(idUsuarioLogado, nome, email, tipo, fotoBytes, () -> {


                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show();


                        if (getActivity() instanceof MainActivity) {
                            ((MainActivity) getActivity()).carregarDadosDoUsuarioToolbar();
                        }

                        //
                        NavHostFragment.findNavController(CadastroFragment.this)
                                .navigate(R.id.action_CadastroFragment_to_FirstFragment);
                });
               });

            } else {
                // O bloco do cadastro normal (registerUser) continua igual aqui embaixo...
                userManager.registerUser(nome, email, senha, tipo, fotoBytes);
                Toast.makeText(getContext(), "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).navigateUp();
            }
        });

        binding.btnVoltar.setOnClickListener(v -> {
            if(isModoEdicao){
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_CodeRequestFragment_to_FirstFragment);
            }
            else {
                NavHostFragment.findNavController(this).navigateUp();
            }
        });

        binding.btnSenha.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (usuarioLogado != null) {
                bundle.putString("email", usuarioLogado.getEmail());
            }
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_CadastroFragment_to_CodeRequestFragment, bundle);
        });

    }
    private boolean isSenhaForte(String senha) {

        String regexSenha = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#*$%^&+=!])(?=\\S+$).{8,}$";
        return senha.matches(regexSenha);
    }
    private void configurarLayoutEdicao() {
        binding.txtCadastro.setText("Alterar Dados");
        binding.btnCadastrarUser.setText("Salvar");
        binding.edtSenhaUser.setVisibility(View.INVISIBLE);
        binding.txtSenha.setVisibility(View.INVISIBLE);
        binding.btnSenha.setVisibility(View.VISIBLE);
    }



    private void carregarDadosDoUsuario() {
        new Thread(() -> {
            usuarioLogado = db.usuarioDao().getUsuarioById(idUsuarioLogado);

            if (usuarioLogado != null) {
                requireActivity().runOnUiThread(() -> {
                    binding.edtNomeUser.setText(usuarioLogado.getNome_usuario());
                    binding.edtEmailUser.setText(usuarioLogado.getEmail());

                    binding.txtTIPOuser.setVisibility(GONE);
                    binding.radioAluno.setVisibility(GONE);
                    binding.radioProfessor.setVisibility(GONE);
                    if ("Professor".equals(usuarioLogado.getTipo())) {
                        binding.radioProfessor.setChecked(true);
                    } else {
                        binding.radioAluno.setChecked(true);
                    }

                    byte[] fotoBytes = usuarioLogado.getImagem_usuario();
                    if (fotoBytes != null && fotoBytes.length > 0) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);
                        binding.imgFoto.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();
    }

    private Bitmap redimensionarBitmap(Bitmap imagem, int tamanhoMaximo) {
        int largura = imagem.getWidth();
        int altura = imagem.getHeight();

        float proporcaoBitmap = (float) largura / (float) altura;
        if (proporcaoBitmap > 1) {
            largura = tamanhoMaximo;
            altura = (int) (largura / proporcaoBitmap);
        } else {
            altura = tamanhoMaximo;
            largura = (int) (altura * proporcaoBitmap);
        }
        return Bitmap.createScaledBitmap(imagem, largura, altura, true);
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
        SharedPreferences pref = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        pref.edit().remove("id_usuario").apply();

        super.onDestroyView();
        binding = null;
    }
}
