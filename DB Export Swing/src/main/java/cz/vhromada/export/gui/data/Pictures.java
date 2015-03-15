package cz.vhromada.export.gui.data;

import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import cz.vhromada.validators.Validators;

/**
 * A class represents pictures.
 *
 * @author Vladimir Hromada
 */
public final class Pictures {

    /**
     * Pictures (Map: Name -> Picture)
     */
    private static final Map<String, ImageIcon> PICTURES;

    static {
        PICTURES = new HashMap<>();
        PICTURES.put("back", new ImageIcon("pics/back.jpg"));
        PICTURES.put("continue", new ImageIcon("pics/continue.jpg"));
        PICTURES.put("exit", new ImageIcon("pics/exit.jpg"));
        PICTURES.put("export", new ImageIcon("pics/export.jpg"));
    }

    /**
     * Creates a new instance of Pictures.
     */
    private Pictures() {
    }

    /**
     * Returns picture.
     *
     * @param name picture's name
     * @return picture
     * @throws IllegalArgumentException if picture's name is null
     */
    public static ImageIcon getPicture(final String name) {
        Validators.validateArgumentNotNull(name, "Picture's name");

        return PICTURES.get(name);
    }

}
