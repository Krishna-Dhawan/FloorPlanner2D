import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Screen screen = new Screen();
    }
}

class Screen extends JFrame {
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
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.8; // 80% of horizontal space
        gbc.weighty = 1.0; // Full vertical space
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        add(p, gbc);

        // Control Panel on the right side
        ControlPanel cp = new ControlPanel();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.2; // 20% of horizontal space
        gbc.weighty = 1.0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        add(cp.cp, gbc);

        setSize(1200, 800);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
