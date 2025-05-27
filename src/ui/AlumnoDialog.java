package ui;

import modelo.Alumno;
import javax.swing.*;
import java.awt.*;

public class AlumnoDialog extends JDialog {
    private JTextField nombreField;
    private JTextField apellidoField;
    private JSpinner edadSpinner;
    private boolean confirmed = false;
    private Alumno alumno;

    public AlumnoDialog(JFrame parent, Alumno alumnoExistente) {
        super(parent, alumnoExistente == null ? "Agregar Alumno" : "Editar Alumno", true);
        this.alumno = alumnoExistente;

        setSize(300, 200);
        setLocationRelativeTo(parent);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        panel.add(nombreField);

        panel.add(new JLabel("Apellido:"));
        apellidoField = new JTextField();
        panel.add(apellidoField);

        panel.add(new JLabel("Edad:"));
        edadSpinner = new JSpinner(new SpinnerNumberModel(18, 1, 100, 1));
        panel.add(edadSpinner);

        if (alumno != null) {
            nombreField.setText(alumno.getNombre());
            apellidoField.setText(alumno.getApellido());
            edadSpinner.setValue(alumno.getEdad());
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

    public Alumno getAlumno() {
        if (alumno == null) {
            return new Alumno(0, nombreField.getText(), apellidoField.getText(), (int) edadSpinner.getValue());
        } else {
            return new Alumno(alumno.getId(), nombreField.getText(), apellidoField.getText(),
                    (int) edadSpinner.getValue());
        }
    }
}