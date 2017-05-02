package ui;

import core.AbstractProcessor;
import generated.Group;
import generated.Student;
import ui.BaseClasses.ChangeObjectFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by anastasia on 4/24/17.
 */
public class ChangeStudentFrame extends ChangeObjectFrame {

    private JComboBox<String> groupComboBox;
    private JComboBox<Integer> studentComboBox;
    private JComboBox<String> newGroupComboBox;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField addressField;
    private JTextField phoneNumberField;

    public ChangeStudentFrame(AbstractProcessor processor, Point parentLocation) {
        super("Change student data", processor, parentLocation);
        setSize(WIDTH, HEIGHT + 100);
    }

    @Override
    protected void submitChanges() {
        String groupName = (String) groupComboBox.getSelectedItem();
        Integer studentId  = (Integer) studentComboBox.getSelectedItem();
        String newGroupName = (String) newGroupComboBox.getSelectedItem();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();
        String phoneNumber = phoneNumberField.getText();
        try {
            if (Objects.equals(groupName, "") || Objects.equals(studentId, "")
                    || Objects.equals(groupName, "") || (Objects.equals(firstName, ""))
                    || (Objects.equals(lastName, "")) || Objects.equals(address, "")
                    || Objects.equals(phoneNumber, "")) {
                throw new Exception("Empty fields are not allowed");
            }
        } catch (Exception e1) {
            errorLabel.setText(e1.getMessage());
            return;
        }
        try {
            processor.changeStudent(groupName, studentId, "firstName", firstName);
            processor.changeStudent(groupName, studentId, "lastName", lastName);
            processor.changeStudent(groupName, studentId, "address", address);
            processor.changeStudent(groupName, studentId, "phone", phoneNumber);
            if ((newGroupName != null) && !newGroupName.equals("") && !newGroupName.equals(groupName)) {
                processor.changeStudent(groupName, studentId, "group", newGroupName);
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
        addComboBoxes(panel);
        addFields(panel);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private void addComboBoxes(JPanel panel) {
        addComboBox(panel, groupComboBox);
        addComboBox(panel, studentComboBox);
        addComboBox(panel, newGroupComboBox);
    }

    private void addFields(JPanel panel) {
        addField(panel, firstNameField);
        addField(panel, lastNameField);
        addField(panel, addressField);
        addField(panel, phoneNumberField);
    }

    @Override
    protected JPanel createLabelsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        addLabel(panel, "Group Name:");
        addLabel(panel, "Student ID:");
        addLabel(panel, "New Group:");
        addLabel(panel, "First Name:");
        addLabel(panel, "SecondName:");
        addLabel(panel, "Address:");
        addLabel(panel, "Phone Number:");
        panel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        return panel;
    }

    @Override
    protected void initFields() {
        groupComboBox = createComboBox();
        newGroupComboBox = createComboBox();
        studentComboBox = createIntegerComboBox();
        fillGroupComboBoxWithInfo(groupComboBox);
        fillGroupComboBoxWithInfo(newGroupComboBox);
        addComboBoxActions();
        firstNameField = createTextField();
        lastNameField = createTextField();
        addressField = createTextField();
        phoneNumberField = createTextField();
    }

    private void addComboBoxActions() {
        groupComboBox.addActionListener(this::responseAction);
        studentComboBox.addActionListener(this::responseAction);
    }

    private void responseAction(ActionEvent e) {
        if (e.getSource() == groupComboBox) {
            Group group = processor.getGroup((String) groupComboBox.getSelectedItem());
            fillStudentComboBoxWithInfo(group);
        } else if (e.getSource() == studentComboBox) {
            if (studentComboBox.getItemCount() != 0) {
                int studentId = (Integer) studentComboBox.getSelectedItem();
                Student student = processor.getStudent(studentId);
                fillFieldsWithData(student);
            }
        }
    }

    private void fillStudentComboBoxWithInfo(Group group) {
        studentComboBox.removeAllItems();
        ArrayList<Student> students = (ArrayList<Student>) group.getStudents();
        for (int i = 0; i < students.size(); i++) {
            studentComboBox.addItem(students.get(i).getId());
        }
    }

    private void fillFieldsWithData(Student student) {
        firstNameField.setText(student.getFirstName());
        lastNameField.setText(student.getLastName());
        addressField.setText(student.getAddress());
        phoneNumberField.setText(student.getPhoneNumber());
    }
}
