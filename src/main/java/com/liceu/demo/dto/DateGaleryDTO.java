package com.liceu.demo.dto;

public record DateGaleryDTO(
        int id,
        String jsonShapes,
        String drawName,
        String name,
        int width,
        int height,
        boolean trash,
        boolean publico,
        String DateCreate
) {
}
