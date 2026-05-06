package com.example.ditado.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.ditado.entities.Animal;
import com.example.ditado.entities.PalavrasAprendidas;

import java.util.List;

@Dao
public interface PalavrasAprendidasDao {

    @Query("SELECT * FROM PalavrasAprendidas")
    List<PalavrasAprendidas> getAll();

    @Query("SELECT * FROM PalavrasAprendidas WHERE id_palavras_aprendidas = :id")
    List<PalavrasAprendidas> getById(int id);

    @Insert
    void insert(PalavrasAprendidas palavrasAprendidas);

    @Update
    void update(PalavrasAprendidas palavrasAprendidas);

    @Delete
    void delete(PalavrasAprendidas palavrasAprendidas);

}
