package ui;

import builder.XMLProcessor;
import generated.Group;
import ui.BaseClasses.ShowDetailedObjectFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by anastasia on 4/24/17.
 */
public class DeleteGroupFrame extends ShowDetailedObjectFrame {

    private JComboBox<String> nameComboBox;

    public DeleteGroupFrame(XMLProcessor processor, Point parentLocation) {
        super("Delete group", processor, parentLocation);
    }

    @Override
    protected void submitChanges() {
        String name = (String) nameComboBox.getSelectedItem();

        if (processor.deleteGroup(name)) {
            JOptionPane.showMessageDialog(this, "Successfully deleted!");
            MainFrame.dataWasChanged = true;
            dispose();
        } else {
            errorLabel.setText("Failed to delete");
        }
    }

    @Override
    protected JPanel createFieldsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(nameComboBox);
        return panel;
    }

    @Override
    protected JPanel createLabelsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        addLabel(panel, "Select group:");
        return panel;
    }

    @Override
    protected void initFields() {
        nameComboBox = createComboBox();
        fillComboBoxWithInfo();
        addComboBoxAction();
        info = new JTextArea();
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
                info.setText(getInfo(group));
                okButton.setEnabled(true);
            }
        });
    }
}
