package com.example.ditado.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.ditado.entities.Animal;
import java.util.List;

@Dao
public interface AnimalDao {

    @Query("SELECT * FROM Animal")
    List<Animal> getAll();

    @Query("SELECT * FROM Animal WHERE id_animal = :id")
    List<Animal> getById(int id);

    @Query("SELECT * FROM Animal WHERE filo_animal = :filo")
    List<Animal> buscarPorFilo(String filo);

    @Insert
    void insert(Animal animal);

    @Update
    void update(Animal animal);

    @Delete
    void delete(Animal animal);


}
