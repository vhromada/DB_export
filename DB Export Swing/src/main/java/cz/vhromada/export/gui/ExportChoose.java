package cz.vhromada.export.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.DocumentListener;

import cz.vhromada.export.api.Export;
import cz.vhromada.export.api.entities.Database;
import cz.vhromada.export.commons.SwingUtils;
import cz.vhromada.export.gui.data.InputValidator;
import cz.vhromada.export.gui.data.Pictures;
import cz.vhromada.export.sql.SqlExporter;
import cz.vhromada.export.xml.XmlExporter;
import cz.vhromada.validators.Validators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class represents choosing exportation type.
 *
 * @author Vladimir Hromada
 */
public class ExportChoose extends JFrame {

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(ExportChoose.class);

    /** Serial version UID */
    private static final long serialVersionUID = 1L;

    /** Combo box for exportation types */
    private JComboBox<String> exportType = new JComboBox<>(new String[]{ "SQL", "XML" });

    /** Label for directory */
    private JLabel directoryLabel = new JLabel("Directory");

    /** Text field for directory */
    private JTextField directoryData = new JTextField();

    /** Label for file */
    private JLabel fileLabel = new JLabel("File");

    /** Text field for file */
    private JTextField fileData = new JTextField();

    /** Button Back */
    private JButton backButton = new JButton("Back", Pictures.getPicture("back"));

    /** Button ExportChoose */
    private JButton exportButton = new JButton("ExportChoose", Pictures.getPicture("export"));

    /** Button Exit */
    private JButton exitButton = new JButton("Exit", Pictures.getPicture("exit"));

    /** Database description */
    private Database database;

    /** Charset */
    private Charset charset;

    /**
     * Creates a new instance of ExportChoose.
     *
     * @param database database description
     * @param charset  charset
     * @throws IllegalArgumentException if database description is null
     *                                  or charset is null
     */
    public ExportChoose(final Database database, final Charset charset) {
        Validators.validateArgumentNotNull(database, "Database");
        Validators.validateArgumentNotNull(charset, "Charset");

        this.database = database;
        this.charset = charset;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Database export");
        setIconImage(Pictures.getPicture("export").getImage());

        exportType.setSelectedItem(null);
        exportType.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                exportTypeAction();
            }

        });

        directoryLabel.setLabelFor(directoryData);
        directoryLabel.setFocusable(false);

        fileLabel.setLabelFor(fileData);
        fileLabel.setFocusable(false);

        SwingUtils.initLabelComponent(directoryLabel, directoryData);
        SwingUtils.initLabelComponent(fileLabel, fileData);

        final DocumentListener inputValidator = new InputValidator(exportButton) {

            @Override
            public boolean isInputValid() {
                return ExportChoose.this.isInputValid();
            }

        };
        SwingUtils.addInputValidator(inputValidator, directoryData, fileData);

        backButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                backAction();
            }

        });

        exportButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                exportAction();
            }

        });

        exitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }

        });

        final GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(createHorizontalLayout(layout));
        layout.setVerticalGroup(createVerticalLayout(layout));

        pack();
        setLocationRelativeTo(getRootPane());
    }

    /** Performs action for combo box for exportation types. */
    private void exportTypeAction() {
        exportButton.setEnabled(isInputValid());
    }

    /** Performs action for button Back. */
    private void backAction() {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                setVisible(false);
                new DbChoose(charset).setVisible(true);
            }

        });

    }

    /** Performs action for button ExportChoose. */
    private void exportAction() {
        logger.info("Type: {}", exportType.getSelectedItem());
        logger.info("Directory: {}", directoryData.getText());
        logger.info("File: {}", fileData.getText());
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new ExportDialog(getExport(), database, charset).setVisible(true);
            }

        });
    }

    /**
     * Returns export.
     *
     * @return export
     */
    private Export getExport() {
        final Path directory = Paths.get(directoryData.getText());
        final String file = fileData.getText();
        switch (exportType.getSelectedIndex()) {
            case 0:
                return new SqlExporter(directory, file);
            case 1:
                return new XmlExporter(directory, file);
            default:
                throw new IllegalArgumentException("Bad selected export type.");
        }
    }

    /**
     * Returns true if input is valid: exportation type is selected and directory isn't empty string and file isn't empty string.
     *
     * @return true if input is valid: exportation type is selected and directory isn't empty string and file isn't empty string
     */
    private boolean isInputValid() {
        return exportType.getSelectedIndex() >= 0 && !directoryData.getText().isEmpty() && !fileData.getText().isEmpty();
    }

    /**
     * Returns horizontal layout.
     *
     * @param layout layout
     * @return horizontal layout
     */
    private GroupLayout.Group createHorizontalLayout(final GroupLayout layout) {
        final Map<JLabel, JTextField> components = new LinkedHashMap<>(4);
        components.put(directoryLabel, directoryData);
        components.put(fileLabel, fileData);

        return SwingUtils.createHorizontalLayout(layout, exportType, components, backButton, exportButton, exitButton);
    }

    /**
     * Returns vertical layout.
     *
     * @param layout layout
     * @return vertical layout
     */
    private GroupLayout.Group createVerticalLayout(final GroupLayout layout) {
        final Map<JLabel, JTextField> components = new LinkedHashMap<>(4);
        components.put(directoryLabel, directoryData);
        components.put(fileLabel, fileData);

        return SwingUtils.createVerticalLayout(layout, exportType, components, backButton, exportButton, exitButton);
    }

}
