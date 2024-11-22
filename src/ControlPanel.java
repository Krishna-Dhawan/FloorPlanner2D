import java.util.*;
import comps.*;
import java.awt.*;
import javax.swing.*;

public class ControlPanel extends Component {
    JPanel cp;
    private final Screen mediator;

    public JTextField x = new JTextField(20);
    public JTextField y = new JTextField(20);
    public JTextField w = new JTextField(10);
    public JTextField r  = new JTextField(10);
    public JTextField h = new JTextField(10);

    private int currRotation;

    public ControlPanel(Screen screen) {
        mediator = screen;
        cp = new JPanel();
        cp.setLayout(null); // Set layout to null for absolute positioning
        cp.setBackground(Color.LIGHT_GRAY);
        currRotation = 0;

        JLabel l1 = new JLabel("ADD ROOM");
        l1.setBounds(100, 0, 150, 30); // Set position and size
        cp.add(l1);

        JButton r1 = new JButton("Bathroom");
        r1.setBounds(10, 40, 100, 30);
        cp.add(r1);

        JButton r2 = new JButton("Living/Dining Room");
        r2.setBounds(170, 40, 100, 30);
        cp.add(r2);

        JButton r3 = new JButton("Bedroom");
        r3.setBounds(10, 80, 100, 30);
        cp.add(r3);

        JButton r4 = new JButton("Kitchen");
        r4.setBounds(170, 80, 100, 30);
        cp.add(r4);

        JButton r5 = new JButton("Study Room");
        r5.setBounds(90,120, 100, 30);
        cp.add(r5);

        JLabel k1JLabel = new JLabel("Position:");
        k1JLabel.setBounds(10, 140, 150, 30);
        cp.add(k1JLabel);

        x.setBounds(30,170, 250, 30);
        cp.add(x);

        y.setBounds(30, 210, 250, 30);
        cp.add(y);

        JLabel k2JLabel = new JLabel("Dimensions:");
        k2JLabel.setBounds(10, 240, 150, 30);
        cp.add(k2JLabel);

        h.setBounds(30, 265, 250, 30);
        cp.add(h);

        w.setBounds(30, 305, 250, 30);
        cp.add(w);

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
        l2.setBounds(100, 350, 100, 30);
        cp.add(l2);

        JButton f1 = new JButton("Bed");
        f1.setBounds(10, 390, 100, 30);
        cp.add(f1);

        JButton f2 = new JButton("Chair");
        f2.setBounds(170, 390, 100, 30);
        cp.add(f2);

        JButton f3 = new JButton("Dining Set");
        f3.setBounds(10, 430, 100, 30);
        cp.add(f3);

        JButton f4 = new JButton("Sofa");
        f4.setBounds(170, 430, 100, 30);
        cp.add(f4);

        JButton f5 = new JButton("Table");
        f5.setBounds(10, 470, 100, 30);
        cp.add(f5);

        JButton f6 = new JButton("Commode");
        f6.setBounds(170, 470, 100, 30);
        cp.add(f6);

        JButton f7 = new JButton("Closet");
        f7.setBounds(10, 510, 100, 30);
        cp.add(f7);

        JButton f8 = new JButton("Shower");
        f8.setBounds(170, 510, 100, 30);
        cp.add(f8);

        JButton f9 = new JButton("Kitchen Sink");
        f9.setBounds(10, 550, 100, 30);
        cp.add(f9);

        JButton f10 = new JButton("Stove");
        f10.setBounds(170, 550, 100, 30);
        cp.add(f10);

        JLabel k3JLabel = new JLabel("Rotation:");
        k3JLabel.setBounds(10, 590, 150, 30);
        cp.add(k3JLabel);

        r.setBounds(30, 620, 250, 30);
        r.setText(currRotation + "");
        cp.add(r);

        JButton incR = new JButton("++");
        incR.setBounds(280, 620, 30, 30);
        cp.add(incR);

        f1.addActionListener(e -> {
            String[] vals = {x.getText(), y.getText(), h.getText(), w.getText(), r.getText()};
            mediator.controlPanelAction("Add Furniture", "bed", vals);
        });
        f2.addActionListener(e -> {
            String[] vals = {x.getText(), y.getText(), h.getText(), w.getText(), r.getText()};
            mediator.controlPanelAction("Add Furniture", "chair", vals);
        });
        f3.addActionListener(e -> {
            String[] vals = {x.getText(), y.getText(), h.getText(), w.getText(), r.getText()};
            mediator.controlPanelAction("Add Furniture", "dining_set", vals);
        });
        f4.addActionListener(e -> {
            String[] vals = {x.getText(), y.getText(), h.getText(), w.getText(), r.getText()};
            mediator.controlPanelAction("Add Furniture", "sofa", vals);
        });
        f5.addActionListener(e -> {
            String[] vals = {x.getText(), y.getText(), h.getText(), w.getText(), r.getText()};
            mediator.controlPanelAction("Add Furniture", "table", vals);
        });
        f6.addActionListener(e -> {
            String[] vals = {x.getText(), y.getText(), h.getText(), w.getText(), r.getText()};
            mediator.controlPanelAction("Add Furniture", "commode", vals);
        });
        f7.addActionListener(e -> {
            String[] vals = {x.getText(), y.getText(), h.getText(), w.getText(), r.getText()};
            mediator.controlPanelAction("Add Furniture", "closet", vals);
        });
        f8.addActionListener(e -> {
            String[] vals = {x.getText(), y.getText(), h.getText(), w.getText(), r.getText()};
            mediator.controlPanelAction("Add Furniture", "shower", vals);
        });
        f9.addActionListener(e -> {
            String[] vals = {x.getText(), y.getText(), h.getText(), w.getText(), r.getText()};
            mediator.controlPanelAction("Add Furniture", "kit_sink", vals);
        });
        f10.addActionListener(e -> {
            String[] vals = {x.getText(), y.getText(), h.getText(), w.getText(), r.getText()};
            mediator.controlPanelAction("Add Furniture", "stove", vals);
        });

        incR.addActionListener(e -> incRotation());
    }

    public void setVals(Room room) {
        x.setText(room.pos.x + "");
        y.setText(room.pos.y + "");
        h.setText(room.dim.height + "");
        w.setText(room.dim.width + "");
    }
    public void setVals(Furniture furniture) {
        x.setText(furniture.pos.x + "");
        y.setText(furniture.pos.y + "");
        h.setText(furniture.dim.height + "");
        w.setText(furniture.dim.width + "");
        r.setText(furniture.rotation + "");
    }
    public void setVals() {
        x.setText("");
        y.setText("");
        h.setText("");
        w.setText("");
        r.setText("");
    }

    public void incRotation() {
        currRotation += 90;
        if (currRotation >= 360) {
            currRotation = 0;
        }
        r.setText(currRotation + "");
    }
}
