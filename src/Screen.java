import javax.swing.*;
import java.awt.*;
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

    public void controlPanelAction(String action) {
        switch (action) {
            case "Add Room":
                plan.addRoom();
                break;
            case "Add Furniture":
                plan.addFurniture();
                break;
            default:
                break;
        }
    }
}
