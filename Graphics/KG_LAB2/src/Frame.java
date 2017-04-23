import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Creation and initialisation of form and elements on it
 * Entry point of this program
 * Created by Анастасия on 03.03.2017.
 */
public class Frame {
    private static final DrawArea drawArea = new DrawArea();

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(drawArea, BorderLayout.CENTER);

        JTextArea info = new JTextArea();
        content.add( info, BorderLayout.EAST);

        JPanel controls = new JPanel();
        setControls(controls);
        content.add(controls, BorderLayout.NORTH);


        JPanel methodSelectionPanel = new JPanel();
        setMethodsControls(methodSelectionPanel);
        content.add(methodSelectionPanel, BorderLayout.EAST);

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void setControls( JPanel controls) {
        JButton drawVertexesButton = new JButton("Vertexes");
        JButton drawEdgesButton = new JButton("Edges");
        JButton locatePointButton = new JButton("Locate Point");
        JButton okButton = new JButton("OK");
        JButton clearButton = new JButton("Clear");

        ActionListener actionListener = e -> {
            if(e.getSource() == drawVertexesButton) {
                drawArea.setCurrentMode(DrawArea.DrawingMode.GRAPH_VERTEXES);
            } else if (e.getSource() == drawEdgesButton) {
                drawArea.setCurrentMode(DrawArea.DrawingMode.GRAPH_EDGES);
            } else if (e.getSource() == locatePointButton) {
                drawArea.setCurrentMode(DrawArea.DrawingMode.POINTS);
            } else if (e.getSource() == okButton) {
                drawArea.setCurrentMode(DrawArea.DrawingMode.NONE);
            } else if(e.getSource() == clearButton) {
                drawArea.clearData();
            }
        };

        drawVertexesButton.addActionListener(actionListener);
        drawEdgesButton.addActionListener(actionListener);
        locatePointButton.addActionListener(actionListener);
        okButton.addActionListener(actionListener);
        clearButton.addActionListener(actionListener);

        controls.add(drawVertexesButton);
        controls.add(drawEdgesButton);
        controls.add(locatePointButton);
        controls.add(okButton);
        controls.add(clearButton);
    }

    private static void setMethodsControls(JPanel methodSelectionPanel) {
        methodSelectionPanel.setLayout(new BoxLayout(methodSelectionPanel, BoxLayout.Y_AXIS));
        JRadioButton stripsMethodRadioButton = new JRadioButton("Strips Method");
        JRadioButton chainsMethodRadioButton = new JRadioButton("Chains Methods");

        ActionListener actionListener = e -> {
            if(e.getSource() == stripsMethodRadioButton) {
                drawArea.setCurrentLocationMethod(DrawArea.METHOD.STRIPS);
                chainsMethodRadioButton.setSelected(false);
            } else if(e.getSource() == chainsMethodRadioButton) {
                drawArea.setCurrentLocationMethod(DrawArea.METHOD.CHAINS);
                stripsMethodRadioButton.setSelected(false);
            }
        };

        stripsMethodRadioButton.addActionListener(actionListener);
        chainsMethodRadioButton.addActionListener(actionListener);

        methodSelectionPanel.add(stripsMethodRadioButton);
        methodSelectionPanel.add(chainsMethodRadioButton);
    }
}
