package comps;

import java.awt.*;
import javax.swing.*;
import java.io.Serializable;
import java.util.*;

public class Room implements Serializable {
    public String roomType;
    public Dim dim;
    public Pos pos; //top-left(?)
    int roomId;

    public Room(String roomType, Dim dimensions, Pos pos) {
        this.roomType = roomType;
        this.dim = dimensions;
        this.pos = pos;
        roomId = 0;
    }

    // TODO: Test this
    public boolean checkOverlap(java.util.List<Room> roomList) {
        for (Room room : roomList) {
            if (!((this.pos.x + this.dim.width <= room.pos.x) ||
                    (this.pos.x >= room.pos.x + room.dim.width) ||
                    (this.pos.y + this.dim.height <= room.pos.y) ||
                    (this.pos.y >= room.pos.y + room.dim.height))) {
                return true;
            }
        }
        return false;
    }

    public void draw(Graphics g) {
        HashMap<String, Color> roomColors = new HashMap<String, Color>();
        roomColors.put("bath", new Color(135, 206, 250));
        roomColors.put("living", new Color(255, 213, 71));
        roomColors.put("bedroom", new Color(155, 255, 75));
        roomColors.put("kitchen", new Color(255, 79, 79));
        roomColors.put("misc", new Color(222, 141, 255));

        Graphics2D g1 = (Graphics2D) g;
        g.setColor(Color.BLACK);
        g1.setStroke(new BasicStroke(5));
        g.drawRect(pos.x, pos.y, dim.width, dim.height);
        g.setColor(roomColors.get(this.roomType));
        g.fillRect(pos.x, pos.y, dim.width, dim.height);

        // Draw room type label in the center of the room
        g.setColor(Color.BLACK);
        g.drawString(roomType, pos.x + dim.width / 4, pos.y + dim.height / 2);
    }
}

