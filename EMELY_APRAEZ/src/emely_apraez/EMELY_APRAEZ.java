/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package emely_apraez;

import emely_apraez.Modelos.Tarea;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author usuario
 */
public class EMELY_APRAEZ {

    private static List<Tarea> tareas = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        
        System.setOut(new PrintStream(System.out, true, "UTF-8"));

        precargarDatos();
        
        System.out.println("=== SISTEMA DE GESTIÓN DE TAREAS ===");
        
        boolean ejecutar = true;
        while (ejecutar) {
            mostrarMenu();
            int opcion = leerOpcion();
            
            switch (opcion) {
                case 1:
                    agregarTarea();
                    break;
                case 2:
                    editarTarea();
                    break;
                case 3:
                    eliminarTarea();
                    break;
                case 4:
                    listarTareas();
                    break;
                case 5:
                    marcarTareaCompletada();
                    break;
                case 6:
                    ordenarTareas();
                    break;
                case 7:
                    filtrarTareas();
                    break;
                case 8:
                    ejecutar = false;
                    System.out.println("¡Gracias por usar el sistema de gestión de tareas!");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
        scanner.close();
    }
    
    /**
     * Muestra el menú principal de la aplicación.
     */
    private static void mostrarMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Agregar nueva tarea");
        System.out.println("2. Editar tarea existente");
        System.out.println("3. Eliminar tarea");
        System.out.println("4. Listar todas las tareas");
        System.out.println("5. Marcar tarea como completada");
        System.out.println("6. Ordenar tareas");
        System.out.println("7. Filtrar tareas");
        System.out.println("8. Salir");
        System.out.print("Seleccione una opción: ");
    }
    
    /**
     * Lee y valida la opción seleccionada por el usuario
     * @return número de opción válido
     */
    private static int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Agrega una nueva tarea al sistema.
     */
    private static void agregarTarea() throws IOException {
        System.out.println("\n--- AGREGAR NUEVA TAREA ---");
        
        String titulo = leerCampo("Título");
        String descripcion = leerCampo("Descripción");
        LocalDate fechaVencimiento = leerFecha();
        String prioridad = leerPrioridad();
        
        Tarea nuevaTarea = new Tarea(titulo, descripcion, fechaVencimiento, prioridad);
        tareas.add(nuevaTarea);
        
        System.out.println("Tarea agregada correctamente con ID: " + nuevaTarea.getId());
    }
    
    /**
     * Edita una tarea existente en el sistema.
     */
    private static void editarTarea() {
        System.out.println("\n--- EDITAR TAREA ---");

        if (tareas.isEmpty()) {
            System.out.println("No hay tareas para editar.");
            return;
        }

        listarTareas();

        // Bucle para pedir el ID hasta que sea válido
        String id;
        Tarea tareaEncontrada;
        do {
            System.out.print("Ingrese el ID de la tarea a editar: ");
            id = scanner.nextLine().trim();

            tareaEncontrada = buscarTareaPorId(id);

            if (tareaEncontrada == null) {
                System.out.println("No se encontró ninguna tarea con el ID especificado. Intente nuevamente.");
            }
        } while (tareaEncontrada == null); // Si la tarea no existe, sigue pidiendo el ID

        System.out.println("Editando tarea: " + tareaEncontrada.getTitulo());
        System.out.println("Deje en blanco para mantener el valor actual.");

        String nuevoTitulo = leerCampoOpcional("Título [" + tareaEncontrada.getTitulo() + "]");
        if (!nuevoTitulo.isEmpty()) {
            tareaEncontrada.setTitulo(nuevoTitulo);
        }

        String nuevaDescripcion = leerCampoOpcional("Descripción [" + tareaEncontrada.getDescripcion() + "]");
        if (!nuevaDescripcion.isEmpty()) {
            tareaEncontrada.setDescripcion(nuevaDescripcion);
        }

        // Validación para cambiar la fecha de vencimiento (S/N)
        String respuestaFecha;
        do {
            System.out.print("¿Desea cambiar la fecha de vencimiento? (S/N): ");
            respuestaFecha = scanner.nextLine().trim();
            if (!respuestaFecha.equalsIgnoreCase("S") && !respuestaFecha.equalsIgnoreCase("N")) {
                System.out.println("Por favor ingrese 'S' para sí o 'N' para no.");
            }
        } while (!respuestaFecha.equalsIgnoreCase("S") && !respuestaFecha.equalsIgnoreCase("N"));

        if (respuestaFecha.equalsIgnoreCase("S")) {
            LocalDate nuevaFecha = leerFecha();
            tareaEncontrada.setFechaVencimiento(nuevaFecha);
        }

        // Validación para cambiar la prioridad (S/N)
        String respuestaPrioridad;
        do {
            System.out.print("¿Desea cambiar la prioridad? (S/N): ");
            respuestaPrioridad = scanner.nextLine().trim();
            if (!respuestaPrioridad.equalsIgnoreCase("S") && !respuestaPrioridad.equalsIgnoreCase("N")) {
                System.out.println("Por favor ingrese 'S' para sí o 'N' para no.");
            }
        } while (!respuestaPrioridad.equalsIgnoreCase("S") && !respuestaPrioridad.equalsIgnoreCase("N"));

        if (respuestaPrioridad.equalsIgnoreCase("S")) {
            String nuevaPrioridad = leerPrioridad();
            tareaEncontrada.setPrioridad(nuevaPrioridad);
        }

        System.out.println("Tarea actualizada correctamente.");
    }

    
    /**
     * Elimina una tarea del sistema.
     */
    private static void eliminarTarea() {
        System.out.println("\n--- ELIMINAR TAREA ---");

        if (tareas.isEmpty()) {
            System.out.println("No hay tareas para eliminar.");
            return;
        }

        listarTareas();

        // Bucle para pedir el ID hasta que sea válido
        String id;
        Tarea tareaEncontrada;
        do {
            System.out.print("Ingrese el ID de la tarea a eliminar: ");
            id = scanner.nextLine().trim();

            tareaEncontrada = buscarTareaPorId(id);

            if (tareaEncontrada == null) {
                System.out.println("No se encontró ninguna tarea con el ID especificado. Intente nuevamente.");
            }
        } while (tareaEncontrada == null); // Si la tarea no existe, sigue pidiendo el ID

        // Bucle para asegurarse que la respuesta sea "S" o "N"
        String respuesta;
        do {
            System.out.print("¿Está seguro de eliminar la tarea '" + tareaEncontrada.getTitulo() + "'? (S/N): ");
            respuesta = scanner.nextLine().trim();

            if (!respuesta.equalsIgnoreCase("S") && !respuesta.equalsIgnoreCase("N")) {
                System.out.println("Respuesta inválida. Por favor ingrese 'S' para sí o 'N' para no.");
            }
        } while (!respuesta.equalsIgnoreCase("S") && !respuesta.equalsIgnoreCase("N")); // Se repite hasta que la respuesta sea válida

        if (respuesta.equalsIgnoreCase("S")) {
            tareas.remove(tareaEncontrada);
            System.out.println("Tarea eliminada correctamente.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    
    /**
     * Lista todas las tareas en el sistema.
     */
    private static void listarTareas() {
        System.out.println("\n--- LISTA DE TAREAS ---");
        
        if (tareas.isEmpty()) {
            System.out.println("No hay tareas registradas.");
            return;
        }
        
        mostrarEncabezadoTabla();
        for (Tarea tarea : tareas) {
            System.out.println(tarea);
        }
    }
    
    /**
     * Marca una tarea como completada.
     */
    private static void marcarTareaCompletada() {
        System.out.println("\n--- MARCAR TAREA COMO COMPLETADA ---");

        if (tareas.isEmpty()) {
            System.out.println("No hay tareas disponibles.");
            return;
        }

        // Filtrar y mostrar solo tareas pendientes
        List<Tarea> tareasPendientes = new ArrayList<>();
        for (Tarea tarea : tareas) {
            if (!tarea.isCompletada()) {
                tareasPendientes.add(tarea);
            }
        }

        if (tareasPendientes.isEmpty()) {
            System.out.println("No hay tareas pendientes para completar.");
            return;
        }

        System.out.println("Tareas pendientes:");
        mostrarEncabezadoTabla();
        for (Tarea tarea : tareasPendientes) {
            System.out.println(tarea);
        }

        // Bucle para pedir el ID hasta que sea válido
        String id;
        Tarea tareaEncontrada;
        do {
            System.out.print("Ingrese el ID de la tarea a marcar como completada: ");
            id = scanner.nextLine().trim();

            tareaEncontrada = buscarTareaPorId(id);

            if (tareaEncontrada == null) {
                System.out.println("No se encontró ninguna tarea con el ID especificado. Intente nuevamente.");
            }
        } while (tareaEncontrada == null); // Si la tarea no existe, sigue pidiendo el ID

        // Verificar si la tarea ya está completada
        if (tareaEncontrada.isCompletada()) {
            System.out.println("Esta tarea ya está marcada como completada.");
            return;
        }

        tareaEncontrada.setCompletada(true);
        System.out.println("Tarea marcada como completada correctamente.");
    }

    
    /**
     * Ordena las tareas por fecha o prioridad.
     */
    private static void ordenarTareas() {
        System.out.println("\n--- ORDENAR TAREAS ---");

        if (tareas.isEmpty()) {
            System.out.println("No hay tareas para ordenar.");
            return;
        }

        // Bucle para seleccionar el criterio de ordenación
        int opcion;
        do {
            System.out.println("Criterio de ordenación:");
            System.out.println("1. Por fecha de vencimiento (más próxima primero)");
            System.out.println("2. Por prioridad (Alta a Baja)");
            System.out.print("Seleccione una opción: ");

            opcion = leerOpcion();

            if (opcion != 1 && opcion != 2) {
                System.out.println("Opción no válida. Por favor, seleccione 1 o 2.");
            }
        } while (opcion != 1 && opcion != 2);  // Repetir hasta que la opción sea válida

        // Ordenación por fecha de vencimiento
        if (opcion == 1) {
            Collections.sort(tareas, Comparator.comparing(Tarea::getFechaVencimiento));
            System.out.println("Tareas ordenadas por fecha de vencimiento:");
        } 
        // Ordenación por prioridad
        else if (opcion == 2) {
            // Bucle para validar la prioridad
            Collections.sort(tareas, new Comparator<Tarea>() {
                @Override
                public int compare(Tarea t1, Tarea t2) {
                    return obtenerValorPrioridad(t2.getPrioridad()) - obtenerValorPrioridad(t1.getPrioridad());
                }

                private int obtenerValorPrioridad(String prioridad) {
                    String prioridadUpper = prioridad.toUpperCase();

                    // Bucle para manejar la prioridad con validación
                    switch (prioridadUpper) {
                        case "ALTA": return 3;
                        case "MEDIA": return 2;
                        case "BAJA": return 1;
                        default: return 0;  // Por si acaso se pasa una prioridad no válida
                    }
                }
            });
            System.out.println("Tareas ordenadas por prioridad:");
        }

        mostrarEncabezadoTabla();
        for (Tarea tarea : tareas) {
            System.out.println(tarea);
        }
    }

    
    /**
     * Filtra las tareas por su estado (completadas o pendientes).
     */
    private static void filtrarTareas() {
        System.out.println("\n--- FILTRAR TAREAS ---");

        if (tareas.isEmpty()) {
            System.out.println("No hay tareas para filtrar.");
            return;
        }

        // Bucle para seleccionar el filtro de estado
        int opcion;
        do {
            System.out.println("Filtrar por estado:");
            System.out.println("1. Mostrar solo tareas pendientes");
            System.out.println("2. Mostrar solo tareas completadas");
            System.out.print("Seleccione una opción: ");

            opcion = leerOpcion();

            if (opcion != 1 && opcion != 2) {
                System.out.println("Opción no válida. Por favor, seleccione 1 o 2.");
            }
        } while (opcion != 1 && opcion != 2);  // Repetir hasta que la opción sea válida

        boolean estadoFiltro = (opcion == 2); // true para completadas, false para pendientes

        List<Tarea> tareasFiltradas = new ArrayList<>();
        for (Tarea tarea : tareas) {
            if (tarea.isCompletada() == estadoFiltro) {
                tareasFiltradas.add(tarea);
            }
        }

        if (tareasFiltradas.isEmpty()) {
            System.out.println("No hay tareas que cumplan con el filtro seleccionado.");
            return;
        }

        String tipoTareas = estadoFiltro ? "completadas" : "pendientes";
        System.out.println("Tareas " + tipoTareas + ":");

        mostrarEncabezadoTabla();
        for (Tarea tarea : tareasFiltradas) {
            System.out.println(tarea);
        }
    }
    
    /**
     * Busca una tarea por su ID
     * @param id ID de la tarea a buscar
     * @return Tarea encontrada o null si no existe
     */
    private static Tarea buscarTareaPorId(String id) {
        for (Tarea tarea : tareas) {
            if (tarea.getId().equals(id)) {
                return tarea;
            }
        }
        return null;
    }
    
    
    
    /**
     * Lee un campo obligatorio del usuario
     * @param nombreCampo Nombre del campo a leer
     * @return Valor válido del campo
     */
    private static String leerCampo(String nombreCampo) {
        String valor;
        do {
            System.out.print(nombreCampo + ": ");
            valor = scanner.nextLine().trim();
            if (valor.isEmpty()) {
                System.out.println("El " + nombreCampo + " no puede estar vacío. Intente nuevamente.");
            }
        } while (valor.isEmpty());
        return valor;
    }
    
    /**
     * Lee un campo opcional del usuario
     * @param prompt Mensaje a mostrar
     * @return Valor del campo (puede estar vacío)
     */
    private static String leerCampoOpcional(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }
    
    /**
     * Lee y valida una fecha del usuario
     * @return Fecha válida
     */
    private static LocalDate leerFecha() {
        LocalDate fecha = null;
        boolean fechaValida = false;
        
        while (!fechaValida) {
            System.out.print("Fecha de vencimiento (DD/MM/YYYY): ");
            String fechaStr = scanner.nextLine().trim();
            
            try {
                fecha = LocalDate.parse(fechaStr, dateFormatter);
                fechaValida = true;
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha inválido. Use el formato DD/MM/YYYY.");
            }
        }
        
        return fecha;
    }
    
    /**
     * Lee y valida la prioridad de una tarea
     * @return Prioridad válida (Alta, Media, Baja)
     */
    private static String leerPrioridad() {
        String prioridad;
        boolean prioridadValida = false;
        
        do {
            System.out.println("Prioridad (1: Baja, 2: Media, 3: Alta): ");
            String opcion = scanner.nextLine().trim();
            
            switch (opcion) {
                case "1":
                    prioridad = "Baja";
                    prioridadValida = true;
                    break;
                case "2":
                    prioridad = "Media";
                    prioridadValida = true;
                    break;
                case "3":
                    prioridad = "Alta";
                    prioridadValida = true;
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    prioridad = "";
            }
        } while (!prioridadValida);
        
        return prioridad;
    }
       
    /**
     * Muestra el encabezado de la tabla de tareas.
     */
    private static void mostrarEncabezadoTabla() {
        System.out.printf("%-36s %-20s %-30s %-12s %-10s %-10s%n", 
                "ID", "TÍTULO", "DESCRIPCIÓN", "VENCIMIENTO", "PRIORIDAD", "ESTADO");
        System.out.println("---------------------------------------------------------------------------------------------------");
    }
    
    /**
     * Inserta datos inciales.
     */
    private static void precargarDatos() {
        // Carga de datos para pruebas
        tareas.add(new Tarea("Tarea 1", "Descripción de la tarea 1", LocalDate.of(2025, 3, 23), "ALTA"));
        tareas.add(new Tarea("Tarea 2", "Descripción de la tarea 2", LocalDate.of(2025, 3, 25), "MEDIA"));
        tareas.add(new Tarea("Tarea 3", "Descripción de la tarea 3", LocalDate.of(2025, 3, 20), "BAJA"));
    }
    
    
}
