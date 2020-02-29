package com.project;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

class EditorGUI extends JFrame{
    private JTextArea jTextArea = new JTextArea();
    private JScrollPane jScrollPane = new JScrollPane(jTextArea);
    private JTextField searchField = new JTextField();
    private JButton saveButton = new JButton(new ImageIcon("resources/save.png"));
    private JButton loadButton = new JButton(new ImageIcon("resources/load.png"));
    private JButton searchButton = new JButton(new ImageIcon("resources/search.jpg"));
    private JButton nextButton = new JButton(new ImageIcon("resources/next.jpg"));
    private JButton prevButton = new JButton(new ImageIcon("resources/prev.jpg"));
    private JMenu fileMenu = new JMenu("File");
    private JMenuItem newMenuItem = new JMenuItem("New");
    private JMenuItem loadMenuItem = new JMenuItem("Open");
    private JMenuItem saveMenuItem = new JMenuItem("Save");
    private JMenuItem exitMenuItem = new JMenuItem("Exit");
    private JMenuItem saveAsMenuItem = new JMenuItem("Save as");
    private JMenuBar jMenuBar = new JMenuBar();
    private JCheckBox useRegexp = new JCheckBox("Use regexp");

    private File currDir = new File(System.getProperty("user.dir"));
    private JFileChooser fileChooser = new JFileChooser(currDir);

    private List<Integer> searchIndexes;
    private int index;

    EditorGUI() throws HeadlessException {
        super("My editor");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth(), height = (int) screenSize.getHeight();
        setBounds( width / 6, height / 6, width / 6 * 4, height / 6 * 4);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);

        jTextArea.setName("TextArea");
        searchField.setName("FilenameField");
        saveButton.setName("SaveButton");
        loadButton.setName("LoadButton");
        jScrollPane.setName("ScrollPane");
        fileMenu.setName("MenuFile");
        loadMenuItem.setName("MenuLoad");
        saveMenuItem.setName("MenuSave");
        exitMenuItem.setName("MenuExit");
        newMenuItem.setName("MenuNew");
        saveAsMenuItem.setName("MenuSaveAs");

        createLayout();
        createMenu();
        createActionListeners();
    }

    private void createLayout() {
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(groupLayout);

        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup()
                    .addComponent(saveButton,24,24,24)
                    .addComponent(loadButton,24,24,24)
                    .addComponent(searchField, 24, 24, 24)
                    .addComponent(searchButton, 24, 24, 24)
                    .addComponent(prevButton, 24, 24, 24)
                    .addComponent(nextButton, 24, 24, 24)
                    .addComponent(useRegexp))
                .addComponent(jScrollPane)
        );

        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup()
                .addGroup(groupLayout.createSequentialGroup()
                    .addComponent(saveButton)
                    .addComponent(loadButton)
                    .addComponent(searchField)
                    .addComponent(searchButton)
                    .addComponent(prevButton)
                    .addComponent(nextButton)
                    .addComponent(useRegexp))
                .addComponent(jScrollPane)
        );
    }

    private void createMenu() {
        setJMenuBar(jMenuBar);
        jMenuBar.add(fileMenu);

        fileMenu.setMnemonic(KeyEvent.VK_F);

        fileMenu.add(newMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
    }

    private void createActionListeners() {
        ActionListener saveListener = e -> {
            if (fileChooser.getSelectedFile() == null && !isSaveDirSelected()) {
                return;
            }

            saveFile();
        };

        ActionListener loadListener = e -> {
            if (fileChooser.showOpenDialog(null) == JFileChooser.CANCEL_OPTION) {
                fileChooser.setCurrentDirectory(currDir);
                return;
            }
            currDir = fileChooser.getCurrentDirectory();
            fileChooser.setCurrentDirectory(currDir);

            String loadedFile = new FileEditor().loadFile(fileChooser.getSelectedFile().getAbsolutePath());
            if (loadedFile == null) {
                JOptionPane.showMessageDialog(null, "File not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            jTextArea.setText(loadedFile);
            jTextArea.setCaretPosition(0);
        };

        ActionListener nextWordFind = e -> {
            if (searchIndexes == null) {
                return;
            }

            index++;
            if (index == searchIndexes.size()) {
                index = 0;
            }

            setCaret(searchIndexes.get(index));
        };

        loadButton.addActionListener(loadListener);

        saveButton.addActionListener(saveListener);

        exitMenuItem.addActionListener(e -> System.exit(0));

        loadMenuItem.addActionListener(loadListener);

        saveMenuItem.addActionListener(saveListener);

        saveAsMenuItem.addActionListener(e -> {
            if (isSaveDirSelected()) {
                saveFile();
            }
        });

        newMenuItem.addActionListener(e -> {
            jTextArea.setText("");
            fileChooser.setSelectedFile(null);
        });

        searchButton.addActionListener(e -> {
            if (searchIndexes == null) {
                searchString(searchField.getText());
                index = -1;
            }

            nextWordFind.actionPerformed(e);
        });

        nextButton.addActionListener(nextWordFind);

        prevButton.addActionListener(e -> {
            if (searchIndexes == null) {
                return;
            }


            if (index == 0) {
                index = searchIndexes.size();
            }

            index--;
            setCaret(searchIndexes.get(index));
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchIndexes = null;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchIndexes = null;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchIndexes = null;
            }
        });
    }

    private void setCaret(int index) {
        jTextArea.setCaretPosition(index + searchField.getText().length());
        jTextArea.select(index, index + searchField.getText().length());
        jTextArea.grabFocus();
    }

    private boolean isSaveDirSelected() {
        if (fileChooser.showSaveDialog(null) == JFileChooser.CANCEL_OPTION) {
            fileChooser.setCurrentDirectory(currDir);
            return false;
        }
        currDir = fileChooser.getCurrentDirectory();
        fileChooser.setCurrentDirectory(currDir);

        return  true;
    }

    private void saveFile() {
        if (!new FileEditor().saveFile(fileChooser.getSelectedFile().getAbsolutePath(), jTextArea.getText())) {
            JOptionPane.showMessageDialog(null, "Error in saving file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchString(String search) {
        int index;
        List<Integer> list = new ArrayList<>();

        jTextArea.setCaretPosition(0);
        while ((index = jTextArea.getText().indexOf(search, jTextArea.getCaretPosition())) != -1) {
            list.add(index);
            jTextArea.setCaretPosition(index+1);
        }

        searchIndexes = list;
    }

//    private int searchStringRegex(Pattern regexp) {
//        Matcher matcher = regexp.matcher(jTextArea.getText());
//
//    }

//    private int[] searchString(Regexp regexp) {
//
//    }
}
