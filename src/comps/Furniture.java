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
    public boolean checkWallOverlap(java.util.List<Wall> wallList) {
        for (Wall wall : wallList) {
            // Check if furniture overlaps with wall
            if (wall.p1.x == wall.p2.x) { // Vertical wall
                if (this.pos.x < wall.p1.x && this.pos.x + this.dim.width > wall.p1.x &&
                        this.pos.y < Math.max(wall.p1.y, wall.p2.y) &&
                        this.pos.y + this.dim.height > Math.min(wall.p1.y, wall.p2.y)) {
                    return true;
                }
            } else if (wall.p1.y == wall.p2.y) { // Horizontal wall
                if (this.pos.y < wall.p1.y && this.pos.y + this.dim.height > wall.p1.y &&
                        this.pos.x < Math.max(wall.p1.x, wall.p2.x) &&
                        this.pos.x + this.dim.width > Math.min(wall.p1.x, wall.p2.x)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void draw(Graphics g) {
        try {
            furnitureImages.put("bed", ImageIO.read(new File("./Images/Furnitures/bed.png")));
            furnitureImages.put("chair", ImageIO.read(new File("./Images/Furnitures/chair.png")));
            furnitureImages.put("dining_set", ImageIO.read(new File("./Images/Furnitures/dinig_set.png")));
            furnitureImages.put("sofa", ImageIO.read(new File("./Images/Furnitures/sofa.png")));
            furnitureImages.put("table", ImageIO.read(new File("./Images/Furnitures/table.png")));
            furnitureImages.put("commode", ImageIO.read(new File("./Images/Furnitures/commode.png")));
            furnitureImages.put("closet", ImageIO.read(new File("./Images/Furnitures/closet.png")));
            furnitureImages.put("shower", ImageIO.read(new File("./Images/Furnitures/shower.png")));
            furnitureImages.put("kit_sink", ImageIO.read(new File("./Images/Furnitures/kitchensink.png")));
            furnitureImages.put("stove", ImageIO.read(new File("./Images/Furnitures/stove.png")));
        } catch (IOException e) {
            System.out.println("Error reading furniture images");
        }
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
