package com.liceu.demo.dto;

public record CanvasClientDTO(int id,
                              String jsonShapes,
                              String drawName,
                              int idUser,
                              int width,
                              int height,
                              boolean trash,
                              boolean publico) {
}
