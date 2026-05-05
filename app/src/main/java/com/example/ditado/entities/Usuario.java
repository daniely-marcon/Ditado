package com.example.ditado.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Usuario {
    @PrimaryKey(autoGenerate = true)
    private int id_usuario;

    private String nome_user;
    private String email;
    private String senha;
    private Byte imagem_user;
    private String tipo;

}
