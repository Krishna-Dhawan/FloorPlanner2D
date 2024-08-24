import javax.swing.*;
import java.awt.FlowLayout;

public class AddGUI {
    public static void main(String[] args) {
        Addition obj = new Addition();
    }
}

class Addition extends JFrame /*implements ActionListener*/ {
    JTextField t1, t2;
    JLabel l1;
    JButton b1;

    public Addition() {
        t1 = new JTextField(20);
        t2 = new JTextField(20);

        b1 = new JButton("Add");
        l1 = new JLabel("Result");

        add(t1);
        add(t2);
        add(b1);
        add(l1);

    //        ActionListener al = new ActionListener() {
    //            public void actionPerformed(ActionEvent e) {
    //                int num1 = Integer.parseInt(t1.getText());
    //                int num2 = Integer.parseInt(t2.getText());
    //
    //                int value = num1 + num2;
    //                l1.setText(value + "");
    //            }
    //        };

        b1.addActionListener(/*this*/ e -> {
            int num1 = Integer.parseInt(t1.getText());
            int num2 = Integer.parseInt(t2.getText());

            int value = num1 + num2;
            l1.setText(value + "");
        }); // ActionListener is an Interface
        //so, compulsory to define a void actionPerformed


        setLayout(new FlowLayout());
        setVisible(true);
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    //    public void actionPerformed(ActionEvent e) {
    //        int num1 = Integer.parseInt(t1.getText());
    //        int num2 = Integer.parseInt(t2.getText());
    //
    //        int value = num1 + num2;
    //        l1.setText(value + "");
    //    }
}
