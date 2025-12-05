package com.liceu.demo.models;

public class Draw {
    int id;
    String NameDraw;
    int idUser;
    String DateCreate;
    int width;
    int height;
    boolean trash;
    boolean publico;
    public boolean isPublico() {
        return publico;
    }

    public void setPublico(boolean publico) {
        this.publico = publico;
    }



    public int getHeight() {
        return height;
    }

    public boolean isTrash() {
        return trash;
    }

    public void setTrash(boolean trash) {
        this.trash = trash;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight(int height) {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameDraw() {
        return NameDraw;
    }

    public void setNameDraw(String nameDraw) {
        NameDraw = nameDraw;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getDateCreate() {
        return DateCreate;
    }

    public void setDateCreate(String dateCreate) {
        DateCreate = dateCreate;
    }


}
