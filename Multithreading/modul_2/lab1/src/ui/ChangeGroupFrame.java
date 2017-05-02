package ui;

import builder.XMLProcessor;
import generated.Group;
import ui.BaseClasses.ChangeObjectFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by anastasia on 4/24/17.
 */
public class ChangeGroupFrame extends ChangeObjectFrame {

    private JComboBox<String> nameComboBox;
    private JTextField newNameField;
    private JTextField facultyField;
    private JTextField courseField;

    public ChangeGroupFrame(XMLProcessor processor, Point parentLocation) {
        super("Change group data", processor, parentLocation);
    }

    @Override
    protected void submitChanges() {
        String name = (String) nameComboBox.getSelectedItem();
        String newName = newNameField.getText();
        String faculty = facultyField.getText();
        int course;
        try {
            if (Objects.equals(faculty, "")) {
                throw new Exception("Empty fields are not allowed");
            }
            course = Integer.valueOf(courseField.getText());
            if ((course < 1) || (course > 6)) {
                throw  new Exception("Only decimal numbers (1-6) are allowed");
            }
        } catch (Exception e1) {
            errorLabel.setText(e1.getMessage());
            return;
        }
        try {
            processor.changeGroupAttributeValue(name, "faculty", faculty);
            processor.changeGroupAttributeValue(name, "course", String.valueOf(course));
            if(!Objects.equals(newName, "") && !Objects.equals(newName, null) && !Objects.equals(newName, name)){
                processor.changeGroupAttributeValue(name, "name", newName);
            }
            MainFrame.dataWasChanged = true;
            dispose();
        } catch (Exception e1) {
            errorLabel.setText(e1.getMessage());
        }
    }

    @Override
    protected JPanel createFieldsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(nameComboBox);
        panel.add(Box.createRigidArea(MainFrame.verticalGap));
        addField(panel, newNameField);
        addField(panel, facultyField);
        addField(panel, courseField);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    @Override
    protected JPanel createLabelsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        addLabel(panel, "Name:");
        addLabel(panel, "New Name:");
        addLabel(panel, "Faculty:");
        addLabel(panel, "Course:");
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    @Override
    protected void initFields() {
        nameComboBox = createComboBox();
        fillComboBoxWithInfo();
        addComboBoxAction();
        newNameField = createTextField();
        facultyField = createTextField();
        courseField = createTextField();
    }

    private void fillComboBoxWithInfo() {
        ArrayList<Group> groups = (ArrayList<Group>) processor.getGroups();
        for (int i = 0; i < groups.size(); i++) {
            nameComboBox.addItem(groups.get(i).getName());
        }
    }

    private void addComboBoxAction() {
        nameComboBox.addActionListener(e -> {
            if (e.getSource() == nameComboBox) {
                Group group = processor.getGroupByUniqueName((String) nameComboBox.getSelectedItem());
                facultyField.setText(group.getFaculty());
                courseField.setText(String.valueOf(group.getCourse()));
            }
        });
    }
}
