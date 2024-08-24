import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseDemo {
    public static void main(String[] args) {
        Xyz xyz = new Xyz();
    }
}

class Xyz extends JFrame {
    public Xyz() {
        // addMouseListener(new MouseListener(){});
        // CANNOT use, the interface has too many unnecessary methods

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println(x + " " + y);
            }
        });

        setLayout(new FlowLayout());
        setVisible(true);
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
