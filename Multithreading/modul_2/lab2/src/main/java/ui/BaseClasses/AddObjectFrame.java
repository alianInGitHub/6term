package ui.BaseClasses;

import core.AbstractProcessor;

import javax.swing.*;
import java.awt.*;

/**
 * Created by anastasia on 4/24/17.
 */
public abstract class AddObjectFrame extends BaseFrame {

    protected AbstractProcessor processor;

    protected AddObjectFrame(String name, AbstractProcessor processor, Point parentLocation) {
        super(name);
        this.processor = processor;
        setLocationAccordingToParent(parentLocation);
        initialize();
    }

    private void initialize() {
        setSize(WIDTH, HEIGHT + 50);
        setVisible(true);
        setResizable(false);
        Container content = getContentPane();
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        content.add(createPanel());
    }
}
