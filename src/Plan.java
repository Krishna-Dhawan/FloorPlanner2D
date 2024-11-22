import java.awt.event.*;
import java.util.*;
import java.awt.*;
import java.util.List;
import comps.*;
import javax.swing.*;

public class Plan extends Canvas {
    public List<Room> roomList;
    public List<Furniture> furnitureList;
    public List<Wall> wallList;
    public Screen mediator;

    private Room selected_Room;
    private Furniture selected_Furniture;
    private Point initialPos;
    private Point dynamicPos;
    private boolean isDragging = false;

    public Plan(Screen mediator) {
        this.mediator = mediator;
        this.roomList = new ArrayList<>();
        this.furnitureList = new ArrayList<>();
        this.wallList = new ArrayList<>();
        setBackground(Color.WHITE);

        // Add the mouse listener
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e);
            }
            public void mouseReleased(MouseEvent e) {
                if (isDragging && (selected_Room != null || selected_Furniture != null)) {
                    try{
                        handleMouseRelease(e);
                    } catch (OverlapException ex) {
                        mediator.handleOverlapException(false, ex.getMessage());
                    }
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (isDragging && (selected_Room != null || selected_Furniture != null)) {
                    handleMouseDrag(e);
                }
            }
        });
    }

    // Handle mouse press event (right-click)
    private static final int WALL_THICKNESS_TOLERANCE = 6; // Tolerance for wall thickness

    // Check if the click is within a wall's thickness range
    private Wall findWallAtPosition(int x, int y) {
        for (Wall wall : wallList) {
            if (isNearWall(wall, x, y)) {
                return wall;
            }
        }
        return null;
    }

    // Helper method to check if (x, y) is near a given wall within tolerance
    private boolean isNearWall(Wall wall, int x, int y) {
        if (wall.p1.y == wall.p2.y) {  // Horizontal wall
            int minX = Math.min(wall.p1.x, wall.p2.x);
            int maxX = Math.max(wall.p1.x, wall.p2.x);
            return x >= minX && x <= maxX && Math.abs(y - wall.p1.y) <= WALL_THICKNESS_TOLERANCE;
        } else if (wall.p1.x == wall.p2.x) {  // Vertical wall
            int minY = Math.min(wall.p1.y, wall.p2.y);
            int maxY = Math.max(wall.p1.y, wall.p2.y);
            return y >= minY && y <= maxY && Math.abs(x - wall.p1.x) <= WALL_THICKNESS_TOLERANCE;
        }
        return false;
    }

    // Modify findRoomAtPosition to detect if a wall was clicked
    private Room findRoomAtPosition(int x, int y) {
        // Check if a wall is clicked, if so return null
        Wall clickedWall = findWallAtPosition(x, y);
        if (clickedWall != null) {
            System.out.println("Wall clicked: " + clickedWall);
            return null;
        }

        // If no wall is clicked, continue with room detection
        Room foundRoom = null;
        for (Room room : roomList) {
            if (room.pos.x < x && x < room.pos.x + room.dim.width &&
                    room.pos.y < y && y < room.pos.y + room.dim.height) {
                System.out.println(room);
                foundRoom = room;
            }
        }

        // Check if furniture is clicked, if so return null
        Furniture foundFurniture = findFurnitureAtPosition(x, y);
        if (foundFurniture != null) {
            return null;
        }
        return foundRoom;
    }

    // Modify handleMousePressed to show wall options if a wall is clicked
    private void handleMousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        System.out.println(x + " " + y);

        if (SwingUtilities.isRightMouseButton(e)) {
            Room selectedRoom = findRoomAtPosition(x, y);
            if (selectedRoom != null) {
                showRoomOptionsMenu(selectedRoom, x, y);
            } else {
                Wall selectedWall = findWallAtPosition(x, y);
                if (selectedWall != null) {
                    showWallOptionsMenu(selectedWall, x, y); // Implement wall options menu
                } else {
                    Furniture selectedFurniture = findFurnitureAtPosition(x, y);
                    if (selectedFurniture != null) {
                        showFurnitureOptionsMenu(selectedFurniture, x, y);
                    }
                }
            }
        } else if (SwingUtilities.isLeftMouseButton(e) && (selected_Room != null || selected_Furniture != null)) {
            dynamicPos = e.getPoint();
            isDragging = true;
        }
    }


    private void handleMouseDrag(MouseEvent e) {
        int xMoved = e.getX() - dynamicPos.x;
        int yMoved = e.getY() - dynamicPos.y;

        if (selected_Room != null) {
            selected_Room.pos.x += xMoved;
            selected_Room.pos.y += yMoved;
        } else {
            selected_Furniture.pos.x += xMoved;
            selected_Furniture.pos.y += yMoved;
        }

        dynamicPos = e.getPoint();

        repaint();
    }

    private void handleMouseRelease(MouseEvent e) throws OverlapException{
        isDragging = false;
        if (selected_Room != null) {
            java.util.List<Room> excludedRoomList = new java.util.ArrayList<>();
            for (Room room : roomList) {
                if (room != selected_Room) {
                    excludedRoomList.add(room);
                }
            }
            if (selected_Room.checkOverlap(excludedRoomList)) {
                throw new OverlapException("Overlapping room");
            }
            addWallsAround(selected_Room);
            repaint();
            selected_Room = null;
        } else {
            java.util.List<Furniture> excludedFurnitureList = new java.util.ArrayList<>();
            for (Furniture furniture : furnitureList) {
                if (furniture != selected_Furniture) {
                    excludedFurnitureList.add(furniture);
                }
            }
            if (selected_Furniture.checkOverlap(excludedFurnitureList)) {
                throw new OverlapException("Overlapping furniture");
            }
            if (selected_Furniture.checkWallOverlap(wallList)) {
                throw new OverlapException("Furniture overlaps with wall");
            }
        }
    }

    public void snapBack() {
        if (selected_Room != null) {
            selected_Room.pos = new Pos(initialPos.x, initialPos.y);
            addWallsAround(selected_Room);
            selected_Room = null;
        } else {
            selected_Furniture.pos = new Pos(initialPos.x, initialPos.y);
            selected_Furniture = null;
        }
        repaint();
    }

    private Furniture findFurnitureAtPosition(int x, int y) {
        for (Furniture furniture : furnitureList) {
            if (furniture.pos.x < x && x < furniture.pos.x + furniture.dim.width &&
                    furniture.pos.y < y && y < furniture.pos.y + furniture.dim.height) {
                System.out.println(furniture);
                return furniture;
            }
        }
        return null;
    }

    // Show the popup menu with options related to the selected room on right-click
    private void showRoomOptionsMenu(Room selectedRoom, int x, int y) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem addRelativeRoom = new JMenuItem("Add Room Relative to this");
        JMenuItem moveRoom = new JMenuItem("Move Room");
        JMenuItem removeRoom = new JMenuItem("Remove this Room");

        addRelativeRoom.addActionListener(ev -> showAddRoomDialog(selectedRoom));
        popupMenu.add(addRelativeRoom);

        moveRoom.addActionListener(ev -> {selected_Room = selectedRoom; initialPos = new Point(selectedRoom.pos.x, selectedRoom.pos.y);});
        popupMenu.add(moveRoom);

        removeRoom.addActionListener(ev -> removeRoom(selectedRoom));
        popupMenu.add(removeRoom);

        showPopupMenu(popupMenu, x, y);
    }

    private void showFurnitureOptionsMenu(Furniture selectedFurniture, int x, int y) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem moveFurnitureOption = new JMenuItem("Move Furniture");
        JMenuItem resizeFurnitureOption = new JMenuItem("Resize Furniture");
        JMenuItem removeFurnitureOption = new JMenuItem("Remove Furniture");

        moveFurnitureOption.addActionListener(ev -> {selected_Furniture = selectedFurniture; initialPos = new Point(selectedFurniture.pos.x, selectedFurniture.pos.y);});
        popupMenu.add(moveFurnitureOption);

        resizeFurnitureOption.addActionListener(ev -> showResizeFurnitureDialog(selectedFurniture));
        popupMenu.add(resizeFurnitureOption);

        removeFurnitureOption.addActionListener(ev -> removeFurniture(selectedFurniture));
        popupMenu.add(removeFurnitureOption);

        showPopupMenu(popupMenu, x, y);
    }

    private void showWallOptionsMenu (Wall selectedWall, int x, int y) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem addWinOption = new JMenuItem("Add Window");
        JMenuItem addDoorOption = new JMenuItem("Add Door");

        addWinOption.addActionListener(ev -> addSection(selectedWall, x, y, "win"));
        popupMenu.add(addWinOption);
        addDoorOption.addActionListener(ev -> addSection(selectedWall, x, y, "door"));
        popupMenu.add(addDoorOption);

        showPopupMenu(popupMenu, x, y);
    }

    // Show a dialog to add a room relative to the selected room
    private void showAddRoomDialog(Room selectedRoom) {
        JDialog dialog = new JDialog(mediator);
        dialog.setTitle("Add Room Relative to this");

        ButtonGroup roomType = new ButtonGroup();
        JRadioButton livingRoom = new JRadioButton("Living Room");
        JRadioButton kitchen = new JRadioButton("Kitchen");
        JRadioButton bedroom = new JRadioButton("Bedroom");
        JRadioButton bathroom = new JRadioButton("Bathroom");
        JRadioButton misc = new JRadioButton("Misc. Room");

        roomType.add(livingRoom);
        roomType.add(kitchen);
        roomType.add(bedroom);
        roomType.add(bathroom);
        roomType.add(misc);

        JLabel hl = new JLabel("Height: ");
        JTextField h = new JTextField(10);
        JLabel wl = new JLabel("Width: ");
        JTextField w = new JTextField(10);

        JComboBox<String> directions = new JComboBox<>(new String[]{"North", "South", "East", "West"});

        JButton addRoomButton = new JButton("Add Room");
        addRoomButton.addActionListener(ev -> {
            String selectedRoomType = getSelectedRoomType(livingRoom, kitchen, bedroom, bathroom, misc);
            if (selectedRoomType.isEmpty()) {
                System.out.println("No room selected");
                return;
            }

            Pos newPos = calculateNewRoomPosition(selectedRoom, directions, w, h);
            try {
                addRoom(selectedRoomType, newPos, new Dim(Integer.parseInt(w.getText()), Integer.parseInt(h.getText())));
                dialog.dispose();
            } catch (OverlapException ex) {
                mediator.handleOverlapException(true, ex.getMessage());
            }
        });

        // Add components to the dialog
        // TODO: Remove the flow layout and add setBounds
        dialog.setLayout(new FlowLayout());
        dialog.add(livingRoom);
        dialog.add(kitchen);
        dialog.add(bedroom);
        dialog.add(bathroom);
        dialog.add(misc);
        dialog.add(hl);
        dialog.add(h);
        dialog.add(wl);
        dialog.add(w);
        dialog.add(directions);
        dialog.add(addRoomButton);
        dialog.setSize(300, 300);
        dialog.setVisible(true);
    }

    // Get the selected room type
    private String getSelectedRoomType(JRadioButton livingRoom, JRadioButton kitchen, JRadioButton bedroom,
                                       JRadioButton bathroom, JRadioButton misc) {
        if (livingRoom.isSelected()) return "living";
        if (kitchen.isSelected()) return "kitchen";
        if (bedroom.isSelected()) return "bedroom";
        if (bathroom.isSelected()) return "bath";
        if (misc.isSelected()) return "misc";
        return "";
    }

    // Calculate the new room position based on the direction and dimensions
    private Pos calculateNewRoomPosition(Room selectedRoom, JComboBox<String> directions, JTextField w, JTextField h) {
        return switch (directions.getSelectedIndex()) {
            case 0 -> new Pos(selectedRoom.pos.x, selectedRoom.pos.y - Integer.parseInt(h.getText()));  // North
            case 1 -> new Pos(selectedRoom.pos.x, selectedRoom.pos.y + selectedRoom.dim.height);        // South
            case 2 -> new Pos(selectedRoom.pos.x + selectedRoom.dim.width, selectedRoom.pos.y);         // East
            case 3 -> new Pos(selectedRoom.pos.x - Integer.parseInt(w.getText()), selectedRoom.pos.y);  // West
            default -> new Pos(0, 0);
        };
    }

    private void addSection(Wall selectedWall, int x, int y, String type) {
        if (type.equals("win") && selectedWall.isRoomBoundary) {
            JOptionPane.showMessageDialog(this, "Cannot add window to a room boundary wall");
            return;
        }
        JDialog addWinDialog = new JDialog(mediator);
        addWinDialog.setTitle("Add " + ((type.equals("win"))? "Window": "Door"));
        JLabel wl = new JLabel("Width: ");
        JTextField w = new JTextField(10); // Input field for window width
        JButton ok = new JButton("OK");

        ok.addActionListener(ev -> {
            try {
                int width = Integer.parseInt(w.getText()); // Get width from user input

                // Calculate the start position relative to the wall
                int start = calculateStartPosition(selectedWall, x, y);

                // Add the window to the wall
                try {
                    if (type.equals("win")) {
                        selectedWall.addWindow(start, width);
                    } else {
                        selectedWall.addDoor(start, width);
                    }
                } catch (OverlapException ex) {
                    mediator.handleOverlapException(true, ex.getMessage());
                }
                repaint();
                // Close the dialog
                addWinDialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addWinDialog, "Invalid width. Please enter a number.");
            }
        });

        // Layout the dialog
        addWinDialog.setLayout(new FlowLayout());
        addWinDialog.add(wl);
        addWinDialog.add(w);
        addWinDialog.add(ok);
        addWinDialog.setSize(300, 150);
        addWinDialog.setVisible(true);
    }

    // Helper function to calculate the start position relative to the wall
    private int calculateStartPosition(Wall wall, int x, int y) {
        if (wall.p1.y == wall.p2.y) { // Horizontal wall
            return Math.abs(x - wall.p1.x);
        } else if (wall.p1.x == wall.p2.x) { // Vertical wall
            return Math.abs(y - wall.p1.y);
        }
        return 0; // Default case (shouldn't occur for strictly horizontal/vertical walls)
    }

    public void showPopupMenu(JPopupMenu popupMenu, int x, int y) {
        popupMenu.show(this, x, y);
    }

    public void fetchRoomList(List<Room> roomList) {
        this.roomList = roomList;
        repaint();
    }
    public void fetchFurnitureList(List<Furniture> furnitureList) {
        this.furnitureList = furnitureList;
        repaint();
    }
    public void fetchWallList(List<Wall> wallList) {
        this.wallList = wallList;
        repaint();
    }

    public void paint(Graphics g) {
        // super.paintComponent(g);
        for (Room room : roomList) {
            room.draw(g);
        }
        for (Furniture furniture: furnitureList) {
            furniture.draw(g);
        }
        java.util.Iterator<Wall> iterator = wallList.iterator();
        while (iterator.hasNext()) {
            Wall wall = iterator.next();
            boolean isAdjacent = false;
            // Check if the wall is adjacent to any room
            // also, account for cases where a room with larger wall was deleted
            // which was adjacent to a room with smaller edge
            Pos cP1 = new Pos(0, 0);
            Pos cP2 = new Pos(0, 0);
            for (Room room : roomList) {
                if ((wall.p1.x == room.pos.x && wall.p2.x == room.pos.x && wall.p1.y < room.pos.y + room.dim.height && wall.p2.y > room.pos.y) ||  // Left wall
                        (wall.p1.x == room.pos.x + room.dim.width && wall.p2.x == room.pos.x + room.dim.width && wall.p1.y < room.pos.y + room.dim.height && wall.p2.y > room.pos.y) ||  // Right wall
                        (wall.p1.y == room.pos.y && wall.p2.y == room.pos.y && wall.p1.x < room.pos.x + room.dim.width && wall.p2.x > room.pos.x) ||  // Top wall
                        (wall.p1.y == room.pos.y + room.dim.height && wall.p2.y == room.pos.y + room.dim.height && wall.p1.x < room.pos.x + room.dim.width && wall.p2.x > room.pos.x)  // Bottom wall
                ) {
                    isAdjacent = true;
                    Pos tP1 = new Pos(0, 0);
                    Pos tP2 = new Pos(0, 0);
                    if (wall.p1.x == room.pos.x && wall.p2.x == room.pos.x && wall.p1.y < room.pos.y + room.dim.height) {
                        tP1 = new Pos(room.pos.x, room.pos.y);
                        tP2 = new Pos(room.pos.x, room.pos.y + room.dim.height);
                    } else if (wall.p1.x == room.pos.x + room.dim.width && wall.p2.x == room.pos.x + room.dim.width && wall.p1.y < room.pos.y + room.dim.height && wall.p2.y > room.pos.y) {
                        tP1 = new Pos(room.pos.x + room.dim.width, room.pos.y);
                        tP2 = new Pos(room.pos.x + room.dim.width, room.pos.y + room.dim.height);
                    } else if (wall.p1.y == room.pos.y && wall.p2.y == room.pos.y && wall.p1.x < room.pos.x + room.dim.width) {
                        tP1 = new Pos(room.pos.x, room.pos.y);
                        tP2 = new Pos(room.pos.x + room.dim.width, room.pos.y);
                    } else if (wall.p1.y == room.pos.y + room.dim.height && wall.p2.y == room.pos.y + room.dim.height && wall.p1.x < room.pos.x + room.dim.width && wall.p2.x > room.pos.x) {
                        tP1 = new Pos(room.pos.x, room.pos.y + room.dim.height);
                        tP2 = new Pos(room.pos.x + room.dim.width, room.pos.y + room.dim.height);
                    }
                    if (getLength(tP1, tP2) > getLength(cP1, cP2)) {
                        cP1 = tP1;
                        cP2 = tP2;
                        wall.p1 = cP1;
                        wall.p2 = cP2;
                    }
                }
            }
            if (!isAdjacent) {
                iterator.remove();
            }
        }
        for (Room room : roomList) {
            int leftCount = 0;
            int topCount = 0;
            int rightCount = 0;
            int bottomCount = 0;

            java.util.Iterator<Wall> iterator2 = wallList.iterator();
            while (iterator2.hasNext()) {
                Wall wall = iterator2.next();
                if (wall.p1.x == room.pos.x && wall.p2.x == room.pos.x &&
                        wall.p1.y >= room.pos.y && wall.p2.y <= room.pos.y + room.dim.height) {
                    leftCount++;
                    if (leftCount > 1) {
                        iterator2.remove();
                    }
                } else if (wall.p1.x == room.pos.x + room.dim.width && wall.p2.x == room.pos.x + room.dim.width &&
                        wall.p1.y >= room.pos.y && wall.p2.y <= room.pos.y + room.dim.height) {
                    rightCount++;
                    if (rightCount > 1) {
                        iterator2.remove();
                    }
                } else if (wall.p1.y == room.pos.y && wall.p2.y == room.pos.y &&
                        wall.p1.x >= room.pos.x && wall.p2.x <= room.pos.x + room.dim.width) {
                    topCount++;
                    if (topCount > 1) {
                        iterator2.remove();
                    }
                } else if (wall.p1.y == room.pos.y + room.dim.height && wall.p2.y == room.pos.y + room.dim.height &&
                        wall.p1.x >= room.pos.x && wall.p2.x <= room.pos.x + room.dim.width) {
                    bottomCount++;
                    if (bottomCount > 1) {
                        iterator2.remove();
                    }
                }
            }
        }
        for (Wall wall: wallList) {
            wall.draw(g);
        }
        System.out.println(wallList);
    }

    private int getLength(Pos p1, Pos p2) {
        return (int) Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
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

    private Wall createBoundaryWall(Pos start, Pos end) {
        Wall wall = new Wall(start, end);
        wall.isRoomBoundary = false; // Default to not a boundary
        return wall;
    }
    private void addWallsAround(Room room) {
        // top
        addOrUpdateWall(new Pos(room.pos.x, room.pos.y), new Pos(room.pos.x + room.dim.width, room.pos.y), room);
        // left
        addOrUpdateWall(new Pos(room.pos.x, room.pos.y), new Pos(room.pos.x, room.pos.y + room.dim.height), room);
        // bottom
        addOrUpdateWall(new Pos(room.pos.x, room.pos.y + room.dim.height), new Pos(room.pos.x + room.dim.width, room.pos.y + room.dim.height), room);
        // right
        addOrUpdateWall(new Pos(room.pos.x + room.dim.width, room.pos.y), new Pos(room.pos.x + room.dim.width, room.pos.y + room.dim.height), room);
    }

    // Adds a wall or updates an existing one, adjusting the isRoomBoundary property
    private void addOrUpdateWall(Pos start, Pos end, Room room) {
        Wall existingWall = findWallAtPosition(start, end);
        if (existingWall != null) {
            // Wall already exists, update it to be a room boundary
            existingWall.isRoomBoundary = true;
        } else {
            Wall newWall = createBoundaryWall(start, end);
            wallList.add(newWall);
        }
    }

    // Helper to find an existing wall between two positions
    private Wall findWallAtPosition(Pos start, Pos end) {
        Pos normalizedStart = normalizePos(start);
        Pos normalizedEnd = normalizePos(end);

        for (Wall wall : wallList) {
            if ((wall.p1.equals(normalizedStart) && wall.p2.equals(normalizedEnd)) ||
                    (wall.p1.equals(normalizedEnd) && wall.p2.equals(normalizedStart))) {
                return wall; // Return the existing wall
            }
        }
        return null; // No wall found
    }

    // Normalize a position to ensure consistent order (e.g., p1 is "smaller")
    private Pos normalizePos(Pos pos) {
        return new Pos(Math.min(pos.x, pos.y), Math.max(pos.x, pos.y));
    }

    private void createRoom(String roomType, Pos pos, Dim dim) throws OverlapException {
        Room newRoom = new Room(roomType, dim, pos);
        if (newRoom.checkOverlap(roomList)) {
            throw new OverlapException("Overlapping room");
        }
        roomList.add(newRoom);
        System.out.println("Room added to the plan!");

        addWallsAround(newRoom);

        repaint();
        mediator.canvasPanelAction("Add Room", newRoom);
    }

    public void addRoom(String roomType, Pos pos) throws OverlapException {
        Dim dim = new Dim(150, 150);
        createRoom(roomType, pos, dim);
    }
    public void addRoom(String roomType, Pos pos, Dim dim) throws OverlapException {
        createRoom(roomType, pos, dim);
    }
    public void addRoom(String roomType) throws OverlapException {
        Pos pos = findSpace();
        Dim dim = new Dim(150, 150);
        createRoom(roomType, pos, dim);
    }
    public void addRoom(String roomType, Dim dim) throws OverlapException {
        Pos pos = findSpace();
        createRoom(roomType, pos, dim);
    }

    public void removeRoom(Room remRoom) {
        roomList.remove(remRoom);
        mediator.canvasPanelAction("Remove Room", remRoom);
        repaint();
    }

    public void removeFurniture(Furniture remFurniture) {
        furnitureList.remove(remFurniture);
        mediator.canvasPanelAction("Remove Furniture", remFurniture);
        repaint();
    }

    public void addFurniture(String type, String[] vals) throws OverlapException {
        System.out.println("Furniture added to the plan!");
        Dim dim = (!vals[3].isEmpty() && !vals[4].isEmpty())? new Dim(Integer.parseInt(vals[3]), Integer.parseInt(vals[2])): new Dim(50, 50);
        Pos pos = new Pos(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]));
        int rotation = Integer.parseInt(vals[4]);
        Furniture newFurniture = new Furniture(type, dim, pos, rotation, roomList);

        if (newFurniture.checkWallOverlap(wallList)) {
            throw new OverlapException("Furniture overlaps with wall");
        }
        furnitureList.add(newFurniture);
        repaint();
    }

    private void resizeFurniture(Furniture furniture, int h, int w) throws OverlapException{
        furniture.dim.height = h;
        furniture.dim.width = w;
        java.util.List<Furniture> excludedFurnitureList = new ArrayList<>();
        for (Furniture f : furnitureList) {
            if (f != furniture) {
                excludedFurnitureList.add(f);
            }
        }
        if (furniture.checkOverlap(excludedFurnitureList)) {
            throw new OverlapException("Overlap Error");
        }
        repaint();
    }

    private void showResizeFurnitureDialog(Furniture furniture) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Resize Furniture");

        JLabel heightLabel = new JLabel("Height:");
        JTextField heightField = new JTextField(String.valueOf(furniture.dim.height), 10);
        JLabel widthLabel = new JLabel("Width:");
        JTextField widthField = new JTextField(String.valueOf(furniture.dim.width), 10);

        JButton resizeButton = new JButton("Resize");
        resizeButton.addActionListener(ev -> {
            try {
                int h = Integer.parseInt(heightField.getText());
                int w = Integer.parseInt(widthField.getText());
                resizeFurniture(furniture, h, w);
            } catch (OverlapException ex) {
                mediator.handleOverlapException(false, ex.getMessage());
            }
            dialog.dispose();
        });

        dialog.add(heightLabel);
        dialog.add(heightField);
        dialog.add(widthLabel);
        dialog.add(widthField);
        dialog.add(resizeButton);

        dialog.setLayout(new FlowLayout());
        dialog.setSize(300, 200);
        dialog.setVisible(true);
    }

}
