import java.util.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import comps.*;

public class Plan extends Canvas {
    public List<Room> roomList;

    public Plan() {
        this.roomList = new ArrayList<>();
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

    // find the next free space according to row-major order
    // only works when everything is in row-major order
    public Pos findSpace() {
        int xMax = 50;
        int yMax = 50;
        int rowHeight = 0;
        for (Room room : roomList) {
            xMax += room.dim.width;
            if(xMax + room.dim.width > 1300) {
                xMax = 50;
                yMax += rowHeight;
                rowHeight = 0;
            }
            if (room.dim.height > rowHeight) {
                rowHeight = room.dim.height;
            }
        }
        return new Pos(xMax, yMax);
    }

    public void addRoom(String roomType, Pos pos) throws OverlapException {
        Room newRoom = new Room(roomType, new Dim(150, 150), pos);
        if (newRoom.checkOverlap(roomList)) {
            throw new OverlapException("Overlapping room");
        } else {
            System.out.println("not overlapping");
        }
        roomList.add(newRoom);
        System.out.println("Room added to the plan!");
        repaint(); // Repaint the canvas
        System.out.println(this.roomList);
    }
    public void addRoom(String roomType, Pos pos, Dim dim) throws OverlapException {
        Room newRoom = new Room(roomType, dim, pos);
        if (newRoom.checkOverlap(roomList)) {
            throw new OverlapException("Overlapping room");
        } else {
            System.out.println("not overlapping");
        }
        roomList.add(newRoom);
        System.out.println("Room added to the plan!");
        repaint(); // Repaint the canvas
        System.out.println(this.roomList);
    }
    public void addRoom(String roomType) throws OverlapException {
        Pos pos = findSpace();
        Room newRoom = new Room(roomType, new Dim(150, 150), pos);
        if (newRoom.checkOverlap(roomList)) {
            throw new OverlapException("Overlapping room");
        } else {
            System.out.println("not overlapping");
        }
        roomList.add(newRoom);
        System.out.println("Room added to the plan!");
        repaint(); // Repaint the canvas
        System.out.println(this.roomList);
    }
    public void addRoom(String roomType, Dim dim) throws OverlapException {
        Pos pos = findSpace();
        Room newRoom = new Room(roomType, dim, pos);
        if (newRoom.checkOverlap(roomList)) {
            throw new OverlapException("Overlapping room");
        } else {
            System.out.println("not overlapping");
        }
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
