package modelo;

public class Alumno {
    private int id;
    private String nombre;
    private String apellido;
    private int edad;

    public Alumno(int id, String nombre, String apellido, int edad) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public int getEdad() {
        return edad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    @Override
    public String toString() {
        return id + "," + nombre + "," + apellido + "," + edad;
    }

    public static Alumno fromString(String str) {
        String[] parts = str.split(",");
        if (parts.length != 4)
            return null;
        try {
            int id = Integer.parseInt(parts[0].trim());
            String nombre = parts[1].trim();
            String apellido = parts[2].trim();
            int edad = Integer.parseInt(parts[3].trim());
            return new Alumno(id, nombre, apellido, edad);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}