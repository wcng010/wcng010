package Demo;
import SimHash.SimHash;

import java.io.*;

public class Enter {
    public static void main(String[] args) throws IOException {
        //输入
        String address1 = null,address2 = null,address3 = null;
        try {
            address1 = args[0];
            address2 = args[1];
            address3 = args[2];
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("ERROR INPUT");
        }
        String txt1 = null, txt2 = null;
        //读取文件
        try{
            txt1 = ReadFile(address1);
            txt2 = ReadFile(address2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("ERROR IO");
        }
        //通过计算海明距离得出文本相似度
        try {
            SimHash hash1 = new SimHash(txt1, 64);
            SimHash hash2 = new SimHash(txt2, 64);
            var Text_Similarity = hash1.getSemblance(hash2);
            //打印，写入文本相似度。
            System.out.println("查重率：" + Text_Similarity);
            WriteFile(address3, "查重率" + Text_Similarity);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("ERROR Calculation");
        }
    }

    public static void WriteFile(String file,String txt) throws IOException
    {
        FileWriter writer= new FileWriter(file,false);
        BufferedWriter buffer = new BufferedWriter(writer);
        buffer.write(txt);
        buffer.newLine();
        buffer.close();
        writer.close();
    }
    //读取文件
    public static String ReadFile(String path) throws IOException
    {
        FileReader reader = new FileReader(new File(path));
        StringBuilder stringBuilder = new StringBuilder();
        char[] buffer = new char[10];
        int size;
        while ((size = reader.read(buffer)) != -1) {
            stringBuilder.append(buffer, 0, size);
        }
        return stringBuilder.toString();
    }
}