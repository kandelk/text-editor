package com.project;

import javax.swing.*;
import java.awt.*;

public class EditorGUI extends JFrame{
    private JTextArea jTextArea = new JTextArea(20,20);
    private JScrollPane jScrollPane = new JScrollPane(jTextArea);
    private JTextField jTextField = new JTextField();
    private JButton saveButton = new JButton("Save");
    private JButton loadButton = new JButton("Open");

    public EditorGUI() throws HeadlessException {
        super("My editor");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth(), height = (int) screenSize.getHeight();
        setBounds( width / 4, height / 4, width / 2, height / 2);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        jTextArea.setName("TextArea");
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        createLayout();
    }

    private void createLayout() {
        GridBagConstraints c = new GridBagConstraints();
        setLayout(new GridBagLayout());

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10,10,10,10);

        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        add(jTextField,c);

        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 0.1;
        add(saveButton, c);

        c.gridx = 4;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 0.1;
        add(loadButton, c);

        c.insets.top = 0;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        add(jScrollPane, c);
    }
}
