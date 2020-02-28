package com.project;

import java.io.*;

class FileEditor {

    String loadFile(String filename) {
        StringBuilder result = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String readLine;

            while ((readLine = reader.readLine()) != null) {
                result.append(readLine);
                result.append("\n");
            }
        } catch (IOException e) {
            return null;
        }

        return result.toString();
    }

    boolean saveFile(String filename, String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(text);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
