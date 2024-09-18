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
        System.out.println("Hello World!");


        /* *setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        */
        Plan p = new Plan();
        add(p);

        ControlPanel Cp = new ControlPanel();
        add(Cp.cp);

        setVisible(true);
        setLayout(new GridLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
