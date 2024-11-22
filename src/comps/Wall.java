package comps;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Wall implements Serializable {
    private static final int WALL_THICKNESS = 6;
    private static final Stroke NORMAL_STROKE = new BasicStroke(WALL_THICKNESS);
    private static final Stroke DASHED_STROKE = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);

    public Pos p1;
    public Pos p2;
    public boolean isRoomBoundary = false;
    public java.util.List<Room> adjRooms = new ArrayList<>();
    private List<Section> sections; // Segments of the wall

    public Wall(Pos p1, Pos p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.sections = new ArrayList<>();
        this.sections.add(new Section(0, calculateLength(), NORMAL_STROKE)); // Entire wall is initially normal
    }

    // Method to add a door at a specified position along the wall
    public void addDoor(int start, int width) throws OverlapException {
        addSegment(start, width, null); // Null stroke to skip the segment (door)
    }

    // Method to add a window at a specified position along the wall
    public void addWindow(int start, int width) throws OverlapException {
        if (!this.isRoomBoundary) {
            addSegment(start, width, DASHED_STROKE); // Dashed stroke for the window
        } else {
            System.out.println("NO!");
        }
    }

    // Adds a new segment to the wall by splitting existing sections
    private void addSegment(int start, int width, Stroke newStroke) throws OverlapException {
        List<Section> newSections = new ArrayList<>();
        int end = start + width;

        for (Section section : sections) {
            if (section.end <= start || section.start >= end) {
                newSections.add(section);
            } else {
                // Check for overlap with incompatible sections (e.g., door vs window)
                if (newStroke == DASHED_STROKE && section.stroke == null) {
                    throw new OverlapException("Cannot place a window on a door.");
                }
                if (newStroke == null && section.stroke == DASHED_STROKE) {
                    throw new OverlapException("Cannot place a door on a window.");
                }
                // Split the section into before, during, and after the new segment
                if (section.start < start) {
                    newSections.add(new Section(section.start, start, section.stroke));
                }
                if (newStroke != null) {
                    newSections.add(new Section(Math.max(start, section.start), Math.min(end, section.end), newStroke));
                }
                if (section.end > end) {
                    newSections.add(new Section(end, section.end, section.stroke));
                }
            }
        }
        this.sections = newSections;
    }


    // Calculate the total length of the wall
    private int calculateLength() {
        if (p1.y == p2.y) { // Horizontal wall
            return Math.abs(p2.x - p1.x);
        } else if (p1.x == p2.x) { // Vertical wall
            return Math.abs(p2.y - p1.y);
        }
        return 0; // Shouldn't happen for strictly horizontal/vertical walls
    }

    // Method to draw the wall
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        for (Section section : sections) {
            g2d.setStroke(section.stroke);
            if (p1.y == p2.y) { // Horizontal wall
                g2d.drawLine(p1.x + section.start, p1.y, p1.x + section.end, p1.y);
            } else if (p1.x == p2.x) { // Vertical wall
                g2d.drawLine(p1.x, p1.y + section.start, p1.x, p1.y + section.end);
            }
        }

        g2d.dispose();
    }

    // Represents a segment of the wall (normal, door, or window)
    private static class Section implements Serializable {
        int start;
        int end;
        transient Stroke stroke; // Mark Stroke as transient
        private String strokeType; // To store the type of stroke (e.g., "normal", "dashed")

        public Section(int start, int end, Stroke stroke) {
            this.start = start;
            this.end = end;
            this.stroke = stroke;
            this.strokeType = (stroke instanceof BasicStroke && ((BasicStroke) stroke).getDashArray() != null) ? "dashed" : "normal";
        }

        // Custom serialization to handle Stroke
        @Serial
        private void writeObject(ObjectOutputStream oos) throws IOException {
            oos.defaultWriteObject(); // Serialize other fields
        }

        // Custom deserialization to reinitialize Stroke
        @Serial
        private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
            ois.defaultReadObject(); // Deserialize other fields

            // Reinitialize Stroke based on strokeType
            if ("dashed".equals(strokeType)) {
                this.stroke = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
            } else {
                this.stroke = new BasicStroke(6); // Normal stroke
            }
        }
    }

}
