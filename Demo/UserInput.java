import javax.swing.*;
import java.awt.*;

public class UserInput {
    public static void main(String[] args) {
        RadioDemo r = new RadioDemo();
    }
}

class RadioDemo extends JFrame{
    JTextField t1;
    JButton b;
    JRadioButton r1, r2;
    JLabel l;
    JCheckBox c1, c2;
    public RadioDemo() {
        t1 = new JTextField(15);
        b = new JButton("Ok");
        r1 = new JRadioButton("Male");
        r2 = new JRadioButton("Female");
        l = new JLabel("Greeting");
        c1 = new JCheckBox("Yes");
        c2 = new JCheckBox("No");

        ButtonGroup bg = new ButtonGroup();
        bg.add(r1);
        bg.add(r2); // Makes the radio button deselect the other

        add(t1);
        add(r1);
        add(r2);
        add(c1);
        add(c2);
        add(b);
        add(l);
        // can also have event Listeners on checkboxes and radios
        b.addActionListener(e -> {
            String name = t1.getText();

            if (r1.isSelected()) {
                name = "Mr. " + name;
            } else {
                name = "Ms. " + name;
            }
            if (c1.isSelected()) {
                name = name + " Yes";
            }
            if (c2.isSelected()) {
                name = name + " No";
            }
            l.setText(name);
        });

        setLayout(new FlowLayout());
        setVisible(true);
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
