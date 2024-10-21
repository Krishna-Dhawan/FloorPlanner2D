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

        // check mouse click
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println(x + " " + y);

                // check if it is right click, and which room is clicked
                if (SwingUtilities.isRightMouseButton(e)) {
                    Room selectedRoom = null;
                    for (Room room : roomList) {
                        if (room.pos.x < x && x < room.pos.x + room.dim.width && room.pos.y < y && y < room.pos.y + room.dim.height) {
                            System.out.println(room);
                            selectedRoom = room;
                        }
                    }
                    if (selectedRoom != null) {
                        JPopupMenu popupMenu = new JPopupMenu();
                        JMenuItem addRelativeRoom = new JMenuItem("Add Room Relative to this");

                        // for adding room with position relative to other
                        Room finalSelectedRoom = selectedRoom;
                        addRelativeRoom.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                JDialog dialog = new JDialog(mediator);
                                dialog.setTitle("Add Room Relative to this");

                                ButtonGroup roomType = new ButtonGroup();
                                JRadioButton livingRoom = new JRadioButton("Living Room");
                                roomType.add(livingRoom);
                                JRadioButton kitchen = new JRadioButton("Kitchen");
                                roomType.add(kitchen);
                                JRadioButton Bedroom = new JRadioButton("Bedroom");
                                roomType.add(Bedroom);
                                JRadioButton Bathroom = new JRadioButton("Bathroom");
                                roomType.add(Bathroom);
                                JRadioButton misc = new JRadioButton("Misc. Room");
                                roomType.add(misc);

                                JLabel hl = new JLabel("Height: ");
                                JTextField h = new JTextField(10);
                                JLabel wl = new JLabel("Width: ");
                                JTextField w = new JTextField(10);

                                JComboBox<String> news = new JComboBox<String>(new String[]{"North", "South", "East", "West"});

                                JButton addRoomButton = new JButton("Add Room");
                                addRoomButton.addActionListener( ev -> {
                                    String selectedRoomType = "";
                                    if (livingRoom.isSelected()) {
                                        selectedRoomType = "living";
                                    } else if (kitchen.isSelected()) {
                                        selectedRoomType = "kitchen";
                                    } else if (Bedroom.isSelected()) {
                                        selectedRoomType = "bedroom";
                                    } else if (Bathroom.isSelected()) {
                                        selectedRoomType = "bath";
                                    } else if (misc.isSelected()) {
                                        selectedRoomType = "misc";
                                    } else {
                                        System.out.println("No room selected");
                                    }
                                    Pos newPos = switch (news.getSelectedIndex()) {
                                        case 0 -> {
                                            news.setSelectedIndex(0);
                                            yield new Pos(finalSelectedRoom.pos.x, finalSelectedRoom.pos.y - Integer.parseInt(h.getText()));
                                        }
                                        case 1 -> {
                                            news.setSelectedIndex(1);
                                            yield new Pos(finalSelectedRoom.pos.x, finalSelectedRoom.pos.y + finalSelectedRoom.dim.height);
                                        }
                                        case 2 -> {
                                            news.setSelectedIndex(2);
                                            yield new Pos(finalSelectedRoom.pos.x + finalSelectedRoom.dim.width, finalSelectedRoom.pos.y);
                                        }
                                        case 3 -> {
                                            news.setSelectedIndex(3);
                                            yield new Pos(finalSelectedRoom.pos.x - Integer.parseInt(w.getText()), finalSelectedRoom.pos.y);
                                        }
                                        default -> new Pos(0, 0);
                                    };
                                    try {
                                        addRoom(selectedRoomType, newPos, new Dim(Integer.parseInt(w.getText()), Integer.parseInt(h.getText())));
                                    } catch (OverlapException ex) {
                                        mediator.handleOverlapException();
                                    }
                                    dialog.dispose();
                                });

                                // TODO: Remove the layout and add setBounds to each element
                                dialog.add(livingRoom);
                                dialog.add(kitchen);
                                dialog.add(Bedroom);
                                dialog.add(Bathroom);
                                dialog.add(misc);
                                dialog.add(hl);
                                dialog.add(h);
                                dialog.add(wl);
                                dialog.add(w);
                                dialog.add(news);
                                dialog.add(addRoomButton);
                                dialog.setVisible(true);
                                dialog.setSize(300, 300);
                                dialog.setLayout(new FlowLayout());
                            }
                        });
                        popupMenu.add(addRelativeRoom);
                        showPopupMenu(popupMenu, x, y);
                    }
                }
            }
        });
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

    public void addFurniture() {
        System.out.println("Furniture added to the plan!");
        repaint();
    }

}
