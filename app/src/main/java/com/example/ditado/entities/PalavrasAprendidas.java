package com.example.ditado.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;


@Entity(primaryKeys = {"id_usuario", "id_animal"},
                        foreignKeys = {
                        @ForeignKey(entity = Usuario.class, parentColumns = "id_usuario", childColumns = "id_usuario", onDelete = ForeignKey.CASCADE),
                        @ForeignKey(entity = Animal.class, parentColumns = "id_animal", childColumns = "id_animal", onDelete = ForeignKey.CASCADE)
                        })
public class PalavrasAprendidas {
    private int id_usuario;
    private int id_animal;

    public PalavrasAprendidas(int id_usuario, int id_animal){
        this.id_animal=id_animal;
        this.id_usuario=id_usuario;
    }

    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }
    public int getId_animal() { return id_animal; }
    public void setId_animal(int id_animal) { this.id_animal = id_animal; }


}
