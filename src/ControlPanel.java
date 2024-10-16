import java.util.*;
import comps.*;
import java.awt.*;
import javax.swing.*;

public class ControlPanel extends Component {
    JPanel cp;
    private final Screen mediator;

    // TODO: remove layout and add obj.setBounds() for each UI element
    public ControlPanel(Screen screen) {
        mediator = screen;
        cp = new JPanel();
        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
        cp.setBackground(Color.LIGHT_GRAY);

        JLabel l1 = new JLabel("ADD ROOM");
        JButton r1 = new JButton("Add Bathroom");
        JButton r2 = new JButton("Add Living/Dining Room");
        JButton r3 = new JButton("Add Bedroom");
        JButton r4 = new JButton("Add Kitchen");
        JButton r5 = new JButton("Add Misc. Room");
        JTextField x = new JTextField(10);
        JTextField y = new JTextField(10);
        JTextField h = new JTextField(10);
        JTextField w = new JTextField(10);

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
        JButton f1 = new JButton("Add Bathroom");
        JButton f2 = new JButton("Add Living/Dining Room");
        JButton f3 = new JButton("Add Bedroom");
        JButton f4 = new JButton("Add Kitchen");
        JButton f5 = new JButton("Add Misc. Room");

        cp.add(l1);
        cp.add(r1);
        cp.add(r2);
        cp.add(r3);
        cp.add(r4);
        cp.add(r5);

        cp.add(x);
        cp.add(y);
        cp.add(h);
        cp.add(w);

        cp.add(l2);
        cp.add(f1);
        cp.add(f2);
        cp.add(f3);
        cp.add(f4);
        cp.add(f5);
//        cp.setLayout(new GridLayout(2, 1));
//        cp.setLayout(new FlowLayout());
    }
}
