import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import comps.*;

public class Plan extends Canvas {
    public List<Room> roomList;

    public Plan() {
        this.roomList = new ArrayList<Room>();
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
        //        super.paintComponent(g);
        for (Room room : roomList) {
            room.draw(g);
        }
    }

    public void addRoom(String roomType, Pos pos) {
        Room newRoom = new Room(roomType, new Dim(400, 400), pos);
        newRoom.checkOverlap();
        roomList.add(newRoom);
        System.out.println("Room added to the plan!");
        repaint(); // Repaint the canvas
        System.out.println(this.roomList);
    }

    public void addFurniture() {
        System.out.println("Furniture added to the plan!");
        repaint();
    }

}
