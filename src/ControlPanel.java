import java.util.*;
import java.awt.*;
import javax.swing.*;

public class ControlPanel extends Component {
    JPanel cp;

    public ControlPanel() {
        cp = new JPanel();
        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
        cp.setBackground(Color.LIGHT_GRAY);

        JButton btn1 = new JButton("Add Room");
        JButton btn2 = new JButton("Add Furniture");

        cp.add(btn1);
        cp.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        cp.add(btn2);
    }
}
