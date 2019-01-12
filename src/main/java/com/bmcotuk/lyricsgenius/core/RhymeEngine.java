package com.bmcotuk.lyricsgenius.core;

import com.bmcotuk.lyricsgenius.conf.ConfigurationLoader;
import com.bmcotuk.lyricsgenius.data.MatchFrom;
import com.bmcotuk.lyricsgenius.gui.ApplicationFrame;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Author: B. Mert Cotuk
 * Date:   09.01.2019
 * Time:   22:26
 * https://github.com/bmertcotuk
 */
public class RhymeEngine {

    private static final Logger      logger                       = Logger.getLogger(RhymeEngine.class);
    private static final String      CHAR_SET_NAME                = "UTF-8";
    private static final String      VOWEL_ZERO_OR_MORE_REGEX     = "[aeıioöuüâîû]*";
    private static final String      VOWEL_ONE_OR_MORE_REGEX      = "[aeıioöuüâîû]+";
    private static final String      CONSONANT_ZERO_OR_MORE_REGEX = "[^aeıioöuüâîû]*";
    private static final String      CONSONANT_ONE_OR_MORE_REGEX  = "[^aeıioöuüâîû]+";
    private static       RhymeEngine instance;

    public static RhymeEngine getInstance() {
        if (instance == null)
            instance = new RhymeEngine();
        return instance;
    }

    private void makeVowelRhyme(String input, List<String> inList, List<String> outList) {
        String inputRegex = prepare(input).replaceAll(CONSONANT_ONE_OR_MORE_REGEX, CONSONANT_ZERO_OR_MORE_REGEX);
        inputRegex = CONSONANT_ZERO_OR_MORE_REGEX + inputRegex + CONSONANT_ZERO_OR_MORE_REGEX;
        compileAndFillResults(inputRegex, inList, outList);
        logger.info("Found " + outList.size() + " rhymes out of " + inList.size() + " words.");
    }

    private void makeVowelRhymeFromHead(String input, List<String> inList, List<String> outList, int length) {

        Pattern patternVowels = Pattern.compile(VOWEL_ONE_OR_MORE_REGEX);
        String[] matches = patternVowels.matcher(input)
                                        .results()
                                        .map(MatchResult::group)
                                        .toArray(String[]::new);

        StringBuilder sb = new StringBuilder();
        sb.append("^(");
        for (int i = 0; i < length && i < matches.length; i++) {
            sb.append(CONSONANT_ZERO_OR_MORE_REGEX);
            sb.append(matches[i]);
        }
        sb.append(").*");

        compileAndFillResults(sb.toString(), inList, outList);
        logger.info("Found " + outList.size() + " rhymes out of " + inList.size() + " words.");
    }

    private void makeVowelRhymeFromTail(String input, List<String> inList, List<String> outList, int length) {

        Pattern patternVowels = Pattern.compile(VOWEL_ONE_OR_MORE_REGEX);
        String[] matches = patternVowels.matcher(input)
                                        .results()
                                        .map(MatchResult::group)
                                        .toArray(String[]::new);

        StringBuilder sb = new StringBuilder();
        sb.append(".*(");
        for (int i = 0; i < length && i < matches.length; i++) {
            sb.append(CONSONANT_ZERO_OR_MORE_REGEX);
            sb.append(matches[matches.length - length + i]);
        }
        sb.append(")$");

        compileAndFillResults(sb.toString(), inList, outList);
        logger.info("Found " + outList.size() + " rhymes out of " + inList.size() + " words.");
    }

    private void makeConsonantRhyme(String input, List<String> inList, List<String> outList) {
        String inputRegex = prepare(input).replaceAll(VOWEL_ONE_OR_MORE_REGEX, VOWEL_ZERO_OR_MORE_REGEX);
        inputRegex = VOWEL_ZERO_OR_MORE_REGEX + inputRegex + VOWEL_ZERO_OR_MORE_REGEX;
        compileAndFillResults(inputRegex, inList, outList);
        logger.info("Found " + outList.size() + " rhymes out of " + inList.size() + " words.");
    }

    private void makeConsonantRhymeFromHead(String input, List<String> inList, List<String> outList, int length) {

        Pattern patternVowels = Pattern.compile(CONSONANT_ONE_OR_MORE_REGEX);
        String[] matches = patternVowels.matcher(input)
                                        .results()
                                        .map(MatchResult::group)
                                        .toArray(String[]::new);

        StringBuilder sb = new StringBuilder();
        sb.append("^(");
        for (int i = 0; i < length && i < matches.length; i++) {
            sb.append(VOWEL_ZERO_OR_MORE_REGEX);
            sb.append(matches[i]);
        }
        sb.append(").*");

        compileAndFillResults(sb.toString(), inList, outList);
        logger.info("Found " + outList.size() + " rhymes out of " + inList.size() + " words.");
    }

    private void makeConsonantRhymeFromTail(String input, List<String> inList, List<String> outList, int length) {

        Pattern patternVowels = Pattern.compile(CONSONANT_ONE_OR_MORE_REGEX);
        String[] matches = patternVowels.matcher(input)
                                        .results()
                                        .map(MatchResult::group)
                                        .toArray(String[]::new);

        StringBuilder sb = new StringBuilder();
        sb.append(".*(");
        for (int i = 0; i < length && i < matches.length; i++) {
            sb.append(VOWEL_ZERO_OR_MORE_REGEX);
            sb.append(matches[matches.length - length + i]);
        }
        sb.append(")$");

        compileAndFillResults(sb.toString(), inList, outList);
        logger.info("Found " + outList.size() + " rhymes out of " + inList.size() + " words.");
    }

    private void makeFullRhyme(String input, List<String> inList, List<String> outList) {
        for (String s : inList)
            if (prepare(s).equals(prepare(input)))
                outList.add(s);
        logger.info("Found " + outList.size() + " rhymes out of " + inList.size() + " words.");
    }

    private void makeFullRhymeFromHead(String input, List<String> inList, List<String> outList, int length) {

        input = prepare(input);
        if (input.length() < length)
            return;

        for (String s : inList)
            if (prepare(s).startsWith(input.substring(0, length)))
                outList.add(s);
        logger.info("Found " + outList.size() + " rhymes out of " + inList.size() + " words.");
    }

    private void makeFullRhymeFromTail(String input, List<String> inList, List<String> outList, int length) {

        input = prepare(input);
        if (input.length() < length)
            return;

        for (String s : inList)
            if (prepare(s).endsWith(input.substring(input.length() - length)))
                outList.add(s);
        logger.info("Found " + outList.size() + " rhymes out of " + inList.size() + " words.");
    }

    private void compileAndFillResults(String inputRegex, List<String> inList, List<String> outList) {

        Pattern pattern = Pattern.compile(inputRegex);
        logger.info("Input regular expression: " + inputRegex);
        for (String s : inList)
            if (pattern.matcher(prepare(s)).matches())
                outList.add(s);
    }

    private String prepare(String s) {
        return s.toLowerCase().replaceAll("\\s", "");
    }

    public void getAllWords(String dir, List<String> list) {

        try (InputStreamReader inputStreamReader = new InputStreamReader(ApplicationFrame.class.getClassLoader().getResourceAsStream(dir),
                                                                         CHAR_SET_NAME);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            while (reader.ready())
                list.add(reader.readLine());
        } catch (UnsupportedEncodingException e1) {
            logger.error("Encoding is not supported.", e1);
        } catch (IOException e) {
            logger.error("IO failed.", e);
        }
    }

    public void makeRhyme(String input,
                          List<String> inList,
                          List<String> outList,
                          int length,
                          boolean matchVow,
                          boolean matchCon,
                          String actionCommand) {
        outList.clear();
        logger.info("Parameters are matchVow=" + matchVow +
                    ", matchCon=" + matchCon +
                    ", length=" + length +
                    ", from=" + actionCommand + ".");
        MatchFrom matchFrom = MatchFrom.valueOf(actionCommand);
        switch (matchFrom) {
            case HEAD:
                if (matchVow && !matchCon)
                    makeVowelRhymeFromHead(input, inList, outList, length);
                else if (!matchVow && matchCon)
                    makeConsonantRhymeFromHead(input, inList, outList, length);
                else if (matchVow && matchCon)
                    makeFullRhymeFromHead(input, inList, outList, length);
                break;
            case TAIL:
                if (matchVow && !matchCon)
                    makeVowelRhymeFromTail(input, inList, outList, length);
                else if (!matchVow && matchCon)
                    makeConsonantRhymeFromTail(input, inList, outList, length);
                else if (matchVow && matchCon)
                    makeFullRhymeFromTail(input, inList, outList, length);
                break;
            case BOTH:
                if (matchVow && !matchCon)
                    makeVowelRhyme(input, inList, outList);
                else if (!matchVow && matchCon)
                    makeConsonantRhyme(input, inList, outList);
                else if (matchVow && matchCon)
                    makeFullRhyme(input, inList, outList);
                break;
        }
    }

    public void saveRhymes(String dir, JTextArea textArea) throws IOException {

        try (FileOutputStream output = new FileOutputStream(dir, true)) {
            String content = "\n" + textArea.getText();
            output.write(content.getBytes(CHAR_SET_NAME));
            logger.debug("Saved rhymes to user file.");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            logger.error("Either file not found or encoding is not supported.");
        }
    }

    public void saveLyrics(JTextArea textArea) throws IOException {

        BufferedWriter output = null;
        try {
            JFileChooser saver = new JFileChooser();
            saver.showSaveDialog(saver);
            saver.setDialogTitle(ConfigurationLoader.getString("titleSaveAs"));
            if (saver.getSelectedFile().exists()) {
                Object[] options = {
                        ConfigurationLoader.getString("optionYes"),
                        ConfigurationLoader.getString("optionNo") };
                int n = JOptionPane.showOptionDialog(null,
                                                     ConfigurationLoader.getString("messageFileWillBeOverwritten"),
                                                     ConfigurationLoader.getString("titleWarning"),
                                                     0,
                                                     3,
                                                     null,
                                                     options,
                                                     options[0]);
                if (n == 0) {
                    File f = new File(saver.getCurrentDirectory(), saver.getSelectedFile().getName());
                    output = new BufferedWriter(new FileWriter(f));
                    textArea.write(output);
                }
            } else {
                File f = new File(saver.getCurrentDirectory(), saver.getSelectedFile().getName() + ConfigurationLoader.getString("fileExtension"));
                output = new BufferedWriter(new FileWriter(f));
                textArea.write(output);
            }
            logger.debug("Saved lyrics to the file: " + saver.getSelectedFile().getName());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                                          ConfigurationLoader.getString("messageFileSaveError"),
                                          ConfigurationLoader.getString("titleFileSaveError"),
                                          2);
            logger.error("Lyrics could not be saved to a file.", e);
        } finally {
            if (output != null)
                output.close();
        }
    }

    public void loadLyrics(JTextArea textArea) {
        try {
            JFileChooser chooser = new JFileChooser();
            int x = chooser.showDialog(chooser, ConfigurationLoader.getString("buttonLoadTextFile"));
            if (x == 0) {
                if (!textArea.getText().equals("")) {
                    Object[] options = {
                            ConfigurationLoader.getString("optionYes"),
                            ConfigurationLoader.getString("optionNo") };
                    int y = JOptionPane.showOptionDialog(null,
                                                         ConfigurationLoader.getString("messageTextWillBeDeleted"),
                                                         ConfigurationLoader.getString("titleWarning"),
                                                         0,
                                                         3,
                                                         null,
                                                         options,
                                                         options[0]);
                    if (y == 0) {
                        loadFromFileToTextArea(textArea, chooser);
                    }
                } else {
                    loadFromFileToTextArea(textArea, chooser);
                }
            }
        } catch (IOException e) {
            logger.error("Could not load the lyrics from a file.", e);
        }
    }

    private void loadFromFileToTextArea(JTextArea textArea, JFileChooser chooser) throws IOException {

        try (FileReader fileReader = new FileReader(chooser.getSelectedFile());
             BufferedReader reader = new BufferedReader(fileReader)) {
            textArea.setText(null);
            while (reader.ready())
                textArea.append(reader.readLine() + "\n");
            logger.debug("Loaded lyrics from the file: " + chooser.getSelectedFile().getName());
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null,
                                          ConfigurationLoader.getString("messageFileNotLoaded"),
                                          ConfigurationLoader.getString("titleFileNotLoaded"),
                                          1);
            logger.error("Could not load the text from a file.", e);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                                          ConfigurationLoader.getString("messageFileNotLoaded"),
                                          ConfigurationLoader.getString("titleFileNotLoaded"),
                                          1);
            logger.error("File not found.", e);
        }
    }
}
