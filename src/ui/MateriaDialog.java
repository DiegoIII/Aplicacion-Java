package ui;

import modelo.Materia;
import javax.swing.*;
import java.awt.*;

public class MateriaDialog extends JDialog {
    private JTextField nombreField;
    private JSpinner calificacionSpinner;
    private JSpinner cuatrimestreSpinner;
    private boolean confirmed = false;
    private Materia materia;
    private int idAlumno;

    public MateriaDialog(JFrame parent, Materia materiaExistente, int idAlumno) {
        super(parent, materiaExistente == null ? "Agregar Materia" : "Editar Materia", true);
        this.materia = materiaExistente;
        this.idAlumno = idAlumno;

        setSize(300, 200);
        setLocationRelativeTo(parent);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Nombre Materia:"));
        nombreField = new JTextField();
        panel.add(nombreField);

        panel.add(new JLabel("Calificación:"));
        calificacionSpinner = new JSpinner(new SpinnerNumberModel(5.0, 0.0, 10.0, 0.5));
        panel.add(calificacionSpinner);

        panel.add(new JLabel("Cuatrimestre:"));
        cuatrimestreSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        panel.add(cuatrimestreSpinner);

        if (materia != null) {
            nombreField.setText(materia.getNombreMateria());
            calificacionSpinner.setValue(materia.getCalificacion());
            cuatrimestreSpinner.setValue(materia.getCuatrimestre());
        }

        JButton okButton = new JButton("OK");
        okButton.addActionListener(_ -> {
            confirmed = true;
            dispose();
        });

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(_ -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Materia getMateria() {
        if (materia == null) {
            return new Materia(0, idAlumno, nombreField.getText(),
                    (double) calificacionSpinner.getValue(),
                    (int) cuatrimestreSpinner.getValue());
        } else {
            return new Materia(materia.getId(), idAlumno, nombreField.getText(),
                    (double) calificacionSpinner.getValue(),
                    (int) cuatrimestreSpinner.getValue());
        }
    }
}