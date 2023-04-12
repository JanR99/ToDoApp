import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class ToDoListApp extends JFrame {

    //TODO doesnt show all txt files in the folder

    private final JPanel mainPanel;
    private final ArrayList<JCheckBox> checkBoxList;
    private final ArrayList<String> todoList;


    public static void main(String[] args) {
        new ToDoListApp();
    }

    /**
     * Initializes the components of the app
     */
    public ToDoListApp() {
        // initialize JFrame/self
        super("ToDo List App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setVisible(true);
        setLocationRelativeTo(null);

        // initialize Buttons
        JButton addButton = new JButton("Add ToDo");
        JButton saveButton = new JButton("Save ToDo List");
        JButton loadButton = new JButton("Load ToDo List");
        JButton deleteButton = new JButton("Delete all selected");

        checkBoxList = new ArrayList<>();
        todoList = new ArrayList<>();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));
        topPanel.add(loadButton);
        topPanel.add(addButton);
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JPanel saveAndDeletePanel = new JPanel();
        saveAndDeletePanel.setLayout(new BorderLayout());
        saveAndDeletePanel.add(deleteButton, BorderLayout.NORTH);
        saveAndDeletePanel.add(saveButton, BorderLayout.SOUTH);

        // add components to JFrame/self
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(saveAndDeletePanel, BorderLayout.SOUTH);

        // add the functionality of the buttons
        addButton.addActionListener(e -> addNewToDo());
        saveButton.addActionListener(e -> saveToDoList());
        loadButton.addActionListener(e -> loadToDoList());
        deleteButton.addActionListener(e -> deleteSelected());

        // To make sure all components show at the beginning
        revalidate();
        repaint();
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
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", ".txt"));
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
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", ".txt"));
        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            for(JCheckBox checkBox : checkBoxList) {
                mainPanel.remove(checkBox);
            }
            todoList.clear();
            checkBoxList.clear();
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
                revalidate();
                repaint();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void deleteSelected() {
        for(JCheckBox current : checkBoxList) {
            if(current.isSelected()) {
                // Delete all selected checkboxes
                int index = checkBoxList.indexOf(current);
                todoList.remove(index);
                checkBoxList.remove(current);
                mainPanel.remove(current);
            }
        }
    }
}