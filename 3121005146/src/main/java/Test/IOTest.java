package Test;

import java.io.IOException;

import static Demo.Enter.ReadFile;
import static Demo.Enter.WriteFile;


public class IOTest {
    public static void main(String[] args) {
        String txt1 = null;
        String ReadPath = "D:\\txt\\orig.txt";
        String WritePath ="D:\\write\\1.txt";
        //读取文件
        try{
            txt1 = ReadFile(ReadPath);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR IO");
        }
        System.out.println(txt1);
        try{
            WriteFile(WritePath,"My name is LiHua");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR IO");
        }
    }
}


