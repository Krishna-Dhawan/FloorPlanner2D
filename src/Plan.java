import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import comps.*;
import javax.swing.*;

public class Plan extends Canvas {
    public List<Room> roomList;
    public Screen mediator;

    public Plan(Screen mediator) {
        this.mediator = mediator;
        this.roomList = new ArrayList<>();
        setBackground(Color.WHITE);

        // Add the mouse listener
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e);
            }
        });
    }

    // Handle mouse press event (right-click)
    private void handleMousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        System.out.println(x + " " + y);

        if (SwingUtilities.isRightMouseButton(e)) {
            Room selectedRoom = findRoomAtPosition(x, y);
            if (selectedRoom != null) {
                showRoomOptionsMenu(selectedRoom, x, y);
            }
        }
    }

    // Find a room at the clicked position
    private Room findRoomAtPosition(int x, int y) {
        for (Room room : roomList) {
            if (room.pos.x < x && x < room.pos.x + room.dim.width &&
                    room.pos.y < y && y < room.pos.y + room.dim.height) {
                System.out.println(room);
                return room;
            }
        }
        return null;
    }

    // Show the popup menu with options related to the selected room on right-click
    private void showRoomOptionsMenu(Room selectedRoom, int x, int y) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem addRelativeRoom = new JMenuItem("Add Room Relative to this");
        JMenuItem removeRoom = new JMenuItem("Remove this Room");

        addRelativeRoom.addActionListener(ev -> showAddRoomDialog(selectedRoom));
        popupMenu.add(addRelativeRoom);

        removeRoom.addActionListener(ev -> removeRoom(selectedRoom));
        popupMenu.add(removeRoom);

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
                mediator.handleOverlapException();
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
    public void showPopupMenu(JPopupMenu popupMenu, int x, int y) {
        popupMenu.show(this, x, y);
    }

    public void fetchRoomList(List<Room> roomList) {
        this.roomList = roomList;
        repaint();
    }

    public void paint(Graphics g) {
        // super.paintComponent(g);
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

    private void createRoom(String roomType, Pos pos, Dim dim) throws OverlapException {
        Room newRoom = new Room(roomType, dim, pos);
        if (newRoom.checkOverlap(roomList)) {
            throw new OverlapException("Overlapping room");
        }
        roomList.add(newRoom);
        System.out.println("Room added to the plan!");
        repaint();
        mediator.canvasPanelAction("Add Room", newRoom);
        System.out.println(this.roomList);
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

    public void addFurniture() {
        System.out.println("Furniture added to the plan!");
        repaint();
    }

}
