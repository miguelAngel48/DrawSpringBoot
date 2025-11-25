package com.liceu.demo.models;

public class Draw {
    int id;
    String NameDraw;
    int idUser;
    String DateCreate;


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
