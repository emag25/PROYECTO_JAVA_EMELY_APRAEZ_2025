/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package emely_apraez.Modelos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 *
 * @author usuario
 */
public class Tarea {
    
    private String id;
    private String titulo;
    private String descripcion;
    private LocalDate fechaVencimiento;
    private String prioridad;
    private boolean completada;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    /**
     * Constructor para crear una nueva tarea
     * @param titulo Título de la tarea
     * @param descripcion Descripción de la tarea
     * @param fechaVencimiento Fecha de vencimiento
     * @param prioridad Prioridad (Alta, Media, Baja)
     */
    public Tarea(String titulo, String descripcion, LocalDate fechaVencimiento, String prioridad) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaVencimiento = fechaVencimiento;
        this.prioridad = prioridad;
        this.completada = false;
    }
    
    /**
     * Retorna una representación en texto de la tarea
     */
    @Override
    public String toString() {
        return String.format("%-36s %-20s %-30s %-12s %-10s %-10s", 
                id, 
                (titulo.length() > 18) ? titulo.substring(0, 17) + "..." : titulo,
                (descripcion.length() > 28) ? descripcion.substring(0, 27) + "..." : descripcion,
                fechaVencimiento.format(FORMATTER),
                prioridad,
                completada ? "Completada" : "Pendiente");
    }
    
    // Getters y setters
    
    public String getId() {
        return id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public String getPrioridad() {
        return prioridad;
    }
    
    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
    
    public boolean isCompletada() {
        return completada;
    }
    
    public void setCompletada(boolean completada) {
        this.completada = completada;
    }
    
}
