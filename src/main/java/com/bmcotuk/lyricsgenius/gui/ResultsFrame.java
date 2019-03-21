package com.bmcotuk.lyricsgenius.gui;

import com.bmcotuk.lyricsgenius.conf.ConfigurationLoader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

/**
 * Author: B. Mert Cotuk
 * Date:   06.03.2019
 * Time:   00:39
 * https://github.com/bmertcotuk
 */
public class ResultsFrame extends JFrame {

    private JPanel      contentPane;
    private JLabel      labelLyrics;
    private Color       colorLabel = Color.WHITE;
    private JScrollPane scrollPane;
    private JTextArea   rhymeArea;

    public ResultsFrame(List<String> wordsRhyming) {

        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setResizable(false);
        setSize(345, 480);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(Color.DARK_GRAY);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panelRhymes = new JPanel();
        panelRhymes.setForeground(Color.WHITE);
        panelRhymes.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
                                               ConfigurationLoader.getString("titleRhymes"),
                                               4,
                                               2,
                                               null,
                                               new Color(255, 255, 255)));
        panelRhymes.setBackground(Color.DARK_GRAY);
        panelRhymes.setBounds(15, 10, 310, 435);
        contentPane.add(panelRhymes);
        panelRhymes.setLayout(null);

        labelLyrics = new JLabel(ConfigurationLoader.getString("labelFindHere"));
        labelLyrics.setBounds(25, 20, 89, 14);
        panelRhymes.add(labelLyrics);
        labelLyrics.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"),
                                     0,
                                     ConfigurationLoader.getInteger("defaultLabelTextSize") + 2));
        labelLyrics.setForeground(colorLabel);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(25, 44, 260, 370);
        panelRhymes.add(scrollPane);
        scrollPane.setVerticalScrollBarPolicy(22);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        rhymeArea = new JTextArea();
        scrollPane.setViewportView(rhymeArea);
        rhymeArea.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"), 0, 14));
        rhymeArea.setCaretColor(Color.WHITE);
        rhymeArea.setLineWrap(true);
        rhymeArea.setWrapStyleWord(true);
        rhymeArea.setForeground(Color.WHITE);
        rhymeArea.setBackground(Color.WHITE);
        rhymeArea.setSelectionColor(Color.LIGHT_GRAY);
        rhymeArea.setOpaque(false);
        rhymeArea.setEditable(false);

        String content = fillTextArea(wordsRhyming);
        rhymeArea.setText(content);
    }

    private String fillTextArea(List<String> wordsRhyming) {

        StringBuilder sb = new StringBuilder();
        for (String rhyme : wordsRhyming) {
            sb.append(rhyme);
            sb.append("    ");
        }
        return sb.toString();
    }
}
