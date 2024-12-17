package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    private TextArea text;

    private List<String> parameters;

    @FXML
    private void handleSubmit() {
        String dir = parameters.size() > 0 ? parameters.get(0) : null;
        String content = text.getText();

        if (dir == null) {
            this.infoAndClose("该目录不存在！");
            return;
        }

        File file = new File(dir);
        if (!file.isDirectory()) {
            this.infoAndClose("该目录不是文件夹！");
            return;
        }

        try {
            File ini = new File(dir + File.separator + "desktop.ini");
            if (ini.exists()) {
                writeComment(dir, content);
            } else {
                writeCommentNewFile(dir, content);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            this.infoAndClose("IO异常！");
            return;
        }

        this.infoAndClose("成功");
    }

    @FXML
    private void cancel() {
        this.close();
    }

    private void close() {
        Platform.exit();
    }

    private void infoAndClose(String info) {
        this.showInfo(info);
        this.close();
    }

    private void showInfo(String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText(info);
        alert.showAndWait();
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    private static void writeComment(String filename, String comment) throws IOException, InterruptedException {

        Process process = Runtime.getRuntime().exec("attrib -s -h " + filename + File.separator + "desktop.ini");

        Thread.sleep(1000);

        File ini = new File(filename + File.separator + "desktop.ini");

        Lines lines = new Lines();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ini), "GBK"))) {
            String line;
            int num = 0;
            while ((line = br.readLine()) != null) {
                lines.addLine(new Line(num ++, line));
            }
        }

        Line info = lines.getLine("InfoTip");
        if (info != null) {
            info.replace("InfoTip=" + comment);
        } else {
            Line clazz = lines.getLine("ShellClassInfo");
            int clazzIndex = clazz.num;
            lines.add(clazzIndex + 1, new Line(clazzIndex + 1, "InfoTip=" + comment));
        }
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ini), "GBK"))) {
            for (Line l : lines.lines) {
                bw.write(l.content);
                bw.newLine();
            }
            bw.flush();
        }
        Runtime.getRuntime().exec("attrib +s +h " + ini.getPath());
    }

    private static void writeCommentNewFile(String filename, String comment) throws IOException {

        File ini = new File(filename + File.separator + "desktop.ini");
        File dir = ini.getParentFile();
        Runtime.getRuntime().exec("attrib +s " + dir.getPath());

        String[] s = new String[]{"[.ShellClassInfo]",
                "ConfirmFileOp=0",
                "InfoTip=" + comment};
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ini), "GBK"))) {
            for (String ss : s) {
                bw.write(ss);
                bw.newLine();
            }
            bw.flush();
        }
        Runtime.getRuntime().exec("attrib +s +h " + ini.getPath());
    }

    private static class Lines {
        private List<Line> lines = new ArrayList<>();

        private void addLine(Line line) {
            lines.add(line);
        }

        private Line getLine(String name) {
            return lines.stream().filter(l -> l.content.contains(name)).findFirst().orElse(null);
        }

        private void add(int index, Line line) {
            lines.add(index, line);
        }
    }

    private static class Line {
        private int num;
        private String content;

        private Line(int num, String content) {
            this.num = num;
            this.content = content;
        }

        private void replace(String content) {
            this.content = content;
        }
    }
}
