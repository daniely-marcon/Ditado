package com.example.ditado.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AudioAnimal {
    @PrimaryKey(autoGenerate = true)
    private int id_audio;
    private String titulo;
    private int audioResId;

    public AudioAnimal(String titulo, int audioResId) {
        this.titulo = titulo;
        this.audioResId = audioResId;
    }

    // Getters and Setters
    public int getId_audio() {
        return id_audio;
    }

    public void setId_audio(int id_audio) {
        this.id_audio = id_audio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAudioResId() { return audioResId; }
    public void setAudioResId(int audioResId) { this.audioResId = audioResId; }
}
