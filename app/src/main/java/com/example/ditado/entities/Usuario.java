package com.example.ditado.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Usuario {
    @PrimaryKey(autoGenerate = true)
    private int id_usuario;
    private String nome_usuario;
    private String email;
    private String senha;
    private byte[] imagem_usuario;
    private String tipo;

    public Usuario(String nome_usuario,String email,String senha,byte[] imagem_usuario,String tipo){
        this.nome_usuario=nome_usuario;
        this.email=email;
        this.senha=senha;
        this.imagem_usuario=imagem_usuario;
        this.tipo=tipo;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }



    public String getEmail(){
        return this.email;
    }


    public String getSenha(){
        return this.senha;
    }


    public byte[] getImagem_usuario(){
        return this.imagem_usuario;
    }

    public void setImagem_usuario(byte[] imagem_usuario) {
        this.imagem_usuario = imagem_usuario;
    }

    public String getNome_usuario() {
        return nome_usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo=tipo;
    }

    public void setEmail(String email) {
        this.email=email;
    }

    public void setNome_usuario(String nome_usuario) {
        this.nome_usuario=nome_usuario;
    }

    public void setSenha(String number) { this.senha=number; }

}
