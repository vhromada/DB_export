package cz.vhromada.export.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentListener;

import cz.vhromada.export.api.entities.Database;
import cz.vhromada.export.api.entities.DatabaseType;
import cz.vhromada.export.commons.SwingUtils;
import cz.vhromada.export.gui.data.InputValidator;
import cz.vhromada.export.gui.data.Pictures;
import cz.vhromada.validators.Validators;

import org.apache.derby.jdbc.ClientDataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.hsqldb.jdbc.JDBCDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class represents choosing exporting database.
 *
 * @author Vladimir Hromada
 */
public class DbChoose extends JFrame {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(DbChoose.class);

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * List of default connection URLs
     */
    private static final String[] DEFAULT_CONNECTION_URLS = { "jdbc:h2:<file>", "jdbc:hsqldb:file:<file>", "jdbc:derby://<host>:<port>/<database>" };

    /**
     * Combo box for databases
     */
    private JComboBox<String> databases = new JComboBox<>(new String[]{ "H2", "HSQL", "Derby" });

    /**
     * Label for connection URL
     */
    private JLabel urlLabel = new JLabel("Connection URL");

    /**
     * Text field for connection URL
     */
    private JTextField urlData = new JTextField();

    /**
     * Label for username
     */
    private JLabel usernameLabel = new JLabel("Username");

    /**
     * Text field for username
     */
    private JTextField usernameData = new JTextField();

    /**
     * Label for password
     */
    private JLabel passwordLabel = new JLabel("Password");

    /**
     * Text field for password
     */
    private JTextField passwordData = new JTextField();

    /**
     * Button Continue
     */
    private JButton continueButton = new JButton("Continue", Pictures.getPicture("continue"));

    /**
     * Button Exit
     */
    private JButton exitButton = new JButton("Exit", Pictures.getPicture("exit"));

    /**
     * Charset
     */
    private Charset charset;

    /**
     * Creates a new instance of DbChoose.
     *
     * @param charset charset
     * @throws IllegalArgumentException if charset is null
     */
    public DbChoose(final Charset charset) {
        Validators.validateArgumentNotNull(charset, "Charset");

        this.charset = charset;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Database export");
        setIconImage(Pictures.getPicture("export").getImage());
        setResizable(false);

        databases.setSelectedItem(null);
        databases.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                databasesAction();
            }

        });

        SwingUtils.initLabelComponent(urlLabel, urlData);
        SwingUtils.initLabelComponent(usernameLabel, usernameData);
        SwingUtils.initLabelComponent(passwordLabel, passwordData);

        final DocumentListener inputValidator = new InputValidator(continueButton) {

            @Override
            public boolean isInputValid() {
                return DbChoose.this.isInputValid();
            }

        };
        SwingUtils.addInputValidator(inputValidator, urlData, usernameData, passwordData);

        continueButton.setEnabled(false);
        continueButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                continueAction();
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

    /**
     * Performs action for combo box for databases.
     */
    private void databasesAction() {
        urlData.setText(databases.getSelectedIndex() >= 0 ? DEFAULT_CONNECTION_URLS[databases.getSelectedIndex()] : "");
        continueButton.setEnabled(isInputValid());
    }

    /**
     * Performs action for button Continue.
     */
    private void continueAction() {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                logger.info("Database: {}", databases.getSelectedItem());
                logger.info("URL: {}", urlData.getText());
                logger.info("Username: {}", usernameData.getText());
                logger.info("Password: {}", passwordData.getText());
                setVisible(false);
                new ExportChoose(getDatabase(), charset).setVisible(true);
            }

        });

    }

    /**
     * Returns database description.
     *
     * @return database description
     */
    private Database getDatabase() {
        final DataSource dataSource;
        final DatabaseType type;
        switch (databases.getSelectedIndex()) {
            case 0:
                dataSource = getH2DataSource();
                type = DatabaseType.H2;
                break;
            case 1:
                dataSource = getHsqlDataSource();
                type = DatabaseType.HSQL;
                break;
            case 2:
                dataSource = getDerbyDataSource();
                type = DatabaseType.DERBY;
                break;
            default:
                throw new IllegalArgumentException("Bad selected database.");
        }
        final Database database = new Database();
        database.setDataSource(dataSource);
        database.setType(type);

        return database;
    }

    /**
     * Returns true if input is valid: database is selected and connection URL isn't empty string and username isn't empty string and password isn't empty
     * string.
     *
     * @return true if input is valid: database is selected and connection URL isn't empty string and username isn't empty string and password isn't empty
     * string
     */
    private boolean isInputValid() {
        return databases.getSelectedIndex() >= 0 && !urlData.getText().isEmpty() && !usernameData.getText().isEmpty() && !passwordData.getText().isEmpty();
    }

    /**
     * Returns horizontal layout.
     *
     * @param layout layout
     * @return horizontal layout
     */
    private GroupLayout.Group createHorizontalLayout(final GroupLayout layout) {
        final Map<JLabel, JTextField> components = new LinkedHashMap<>(5);
        components.put(urlLabel, urlData);
        components.put(usernameLabel, usernameData);
        components.put(passwordLabel, passwordData);

        return SwingUtils.createHorizontalLayout(layout, databases, components, continueButton, exitButton);
    }

    /**
     * Returns vertical layout.
     *
     * @param layout layout
     * @return vertical layout
     */
    private GroupLayout.Group createVerticalLayout(final GroupLayout layout) {
        final Map<JLabel, JTextField> components = new LinkedHashMap<>(5);
        components.put(urlLabel, urlData);
        components.put(usernameLabel, usernameData);
        components.put(passwordLabel, passwordData);

        return SwingUtils.createVerticalLayout(layout, databases, components, continueButton, exitButton);
    }

    /**
     * Returns data source for H2 database.
     *
     * @return data source for H2 database
     */
    private DataSource getH2DataSource() {
        final JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(urlData.getText());
        dataSource.setUser(usernameData.getText());
        dataSource.setPassword(passwordData.getText());
        return dataSource;
    }

    /**
     * Returns data source for HSQL database.
     *
     * @return data source for HSQL database
     */
    private DataSource getHsqlDataSource() {
        final JDBCDataSource dataSource = new JDBCDataSource();
        dataSource.setUrl(urlData.getText());
        dataSource.setUser(usernameData.getText());
        dataSource.setPassword(passwordData.getText());
        return dataSource;
    }

    /**
     * Returns data source for Derby database.
     *
     * @return data source for Derby database
     */
    private DataSource getDerbyDataSource() {
        final ClientDataSource dataSource = new ClientDataSource();
        final String[] data = urlData.getText().split("/");
        final String[] server = data[2].split(":");
        dataSource.setServerName(server[0]);
        dataSource.setPortNumber(Integer.parseInt(server[1]));
        dataSource.setDatabaseName(data[3]);
        dataSource.setUser(usernameData.getText());
        dataSource.setPassword(passwordData.getText());
        return dataSource;
    }

}
