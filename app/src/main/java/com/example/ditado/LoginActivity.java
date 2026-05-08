package com.example.ditado;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ditado.databinding.ActivityMainBinding;
import com.example.ditado.entities.Usuario;

public class LoginActivity extends AppCompatActivity {

    public ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void Cadastrar() {
        Intent it=new Intent(this, CadastroFragment.class);
        startActivity(it);
    }
    private void Entrar() {
        String email = binding.edtEmail1.getText().toString();
        String senha = binding.edtSenha1.getText().toString();
        Usuario usuario = db.usuarioDao().buscarUsuario(email, senha);
        if(usuario !=null){
            Intent it= new Intent(MainActivity.this, ActivityLogin.class);
            it.putExtra("nome_usuario", usuario.getNome());
            startActivity(it);
        }else{
            Toast.makeText(this,"Usuario ou senha inválido!",Toast.LENGTH_SHORT).show();
        }
    }
}