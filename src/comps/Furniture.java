package comps;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

public class Furniture implements Serializable {
    public String furnitureType;
    public Room room;
    public Dim dim;
    public Pos pos;
    public int rotation;
    private static final HashMap<String, Image> furnitureImages = new HashMap<>();

    public Furniture (String furnitureType, Dim dim, Pos pos, int rotation, java.util.List<Room> roomList) {
        this.furnitureType = furnitureType;
        this.dim = dim;
        this.pos = pos;
        this.rotation = rotation;
        try {
            furnitureImages.put("bed", ImageIO.read(new File("./Images/Furnitures/bed.png")));
            furnitureImages.put("chair", ImageIO.read(new File("./Images/Furnitures/chair.png")));
            furnitureImages.put("dining_set", ImageIO.read(new File("./Images/Furnitures/dinig_set.png")));
            furnitureImages.put("sofa", ImageIO.read(new File("./Images/Furnitures/sofa.png")));
            furnitureImages.put("table", ImageIO.read(new File("./Images/Furnitures/table.png")));
        } catch (IOException e) {
            System.out.println("Error reading furniture images");
        }
        decideRoom(roomList);
    }

    public void decideRoom(java.util.List<Room> roomList) {
        for (Room room : roomList) {
            if ((room.pos.x <= this.pos.x && this.pos.x <= room.pos.x + room.dim.width) &&
                    room.pos.y <= this.pos.y && this.pos.y <= room.pos.y + room.dim.height) {
                this.room = room;
            }
        }
    }

    public boolean checkOverlap(java.util.List<Furniture> furnitureList) {
        for (Furniture furniture : furnitureList) {
            if (!((this.pos.x + this.dim.width <= furniture.pos.x) ||
                    (this.pos.x >= furniture.pos.x + furniture.dim.width) ||
                    (this.pos.y + this.dim.height <= furniture.pos.y) ||
                    (this.pos.y >= furniture.pos.y + furniture.dim.height))) {
                return true;
            }
        }
        return false;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();

        // Calculate the center of the image
        int centerX = this.pos.x + this.dim.width / 2;
        int centerY = this.pos.y + this.dim.height / 2;

        // Convert degrees to radians and apply rotation around the center
        double radians = Math.toRadians(rotation);
        g2d.rotate(radians, centerX, centerY);
        g2d.drawImage(furnitureImages.get(this.furnitureType), this.pos.x, this.pos.y, this.dim.width, this.dim.height, null);
        g2d.setTransform(originalTransform);
    }
}
