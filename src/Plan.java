import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Plan extends Canvas {
    public Plan() {
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println(x + " " + y);
            }
        });
    }
    public void paint(Graphics g) {
        g.setColor(Color.blue);
        g.drawRect(10, 10, 200, 100);
    }

    public void addRoom() {
        System.out.println("Room added to the plan!");
        repaint(); // Repaint the canvas
    }

    public void addFurniture() {
        System.out.println("Furniture added to the plan!");
        repaint();
    }

}
