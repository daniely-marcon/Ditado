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

    public void setNome(String nome_usuario){
        this.nome_usuario=nome_usuario;
    }
    public String getNome(){
        return this.nome_usuario;
    }

    public void setEmail(String email){
        this.email=email;
    }
    public String getEmail(){
        return this.email;
    }

    public void setSenha(String senha){
        this.senha=senha;
    }
    public String getSenha(){
        return this.senha;
    }

    public void setImagem_usuario(byte[] imagem_usuario){
        this.imagem_usuario=imagem_usuario;
    }
    public byte[] getImagem_usuario(){
        return this.imagem_usuario;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}
