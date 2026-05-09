package com.example.ditado.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity
public class Animal implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id_animal;
    private String nome_animal;
    private byte[] imagem_animal;
    private String filo_animal;

    public Animal(String nome_animal, byte[] imagem_animal,String filo_animal){
        this.nome_animal = nome_animal;
        this.imagem_animal = imagem_animal;
        this.filo_animal=filo_animal;
    }

    public int getId_animal() { return id_animal; }
    public void setId_animal(int id_animal) {
        this.id_animal = id_animal;
    }

    public String getNome_animal(){ return nome_animal; }
    public void setNome_animal(String nome_animal) { this.nome_animal = nome_animal; }

    public byte[] getImagem_animal(){ return imagem_animal; }
    public void setImagem_animal(byte[] imagem_animal) { this.imagem_animal = imagem_animal; }


    public String getFilo_animal(){
        return this.filo_animal;
    }
    public void setFilo_animal(String filo_animal){
        this.filo_animal=filo_animal;
    }

}

