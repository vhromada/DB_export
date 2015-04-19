package cz.vhromada.export.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.concurrent.ExecutionException;

import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import cz.vhromada.export.api.Export;
import cz.vhromada.export.api.entities.Database;
import cz.vhromada.export.api.entities.ExtractData;
import cz.vhromada.validators.Validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class represents dialog for exportation.
 *
 * @author Vladimir Hromada
 */
public class ExportDialog extends JDialog {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ExportDialog.class);

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of ExportDialog.
     *
     * @param export   export
     * @param database database description
     * @param charset  charset
     * @throws IllegalArgumentException if export is null
     *                                  or database description is null
     *                                  or charset is null
     */
    public ExportDialog(final Export export, final Database database, final Charset charset) {
        super(new JFrame(), "Exporting", true);

        Validators.validateArgumentNotNull(export, "Export");
        Validators.validateArgumentNotNull(database, "Database");
        Validators.validateArgumentNotNull(charset, "Charset");

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        final JProgressBar progressBar = new JProgressBar(0, 3);
        progressBar.setStringPainted(true);
        final PropertyChangeListener progressListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if ("progress".equals(evt.getPropertyName())) {
                    progressBar.setValue((Integer) evt.getNewValue());
                }
            }

        };

        final GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(progressBar));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(progressBar));

        pack();
        setLocationRelativeTo(getRootPane());

        final ExportingSwingWorker swingWorker = new ExportingSwingWorker(export, database, charset);
        swingWorker.addPropertyChangeListener(progressListener);
        swingWorker.execute();
    }

    /**
     * A class represents swing worker for exporting data.
     */
    private final class ExportingSwingWorker extends SwingWorker<Object, Object> {

        /**
         * Export
         */
        private Export export;

        /**
         * Database description
         */
        private Database database;

        /**
         * Charset
         */
        private Charset charset;

        /**
         * Creates a new instance of ExportingSwingWorker.
         *
         * @param export   export
         * @param database database description
         * @param charset  charset
         */
        private ExportingSwingWorker(final Export export, final Database database, final Charset charset) {
            this.export = export;
            this.database = database;
            this.charset = charset;
        }

        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return the computed result
         * @throws Exception if unable to compute a result
         */
        @Override
        protected Object doInBackground() throws Exception {
            try (final Connection connection = export.getConnection(database)) {
                setProgress(1);
                final ExtractData extractData = export.extract(database, connection);
                setProgress(2);
                export.export(extractData, charset);
                setProgress(3);
            }
            return null;
        }

        /**
         * Executed on the Event Dispatch Thread after the {@link #doInBackground()} method is finished.
         */
        @Override
        protected void done() {
            try {
                get();
                JOptionPane.showMessageDialog(ExportDialog.this, "Exported", "", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
                dispose();
            } catch (final InterruptedException | ExecutionException ex) {
                logger.error("Error in getting data from Swing Worker.", ex);
                System.exit(2);
            }
        }

    }

}
