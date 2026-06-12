package com.example.ditado;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ditado.database.AppDatabase;
import com.example.ditado.databinding.ActivityMainBinding;
import com.example.ditado.entities.Usuario;
import com.google.android.material.appbar.AppBarLayout;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private AppDatabase db;
    private NavController navController;

    public float fonte = 18f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        androidx.core.splashscreen.SplashScreen splashScreen =
                androidx.core.splashscreen.SplashScreen.installSplashScreen(this);


        final long tempoEspera = System.currentTimeMillis() + 2000;
        splashScreen.setKeepOnScreenCondition(() -> System.currentTimeMillis() < tempoEspera);


        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        db = AppDatabase.getDatabase(this);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.FirstFragment,
                R.id.LoginFragment
        ).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        AppBarLayout appBarLayout = findViewById(R.id.meuAppBarLayout);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int telaAtual = destination.getId();

            if (telaAtual == R.id.LoginFragment ||
                    telaAtual == R.id.CadastroFragment ||
                    telaAtual == R.id.ResetPasswordFragment ||
                    telaAtual == R.id.CodeRequestFragment) {

                appBarLayout.setVisibility(View.GONE);
            } else {
                appBarLayout.setVisibility(View.VISIBLE);
                carregarDadosDoUsuarioToolbar();

                TextView txtNomeTela = findViewById(R.id.txtNomeTela);
                CharSequence nomeDaTela = destination.getLabel();

                if (nomeDaTela != null) {
                    txtNomeTela.setText(nomeDaTela);
                } else {
                    txtNomeTela.setText("");
                }
            }
        });

        tratarDadosDaIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        tratarDadosDaIntent(intent);
    }

    private void tratarDadosDaIntent(Intent intent) {
        if (intent != null && intent.hasExtra("destino")) {
            String destino = intent.getStringExtra("destino");
            if ("abrir_parabens".equals(destino)) {
                intent.removeExtra("destino");

                binding.getRoot().post(() -> {
                    try {
                        if (navController != null) {
                            navController.navigate(R.id.ParabensFragment);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    public void carregarDadosDoUsuarioToolbar() {
        SharedPreferences pref = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        int idUsuario = pref.getInt("id_usuario", -1);

        if (idUsuario != -1) {
            new Thread(() -> {
                Usuario usuarioLogado = db.usuarioDao().getUsuarioById(idUsuario);

                if (usuarioLogado != null) {
                    runOnUiThread(() -> {
                        TextView txtNome = findViewById(R.id.txtNomeUsuarioToolbar);
                        ImageView imgFoto = findViewById(R.id.imgFotoUsuarioToolbar);

                        txtNome.setText(usuarioLogado.getNome_usuario());

                        byte[] fotoBytes = usuarioLogado.getImagem_usuario();
                        if (fotoBytes != null && fotoBytes.length > 0) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);
                            imgFoto.setImageBitmap(bitmap);
                        } else {
                            imgFoto.setImageResource(R.drawable.ic_launcher_background);
                        }

                        View.OnClickListener abrirPerfilListener = v -> {
                            if (navController.getCurrentDestination() != null &&
                                    navController.getCurrentDestination().getId() != R.id.CadastroFragment) {

                                Bundle args = new Bundle();
                                args.putInt("id_usuario_edicao", idUsuario);
                                navController.navigate(R.id.CadastroFragment, args);
                            }
                        };

                        txtNome.setOnClickListener(abrirPerfilListener);
                        imgFoto.setOnClickListener(abrirPerfilListener);
                    });
                }
            }).start();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}