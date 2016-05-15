package cz.vhromada.export.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

import cz.vhromada.export.AbstractExport;
import cz.vhromada.export.api.entities.ColumnItem;
import cz.vhromada.export.api.entities.ExtractData;
import cz.vhromada.export.api.entities.RowItem;
import cz.vhromada.export.api.exceptions.ExportException;
import cz.vhromada.validators.Validators;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;

/**
 * A class represents exporter to XML file.
 *
 * @author Vladimir Hromada
 */
public class XmlExporter extends AbstractExport {

    /**
     * Namespace
     */
    private static final String NAMESPACE = "http://vhromada.cz/export";

    /**
     * Directory where XML file will be created
     */
    private Path directory;

    /**
     * Name of XML file which will be created
     */
    private String fileName;

    /**
     * Creates a new instance of XmlExporter.
     *
     * @param directory directory where XML file will be created
     * @param fileName  name of XML file which will be created
     * @throws IllegalArgumentException                              if directory is null
     *                                                               or name of file is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if name of file is empty string
     */
    public XmlExporter(final Path directory, final String fileName) {
        Validators.validateArgumentNotNull(directory, "Directory");
        Validators.validateArgumentNotNull(fileName, "XML file");
        Validators.validateNotEmptyString(fileName, "XML file");

        this.directory = directory;
        this.fileName = fileName;
    }

    /**
     * @throws ExportException if exporting data failed
     */
    @Override
    protected void exportData(final ExtractData extractData, final Charset charset) {
        final Document document = createXMLDocument(extractData);
        try {
            final Path path = Files.createDirectories(directory).resolve(fileName);
            Files.deleteIfExists(path);
            try (final OutputStream outputStream = Files.newOutputStream(path, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
                final Serializer serializer = new Serializer(outputStream, charset.displayName());
                serializer.write(document);
            }
        } catch (final IOException ex) {
            throw new ExportException("Creating XML file failed.", ex);
        }
    }

    /**
     * Creates XML document.
     *
     * @param extractData extracted data
     * @return created XML document
     */
    private static Document createXMLDocument(final ExtractData extractData) {
        final Element root = new Element("export", NAMESPACE);
        for (final Map.Entry<String, List<RowItem>> entry : extractData.getData().entrySet()) {
            root.appendChild(createTableElement(entry.getKey(), entry.getValue()));
        }

        return new Document(root);
    }

    /**
     * Creates element table.
     *
     * @param table    table
     * @param rowItems row items
     * @return created element table
     */
    private static Element createTableElement(final String table, final List<RowItem> rowItems) {
        final Element tableElement = new Element("table", NAMESPACE);
        tableElement.addAttribute(new Attribute("name", table));
        for (final RowItem rowItem : rowItems) {
            tableElement.appendChild(createRowElement(rowItem));
        }

        return tableElement;
    }

    /**
     * Creates element row.
     *
     * @param rowItem row item
     * @return created element row
     */
    private static Element createRowElement(final RowItem rowItem) {
        final Element rowElement = new Element("row", NAMESPACE);
        for (final ColumnItem columnItem : rowItem.getColumns()) {
            rowElement.appendChild(createColumnElement(columnItem));
        }

        return rowElement;
    }

    /**
     * Creates element column.
     *
     * @param columnItem column item
     * @return created element column
     */
    private static Element createColumnElement(final ColumnItem columnItem) {
        final Element columnElement = new Element("column", NAMESPACE);
        columnElement.addAttribute(new Attribute("name", columnItem.getDescription().getName()));
        columnElement.addAttribute(new Attribute("type", columnItem.getDescription().getType().name()));
        columnElement.appendChild(columnItem.getValue() == null ? null : columnItem.getValue().toString());

        return columnElement;
    }

}
