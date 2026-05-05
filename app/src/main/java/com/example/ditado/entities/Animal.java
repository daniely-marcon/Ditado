package com.example.ditado.entities;

import java.io.Serializable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Animal implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id_animal;
    private String nome_animal;
    private byte[] imagem_animal;
    private byte[] audio_animal;

    public Animal(String nome_animal, byte[] imagem_animal, byte[] audio_animal){
        this.nome_animal = nome_animal;
        this.imagem_animal = imagem_animal;
        this.audio_animal = audio_animal;
    }

    public int getId() { return id_animal; }
    public void setId(int id) { this.id_animal = id_animal; }

    public String getNome(){ return nome_animal; }
    public void setNome(String nome_animal) { this.nome_animal = nome_animal; }

    public byte[] getAudio_animal(){ return audio_animal; }
    public void setAudio_animal(int audio_animal) { this.audio_animal = audio_animal; }

    public byte[] getImagem_animal(){ return imagem_animal; }
    public void setImagem_animal(int imagem_animal) { this.imagem_animal = imagem_animal; }

}

