package comps;

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class Room {
    String roomType;
    Dim dim;
    Pos pos; //top-left(?)
    int roomId;

    public Room(String roomType, Dim dimensions, Pos pos) {
        this.roomType = roomType;
        this.dim = dimensions;
        this.pos = pos;
        roomId = 0;
    }

    public boolean checkOverlap() {
        return false;
    }

    public void draw(Graphics g) {
        HashMap<String, Color> roomColors = new HashMap<String, Color>();
        roomColors.put("bath", new Color(135, 206, 250));
        roomColors.put("living", new Color(255, 213, 71));
        roomColors.put("bedroom", new Color(155, 255, 75));
        roomColors.put("kitchen", new Color(255, 79, 79));
        roomColors.put("misc", new Color(222, 141, 255));

        g.setColor(Color.BLACK); // Room border color
        g.drawRect(pos.x, pos.y, dim.width, dim.height);
        g.setColor(roomColors.get(this.roomType)); // Light blue fill color for the room
        g.fillRect(pos.x, pos.y, dim.width, dim.height);

        // Draw room type label in the center of the room
        g.setColor(Color.BLACK);
        g.drawString(roomType, pos.x + dim.width / 4, pos.y + dim.height / 2);
    }
}

