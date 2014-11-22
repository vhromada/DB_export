package cz.vhromada.export.commons;

import java.util.Map;

import javax.swing.*;
import javax.swing.event.DocumentListener;

import cz.vhromada.validators.Validators;

/**
 * A class represents utility class for creating layouts.
 *
 * @author Vladimir Hromada
 */
public final class SwingUtils {

    /** Name of field components */
    private static final String FIELD_COMPONENTS = "Components";

    /** Name of field buttons */
    private static final String FIELD_BUTTONS = "Buttons";

    /** Creates a new instance of SwingUtils. */
    private SwingUtils() {
    }

    /**
     * Initializes label with text field.
     *
     * @param label     label
     * @param textField text field
     */
    public static void initLabelComponent(final JLabel label, final JTextField textField) {
        label.setLabelFor(textField);
        label.setFocusable(false);
    }

    /**
     * Adds input validator to text fields.
     *
     * @param inputValidator input validator
     * @param textFields     text fields
     */
    public static void addInputValidator(final DocumentListener inputValidator, final JTextField... textFields) {
        for (final JTextField textField : textFields) {
            textField.getDocument().addDocumentListener(inputValidator);
        }
    }

    /**
     * Creates horizontal layout for components.
     *
     * @param layout     layout
     * @param comboBox   combo box
     * @param components components (Map: label -> data)
     * @param buttons    buttons
     * @throws IllegalArgumentException if layout is null
     *                                  or combo box is null
     *                                  or components are null
     *                                  or buttons are null
     *                                  or components contain null key
     *                                  or components contain null value
     *                                  or buttons contain null value
     */
    public static GroupLayout.Group createHorizontalLayout(final GroupLayout layout, final JComboBox<?> comboBox, final Map<JLabel, JTextField> components,
            final JButton... buttons) {
        Validators.validateArgumentNotNull(layout, "Layout");
        Validators.validateArgumentNotNull(comboBox, "Combo box");
        Validators.validateArgumentNotNull(components, FIELD_COMPONENTS);
        Validators.validateArgumentNotNull(buttons, FIELD_BUTTONS);
        Validators.validateMapNotContainNull(components, FIELD_COMPONENTS);
        Validators.validateArrayNotContainNull(buttons, FIELD_BUTTONS);

        final GroupLayout.Group componentsGroup = layout.createParallelGroup()
                .addComponent(comboBox, SwingConstants.HORIZONTAL_LONG_COMPONENT_SIZE, SwingConstants.HORIZONTAL_LONG_COMPONENT_SIZE,
                        SwingConstants.HORIZONTAL_LONG_COMPONENT_SIZE)
                .addGroup(createHorizontalComponentsLayout(layout, components))
                .addGroup(createHorizontalButtonsLayout(layout, buttons));

        return layout.createSequentialGroup()
                .addGap(SwingConstants.HORIZONTAL_GAP_SIZE)
                .addGroup(componentsGroup)
                .addGap(SwingConstants.HORIZONTAL_GAP_SIZE);
    }

    /**
     * Returns vertical layout for components.
     *
     * @param layout     layout
     * @param comboBox   combo box
     * @param components components (Map: label -> data)
     * @param buttons    buttons
     * @throws IllegalArgumentException if layout is null
     *                                  or combo box is null
     *                                  or components are null
     *                                  or buttons are null
     *                                  or components contain null key
     *                                  or components contain null value
     *                                  or buttons contain null value
     */
    public static GroupLayout.Group createVerticalLayout(final GroupLayout layout, final JComboBox<?> comboBox, final Map<JLabel, JTextField> components,
            final JButton... buttons) {
        Validators.validateArgumentNotNull(layout, "Layout");
        Validators.validateArgumentNotNull(comboBox, "Combo box");
        Validators.validateArgumentNotNull(components, FIELD_COMPONENTS);
        Validators.validateArgumentNotNull(buttons, FIELD_BUTTONS);
        Validators.validateMapNotContainNull(components, FIELD_COMPONENTS);
        Validators.validateArrayNotContainNull(buttons, FIELD_BUTTONS);

        final GroupLayout.Group result = layout.createSequentialGroup()
                .addGap(SwingConstants.VERTICAL_GAP_SIZE)
                .addComponent(comboBox, SwingConstants.VERTICAL_COMBO_BOX_SIZE, SwingConstants.VERTICAL_COMBO_BOX_SIZE, SwingConstants.VERTICAL_COMBO_BOX_SIZE)
                .addGap(SwingConstants.VERTICAL_GAP_SIZE);
        createVerticalComponentsLayout(layout, result, components);
        result.addGroup(createVerticalButtonsLayout(layout, buttons)).addGap(SwingConstants.VERTICAL_GAP_SIZE);
        return result;
    }

    /**
     * Returns vertical layout for buttons.
     *
     * @param layout  layout
     * @param buttons buttons
     * @return vertical layout for buttons
     */
    public static GroupLayout.Group createVerticalButtonsLayout(final GroupLayout layout, final JButton... buttons) {
        final GroupLayout.Group group = layout.createParallelGroup();
        for (final JButton button : buttons) {
            group.addComponent(button, SwingConstants.VERTICAL_BUTTON_SIZE, SwingConstants.VERTICAL_BUTTON_SIZE, SwingConstants.VERTICAL_BUTTON_SIZE);
        }
        return group;
    }

    /**
     * Creates horizontal layout for components.
     *
     * @param layout     layout
     * @param components components (Map: label -> data)
     * @throws IllegalArgumentException if layout is null
     *                                  or components are null
     *                                  or components contain null key
     *                                  or components contain null value
     */
    private static GroupLayout.Group createHorizontalComponentsLayout(final GroupLayout layout, final Map<JLabel, JTextField> components) {
        Validators.validateArgumentNotNull(layout, "Layout");
        Validators.validateArgumentNotNull(components, FIELD_COMPONENTS);
        Validators.validateMapNotContainNull(components, FIELD_COMPONENTS);

        final GroupLayout.Group group = layout.createParallelGroup();
        for (final Map.Entry<JLabel, JTextField> data : components.entrySet()) {
            group.addGroup(createHorizontalDataComponentsLayout(layout, data.getKey(), data.getValue()));
        }
        return group;
    }

    /**
     * Returns horizontal layout for label component with data component.
     *
     * @param layout layout
     * @param label  label
     * @param data   data
     * @return horizontal layout for label component with data component
     */
    private static GroupLayout.Group createHorizontalDataComponentsLayout(final GroupLayout layout, final JLabel label, final JTextField data) {
        return layout.createSequentialGroup()
                .addComponent(label, SwingConstants.HORIZONTAL_LABEL_SIZE, SwingConstants.HORIZONTAL_LABEL_SIZE, SwingConstants.HORIZONTAL_LABEL_SIZE)
                .addGap(SwingConstants.HORIZONTAL_GAP_SIZE)
                .addComponent(data, SwingConstants.HORIZONTAL_DATA_SIZE, SwingConstants.HORIZONTAL_DATA_SIZE, SwingConstants.HORIZONTAL_DATA_SIZE);
    }

    /**
     * Creates horizontal layout for buttons.
     *
     * @param layout  layout
     * @param buttons buttons
     * @throws IllegalArgumentException if layout is null
     *                                  or buttons are null
     *                                  or buttons contain null
     */
    private static GroupLayout.Group createHorizontalButtonsLayout(final GroupLayout layout, final JButton... buttons) {
        Validators.validateArgumentNotNull(layout, "Layout");
        Validators.validateArgumentNotNull(buttons, FIELD_BUTTONS);
        Validators.validateArrayNotContainNull(buttons, FIELD_BUTTONS);

        final GroupLayout.Group group = layout.createSequentialGroup();
        final int gapSize = (SwingConstants.HORIZONTAL_LENGTH - buttons.length * SwingConstants.HORIZONTAL_BUTTON_SIZE) / (buttons.length + 1);
        group.addGap(SwingConstants.HORIZONTAL_SHORT_GAP_SIZE);
        for (final JButton button : buttons) {
            group.addGap(gapSize)
                    .addComponent(button, SwingConstants.HORIZONTAL_BUTTON_SIZE, SwingConstants.HORIZONTAL_BUTTON_SIZE, SwingConstants.HORIZONTAL_BUTTON_SIZE);
        }
        group.addGap(gapSize).addGap(SwingConstants.HORIZONTAL_SHORT_GAP_SIZE);
        return group;
    }

    /**
     * Creates vertical layout for components.
     *
     * @param layout     layout
     * @param group      layout group
     * @param components components (Map: label -> data)
     */
    private static void createVerticalComponentsLayout(final GroupLayout layout, final GroupLayout.Group group, final Map<JLabel, JTextField> components) {
        for (final Map.Entry<JLabel, JTextField> data : components.entrySet()) {
            group.addGroup(createVerticalComponents(layout, data.getKey(), data.getValue())).addGap(SwingConstants.VERTICAL_GAP_SIZE);
        }
    }

    /**
     * Returns vertical layout for label component with data component.
     *
     * @param layout layout
     * @param label  label component
     * @param data   data component
     * @return vertical layout for label component with data component
     */
    private static GroupLayout.Group createVerticalComponents(final GroupLayout layout, final JLabel label, final JTextField data) {
        return layout.createParallelGroup()
                .addComponent(label, SwingConstants.VERTICAL_COMPONENT_SIZE, SwingConstants.VERTICAL_COMPONENT_SIZE, SwingConstants.VERTICAL_COMPONENT_SIZE)
                .addGap(SwingConstants.VERTICAL_GAP_SIZE)
                .addComponent(data, SwingConstants.VERTICAL_COMPONENT_SIZE, SwingConstants.VERTICAL_COMPONENT_SIZE, SwingConstants.VERTICAL_COMPONENT_SIZE);
    }

}
