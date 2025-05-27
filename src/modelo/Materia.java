package modelo;

public class Materia {
    private int id;
    private int idAlumno;
    private String nombreMateria;
    private double calificacion;
    private int cuatrimestre;

    public Materia(int id, int idAlumno, String nombreMateria, double calificacion, int cuatrimestre) {
        this.id = id;
        this.idAlumno = idAlumno;
        this.nombreMateria = nombreMateria;
        this.calificacion = calificacion;
        this.cuatrimestre = cuatrimestre;
    }

    public int getId() {
        return id;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public int getCuatrimestre() {
        return cuatrimestre;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public void setCuatrimestre(int cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    @Override
    public String toString() {
        return id + "," + idAlumno + "," + nombreMateria + "," + calificacion + "," + cuatrimestre;
    }

    public static Materia fromString(String str) {
        String[] parts = str.split(",");
        if (parts.length != 5)
            return null;
        try {
            int id = Integer.parseInt(parts[0].trim());
            int idAlumno = Integer.parseInt(parts[1].trim());
            String nombreMateria = parts[2].trim();
            double calificacion = Double.parseDouble(parts[3].trim());
            int cuatrimestre = Integer.parseInt(parts[4].trim());
            return new Materia(id, idAlumno, nombreMateria, calificacion, cuatrimestre);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}