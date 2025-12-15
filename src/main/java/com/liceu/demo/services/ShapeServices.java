package com.liceu.demo.services;

import com.liceu.demo.dao.ShapesDAO;
import com.liceu.demo.models.Shape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShapeServices {
    @Autowired
    ShapesDAO shapesDAO;


    public void saveShapes(int idVersion, String shapes) {
        Shape shape = new Shape();
        shape.setVersionId(idVersion);
        shape.setShapes(shapes);
        shapesDAO.saveShapes(shape);
    }

    public String getShapes(int idVersion){
        Shape shape = shapesDAO.getShape(idVersion);
        if (shape == null) {
            return "[]";
        }
        return shape.getShapes();
    }
    public void copyShapes(int originalVersionId, int newVersionId) {
        Shape originalShape = shapesDAO.getShape(originalVersionId);

        Shape copy = new Shape();
        copy.setVersionId(newVersionId);
        copy.setShapes(originalShape.getShapes());

        shapesDAO.saveShapes(copy);
    }
}