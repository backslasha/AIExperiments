import javax.swing.*;

public class Entry {
    static String[][] sample = {
            {"动物种类", "身体颜色", "眼睛颜色", "白化体"},
            {"兔", "棕", "黑", "负"},
            {"兔", "白", "红", "正"},
            {"兔", "灰", "红", "负"},
            {"兔", "白", "红", "正"},
            {"象", "白", "黑", "负"},
            {"象", "白", "红", "正"},
            {"象", "灰", "红", "负"},
            {"象", "灰", "黑", "负"}

    };
    static String[][] sample1 = {
            {"颜色", "体形", "毛型", "类别"},
            {"黑", "大", "卷毛", "危险"},
            {"棕", "大", "光滑", "危险"},
            {"棕", "中", "卷毛", "不危险"},
            {"黑", "小", "卷毛", "不危险"},
            {"棕", "中", "光滑", "危险"},
            {"黑", "大", "光滑", "危险"},
            {"棕", "小", "卷毛", "危险"},
            {"棕", "小", "光滑", "不危险"},
            {"棕", "大", "卷毛", "危险"},
            {"黑", "中", "卷毛", "不危险"},
            {"黑", "中", "光滑", "不危险"},
            {"黑", "小", "光滑", "不危险"}};

    public static void main(String[] args) {
        String[][] sample = Entry.sample1;

        PolicyTreeBoard policyTreeBoard = new PolicyTreeBoard(
                ID3.buildPolicyTree(sample)
        );

        JFrame jFrame = new JFrame();
        jFrame.setLocation(640, 160);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setContentPane(policyTreeBoard);
        jFrame.setResizable(false);
        jFrame.setTitle("决策树(" + sample[0][sample[0].length - 1] + ")");
        jFrame.setVisible(true);
        jFrame.pack();

    }
}
