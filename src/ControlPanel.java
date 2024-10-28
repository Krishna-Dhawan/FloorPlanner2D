import java.util.*;
import comps.*;
import java.awt.*;
import javax.swing.*;

public class ControlPanel extends Component {
    JPanel cp;
    private final Screen mediator;

    public ControlPanel(Screen screen) {
        mediator = screen;
        cp = new JPanel();
        cp.setLayout(null); // Set layout to null for absolute positioning
        cp.setBackground(Color.LIGHT_GRAY);

        JLabel l1 = new JLabel("ADD ROOM");
        l1.setBounds(100, 10, 100, 30); // Set position and size
        cp.add(l1);

        JButton r1 = new JButton("Add Bathroom");
        r1.setBounds(10, 50, 150, 30);
        cp.add(r1);

        JButton r2 = new JButton("Add Living/Dining Room");
        r2.setBounds(10, 90, 150, 30);
        cp.add(r2);

        JButton r3 = new JButton("Add Bedroom");
        r3.setBounds(10, 130, 150, 30);
        cp.add(r3);

        JButton r4 = new JButton("Add Kitchen");
        r4.setBounds(10, 170, 150, 30);
        cp.add(r4);

        JButton r5 = new JButton("Add Misc. Room");
        r5.setBounds(10, 210, 150, 30);
        cp.add(r5);

        JTextField x = new JTextField(10);
        x.setBounds(180,50, 50, 30);
        cp.add(x);

        JTextField y = new JTextField(10);
        y.setBounds(180, 90, 50, 30);
        cp.add(y);

        JTextField h = new JTextField(10);
        h.setBounds(180, 130, 50, 30);
        cp.add(h);

        JTextField w = new JTextField(10);
        w.setBounds(180, 170, 50, 30);
        cp.add(w);

        JTextField z = new JTextField(10);
        z.setBounds(180, 210, 50, 30);
        cp.add(z);

        r1.addActionListener(e -> {
            String[] vals = {x.getText(), y.getText(), h.getText(), w.getText()};
            mediator.controlPanelAction("Add Room", "bath", vals);
        });
        r2.addActionListener(e -> {
            String[] vals = {x.getText(), y.getText(), h.getText(), w.getText()};
            mediator.controlPanelAction("Add Room", "living", vals);
        });
        r3.addActionListener(e -> {
            String[] vals = {x.getText(), y.getText(), h.getText(), w.getText()};
            mediator.controlPanelAction("Add Room", "bedroom", vals);
        });
        r4.addActionListener(e -> {
            String[] vals = {x.getText(), y.getText(), h.getText(), w.getText()};
            mediator.controlPanelAction("Add Room", "kitchen", vals);
        });
        r5.addActionListener(e -> {
            String[] vals = {x.getText(), y.getText(), h.getText(), w.getText()};
            mediator.controlPanelAction("Add Room", "misc", vals);
        });

        JLabel l2 = new JLabel("ADD FURNITURE");
        l2.setBounds(100, 260, 150, 30);
        cp.add(l2);

        JButton f1 = new JButton("Add Bathroom");
        f1.setBounds(10, 300, 150, 30);
        cp.add(f1);

        JButton f2 = new JButton("Add Living/Dining Room");
        f2.setBounds(10, 340, 150, 30);
        cp.add(f2);

        JButton f3 = new JButton("Add Bedroom");
        f3.setBounds(10, 380, 150, 30);
        cp.add(f3);

        JButton f4 = new JButton("Add Kitchen");
        f4.setBounds(10, 420, 150, 30);
        cp.add(f4);

        JButton f5 = new JButton("Add Misc. Room");
        f5.setBounds(10, 460, 150, 30);
        cp.add(f5);
    }
}
