package com.example.ditado.database;

import com.example.ditado.entities.Animal;
import com.example.ditado.entities.PalavrasAprendidas;
import com.example.ditado.entities.Usuario;

@Database(entities = {Usuario.class, Animal.class, PalavrasAprendidas.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
}
