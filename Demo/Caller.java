import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Caller {
    public static void main(String[] args) {
        X onj = new X();
    }
}

class X extends JFrame implements ActionListener {
    int i = 0;
    JProgressBar pb;
    public X() {
        JButton b = new JButton("ADD Form");
        pb = new JProgressBar(0, 20);
//        int i = 0;

        Timer t = new Timer(200, this);

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                t.start();
            }
        });
        add(b);
        add(pb);

        setLayout(new FlowLayout());
        setVisible(true);
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void actionPerformed(ActionEvent e) {
        if (i == 20) {
            new Addition();
            dispose();
        }
        i++;
        pb.setValue(i);
    }
}
