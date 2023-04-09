import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class ToDoListApp extends JFrame {

    //TODO add an delete Button or something
    //TODO make the window pop up in the middle of the screen
    //TODO make it save the data as txt. file if possible
    //TODO try what happens if you give the program random files

    private final JPanel mainPanel;
    private final JPanel topPanel;
    private final JScrollPane scrollPane;
    private final JButton addButton;
    private final JButton saveButton;
    private final JButton loadButton;
    private ArrayList<JCheckBox> checkBoxList;
    private ArrayList<String> todoList;


    public static void main(String[] args) {
        new ToDoListApp();
    }

    /**
     * Initializes the components of the app
     */
    public ToDoListApp() {
        super("ToDo List App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setVisible(true);

        // initialize components
        addButton = new JButton("Add ToDo");
        saveButton = new JButton("Save ToDo List");
        loadButton = new JButton("Load ToDo List");
        checkBoxList = new ArrayList<>();
        todoList = new ArrayList<>();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));
        topPanel.add(loadButton);
        topPanel.add(addButton);
        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // add components to main frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(saveButton, BorderLayout.SOUTH);

        // add the functionality of the buttons
        addButton.addActionListener(e -> addNewToDo());
        saveButton.addActionListener(e -> saveToDoList());
        loadButton.addActionListener(e -> loadToDoList());
    }

    /**
     * Method to add a new item to the list
     */
    private void addNewToDo() {
        String newToDo = JOptionPane.showInputDialog(this, "Enter new ToDo item:");
        if (newToDo != null && !newToDo.trim().isEmpty()) {
            todoList.add(newToDo);
            JCheckBox newCheckBox = new JCheckBox(newToDo);
            checkBoxList.add(newCheckBox);
            mainPanel.add(newCheckBox);
            mainPanel.revalidate();
            mainPanel.repaint();
        }
    }

    /**
     * Method to save the file on the pc
     */
    private void saveToDoList() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(ToDoListApp.this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (fileToSave.exists()) {
                int confirmResult = JOptionPane.showConfirmDialog(ToDoListApp.this,
                        "Are you sure you want to override this file?", "File Already Exists",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirmResult != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                for (String todo : todoList) {
                    writer.write(todo + "\n");
                }
                writer.close();
                JOptionPane.showMessageDialog(this, "ToDo list saved successfully!");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Method to load a file from the pc
     */
    private void loadToDoList() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load ToDo List");
        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(fileToLoad));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        todoList.add(line);
                        JCheckBox newCheckBox = new JCheckBox(line);
                        checkBoxList.add(newCheckBox);
                        mainPanel.add(newCheckBox);
                    }
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
