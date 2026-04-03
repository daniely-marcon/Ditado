package com.example.ditado;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ditado.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private List<Animal> aves = new ArrayList<>();{
        aves.add(new Animal("Arara", R.drawable.arara, R.raw.arara));
        aves.add(new Animal("Tucano", R.drawable.tucano, R.raw.tucano));
        aves.add(new Animal("Beija-Flor", R.drawable.beijaflor, R.raw.beijaflor));
        aves.add(new Animal("Papagaio", R.drawable.papagaio, R.raw.papagaio));
        aves.add(new Animal("Rolinha", R.drawable.rolinha, R.raw.rolinha));
    }

    private List<Animal> repteis = new ArrayList<>();{
        repteis.add(new Animal("Cobra Coral", R.drawable.coral, R.raw.cobracoral));
        repteis.add(new Animal("Iguana", R.drawable.iguana, R.raw.iguana));
        repteis.add(new Animal("Sucuri", R.drawable.sucuri, R.raw.sucuri));
        repteis.add(new Animal("Tartaruga", R.drawable.tartaruga, R.raw.tartaruga));
        repteis.add(new Animal("Lagartixa", R.drawable.lagartixa, R.raw.lagartixa));
    }

    private List<Animal> peixes = new ArrayList<>();{
        peixes.add(new Animal("Pirarucu", R.drawable.pirarucu, R.raw.pirarucu));
        peixes.add(new Animal("Piau", R.drawable.piau, R.raw.piau));
        peixes.add(new Animal("Dourado", R.drawable.dourado, R.raw.dourado));
        peixes.add(new Animal("Piraputanga", R.drawable.piraputanga, R.raw.piraputanga));
        peixes.add(new Animal("Bagre", R.drawable.bagre, R.raw.bagre));


    }

    private List<Animal> anfibios = new ArrayList<>();{
        anfibios.add(new Animal("Sapo", R.drawable.sapo, R.raw.sapo));
        anfibios.add(new Animal("Axolote", R.drawable.axolote, R.raw.axolote));
        anfibios.add(new Animal("Perereca", R.drawable.perereca, R.raw.perereca));
        anfibios.add(new Animal("Salamandra", R.drawable.salamandra, R.raw.salamandra));

    }
}

