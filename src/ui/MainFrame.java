package ui;

import modelo.Alumno;
import modelo.Materia;
import dao.AlumnoDAO;
import dao.MateriaDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MainFrame extends JFrame {
    private JTable alumnosTable;
    private JTable materiasTable;
    private DefaultTableModel alumnosModel;
    private DefaultTableModel materiasModel;
    
    public MainFrame() {
        setTitle("Gestión de Alumnos y Materias");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initUI();
        cargarAlumnos();
    }
    
    private void initUI() {
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Panel de alumnos
        JPanel alumnosPanel = new JPanel(new BorderLayout());
        alumnosPanel.setBorder(BorderFactory.createTitledBorder("Alumnos"));
        
        alumnosModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Apellido", "Edad"}, 0);
        alumnosTable = new JTable(alumnosModel);
        alumnosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        alumnosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = alumnosTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int idAlumno = (int) alumnosTable.getValueAt(selectedRow, 0);
                    cargarMaterias(idAlumno);
                }
            }
        });
        
        alumnosPanel.add(new JScrollPane(alumnosTable), BorderLayout.CENTER);
        
        // Botones para alumnos
        JPanel alumnosBtnPanel = new JPanel(new FlowLayout());
        JButton agregarAlumnoBtn = new JButton("Agregar");
        JButton editarAlumnoBtn = new JButton("Editar");
        JButton eliminarAlumnoBtn = new JButton("Eliminar");
        
        agregarAlumnoBtn.addActionListener(this::agregarAlumno);
        editarAlumnoBtn.addActionListener(this::editarAlumno);
        eliminarAlumnoBtn.addActionListener(this::eliminarAlumno);
        
        alumnosBtnPanel.add(agregarAlumnoBtn);
        alumnosBtnPanel.add(editarAlumnoBtn);
        alumnosBtnPanel.add(eliminarAlumnoBtn);
        
        alumnosPanel.add(alumnosBtnPanel, BorderLayout.SOUTH);
        
        // Panel de materias
        JPanel materiasPanel = new JPanel(new BorderLayout());
        materiasPanel.setBorder(BorderFactory.createTitledBorder("Materias del Alumno Seleccionado"));
        
        materiasModel = new DefaultTableModel(new Object[]{"ID", "Materia", "Calificación", "Cuatrimestre"}, 0);
        materiasTable = new JTable(materiasModel);
        
        materiasPanel.add(new JScrollPane(materiasTable), BorderLayout.CENTER);
        
        // Botones para materias
        JPanel materiasBtnPanel = new JPanel(new FlowLayout());
        JButton agregarMateriaBtn = new JButton("Agregar Materia");
        JButton editarMateriaBtn = new JButton("Editar Materia");
        JButton eliminarMateriaBtn = new JButton("Eliminar Materia");
        
        agregarMateriaBtn.addActionListener(this::agregarMateria);
        editarMateriaBtn.addActionListener(this::editarMateria);
        eliminarMateriaBtn.addActionListener(this::eliminarMateria);
        
        materiasBtnPanel.add(agregarMateriaBtn);
        materiasBtnPanel.add(editarMateriaBtn);
        materiasBtnPanel.add(eliminarMateriaBtn);
        
        materiasPanel.add(materiasBtnPanel, BorderLayout.SOUTH);
        
        // Dividir la ventana
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, alumnosPanel, materiasPanel);
        splitPane.setResizeWeight(0.5);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void cargarAlumnos() {
        alumnosModel.setRowCount(0);
        List<Alumno> alumnos = AlumnoDAO.obtenerTodos();
        for (Alumno alumno : alumnos) {
            alumnosModel.addRow(new Object[]{
                alumno.getId(),
                alumno.getNombre(),
                alumno.getApellido(),
                alumno.getEdad()
            });
        }
    }
    
    private void cargarMaterias(int idAlumno) {
        materiasModel.setRowCount(0);
        List<Materia> materias = MateriaDAO.obtenerPorAlumno(idAlumno);
        for (Materia materia : materias) {
            materiasModel.addRow(new Object[]{
                materia.getId(),
                materia.getNombreMateria(),
                materia.getCalificacion(),
                materia.getCuatrimestre()
            });
        }
    }
    
    private void agregarAlumno(ActionEvent e) {
        AlumnoDialog dialog = new AlumnoDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            Alumno alumno = dialog.getAlumno();
            AlumnoDAO.agregar(alumno);
            cargarAlumnos();
        }
    }
    
    private void editarAlumno(ActionEvent e) {
        int selectedRow = alumnosTable.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) alumnosTable.getValueAt(selectedRow, 0);
            Alumno alumno = AlumnoDAO.obtenerPorId(id);
            if (alumno != null) {
                AlumnoDialog dialog = new AlumnoDialog(this, alumno);
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    Alumno updatedAlumno = dialog.getAlumno();
                    AlumnoDAO.actualizar(updatedAlumno);
                    cargarAlumnos();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un alumno para editar", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void eliminarAlumno(ActionEvent e) {
        int selectedRow = alumnosTable.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) alumnosTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro de eliminar este alumno y todas sus materias?", 
                    "Confirmar eliminación", 
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                AlumnoDAO.eliminar(id);
                cargarAlumnos();
                materiasModel.setRowCount(0);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un alumno para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void agregarMateria(ActionEvent e) {
        int selectedRow = alumnosTable.getSelectedRow();
        if (selectedRow >= 0) {
            int idAlumno = (int) alumnosTable.getValueAt(selectedRow, 0);
            MateriaDialog dialog = new MateriaDialog(this, null, idAlumno);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                Materia materia = dialog.getMateria();
                MateriaDAO.agregar(materia);
                cargarMaterias(idAlumno);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un alumno primero", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void editarMateria(ActionEvent e) {
        int selectedRow = materiasTable.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) materiasTable.getValueAt(selectedRow, 0);
            Materia materia = MateriaDAO.obtenerPorId(id);
            if (materia != null) {
                int alumnoRow = alumnosTable.getSelectedRow();
                int idAlumno = (int) alumnosTable.getValueAt(alumnoRow, 0);
                MateriaDialog dialog = new MateriaDialog(this, materia, idAlumno);
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    Materia updatedMateria = dialog.getMateria();
                    MateriaDAO.actualizar(updatedMateria);
                    cargarMaterias(idAlumno);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una materia para editar", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void eliminarMateria(ActionEvent e) {
        int selectedRow = materiasTable.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) materiasTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro de eliminar esta materia?", 
                    "Confirmar eliminación", 
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int alumnoRow = alumnosTable.getSelectedRow();
                int idAlumno = (int) alumnosTable.getValueAt(alumnoRow, 0);
                MateriaDAO.eliminar(id);
                cargarMaterias(idAlumno);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una materia para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
}