package com.example.ditado.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.ditado.entities.Animal;
import com.example.ditado.entities.Usuario;

import java.util.List;

@Dao
public interface UsuarioDao {

    @Query("SELECT * FROM Usuario WHERE id_usuario = :id")
    List<Usuario> getById(int id);

    @Query("SELECT * FROM Usuario")
    List<Usuario> loadAllUsers();

    @Query("SELECT ")

    @Update("SELECT * FROM Usuario WHERE email =: email")
    List<Usuario> getByEmail(String email);

    @Insert
    void insert(Usuario usuario);

    @Update
    void updateUsers(Usuario usuario);

    @Delete
    void deleteUsers(Usuario usuario);

}
