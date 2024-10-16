import javax.swing.*;
import java.awt.*;
import comps.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Screen extends JFrame {
    private final Plan plan;
    public Screen() {
        setTitle("Xyz");
        System.out.println("Hello World");

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar); // Add the menu bar to the frame

        // GridBagLayout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // Expand both horizontally and vertically

        // Plan (Canvas)
        Plan p = new Plan();
        plan = p;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.999; // 80% of horizontal space
        gbc.weighty = 1.0; // Full vertical space
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        add(p, gbc);

        // Control Panel on the left side
        ControlPanel cp = new ControlPanel(this);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.001; // 20% of horizontal space
        gbc.weighty = 1.0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        add(cp.cp, gbc);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println(x + " " + y);
            }
        });

        setSize(1200, 800);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void controlPanelAction(String action, String type, String[] vals) {
        switch (action) {
            case "Add Room":
                if (!vals[0].isEmpty() && !vals[1].isEmpty() && !vals[2].isEmpty() && !vals[3].isEmpty()) {
                    Dim dim = new Dim(Integer.parseInt(vals[2]), Integer.parseInt(vals[3]));
                    Pos pos = new Pos(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]));
                    plan.addRoom(type, pos, dim);
                } else if (!vals[0].isEmpty() && !vals[1].isEmpty() && vals[2].isEmpty() && vals[3].isEmpty()) {
                    Pos pos = new Pos(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]));
                    plan.addRoom(type, pos);
                } else if (vals[0].isEmpty() && vals[1].isEmpty() && !vals[2].isEmpty() && !vals[3].isEmpty()) {
                    Dim dim = new Dim(Integer.parseInt(vals[2]), Integer.parseInt(vals[3]));
                    plan.addRoom(type, dim);
                } else {
                    plan.addRoom(type);
                }
                break;
            case "Add Furniture":
                plan.addFurniture();
                break;
            default:
                break;
        }
    }
}
