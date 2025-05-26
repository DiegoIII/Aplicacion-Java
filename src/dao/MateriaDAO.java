package dao;

import modelo.Materia;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MateriaDAO {
    private static final String ARCHIVO = "materias.txt";
    private static int ultimoId = 0;
    
    static {
        actualizarUltimoId();
    }
    
    private static void actualizarUltimoId() {
        List<Materia> materias = obtenerTodos();
        ultimoId = materias.stream().mapToInt(Materia::getId).max().orElse(0);
    }
    
    public static List<Materia> obtenerTodos() {
        List<Materia> materias = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Materia materia = Materia.fromString(linea);
                if (materia != null) {
                    materias.add(materia);
                }
            }
        } catch (IOException e) {
            // Si el archivo no existe, se creará cuando se agregue la primera materia
        }
        return materias;
    }
    
    public static List<Materia> obtenerPorAlumno(int idAlumno) {
        List<Materia> materias = obtenerTodos();
        materias.removeIf(m -> m.getIdAlumno() != idAlumno);
        return materias;
    }
    
    public static Materia obtenerPorId(int id) {
        return obtenerTodos().stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    public static void agregar(Materia materia) {
        materia = new Materia(++ultimoId, materia.getIdAlumno(), materia.getNombreMateria(), 
                            materia.getCalificacion(), materia.getCuatrimestre());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
            bw.write(materia.toString());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void actualizar(Materia materia) {
        List<Materia> materias = obtenerTodos();
        for (int i = 0; i < materias.size(); i++) {
            if (materias.get(i).getId() == materia.getId()) {
                materias.set(i, materia);
                break;
            }
        }
        guardarTodos(materias);
    }
    
    public static void eliminar(int id) {
        List<Materia> materias = obtenerTodos();
        materias.removeIf(m -> m.getId() == id);
        guardarTodos(materias);
    }
    
    public static void eliminarPorAlumno(int idAlumno) {
        List<Materia> materias = obtenerTodos();
        materias.removeIf(m -> m.getIdAlumno() == idAlumno);
        guardarTodos(materias);
    }
    
    private static void guardarTodos(List<Materia> materias) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Materia materia : materias) {
                bw.write(materia.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}