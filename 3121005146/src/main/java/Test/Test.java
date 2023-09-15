package Test;
import SimHash.SimHash;
import java.io.IOException;
import static Demo.Enter.ReadFile;

public class Test {
    public static void main(String[] args) throws IOException {
        TestFunction("D:\\txt\\orig.txt","D:\\txt\\orig_0.8_add.txt");
        TestFunction("D:\\txt\\orig.txt","D:\\txt\\orig_0.8_del.txt");
        TestFunction("D:\\txt\\orig.txt","D:\\txt\\orig_0.8_dis_1.txt");
        TestFunction("D:\\txt\\orig.txt","D:\\txt\\orig_0.8_dis_10.txt");
        TestFunction("D:\\txt\\orig.txt","D:\\txt\\orig_0.8_dis_15.txt");
    }
    @org.junit.jupiter.api.Test
    public static void TestFunction(String filePath1,String filePath2) throws IOException {
        SimHash simhash1 = null,simhash2 = null;
        try {
            simhash1 = new SimHash(ReadFile(filePath1), 64);
            simhash2 = new SimHash(ReadFile(filePath2), 64);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("读取，创建失败");
        }
        try{
            assert simhash1 != null;
            assert simhash2 != null;
            System.out.println("文本相似度：" + simhash2.getSemblance(simhash1)+"\n");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("计算失败");
        }
    }
}
