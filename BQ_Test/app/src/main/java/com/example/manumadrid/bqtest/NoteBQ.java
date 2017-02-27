package com.example.manumadrid.bqtest;

/**
 * Created by ManuMadrid on 25/02/2017.
 * Clase con los parametros de las notas del servidor que necesitamos unicamente
 */

public class NoteBQ {
    /**
     * titulo de la nota
     */
    private String title;
    /**
     * cuerpo de la nota
     */
    private String body;
    /**
     * resumen del cuerpo de la nota en caso de ser demasiado largo para poder previsualizarse
     */
    private String bodyResume;
    /**
     * fecha de creacion de la nota
     */
    private Long fechaCreacion;

    public NoteBQ(String title, String body, Long fechaCreacion) {
        this.title = title;
        this.body = body;
        this.fechaCreacion = fechaCreacion;

        String bodyResume = body;
        if (body.length() > 10) {
            bodyResume = body.substring(0, 10);
        }
        this.bodyResume = bodyResume;

    }

    public void setBodyResume(String bodyResume) {
        this.bodyResume = bodyResume;
    }

    public String getBodyResume() {
        return bodyResume;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Long fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }


}
