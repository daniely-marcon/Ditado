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

    @Query("SELECT A.* FROM Animal AS A INNER JOIN PalavrasAprendidas AS PA ON A.id_animal = PA.id_animal WHERE PA.id_usuario = :id_usuario")
    List<Animal> getPalavrasPorUsuario(int id_usuario);

    @Query("DELETE FROM PalavrasAprendidas WHERE id_usuario = :id_usuario")
    void ApagarPalavrasUsuario(int id_usuario);
    @Insert
    void insert(PalavrasAprendidas palavrasAprendidas);

    @Update
    void update(PalavrasAprendidas palavrasAprendidas);

    @Delete
    void delete(PalavrasAprendidas palavrasAprendidas);

}
