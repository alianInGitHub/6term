package ui.BaseClasses;

import core.AbstractProcessor;
import generated.Group;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by anastasia on 4/24/17.
 */
public abstract class ChangeObjectFrame extends BaseFrame {

    protected AbstractProcessor processor;

    public ChangeObjectFrame(String title, AbstractProcessor processor, Point parentLocation) {
        super(title);
        this.processor = processor;
        setLocationAccordingToParent(parentLocation);
        initialize();
    }

    private void initialize() {
        setSize(WIDTH, HEIGHT + 80);
        setVisible(true);
        setResizable(false);
        Container content = getContentPane();
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        content.add(createPanel());
    }

    protected void fillGroupComboBoxWithInfo(JComboBox<String> comboBox) {
        ArrayList<Group> groups = (ArrayList<Group>) processor.getGroups();
        if (groups != null) {
            for (int i = 0; i < groups.size(); i++) {
                comboBox.addItem(groups.get(i).getName());
            }
        }
    }
}
