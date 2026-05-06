package com.example.ditado.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ditado.dao.AnimalDao;
import com.example.ditado.dao.UsuarioDao;
import com.example.ditado.entities.Animal;
import com.example.ditado.entities.PalavrasAprendidas;
import com.example.ditado.entities.Usuario;

@Database(entities = {Usuario.class, Animal.class, PalavrasAprendidas.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AnimalDao animalDao();
    public abstract  PalavrasAprendidas palavrasAprendidas();
    public abstract UsuarioDao usuarioDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context){
        if(INSTANCE==null){
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,"database")
                    .allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

}
