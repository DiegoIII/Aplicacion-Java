package dao;

import modelo.Alumno;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO {
    private static final String ARCHIVO = "alumnos.txt";
    private static int ultimoId = 0;
    
    static {
        actualizarUltimoId();
    }
    
    private static void actualizarUltimoId() {
        List<Alumno> alumnos = obtenerTodos();
        ultimoId = alumnos.stream().mapToInt(Alumno::getId).max().orElse(0);
    }
    
    public static List<Alumno> obtenerTodos() {
        List<Alumno> alumnos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Alumno alumno = Alumno.fromString(linea);
                if (alumno != null) {
                    alumnos.add(alumno);
                }
            }
        } catch (IOException e) {
            // Si el archivo no existe, se creará cuando se agregue el primer alumno
        }
        return alumnos;
    }
    
    public static Alumno obtenerPorId(int id) {
        return obtenerTodos().stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    public static void agregar(Alumno alumno) {
        alumno = new Alumno(++ultimoId, alumno.getNombre(), alumno.getApellido(), alumno.getEdad());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
            bw.write(alumno.toString());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void actualizar(Alumno alumno) {
        List<Alumno> alumnos = obtenerTodos();
        for (int i = 0; i < alumnos.size(); i++) {
            if (alumnos.get(i).getId() == alumno.getId()) {
                alumnos.set(i, alumno);
                break;
            }
        }
        guardarTodos(alumnos);
    }
    
    public static void eliminar(int id) {
        List<Alumno> alumnos = obtenerTodos();
        alumnos.removeIf(a -> a.getId() == id);
        guardarTodos(alumnos);
        
        // Eliminar materias relacionadas
        MateriaDAO.eliminarPorAlumno(id);
    }
    
    private static void guardarTodos(List<Alumno> alumnos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Alumno alumno : alumnos) {
                bw.write(alumno.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}