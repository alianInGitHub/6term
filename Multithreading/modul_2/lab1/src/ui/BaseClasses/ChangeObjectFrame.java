package ui.BaseClasses;

import builder.XMLProcessor;
import generated.Group;
import ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by anastasia on 4/24/17.
 */
public abstract class ChangeObjectFrame extends BaseFrame {

    protected XMLProcessor processor;

    public ChangeObjectFrame(String title, XMLProcessor processor, Point parentLocation) {
        super(title);
        this.processor = processor;
        initialize();
        setLocation(parentLocation.x + (MainFrame.WIDTH - WIDTH) / 2, parentLocation.y + (MainFrame.HEIGHT - HEIGHT) / 2);
    }

    private void initialize() {
        setSize(WIDTH, HEIGHT + 80);
        setVisible(true);
        setResizable(false);
        Container content = getContentPane();
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        content.add(createPanel());
    }

    protected void fillGroupComboBoxWithInfo(JComboBox<String>... comboBoxes) {
        ArrayList<Group> groups = (ArrayList<Group>) processor.getGroups();
        for (int i = 0; i < groups.size(); i++) {
            for (int j = 0; j < comboBoxes.length; j++) {
                comboBoxes[j].addItem(groups.get(i).getName());
            }
        }
    }
}
