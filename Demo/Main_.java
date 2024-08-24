import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Abc obj = new Abc();
        obj.setVisible(true);
    }
}
class Abc extends JFrame { // CardLayout by default
    public Abc() {
        setLayout(new FlowLayout()); // FlowLayout GridLayout Null
        JLabel l = new JLabel("Hello World!\n");
        add(l);
        JLabel l1 = new JLabel("Welcome");
        add(l1);

        setTitle("Abc");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
    }
}