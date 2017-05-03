package ui;

import core.AbstractProcessor;
import generated.Group;
import ui.BaseClasses.ShowDetailedObjectFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Анастасия on 03.05.2017.
 */
public class GroupInfoFrame extends ShowDetailedObjectFrame {
    private JComboBox<String> nameComboBox;

    public GroupInfoFrame(AbstractProcessor processor, Point parentLocation) {
        super("Group Info", processor, parentLocation);
    }

    @Override
    protected void submitChanges() {
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
        fillGroupComboBoxWithInfo(nameComboBox);
        addComboBoxAction();
        info = new JTextArea();
    }

    private void addComboBoxAction() {
        nameComboBox.addActionListener(e -> {
            if (e.getSource() == nameComboBox) {
                Group group = processor.getGroup((String) nameComboBox.getSelectedItem());
                info.setText(processor.groupToString(group));
                okButton.setEnabled(true);
            }
        });
    }
}
