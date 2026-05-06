package com.example.ditado.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ditado.dao.AnimalDao;
import com.example.ditado.dao.UsuarioDao;
import com.example.ditado.dao.PalavrasAprendidasDao;
import com.example.ditado.entities.Animal;
import com.example.ditado.entities.PalavrasAprendidas;
import com.example.ditado.entities.Usuario;

@Database(entities = {Usuario.class, Animal.class, PalavrasAprendidas.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UsuarioDao usuarioDao();
    public abstract AnimalDao animalDao();
    public abstract PalavrasAprendidasDao palavrasAprendidasDao();
}
