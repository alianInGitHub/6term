package ui;

import core.AbstractProcessor;
import core.JDBCProcessor;
import generated.Group;
import generated.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by anastasia on 4/23/17.
 */
public class MainFrame extends JFrame {
    public static volatile boolean dataWasChanged = false;

    private AbstractProcessor processor;

    private MenuItem loadInfoFromFileMenuItem;
    private MenuItem saveInfoToFileMenuItem;
    private MenuItem addGroupMenuItem;
    private MenuItem addStudentMenuItem;
    private MenuItem changeGroupMenuItem;
    private MenuItem changeStudentMenuItem;
    private MenuItem deleteGroupMenuItem;
    private MenuItem deleteStudentMenuItem;

    private JTextArea groupsTable;
    private JTextArea studentTable;

    public static final int WIDTH = 980;
    public static final int HEIGHT = 600;

    public static final Dimension defaultButtonSize = new Dimension(WIDTH / 3, 30);
    public static final Dimension verticalGap = new Dimension(0, 10);
    public static final Dimension horizontalGap = new Dimension(10, 0);

    public MainFrame() {
        super("Student Department Database");
        initialize();
    }

    private void initialize() {
        processor = new JDBCProcessor();
        setSize(WIDTH, HEIGHT);
        createComponents();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLocation(400, 300);
    }

    public void createComponents() {
        createMenuItems();
        createTables();
        Container contentPane = getContentPane();
        setMenuBar(createMenuBar());
        contentPane.setLayout(new BorderLayout());
        placeComponents(contentPane);
    }

    private void createMenuItems() {
        initMenuItems();
        addListenersToMenuItems();
    }

    private void createTables() {
        groupsTable = createTable();
        studentTable = createTable();
    }

    private JTextArea createTable() {
        JTextArea textArea = new JTextArea();
        textArea.setAutoscrolls(true);
        return textArea;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu menu = createMenu("File", loadInfoFromFileMenuItem, saveInfoToFileMenuItem);
        menuBar.add(menu);
        menu = createMenu("Group", addGroupMenuItem, changeGroupMenuItem, deleteGroupMenuItem);
        menuBar.add(menu);
        menu = createMenu("Student", addStudentMenuItem, changeStudentMenuItem, deleteStudentMenuItem);
        menuBar.add(menu);
        return menuBar;
    }

    private Menu createMenu(String title, MenuItem... items) {
        String space = "         ";
        Menu menu = new Menu(" " + title + space);
        menu.setFont(new Font("Times New Roman", 1, 14));
        for (int i = 0; i < items.length; i++) {
            items[i].setLabel(" " + items[i].getLabel() + space + space);
            menu.add(items[i]);
        }
        return menu;
    }

    private void placeComponents(Container content) {
        content.add(setMenuItemsOnPanel(), BorderLayout.CENTER);
    }

    private JPanel setMenuItemsOnPanel() {
        JPanel content = new JPanel();
        BoxLayout layout = new BoxLayout(content, BoxLayout.PAGE_AXIS);
        content.setLayout(layout);
        content.add(createMainPanel());
        return content;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(Box.createRigidArea(verticalGap));
        panel.add(setGroupAndStudentMenuItems());
        panel.add(Box.createRigidArea(verticalGap));
        return panel;
    }

    private JPanel setGroupAndStudentMenuItems() {
        JPanel departmentPanel = new JPanel();
        departmentPanel.setLayout(new GridLayout(2, 1));
        departmentPanel.add(createTableAndLabelPanel(groupsTable, "Groups"));
        departmentPanel.add(createTableAndLabelPanel(studentTable, "Students"));
        return departmentPanel;
    }

    private JPanel createTableAndLabelPanel(JTextArea table, String label) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(Box.createRigidArea(verticalGap));
        panel.add(createLabel(label));
        panel.add(Box.createRigidArea(MainFrame.verticalGap));
        panel.add(createTablePanel(table));
        return panel;
    }

    private JPanel createTablePanel(JTextArea table) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(Box.createRigidArea(MainFrame.horizontalGap));
        panel.add(table);
        panel.add(Box.createRigidArea(MainFrame.horizontalGap));
        return panel;
    }

    private JPanel createLabel(String text) {
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
        JLabel label = new JLabel(text);
        label.setMaximumSize(defaultButtonSize);
        labelPanel.add(label);
        return labelPanel;
    }

    private void initMenuItems() {
        loadInfoFromFileMenuItem = new MenuItem("Open");
        saveInfoToFileMenuItem = new MenuItem("Save");
        addGroupMenuItem = new MenuItem("Add group");
        addStudentMenuItem = new MenuItem("Add student");
        changeGroupMenuItem = new MenuItem("Change group info");
        changeStudentMenuItem =new MenuItem("Change student info");
        deleteGroupMenuItem = new MenuItem("Delete group");
        deleteStudentMenuItem = new MenuItem("Delete student");
    }

    public void updateTables() {
        groupsTable.setText("");
        studentTable.setText("");
        ArrayList<Group> groups = (ArrayList<Group>) processor.getGroups();
        if (groups == null)
            return;
        for (int i = 0; i < groups.size(); i++) {
            String info = groups.get(i).getName() + "\t"
                    + groups.get(i).getFaculty() + "\t"
                    + groups.get(i).getCourse() + "\n";
            groupsTable.append(info);
            showStudentTable(groups.get(i));
        }
    }

    private void  showStudentTable(Group group) {
        ArrayList<Student> students = (ArrayList<Student>) group.getStudents();
        for (int i = 0; i < students.size(); i++) {
            String info = students.get(i).getFirstName() + "\t"
                    + students.get(i).getLastName() + "\t"
                    + students.get(i).getAddress() + "\t"
                    + students.get(i).getPhoneNumber() + "\n";
            studentTable.append(info);
        }

    }

    private void addListenersToMenuItems() {
        loadInfoFromFileMenuItem.addActionListener(this::actionPerformed);
        saveInfoToFileMenuItem.addActionListener(this::actionPerformed);
        addGroupMenuItem.addActionListener(this::actionPerformed);
        addStudentMenuItem.addActionListener(this::actionPerformed);
        changeGroupMenuItem.addActionListener(this::actionPerformed);
        changeStudentMenuItem.addActionListener(this::actionPerformed);
        deleteGroupMenuItem.addActionListener(this::actionPerformed);
        deleteStudentMenuItem.addActionListener(this::actionPerformed);
    }

    private void actionPerformed(ActionEvent e) {
        if (e.getSource() == loadInfoFromFileMenuItem) {
            openLoadFileFrame();
        } else if (e.getSource() == saveInfoToFileMenuItem) {
            openSaveFileFrame();
        } else if (e.getSource() == addGroupMenuItem) {
            openAddGroupFrame();
        } else if (e.getSource() == addStudentMenuItem) {
            openAddStudentFrame();
        } else if (e.getSource() == changeGroupMenuItem) {
            openChangeGroupFrame();
        } else if (e.getSource() == changeStudentMenuItem) {
            openChangeStudentFrame();
        } else if (e.getSource() == deleteGroupMenuItem) {
            openDeleteGroupFrame();
        } else if (e.getSource() == deleteStudentMenuItem) {
            openDeleteStudentFrame();
        }
    }

    private void openLoadFileFrame() {
        JFileChooser fileChooserFrame = new JFileChooser();
        int returnVal = fileChooserFrame.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooserFrame.getSelectedFile();
            if (Objects.equals(getExtension(file), "xml")) {
                System.out.print("Opening: " + file.getName() + "." + "\n");
                if (!processor.readDataFromFile(file.getPath())) {
                    JOptionPane.showMessageDialog(this, "Failed to open a file");
                } else {
                    dataWasChanged = true;
                }
            }
        } else {
            System.out.print("Open command cancelled by user." + "\n");
        }
    }

    private String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    private void openSaveFileFrame() {
        JFileChooser fileChooserFrame = new JFileChooser();
        int retval = fileChooserFrame.showSaveDialog(this);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File file = fileChooserFrame.getSelectedFile();
            if (file == null) {
                return;
            }
            try {
                processor.saveDatabase(file.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void openAddGroupFrame() {
        SwingUtilities.invokeLater(() -> new AddGroupFrame(processor, this.getLocationOnScreen()));
    }

    private void openAddStudentFrame() {
        SwingUtilities.invokeLater(() -> new AddStudentFrame(processor, this.getLocationOnScreen()));
    }

    private void openChangeGroupFrame() {
        SwingUtilities.invokeLater(() -> new ChangeGroupFrame(processor, this.getLocationOnScreen()));
    }

    private void openChangeStudentFrame() {
        SwingUtilities.invokeLater(() -> new ChangeStudentFrame(processor, this.getLocationOnScreen()));
    }

    private void openDeleteGroupFrame() {
        SwingUtilities.invokeLater(() -> new DeleteGroupFrame(processor, this.getLocationOnScreen()));
    }

    private void openDeleteStudentFrame() {
        SwingUtilities.invokeLater(() -> new DeleteStudentFrame(processor, this.getLocationOnScreen()));
    }

    private void loadDataFromFile(String fileName) {
        processor.readDataFromFile(fileName);
        dataWasChanged = true;
    }

    public static void main(String[] args) {
        Updater updater = new Updater();
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            updater.setFrame(frame);
        });
        new Thread(updater).run();
    }
}
