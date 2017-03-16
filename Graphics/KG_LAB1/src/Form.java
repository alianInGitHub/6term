import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by Анастасия on 03.03.2017.
 */
public class Form {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        final DrawArea drawArea = new DrawArea();
        content.add(drawArea, BorderLayout.CENTER);

        JPanel controls = new JPanel();

        JButton createPolygonButton = new JButton("Create polygon");
        JButton okButton = new JButton("OK");

        ActionListener actionListener = e -> {
            if(e.getSource() == createPolygonButton) {
                drawArea.setMode(DrawArea.DrawingMode.DRAWING_POLYGON);
            } else
                if(e.getSource() == okButton) {
                    drawArea.setMode(DrawArea.DrawingMode.LOCATING_POINT);
                }
        };

        createPolygonButton.addActionListener(actionListener);
        okButton.addActionListener(actionListener);
        controls.add(createPolygonButton);
        controls.add(okButton);
        content.add(controls, BorderLayout.NORTH);

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
