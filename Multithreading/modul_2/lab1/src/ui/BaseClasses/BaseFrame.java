package ui.BaseClasses;

import ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by anastasia on 4/24/17.
 */
abstract class BaseFrame extends JFrame {
    protected final int WIDTH = MainFrame.WIDTH / 2;
    protected final int HEIGHT = MainFrame.HEIGHT / 2;

    private JButton cancelButton;
    protected JButton okButton;

    private final Dimension buttonSize = new Dimension(MainFrame.WIDTH / 4, 30);

    protected JLabel errorLabel;

    public BaseFrame(String title) {
        super(title);
    }

    protected JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(Box.createRigidArea(new Dimension(WIDTH, HEIGHT / 4)));
        panel.add(createLabelsAndFieldsPanel());
        panel.add(Box.createRigidArea(MainFrame.verticalGap));
        panel.add(errorLabel);
        panel.add(Box.createRigidArea(MainFrame.verticalGap));
        panel.add(createButtonsPanel());
        panel.add(Box.createRigidArea(MainFrame.verticalGap));
        return panel;
    }

    protected JPanel createButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        cancelButton = createButton("Cancel");
        addButton(panel, cancelButton);
        okButton = createButton("OK");
        panel.add(okButton);
        panel.add(Box.createRigidArea(MainFrame.horizontalGap));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        return panel;
    }

    private JButton createButton(String name) {
        JButton button = new JButton(name);
        button.addActionListener(this::actionPerformed);
        button.setMaximumSize(buttonSize);
        button.setAlignmentX(Component.RIGHT_ALIGNMENT);
        return button;
    }

    private void addButton(JPanel panel, JButton button) {
        panel.add(button);
        panel.add(Box.createRigidArea(MainFrame.horizontalGap));
    }

    private void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            dispose();
        } else if (e.getSource() == okButton) {
            submitChanges();
        }
    }

    protected abstract void submitChanges();

    protected abstract JPanel createFieldsPanel();

    protected abstract JPanel createLabelsPanel();

    protected abstract void initFields();

    protected JPanel createLabelsAndFieldsPanel() {
        initErrorLabel();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        initFields();
        panel.add(createLabelsPanel());
        panel.add(createFieldsPanel());
        return panel;
    }

    private void initErrorLabel() {
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
    }

    protected void addLabel(JPanel panel, String text) {
        panel.add(createLabel(text));
        panel.add(Box.createRigidArea(MainFrame.verticalGap));
    }

    protected JPanel createLabel(String text) {
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.LINE_AXIS));
        JLabel label = new JLabel(text);
        label.setMaximumSize(MainFrame.defaultButtonSize);
        label.setAlignmentX(Component.RIGHT_ALIGNMENT);
        labelPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        labelPanel.add(label);
        return labelPanel;
    }

    protected void addField(JPanel panel, JTextField field) {
        panel.add(field);
        panel.add(Box.createRigidArea(MainFrame.verticalGap));
    }

    protected JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setMaximumSize(MainFrame.defaultButtonSize);
        textField.setAlignmentX(Component.RIGHT_ALIGNMENT);
        return textField;
    }

    protected JComboBox<String> createComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setMaximumSize(MainFrame.defaultButtonSize);
        return comboBox;
    }

    protected JComboBox<Integer> createIntegerComboBox() {
        JComboBox<Integer> comboBox = new JComboBox<>();
        comboBox.setMaximumSize(MainFrame.defaultButtonSize);
        return comboBox;
    }

    protected void addComboBox(JPanel panel, JComboBox comboBox) {
        panel.add(comboBox);
        panel.add(Box.createRigidArea(MainFrame.verticalGap));
    }
}
