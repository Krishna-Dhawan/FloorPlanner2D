import java.util.*;
import java.awt.*;
import javax.swing.*;

public class ControlPanel extends Component {
    JPanel cp;
    private final Screen mediator;

    public ControlPanel(Screen screen) {
        mediator = screen;
        cp = new JPanel();
        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
        cp.setBackground(Color.LIGHT_GRAY);

        JLabel l1 = new JLabel("ADD ROOM");
        JButton r1 = new JButton("Add Room");
        JButton r2 = new JButton("Add Furniture");

        r2.addActionListener(e -> mediator.controlPanelAction("Add Room"));
        r1.addActionListener(e -> mediator.controlPanelAction("Add Furniture"));

        cp.add(l1);
        cp.add(r1);
//        cp.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        cp.add(r2);
        cp.setLayout(new GridLayout(2, 1));
    }
}
