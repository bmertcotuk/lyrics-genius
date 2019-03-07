package com.bmcotuk.lyricsgenius.gui;

/**
 * Author: B. Mert Cotuk.
 * Date:   04.01.2019
 * Time:   22:16
 * https://github.com/bmertcotuk
 */

import com.bmcotuk.lyricsgenius.conf.ConfigurationLoader;
import com.bmcotuk.lyricsgenius.core.RhymeEngine;
import com.bmcotuk.lyricsgenius.data.MatchFrom;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationFrame extends JFrame {

    private static final Logger       logger       = Logger.getLogger(RhymeEngine.class);
    private              ButtonGroup  rdbtnGroup;
    private              JButton      buttonDecrement;
    private              JButton      buttonIncrement;
    private              JCheckBox    checkBoxConsonants;
    private              JCheckBox    checkBoxIncludeDefaultRhymes;
    private              JCheckBox    checkBoxIncludeMyRhymes;
    private              JCheckBox    checkBoxSaveAsRhyme;
    private              JCheckBox    checkBoxVowels;
    private              JComboBox    comboBox;
    private              JLabel       labelIncludeSource;
    private              JLabel       labelInformation;
    private              JLabel       labelMatch;
    private              JLabel       labelRhymeLength;
    private              JLabel       labelRhymeLengthNum;
    private              JLabel       labelWord;
    private              JPanel       contentPane;
    private              JRadioButton radioButtonBoth;
    private              JRadioButton radioButtonHead;
    private              JRadioButton radioButtonTail;
    private              JTextField   inputField;
    private final        JButton      buttonLoad;
    private final        JButton      buttonSave;
    private final        JLabel       labelWriteHere;
    private final        JScrollPane  scrollPane;
    private final        JTextArea    lyricsArea;
    private              Color        colorLabel   = Color.WHITE;
    private static       String       DIR_ALL_WORDS;
    private static       String       DIR_USER_WORDS;
    private static final String       DIR_APP_LOGO = "rhymerLogo.ico";
    private static       int          degree       = 2;
    private static final List<String> wordsAll     = new ArrayList();
    private static final List<String> wordsRhyming = new ArrayList();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                logger.debug("Started Lyrics Genius v3");
                ConfigurationLoader.initialize();
                ApplicationFrame frame = new ApplicationFrame();
                ImageIcon frameIconImg = new ImageIcon(DIR_APP_LOGO);
                frame.setIconImage(frameIconImg.getImage());
                frame.setVisible(true);
                String languageDir = ConfigurationLoader.getString("language");
                DIR_ALL_WORDS = languageDir + "/all.txt";
                DIR_USER_WORDS = languageDir + "/user.txt";
                RhymeEngine.getInstance().getAllWords(DIR_ALL_WORDS, wordsAll);
                RhymeEngine.getInstance().getAllWords(DIR_USER_WORDS, wordsAll);
                Collections.sort(wordsAll);
            } catch (IOException e) {
                logger.error("IO failed.", e);
            }
        });
    }

    public ApplicationFrame() {

        setDefaultCloseOperation(3);
        setResizable(false);
        setSize(640, 480);
        setLocationRelativeTo(null);
        setTitle(ConfigurationLoader.getString("applicationTitle"));

        contentPane = new JPanel();
        contentPane.setBackground(Color.DARK_GRAY);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        rdbtnGroup = new ButtonGroup();

        JPanel panelLyrics = new JPanel();
        panelLyrics.setForeground(Color.WHITE);
        panelLyrics.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
                                               ConfigurationLoader.getString("titleLyrics"),
                                               4,
                                               2,
                                               null,
                                               new Color(255, 255, 255)));
        panelLyrics.setBackground(Color.DARK_GRAY);
        panelLyrics.setBounds(15, 10, 310, 435);
        contentPane.add(panelLyrics);
        panelLyrics.setLayout(null);

        labelWriteHere = new JLabel(ConfigurationLoader.getString("labelWriteHere"));
        labelWriteHere.setBounds(25, 20, 89, 14);
        panelLyrics.add(labelWriteHere);
        labelWriteHere.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"),
                                        0,
                                        ConfigurationLoader.getInteger("defaultLabelTextSize") + 2));
        labelWriteHere.setForeground(colorLabel);

        checkBoxSaveAsRhyme = new JCheckBox(ConfigurationLoader.getString("checkBoxAsRhymes"));
        checkBoxSaveAsRhyme.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelInformation.setText(ConfigurationLoader.getString("enteredAsRhymes"));
            }
        });
        checkBoxSaveAsRhyme.setBounds(166, 371, 90, 23);
        panelLyrics.add(checkBoxSaveAsRhyme);
        checkBoxSaveAsRhyme.setOpaque(false);
        checkBoxSaveAsRhyme.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"),
                                             0,
                                             ConfigurationLoader.getInteger("defaultLabelTextSize") + 2));
        checkBoxSaveAsRhyme.setForeground(Color.WHITE);

        buttonSave = new JButton(ConfigurationLoader.getString("buttonLoad"));
        buttonSave.setBounds(25, 401, 115, 23);
        panelLyrics.add(buttonSave);
        buttonSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent arg0) {
                buttonSave.setBackground(Color.YELLOW);
                labelInformation.setText(ConfigurationLoader.getString("enteredLoad"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonSave.setBackground(Color.ORANGE);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                RhymeEngine.getInstance().loadLyrics(lyricsArea);
            }
        });
        buttonSave.setBackground(Color.ORANGE);

        buttonLoad = new JButton(ConfigurationLoader.getString("buttonSave"));
        buttonLoad.setBounds(170, 401, 115, 23);
        panelLyrics.add(buttonLoad);
        buttonLoad.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent arg0) {
                buttonLoad.setBackground(Color.YELLOW);
                labelInformation.setText(ConfigurationLoader.getString("enteredSave"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonLoad.setBackground(Color.ORANGE);
            }

            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (!checkBoxSaveAsRhyme.isSelected()) {
                    try {
                        RhymeEngine.getInstance().saveLyrics(lyricsArea);
                    } catch (IOException e) {
                        logger.error("IO failed.", e);
                    }
                } else {
                    try {
                        RhymeEngine.getInstance().saveRhymes(DIR_USER_WORDS, lyricsArea);
                    } catch (IOException e) {
                        logger.error("IO failed.", e);
                    }
                }
            }
        });
        buttonLoad.setBackground(Color.ORANGE);

        scrollPane = new JScrollPane();
        scrollPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelInformation.setText(ConfigurationLoader.getString("enteredLyrics"));
            }
        });
        scrollPane.setBounds(25, 44, 260, 315);
        panelLyrics.add(scrollPane);
        scrollPane.setVerticalScrollBarPolicy(22);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        lyricsArea = new JTextArea();
        scrollPane.setViewportView(lyricsArea);
        lyricsArea.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"), 0, 14));
        lyricsArea.setCaretColor(Color.WHITE);
        lyricsArea.setLineWrap(true);
        lyricsArea.setWrapStyleWord(true);
        lyricsArea.setForeground(Color.WHITE);
        lyricsArea.setBackground(Color.WHITE);
        lyricsArea.setSelectionColor(Color.LIGHT_GRAY);
        lyricsArea.setOpaque(false);
        lyricsArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent arg0) {
                if (arg0.isAltDown())
                    if (arg0.getKeyChar() == 'e') {
                        inputField.setText(lyricsArea.getSelectedText());
                    } else if (arg0.getKeyChar() == 'r') {
                        RhymeEngine.getInstance().makeRhyme(inputField.getText(),
                                                            wordsAll, wordsRhyming, degree,
                                                            checkBoxVowels.isSelected(), checkBoxConsonants.isSelected(),
                                                            rdbtnGroup.getSelection().getActionCommand());
                        comboBox.removeAllItems();
                        for (int i = 0; i < wordsRhyming.size(); i++)
                            comboBox.addItem(wordsRhyming.get(i));
                    } else if (arg0.getKeyChar() == 'd') {
                        if (degree > 0)
                            degree -= 1;
                        labelRhymeLengthNum.setText(String.valueOf(degree));
                    } else if (arg0.getKeyChar() == 'f') {
                        degree += 1;
                        labelRhymeLengthNum.setText(String.valueOf(degree));
                    } else if (arg0.getKeyChar() == 'x') {
                        checkBoxConsonants.setSelected(!checkBoxConsonants.isSelected());
                    } else if (arg0.getKeyChar() == 'z') {
                        checkBoxVowels.setSelected(!checkBoxConsonants.isSelected());
                    } else if (arg0.getKeyChar() == 'c') {
                        if (radioButtonHead.isSelected())
                            radioButtonTail.setSelected(true);
                        else if (radioButtonTail.isSelected())
                            radioButtonBoth.setSelected(true);
                        else
                            radioButtonHead.setSelected(true);
                    } else if (arg0.getKeyChar() == 'v') {
                        if (comboBox.getSelectedItem() != null)
                            lyricsArea.insert(comboBox.getSelectedItem().toString(),
                                              lyricsArea.getCaretPosition());
                    } else if (arg0.getKeyChar() == 'a') {
                        checkBoxIncludeDefaultRhymes.setSelected(!checkBoxConsonants.isSelected());
                    } else if (arg0.getKeyChar() == 's') {
                        checkBoxIncludeMyRhymes.setSelected(!checkBoxIncludeMyRhymes.isSelected());
                    }
            }
        });
        JPanel panelInfo = new JPanel();
        panelInfo.setBorder(new TitledBorder(null, ConfigurationLoader.getString("titleInformation"), 4, 2, null, Color.WHITE));
        panelInfo.setBackground(Color.DARK_GRAY);
        panelInfo.setBounds(350, 10, 270, 140);
        contentPane.add(panelInfo);

        labelInformation = new JLabel(ConfigurationLoader.getString("labelWelcome"));
        labelInformation.setHorizontalAlignment(0);
        labelInformation.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelInformation.setText(ConfigurationLoader.getString("enteredHelp"));
            }
        });
        labelInformation.setForeground(Color.WHITE);
        labelInformation.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"),
                                          0,
                                          ConfigurationLoader.getInteger("defaultLabelTextSize") + 2));
        panelInfo.add(labelInformation);

        JPanel panelRhyme = new JPanel();
        panelRhyme.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
                                              ConfigurationLoader.getString("titleRhymeParameters"),
                                              4,
                                              2,
                                              null,
                                              Color.WHITE));
        panelRhyme.setBackground(Color.DARK_GRAY);
        panelRhyme.setBounds(350, 160, 270, 285);
        contentPane.add(panelRhyme);
        panelRhyme.setLayout(null);

        final JButton rhymeButton = new JButton(ConfigurationLoader.getString("buttonRhyme"));
        rhymeButton.setBounds(165, 250, 95, 23);
        panelRhyme.add(rhymeButton);
        rhymeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent arg0) {
                rhymeButton.setBackground(Color.YELLOW);
                labelInformation.setText(ConfigurationLoader.getString("enteredRhyme"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                rhymeButton.setBackground(Color.ORANGE);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                RhymeEngine.getInstance().makeRhyme(inputField.getText(),
                                                    wordsAll, wordsRhyming, degree,
                                                    checkBoxVowels.isSelected(), checkBoxConsonants.isSelected(),
                                                    rdbtnGroup.getSelection().getActionCommand());
                comboBox.removeAllItems();
                for (int i = 0; i < wordsRhyming.size(); i++)
                    comboBox.addItem(wordsRhyming.get(i));
            }
        });
        rhymeButton.setBackground(Color.ORANGE);

        inputField = new JTextField();
        inputField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelInformation.setText(ConfigurationLoader.getString("enteredRhymingWith"));
            }
        });
        inputField.setBounds(130, 20, 130, 23);
        panelRhyme.add(inputField);
        inputField.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"), 0, 14));
        inputField.setColumns(10);

        radioButtonTail = new JRadioButton(ConfigurationLoader.getString("radioButtonTail"));
        radioButtonTail.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelInformation.setText(ConfigurationLoader.getString("enteredTail"));
            }
        });
        radioButtonTail.setBounds(175, 203, 43, 23);
        panelRhyme.add(radioButtonTail);
        radioButtonTail.setOpaque(false);
        radioButtonTail.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"),
                                         0,
                                         ConfigurationLoader.getInteger("defaultLabelTextSize")));
        radioButtonTail.setForeground(colorLabel);
        radioButtonTail.setActionCommand(MatchFrom.TAIL.toString());
        rdbtnGroup.add(radioButtonTail);

        radioButtonHead = new JRadioButton(ConfigurationLoader.getString("radioButtonHead"));
        radioButtonHead.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelInformation.setText(ConfigurationLoader.getString("enteredHead"));
            }
        });
        radioButtonHead.setBounds(130, 203, 53, 23);
        panelRhyme.add(radioButtonHead);
        radioButtonHead.setOpaque(false);
        radioButtonHead.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"),
                                         0,
                                         ConfigurationLoader.getInteger("defaultLabelTextSize")));
        radioButtonHead.setForeground(colorLabel);
        radioButtonHead.setActionCommand(MatchFrom.HEAD.toString());
        rdbtnGroup.add(radioButtonHead);

        radioButtonBoth = new JRadioButton(ConfigurationLoader.getString("radioButtonBoth"));
        radioButtonBoth.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelInformation.setText(ConfigurationLoader.getString("enteredBoth"));
            }
        });
        radioButtonBoth.setBounds(210, 203, 53, 23);
        panelRhyme.add(radioButtonBoth);
        radioButtonBoth.setOpaque(false);
        radioButtonBoth.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"),
                                         0,
                                         ConfigurationLoader.getInteger("defaultLabelTextSize")));
        radioButtonBoth.setForeground(colorLabel);
        radioButtonBoth.setSelected(true);
        radioButtonBoth.setActionCommand(MatchFrom.BOTH.toString());
        rdbtnGroup.add(radioButtonBoth);

        checkBoxVowels = new JCheckBox(ConfigurationLoader.getString("checkBoxVows"));
        checkBoxVowels.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelInformation.setText(ConfigurationLoader.getString("enteredVows"));
            }
        });
        checkBoxVowels.setBounds(130, 106, 59, 23);
        panelRhyme.add(checkBoxVowels);
        checkBoxVowels.setOpaque(false);
        checkBoxVowels.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"), 0, ConfigurationLoader.getInteger("defaultLabelTextSize")));
        checkBoxVowels.setForeground(colorLabel);

        checkBoxVowels.setSelected(true);

        checkBoxConsonants = new JCheckBox(ConfigurationLoader.getString("checkBoxCons"));
        checkBoxConsonants.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelInformation.setText(ConfigurationLoader.getString("enteredCons"));
            }
        });
        checkBoxConsonants.setBounds(202, 106, 53, 23);
        panelRhyme.add(checkBoxConsonants);
        checkBoxConsonants.setOpaque(false);
        checkBoxConsonants.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"),
                                            0,
                                            ConfigurationLoader.getInteger("defaultLabelTextSize")));
        checkBoxConsonants.setForeground(colorLabel);

        checkBoxConsonants.setSelected(true);

        comboBox = new JComboBox();
        comboBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelInformation.setText(ConfigurationLoader.getString("enteredList"));
            }
        });
        comboBox.setBounds(25, 250, 122, 23);
        panelRhyme.add(comboBox);
        comboBox.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"), 0, 14));

        labelWord = new JLabel(ConfigurationLoader.getString("labelRhymingWith"));
        labelWord.setBounds(25, 25, 85, 15);
        panelRhyme.add(labelWord);
        labelWord.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"), 0, ConfigurationLoader.getInteger("defaultLabelTextSize") + 2));
        labelWord.setForeground(colorLabel);

        labelMatch = new JLabel(ConfigurationLoader.getString("labelMatch"));
        labelMatch.setBounds(25, 110, 75, 15);
        panelRhyme.add(labelMatch);
        labelMatch.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"), 0, ConfigurationLoader.getInteger("defaultLabelTextSize") + 2));
        labelMatch.setForeground(Color.WHITE);

        labelRhymeLength = new JLabel(ConfigurationLoader.getString("labelLength"));
        labelRhymeLength.setBounds(25, 163, 60, 15);
        panelRhyme.add(labelRhymeLength);
        labelRhymeLength.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"),
                                          0,
                                          ConfigurationLoader.getInteger("defaultLabelTextSize") + 2));
        labelRhymeLength.setForeground(colorLabel);

        buttonIncrement = new JButton("+");
        buttonIncrement.setBounds(200, 160, 60, 23);
        panelRhyme.add(buttonIncrement);
        buttonIncrement.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                degree += 1;
                labelRhymeLengthNum.setText(String.valueOf(degree));
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
                buttonIncrement.setBackground(Color.YELLOW);
                labelInformation.setText(ConfigurationLoader.getString("enteredPlus"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonIncrement.setBackground(Color.ORANGE);
            }
        });
        buttonIncrement.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"), 0, 11));
        buttonIncrement.setBackground(Color.ORANGE);

        buttonDecrement = new JButton("-");
        buttonDecrement.setBounds(130, 160, 60, 23);
        panelRhyme.add(buttonDecrement);
        buttonDecrement.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (degree > 0)
                    degree -= 1;
                labelRhymeLengthNum.setText(String.valueOf(degree));
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
                buttonDecrement.setBackground(Color.YELLOW);
                labelInformation.setText(ConfigurationLoader.getString("enteredMinus"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonDecrement.setBackground(Color.ORANGE);
            }
        });
        buttonDecrement.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"), 0, 11));
        buttonDecrement.setBackground(Color.ORANGE);

        JLabel fromLabel = new JLabel(ConfigurationLoader.getString("labelFrom"));
        fromLabel.setBounds(25, 207, 45, 15);
        panelRhyme.add(fromLabel);
        fromLabel.setForeground(Color.WHITE);
        fromLabel.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"), 0, ConfigurationLoader.getInteger("defaultLabelTextSize") + 2));

        labelRhymeLengthNum = new JLabel(String.valueOf(degree));
        labelRhymeLengthNum.setBounds(167, 139, 56, 14);
        panelRhyme.add(labelRhymeLengthNum);
        labelRhymeLengthNum.setHorizontalAlignment(0);
        labelRhymeLengthNum.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"), 0, 14));
        labelRhymeLengthNum.setForeground(Color.WHITE);

        labelIncludeSource = new JLabel(ConfigurationLoader.getString("labelSource"));
        labelIncludeSource.setForeground(Color.WHITE);
        labelIncludeSource.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"),
                                            0,
                                            ConfigurationLoader.getInteger("defaultLabelTextSize") + 2));
        labelIncludeSource.setBounds(25, 67, 59, 15);
        panelRhyme.add(labelIncludeSource);

        checkBoxIncludeDefaultRhymes = new JCheckBox(ConfigurationLoader.getString("checkBoxDefault"));
        checkBoxIncludeDefaultRhymes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelInformation.setText(ConfigurationLoader.getString("enteredDefault"));
            }
        });
        checkBoxIncludeDefaultRhymes.setBounds(130, 63, 65, 23);
        panelRhyme.add(checkBoxIncludeDefaultRhymes);
        checkBoxIncludeDefaultRhymes.setSelected(true);
        checkBoxIncludeDefaultRhymes.addItemListener(arg0 -> {
            wordsAll.clear();
            if (checkBoxIncludeDefaultRhymes.isSelected()) {
                RhymeEngine.getInstance().getAllWords(DIR_ALL_WORDS, wordsAll);
            }
            if (checkBoxIncludeMyRhymes.isSelected()) {
                RhymeEngine.getInstance().getAllWords(DIR_USER_WORDS, wordsAll);
            }
            Collections.sort(wordsAll);
        });
        checkBoxIncludeDefaultRhymes.setOpaque(false);
        checkBoxIncludeDefaultRhymes.setForeground(Color.WHITE);
        checkBoxIncludeDefaultRhymes.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"),
                                                      0,
                                                      ConfigurationLoader.getInteger("defaultLabelTextSize")));

        checkBoxIncludeMyRhymes = new JCheckBox(ConfigurationLoader.getString("checkBoxUser"));
        checkBoxIncludeMyRhymes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelInformation.setText(ConfigurationLoader.getString("enteredUser"));
            }
        });
        checkBoxIncludeMyRhymes.setBounds(202, 63, 49, 23);
        panelRhyme.add(checkBoxIncludeMyRhymes);
        checkBoxIncludeMyRhymes.setSelected(true);
        checkBoxIncludeMyRhymes.addItemListener(arg0 -> {
            wordsAll.clear();
            if (checkBoxIncludeDefaultRhymes.isSelected()) {
                RhymeEngine.getInstance().getAllWords(DIR_ALL_WORDS, wordsAll);
            }
            if (checkBoxIncludeMyRhymes.isSelected()) {
                RhymeEngine.getInstance().getAllWords(DIR_USER_WORDS, wordsAll);
            }
            Collections.sort(wordsAll);
        });
        checkBoxIncludeMyRhymes.setOpaque(false);
        checkBoxIncludeMyRhymes.setForeground(Color.WHITE);
        checkBoxIncludeMyRhymes.setFont(new Font(ConfigurationLoader.getString("defaultTypeFace"),
                                                 0,
                                                 ConfigurationLoader.getInteger("defaultLabelTextSize")));
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent arg0) {
                if (arg0.isAltDown())
                    if (arg0.getKeyChar() == 'e') {
                        inputField.setText(lyricsArea.getSelectedText());
                    } else if (arg0.getKeyChar() == 'r') {
                        RhymeEngine.getInstance().makeRhyme(inputField.getText(),
                                                            wordsAll, wordsRhyming, degree,
                                                            checkBoxVowels.isSelected(), checkBoxConsonants.isSelected(),
                                                            rdbtnGroup.getSelection().getActionCommand());
                        comboBox.removeAllItems();
                        for (int i = 0; i < wordsRhyming.size(); i++)
                            comboBox.addItem(wordsRhyming.get(i));
                    } else if (arg0.getKeyChar() == 'd') {
                        if (degree > 0)
                            degree -= 1;
                        labelRhymeLengthNum.setText(String.valueOf(degree));
                    } else if (arg0.getKeyChar() == 'f') {
                        degree += 1;
                        labelRhymeLengthNum.setText(String.valueOf(degree));
                    } else if (arg0.getKeyChar() == 'x') {
                        checkBoxConsonants.setSelected(!checkBoxConsonants.isSelected());
                    } else if (arg0.getKeyChar() == 'z') {
                        checkBoxVowels.setSelected(!checkBoxVowels.isSelected());
                    } else if (arg0.getKeyChar() == 'c') {
                        if (radioButtonTail.isSelected())
                            radioButtonHead.setSelected(true);
                        else
                            radioButtonTail.setSelected(true);
                    } else if (arg0.getKeyChar() == 'v') {
                        if (comboBox.getSelectedItem() != null)
                            lyricsArea.insert(comboBox.getSelectedItem().toString(),
                                              lyricsArea.getCaretPosition());
                    } else if (arg0.getKeyChar() == 'a') {
                        checkBoxIncludeDefaultRhymes.setSelected(!checkBoxIncludeDefaultRhymes.isSelected());
                    } else if (arg0.getKeyChar() == 's') {
                        checkBoxIncludeMyRhymes.setSelected(!checkBoxIncludeDefaultRhymes.isSelected());
                    }
            }
        });
    }

}