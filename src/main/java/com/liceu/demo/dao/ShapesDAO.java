package com.liceu.demo.dao;

import com.liceu.demo.models.Shape;

public interface ShapesDAO {
    void saveShapes(Shape shape);

    Shape getShape(int idVersion);
}
