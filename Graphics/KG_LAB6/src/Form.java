import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by anastasia on 4/12/17.
 */
public class Form {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        final DrawArea drawArea = new DrawArea();
        content.add(drawArea, BorderLayout.CENTER);

        JPanel controls = new JPanel();

        JButton newPointsButton = new JButton("New points");
        JButton addPointsButton = new JButton("Add");
        JButton removePointsButton = new JButton("Remove");
        JButton okButton = new JButton("OK");

        ActionListener actionListener = e -> {
            if (e.getSource() == newPointsButton) {
                drawArea.clearData();
            } else if (e.getSource() == okButton) {
                drawArea.createConvexHull();
            } else if (e.getSource() == addPointsButton) {
                drawArea.setAddingPointsMode();
            } else if (e.getSource() == removePointsButton) {
                drawArea.setRemovingPointsMode();
            }
        };

        newPointsButton.addActionListener(actionListener);
        okButton.addActionListener(actionListener);
        controls.add(newPointsButton);
        controls.add(okButton);
        content.add(controls, BorderLayout.NORTH);

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
