package com.example.ditado.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ditado.entities.AudioAnimal;

import java.util.List;

@Dao
public interface AudioAnimalDao {

    @Query("SELECT * FROM AudioAnimal")
    List<AudioAnimal> getAll();

    @Query("SELECT * FROM AudioAnimal WHERE id_audio = :id LIMIT 1")
    AudioAnimal getAudioById(int id);

    @Insert
    void insert(AudioAnimal audio);

    @Update
    void update(AudioAnimal audio);

    @Delete
    void deletarAudio(AudioAnimal audio);
}