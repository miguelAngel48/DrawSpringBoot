package com.liceu.demo.models;

public class Shape {
    int id;
    int VersionId;
    String shapes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersionId() {
        return VersionId;
    }

    public void setVersionId(int versionId) {
        VersionId = versionId;
    }

    public String getShapes() {
        return shapes;
    }

    public void setShapes(String shapes) {
        this.shapes = shapes;
    }
}
