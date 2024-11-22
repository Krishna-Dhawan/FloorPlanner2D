import javax.swing.*;
import java.awt.*;
import comps.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class Screen extends JFrame {
    private String title = "";

    private final Plan plan;
    public java.util.List<Room> roomList = new ArrayList<>();
    public java.util.List<Furniture> furnitureList = new ArrayList<>();
    public java.util.List<Wall> wallList = new ArrayList<>();

    ControlPanel cp = new ControlPanel(this);

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
        gbc.weightx = 0.95; // 75% of horizontal space
        gbc.weighty = 1.0; // Full vertical space
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        add(p, gbc);

        // Control Panel Constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.05; // Ensure consistent weight
        gbc.weighty = 1.0;  // Full vertical space
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        add(cp.cp, gbc);

// Set Preferred Size
        cp.cp.setPreferredSize(new Dimension(300, getHeight()));
        cp.cp.setMinimumSize(new Dimension(300, 0));
        cp.cp.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));

// Revalidate and Repaint After Updates
        cp.cp.revalidate();
        cp.cp.repaint();


        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println(x + " " + y);
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (title.isEmpty() && !roomList.isEmpty()) {
                    int confirmed = JOptionPane.showConfirmDialog(null,
                            "Save before leaving?",
                            "Exit Confirmation",
                            JOptionPane.YES_NO_OPTION);

                    if (confirmed == JOptionPane.NO_OPTION) {
                        dispose();
                        System.exit(0);
                    } else {
                        savePlan(true);
                    }
                } else {
                    dispose();
                    System.exit(0);
                }
            }
        });

//        setSize(1200, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private JMenuBar getjMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem loadMenuItem = new JMenuItem("Load");
        saveMenuItem.addActionListener( e -> {
            savePlan(false);
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
                    handleOverlapException(true, e.getMessage());
                    break;
                }
            case "Add Furniture":
                try {
                    plan.addFurniture(type, vals);
                    break;
                } catch (OverlapException e) {
                    handleOverlapException(true, e.getMessage());
                }
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
            case "Remove Room":
                this.roomList.remove(newRoom);
                break;
            case "Snap Back":
                plan.snapBack();
                break;
            case "sel-room":
                cp.setVals(newRoom);
                break;
            case "set-null":
                cp.setVals();
                break;
            default:
                break;
        }
    }
    public void canvasPanelAction(String action, Furniture newFurniture) {
        switch (action) {
            case "Add Room":
                this.furnitureList.add(newFurniture);
                break;
            case "Add Furniture":
                System.out.println(newFurniture);
                break;
            case "Remove Furniture":
                this.furnitureList.remove(newFurniture);
                break;
            case "Snap Back":
                plan.snapBack();
                break;
            case "sel-furniture":
                cp.setVals(newFurniture);
                break;
            default:
                break;
        }
    }
    public void handleOverlapException(boolean isNewRoom, String msg) {
        System.out.println("OverlapError");
        JDialog overlap = new JDialog(this, "OverlapError");
        JLabel l = new JLabel(msg);
        overlap.add(l);
        overlap.setLocationRelativeTo(this);
        overlap.setVisible(true);
        overlap.setSize(300, 100);
        overlap.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (!isNewRoom) {
                    Room x = null;
                    canvasPanelAction("Snap Back", x);
                }
                overlap.dispose();
            }
        });
    }

    // TODO: Make the JFileChooser in normal mode instead of DIRECTORIES_ONLY
    public void savePlan(boolean exitOnClose) {
        wallList = plan.wallList;

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
                    ok.addActionListener( ev -> emptyListDialog.dispose());
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
                    ok.addActionListener( ev -> emptyNameDialog.dispose());
                    emptyNameDialog.add(emptyNameLabel);
                    emptyNameDialog.add(ok);

                    emptyNameDialog.setVisible(true);
                    emptyNameDialog.setSize(500, 200);
                    return;
                }
                File file = new File(selectedDirectory.getAbsolutePath(), this.title + ".fp");

                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                    oos.writeObject(roomList);
                    oos.writeObject(furnitureList);
                    oos.writeObject(wallList);
                    System.out.println("Room list saved to: " + file.getAbsolutePath());
                    if (exitOnClose) {
                        this.dispose();
                        System.exit(0);
                    }
                } catch (IOException err) {
                    // e.printStackTrace();
                    System.out.println("IOException: " + err.getMessage());
                }
                this.setTitle("Floor Planner 2D" + (this.title.isEmpty()?"":" - " + this.title));
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
        java.util.List<Furniture> loadedFurnitures = new ArrayList<>();
        java.util.List<Wall> loadedWalls = new ArrayList<>();

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(null, "fp"));
        File defaultDirectory = new File("./SavedPlans");
        chooser.setCurrentDirectory(defaultDirectory);

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            System.out.println("Selected File: " + selectedFile.getAbsolutePath());

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile))) {
                loadedRooms = (java.util.List<Room>) ois.readObject();
                loadedFurnitures = (java.util.List<Furniture>) ois.readObject();
                loadedWalls = (java.util.List<Wall>) ois.readObject();
                System.out.println("Room list loaded from: " + selectedFile.getAbsolutePath());
            } catch(IOException err) {
                System.out.println("IOException: " + err.getMessage());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            this.title = selectedFile.getName();
        }
        this.roomList = loadedRooms;
        this.furnitureList = loadedFurnitures;
        this.wallList = loadedWalls;
        plan.fetchRoomList(this.roomList);
        plan.fetchFurnitureList(this.furnitureList);
        plan.fetchWallList(this.wallList);
        repaint();
    }
}

  
     
