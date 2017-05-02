package ui.BaseClasses;

import builder.XMLProcessor;
import ui.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by anastasia on 4/24/17.
 */
public abstract class AddObjectFrame extends BaseFrame {
    protected final int WIDTH = MainFrame.WIDTH / 2;
    protected final int HEIGHT = MainFrame.HEIGHT / 2;


    protected XMLProcessor processor;

    public AddObjectFrame(String name, XMLProcessor processor, Point parentLocation) {
        super(name);
        this.processor = processor;
        initialize();
        setLocation(parentLocation.x + (MainFrame.WIDTH - WIDTH) / 2, parentLocation.y + (MainFrame.HEIGHT - HEIGHT) / 2);
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
