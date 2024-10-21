import javax.swing.*;
import java.awt.*;
import comps.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;

public class Screen extends JFrame {
    private String title = "";

    private final Plan plan;
    public java.util.List<Room> roomList = new ArrayList<Room>();
    // public java.util.List<Furniture> furnitureList;

    public Screen() {
        setTitle("Floor Planner 2D" + (this.title.isEmpty()?"":" - " + this.title));
        System.out.println("Hello World");

        JMenuBar menuBar = getjMenuBar();
        setJMenuBar(menuBar);

        // GridBagLayout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // Expand both horizontally and vertically

        // Plan (Canvas)
        Plan p = new Plan(this);
        plan = p;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.75; // 75% of horizontal space
        gbc.weighty = 1.0; // Full vertical space
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        add(p, gbc);

        // Control Panel on the left side
        ControlPanel cp = new ControlPanel(this);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.25; // 25% of horizontal space
        gbc.weighty = 1.0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        add(cp.cp, gbc);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println(x + " " + y);
            }
        });

        setSize(1200, 800);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JMenuBar getjMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem loadMenuItem = new JMenuItem("Load");
        saveMenuItem.addActionListener( e -> {
            savePlan();
        });
        loadMenuItem.addActionListener( e -> {
            loadPlan();
        });
        // JMenu editMenu = new JMenu("Edit");
        // JMenu helpMenu = new JMenu("Help");
        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);
        menuBar.add(fileMenu);
        // menuBar.add(editMenu);
        // menuBar.add(helpMenu);
        return menuBar;
    }

    public void controlPanelAction(String action, String type, String[] vals) {
        switch (action) {
            case "Add Room":
                try {
                    if (!vals[0].isEmpty() && !vals[1].isEmpty() && !vals[2].isEmpty() && !vals[3].isEmpty()) {
                        Dim dim = new Dim(Integer.parseInt(vals[2]), Integer.parseInt(vals[3]));
                        Pos pos = new Pos(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]));
                        plan.addRoom(type, pos, dim);
                    } else if (!vals[0].isEmpty() && !vals[1].isEmpty() && vals[2].isEmpty() && vals[3].isEmpty()) {
                        Pos pos = new Pos(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]));
                        plan.addRoom(type, pos);
                    } else if (vals[0].isEmpty() && vals[1].isEmpty() && !vals[2].isEmpty() && !vals[3].isEmpty()) {
                        Dim dim = new Dim(Integer.parseInt(vals[2]), Integer.parseInt(vals[3]));
                        plan.addRoom(type, dim);
                    } else {
                        plan.addRoom(type);
                    }
                    break;
                } catch (OverlapException e) {
                    System.out.println("OverlapError");
                    JDialog overlap = new JDialog(this, "OverlapError");
                    JLabel l = new JLabel("Overlapping Rooms. Change the position or dimensions.");
                    overlap.add(l);
                    overlap.setLocationRelativeTo(this);
                    overlap.setVisible(true);
                    overlap.setSize(200, 100);
                    break;
                }
            case "Add Furniture":
                plan.addFurniture();
                break;
            default:
                break;
        }
    }
    public void canvasPanelAction(String action, Room newRoom) {
        switch (action) {
            case "Add Room":
                this.roomList.add(newRoom);
                break;
            case "Add Furniture":
                System.out.println(newRoom);
                break;
            default:
                break;
        }
    }
    public void savePlan() {
        JFileChooser chooser = new JFileChooser();
        File defaultDirectory = new File("./SavedPlans");
        chooser.setCurrentDirectory(defaultDirectory);

        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = chooser.getSelectedFile();
            System.out.println("Selected Directory: " + selectedDirectory.getAbsolutePath());

            JDialog fileNameDialog = new JDialog(this, "Enter Name");
            JLabel l = new JLabel("File will be stored in " + selectedDirectory.getAbsolutePath());
            JTextField fileName = new JTextField(15);
            JButton okButton = new JButton("OK");

            okButton.addActionListener( e -> {
                this.title = fileName.getText();
                if (roomList == null || roomList.isEmpty()) {
                    System.out.println("Room list is empty, nothing to save.");
                    JDialog emptyListDialog = new JDialog(this, "Empty Canvas");
                    JLabel emptyListLabel = new JLabel("There is nothing to save.");
                    JButton ok = new JButton("OK");
                    ok.addActionListener( ev -> {emptyListDialog.dispose();});
                    emptyListDialog.add(emptyListLabel);
                    emptyListDialog.add(ok);

                    emptyListDialog.setVisible(true);
                    emptyListDialog.setSize(500, 200);
                    return;
                }
                if (fileName.getText().isEmpty()) {
                    JDialog emptyNameDialog = new JDialog(this, "no file name entered");
                    JLabel emptyNameLabel = new JLabel("Please Enter a file name");
                    JButton ok = new JButton("OK");
                    ok.addActionListener( ev -> {emptyNameDialog.dispose();});
                    emptyNameDialog.add(emptyNameLabel);
                    emptyNameDialog.add(ok);

                    emptyNameDialog.setVisible(true);
                    emptyNameDialog.setSize(500, 200);
                    return;
                }
                File file = new File(selectedDirectory.getAbsolutePath(), this.title + ".dat");

                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                    oos.writeObject(roomList);
                    System.out.println("Room list saved to: " + file.getAbsolutePath());
                } catch (IOException err) {
                    // e.printStackTrace();
                    System.out.println("IOException: " + err.getMessage());
                }
                setTitle("Floor Planner 2D" + (this.title.isEmpty()?"":" - " + this.title));
                fileNameDialog.dispose();
            });

            fileNameDialog.add(l);
            fileNameDialog.add(fileName);
            fileNameDialog.add(okButton);

            fileNameDialog.setLocationRelativeTo(this);
            fileNameDialog.setVisible(true);
            fileNameDialog.setSize(500, 200);
            fileNameDialog.setLayout(new FlowLayout());
        } else {
            System.out.println("No directory selected");
        }
    }

    @SuppressWarnings("unchecked")
    public void loadPlan() {
        java.util.List<Room> loadedRooms = new ArrayList<>();

        JFileChooser chooser = new JFileChooser();
        File defaultDirectory = new File("./SavedPlans");
        chooser.setCurrentDirectory(defaultDirectory);

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            System.out.println("Selected File: " + selectedFile.getAbsolutePath());

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile))) {
                loadedRooms = (java.util.List<Room>) ois.readObject();
                System.out.println("Room list loaded from: " + selectedFile.getAbsolutePath());
            } catch(IOException err) {
                System.out.println("IOException: " + err.getMessage());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        this.roomList = loadedRooms;
        plan.fetchRoomList(this.roomList);
    }
}
