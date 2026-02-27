package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainWindow extends JFrame {

    private ArrayList<Task> tasks = new ArrayList<>();
    private DefaultListModel<String> listModel;
    private JList<String> taskList;
    private JTextField inputField;

    // Palette de couleurs
    private final Color BG_COLOR     = new Color(15, 15, 25);
    private final Color PANEL_COLOR  = new Color(25, 25, 40);
    private final Color ACCENT_COLOR = new Color(99, 102, 241);
    private final Color TEXT_COLOR   = new Color(230, 230, 255);
    private final Color DONE_COLOR   = new Color(80, 200, 120);
    private final Color DANGER_COLOR = new Color(239, 68, 68);

    public MainWindow() {
        setTitle("Ma To-Do List");
        setSize(480, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // --- TITRE ---
        JLabel titleLabel = new JLabel("Ma To-Do List");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // --- SAISIE ---
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(BG_COLOR);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        inputField = new JTextField();
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        inputField.setBackground(PANEL_COLOR);
        inputField.setForeground(TEXT_COLOR);
        inputField.setCaretColor(TEXT_COLOR);
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        inputField.addActionListener(e -> addTask());

        JButton addButton = createButton("+ Ajouter", ACCENT_COLOR);
        addButton.addActionListener(e -> addTask());

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);

        // --- LISTE ---
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setFont(new Font("SansSerif", Font.PLAIN, 15));
        taskList.setBackground(PANEL_COLOR);
        taskList.setForeground(TEXT_COLOR);
        taskList.setSelectionBackground(ACCENT_COLOR);
        taskList.setSelectionForeground(Color.WHITE);
        taskList.setFixedCellHeight(46);
        taskList.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 70), 1));
        scrollPane.getViewport().setBackground(PANEL_COLOR);

        // --- BOUTONS ---
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));

        JButton completeButton = createButton("Terminer", DONE_COLOR);
        completeButton.addActionListener(e -> completeTask());

        JButton deleteButton = createButton("Supprimer", DANGER_COLOR);
        deleteButton.addActionListener(e -> deleteTask());

        buttonPanel.add(completeButton);
        buttonPanel.add(deleteButton);

        // --- ASSEMBLAGE ---
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BG_COLOR);
        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        addDemoTasks();

        add(mainPanel);
        setVisible(true);
    }

    private void addTask() {
        String text = inputField.getText().trim();

        if (text.isEmpty()) {
            inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DANGER_COLOR, 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            return;
        }

        Task newTask = new Task(text);
        tasks.add(newTask);
        listModel.addElement(newTask.toString());

        inputField.setText("");
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        inputField.requestFocus();
    }

    private void completeTask() {
        int index = taskList.getSelectedIndex();
        if (index == -1) { showError("Sélectionne une tâche d'abord !"); return; }

        Task task = tasks.get(index);
        task.setCompleted(true);
        listModel.set(index, task.toString());
    }

    private void deleteTask() {
        int index = taskList.getSelectedIndex();
        if (index == -1) { showError("Sélectionne une tâche d'abord !"); return; }

        int confirm = JOptionPane.showConfirmDialog(
            this, "Supprimer cette tâche ?", "Confirmation", JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            tasks.remove(index);
            listModel.remove(index);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Attention", JOptionPane.WARNING_MESSAGE);
    }

    private void addDemoTasks() {
        String[] demos = { "Apprendre Java ☕", "Créer un projet portfolio", "Pousser sur GitHub" };
        for (String demo : demos) {
            Task t = new Task(demo);
            tasks.add(t);
            listModel.addElement(t.toString());
        }
        tasks.get(0).setCompleted(true);
        listModel.set(0, tasks.get(0).toString());
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(color); }
        });

        return btn;
    }
}