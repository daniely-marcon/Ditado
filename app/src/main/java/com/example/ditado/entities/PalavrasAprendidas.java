package com.example.ditado.entities;

import androidx.room.Entity;

@Entity(primaryKeys = {"id_usuario", "id_animal"})
public class PalavrasAprendidas {
    private int id_usuario;
    private int id_animal;

    public PalavrasAprendidas(int id_usuario, int id_animal){
        this.id_animal=id_animal;
        this.id_usuario=id_usuario;
    }

    public int getId_usuario(int id_usuario){
        return this.id_usuario;
    }
    public void setId_usuario(int id_usuario){
        this.id_usuario=id_usuario;
    }
    public int getId_animal(int id_usuarioanimal){
        return this.id_animal;
    }
    public void setId_animal(int id_animal){
        this.id_animal=id_animal;
    }


}
