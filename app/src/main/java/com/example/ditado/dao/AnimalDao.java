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

    @Insert
    void insert(Animal animal);

    @Update
    void update(Animal animal);

    @Delete
    void delete(Animal animal);

}
