package com.example.ditado.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Usuario {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nome;
    private String email;
    private String senha;
    private byte[] imagem_usuario;
    private String tipo;

    public Usuario(String nome,String email,String senha,byte[] imagem_usuario,String tipo){
        this.nome=nome;
        this.email=email;
        this.senha=senha;
        this.imagem_usuario=imagem_usuario;
        this.tipo=tipo;
    }

    public void setNome(String nome){
        this.nome=nome;
    }
    public String getNome(){
        return this.nome;
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
