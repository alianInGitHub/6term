package ui;

import builder.XMLProcessor;
import generated.Group;
import ui.BaseClasses.AddObjectFrame;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by anastasia on 4/24/17.
 */
public class AddStudentFrame extends AddObjectFrame {

    private JComboBox<String> groupComboBox;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField addressField;
    private JTextField phoneNumberField;

    public AddStudentFrame(XMLProcessor processor, Point parentLocation) {
        super("Add new student", processor, parentLocation);
    }


    protected void initFields() {
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        addressField = new JTextField();
        phoneNumberField = new JTextField();
    }

    public JPanel createLabelsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        addLabel(panel, "Group Name:\t");
        addLabel(panel, "First Name:\t");
        addLabel(panel, "SecondName:\t");
        addLabel(panel, "Address:\t");
        addLabel(panel, "Phone Number:\t");
        panel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        return panel;
    }

    protected JPanel createFieldsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        addGroupComboBox(panel);
        firstNameField = createTextField();
        addField(panel, firstNameField);
        lastNameField = createTextField();
        addField(panel, lastNameField);
        addressField = createTextField();
        addField(panel, addressField);
        phoneNumberField = createTextField();
        addField(panel, phoneNumberField);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private void addGroupComboBox(JPanel panel) {
        groupComboBox = createComboBox();
        fillComboBoxWithInfo();
        panel.add(groupComboBox);
        panel.add(Box.createRigidArea(MainFrame.verticalGap));
    }

    private void fillComboBoxWithInfo() {
        ArrayList<Group> groups = (ArrayList<Group>) processor.getGroups();
        for (int i = 0; i < groups.size(); i++) {
            groupComboBox.addItem(groups.get(i).getName());
        }
    }

    protected void submitChanges() {
        String groupName = (String) groupComboBox.getSelectedItem();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();
        String phoneNumber = phoneNumberField.getText();
        try {
            if (Objects.equals(groupName, "") || (Objects.equals(firstName, ""))
                    || (Objects.equals(lastName, "")) || Objects.equals(address, "")
                    || Objects.equals(phoneNumber, "")) {
                throw new Exception("Empty fields are not allowed");
            }
        } catch (Exception e1) {
            errorLabel.setText(e1.getMessage());
            return;
        }
        try {
            processor.addStudent(groupName, firstName, lastName, address, phoneNumber);
            MainFrame.dataWasChanged = true;
            dispose();
        } catch (Exception e1) {
            errorLabel.setText(e1.getMessage());
        }
    }
}
