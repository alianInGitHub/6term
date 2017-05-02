package ui;

import core.AbstractProcessor;
import generated.Group;
import ui.BaseClasses.AddObjectFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Created by anastasia on 4/23/17.
 */
public class AddGroupFrame extends AddObjectFrame {


    private JTextField nameField;
    private JTextField facultyField;
    private JTextField courseFiled;

    public AddGroupFrame(AbstractProcessor processor, Point parentLocation) {
        super("Add new group", processor, parentLocation);
    }

    protected void initFields() {
        nameField = new JTextField();
        facultyField = new JTextField();
        courseFiled = new JTextField();
    }

    public JPanel createLabelsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        addLabel(panel, "Name:\t");
        addLabel(panel, "Faculty:\t");
        addLabel(panel, "Course:\t");
        panel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        return panel;
    }

    protected void addLabel(JPanel panel, String text) {
        panel.add(createLabel(text));
        panel.add(Box.createRigidArea(MainFrame.verticalGap));
    }

    protected JPanel createFieldsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        nameField = createTextField();
        addField(panel, nameField);
        facultyField = createTextField();
        addField(panel, facultyField);
        courseFiled = createTextField();
        addField(panel, courseFiled);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    protected void submitChanges() {
        String name = nameField.getText();
        String faculty = facultyField.getText();
        int course;
        try {
            if ((Objects.equals(name, "")) || (Objects.equals(faculty, ""))) {
                throw new Exception("Empty fields are not allowed");
            }
            course = Integer.valueOf(courseFiled.getText());
            if ((course < 1) || (course > 6)) {
                throw  new Exception("Only decimal numbers (1-6) are allowed");
            }
        } catch (Exception e1) {
            errorLabel.setText(e1.getMessage());
            return;
        }
        try {
            Group newGroup = processor.createGroup(name, faculty, course);
            processor.addGroup(newGroup);
            MainFrame.dataWasChanged = true;
            dispose();
        } catch (Exception e1) {
            errorLabel.setText(e1.getMessage());
        }
    }
}
