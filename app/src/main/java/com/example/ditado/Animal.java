package com.example.ditado;

import java.util.ArrayList;
import java.util.List;

public class Animal{
    private String nome;
    private int imagemId; // ref para res/drawable
    private int audioId; // ref para res/raw

    public Animal(String nome, int imagemId, int audioId){
        this.nome = nome;
        this.imagemId = imagemId;
        this.audioId = audioId;
    }

    public String getNome(){
        return nome;
    }

    public int getImagemId(){
        return imagemId;
    }

    public int getAudioId(){
        return audioId;
    }


}

