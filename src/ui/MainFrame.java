package ui;

import modelo.Alumno;
import modelo.Materia;
import dao.AlumnoDAO;
import dao.MateriaDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {
    private JTable alumnosTable, materiasTable;
    private DefaultTableModel alumnosModel, materiasModel;
    private JTabbedPane tabbedPane;

    public MainFrame() {
        setTitle("Gestión de Alumnos y Materias");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        cargarAlumnos();
    }

    private void initUI() {
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Alumnos", createAlumnosPanel());
        tabbedPane.addTab("Materias", createMateriasPanel());
        add(tabbedPane);
    }

    private JPanel createAlumnosPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        alumnosModel = new DefaultTableModel(new Object[] { "ID", "Nombre", "Apellido", "Edad" }, 0);
        alumnosTable = new JTable(alumnosModel);
        alumnosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        alumnosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabbedPane.getSelectedIndex() == 1) {
                int row = alumnosTable.getSelectedRow();
                if (row >= 0)
                    cargarMaterias((int) alumnosTable.getValueAt(row, 0));
            }
        });

        panel.add(new JScrollPane(alumnosTable), BorderLayout.CENTER);

        JButton agregarBtn = new JButton("Agregar"), editarBtn = new JButton("Editar"),
                eliminarBtn = new JButton("Eliminar"), verMateriasBtn = new JButton("Ver Materias"),
                agregarMateriaBtn = new JButton("Agregar Materia");

        agregarBtn.addActionListener(this::agregarAlumno);
        editarBtn.addActionListener(this::editarAlumno);
        eliminarBtn.addActionListener(this::eliminarAlumno);
        verMateriasBtn.addActionListener(_ -> {
            int row = alumnosTable.getSelectedRow();
            if (row >= 0) {
                tabbedPane.setSelectedIndex(1);
                cargarMaterias((int) alumnosTable.getValueAt(row, 0));
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un alumno primero", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        agregarMateriaBtn.addActionListener(_ -> {
            int row = alumnosTable.getSelectedRow();
            if (row >= 0) {
                int idAlumno = (int) alumnosTable.getValueAt(row, 0);
                MateriaDialog dialog = new MateriaDialog(this, null, idAlumno);
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    MateriaDAO.agregar(dialog.getMateria());
                    if (tabbedPane.getSelectedIndex() == 1) {
                        cargarMaterias(idAlumno);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un alumno primero", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(agregarBtn);
        buttonPanel.add(editarBtn);
        buttonPanel.add(eliminarBtn);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(verMateriasBtn);
        buttonPanel.add(agregarMateriaBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMateriasPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        materiasModel = new DefaultTableModel(
                new Object[] { "ID", "Materia", "Calificación", "Cuatrimestre", "Alumno" }, 0);
        materiasTable = new JTable(materiasModel);
        panel.add(new JScrollPane(materiasTable), BorderLayout.CENTER);

        JButton agregarBtn = new JButton("Agregar"), editarBtn = new JButton("Editar"),
                eliminarBtn = new JButton("Eliminar"), verAlumnosBtn = new JButton("Ver Alumnos");

        agregarBtn.addActionListener(_ -> {
            int alumnoRow = alumnosTable.getSelectedRow();
            if (alumnoRow >= 0) {
                int idAlumno = (int) alumnosTable.getValueAt(alumnoRow, 0);
                MateriaDialog dialog = new MateriaDialog(this, null, idAlumno);
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    MateriaDAO.agregar(dialog.getMateria());
                    cargarMaterias(idAlumno);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Para agregar una materia, primero seleccione un alumno en la pestaña de Alumnos",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        editarBtn.addActionListener(this::editarMateria);
        eliminarBtn.addActionListener(this::eliminarMateria);
        verAlumnosBtn.addActionListener(_ -> tabbedPane.setSelectedIndex(0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(agregarBtn);
        buttonPanel.add(editarBtn);
        buttonPanel.add(eliminarBtn);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(verAlumnosBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void cargarAlumnos() {
        alumnosModel.setRowCount(0);
        AlumnoDAO.obtenerTodos().forEach(
                a -> alumnosModel.addRow(new Object[] { a.getId(), a.getNombre(), a.getApellido(), a.getEdad() }));
    }

    private void cargarMaterias(int idAlumno) {
        materiasModel.setRowCount(0);
        Alumno alumno = AlumnoDAO.obtenerPorId(idAlumno);
        String nombreAlumno = alumno != null ? alumno.getNombre() + " " + alumno.getApellido() : "Desconocido";
        MateriaDAO.obtenerPorAlumno(idAlumno).forEach(m -> materiasModel.addRow(new Object[] { m.getId(),
                m.getNombreMateria(), m.getCalificacion(), m.getCuatrimestre(), nombreAlumno }));
    }

    private void cargarTodasMaterias() {
        materiasModel.setRowCount(0);
        MateriaDAO.obtenerTodos().forEach(m -> {
            Alumno a = AlumnoDAO.obtenerPorId(m.getIdAlumno());
            String nombreAlumno = a != null ? a.getNombre() + " " + a.getApellido() : "Desconocido";
            materiasModel.addRow(new Object[] { m.getId(), m.getNombreMateria(), m.getCalificacion(),
                    m.getCuatrimestre(), nombreAlumno });
        });
    }

    private void agregarAlumno(ActionEvent e) {
        AlumnoDialog dialog = new AlumnoDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            AlumnoDAO.agregar(dialog.getAlumno());
            cargarAlumnos();
        }
    }

    private void editarAlumno(ActionEvent e) {
        int row = alumnosTable.getSelectedRow();
        if (row >= 0) {
            Alumno alumno = AlumnoDAO.obtenerPorId((int) alumnosTable.getValueAt(row, 0));
            if (alumno != null) {
                AlumnoDialog dialog = new AlumnoDialog(this, alumno);
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    AlumnoDAO.actualizar(dialog.getAlumno());
                    cargarAlumnos();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un alumno para editar", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarAlumno(ActionEvent e) {
        int row = alumnosTable.getSelectedRow();
        if (row >= 0
                && JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este alumno y todas sus materias?",
                        "Confirmar eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            AlumnoDAO.eliminar((int) alumnosTable.getValueAt(row, 0));
            cargarAlumnos();
            if (tabbedPane.getSelectedIndex() == 1)
                cargarTodasMaterias();
        } else if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un alumno para eliminar", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editarMateria(ActionEvent e) {
        int row = materiasTable.getSelectedRow();
        if (row >= 0) {
            Materia materia = MateriaDAO.obtenerPorId((int) materiasTable.getValueAt(row, 0));
            if (materia != null) {
                MateriaDialog dialog = new MateriaDialog(this, materia, materia.getIdAlumno());
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    MateriaDAO.actualizar(dialog.getMateria());
                    cargarMaterias(materia.getIdAlumno());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una materia para editar", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarMateria(ActionEvent e) {
        int row = materiasTable.getSelectedRow();
        if (row >= 0 && JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar esta materia?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            Materia m = MateriaDAO.obtenerPorId((int) materiasTable.getValueAt(row, 0));
            MateriaDAO.eliminar(m.getId());
            if (m != null)
                cargarMaterias(m.getIdAlumno());
        } else if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una materia para eliminar", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}