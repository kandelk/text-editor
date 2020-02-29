package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

class EditorGUI extends JFrame{
    private JTextArea jTextArea = new JTextArea();
    private JScrollPane jScrollPane = new JScrollPane(jTextArea);
    private JTextField fileName = new JTextField();
    private JButton saveButton = new JButton(new ImageIcon("resources/save.png"));
    private JButton loadButton = new JButton(new ImageIcon("resources/load.png"));
    private JMenu fileMenu = new JMenu("File");
    private JMenuItem newMenuItem = new JMenuItem("New");
    private JMenuItem loadMenuItem = new JMenuItem("Open");
    private JMenuItem saveMenuItem = new JMenuItem("Save");
    private JMenuItem exitMenuItem = new JMenuItem("Exit");
    private JMenuBar jMenuBar = new JMenuBar();
    private JMenuItem saveAsMenuItem = new JMenuItem("Save as");

    private File currDir = new File(System.getProperty("user.dir"));
    private JFileChooser fileChooser = new JFileChooser(currDir);

    EditorGUI() throws HeadlessException {
        super("My editor");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth(), height = (int) screenSize.getHeight();
        setBounds( width / 6, height / 6, width / 6 * 4, height / 6 * 4);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);

        jTextArea.setName("TextArea");
        fileName.setName("FilenameField");
        saveButton.setName("SaveButton");
        loadButton.setName("LoadButton");
        jScrollPane.setName("ScrollPane");
        fileMenu.setName("MenuFile");
        loadMenuItem.setName("MenuLoad");
        saveMenuItem.setName("MenuSave");
        exitMenuItem.setName("MenuExit");
        newMenuItem.setName("MenuNew");

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
                    .addComponent(fileName, 24, 24, 24))
                .addComponent(jScrollPane)
        );

        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup()
                .addGroup(groupLayout.createSequentialGroup()
                    .addComponent(saveButton)
                    .addComponent(loadButton)
                    .addComponent(fileName))
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
        fileName.add(saveAsMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
    }

    private void createActionListeners() {
        ActionListener saveListener = e -> {
            if (!new FileEditor().saveFile(fileChooser.getSelectedFile().getAbsolutePath(), jTextArea.getText())) {
                JOptionPane.showMessageDialog(null, "Error in saving file", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
            }

            jTextArea.setText(loadedFile);
        };

        loadButton.addActionListener(loadListener);

        saveButton.addActionListener(saveListener);

        exitMenuItem.addActionListener(e -> System.exit(0));

        loadMenuItem.addActionListener(loadListener);

        saveMenuItem.addActionListener(saveListener);

        //TODO add action listener for save as
        saveAsMenuItem.addActionListener(e -> {

        });

        newMenuItem.addActionListener(e -> {
            jTextArea.setText("");
            fileName.setText("newFile.txt");
        });
    }
}
