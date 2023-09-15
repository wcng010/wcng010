package Test;
import SimHash.SimHash;
import static Demo.Enter.ReadFile;

public class HashTest {
    public static void main(String[] args) {
            String ReadPath = "D:\\txt\\orig.txt";
            SimHash simhash1 = null;
            String txt = null;
            try {
                txt = ReadFile(ReadPath);
                simhash1 = new SimHash(ReadFile(ReadPath), 64);
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("读取，创建失败");
            }
            try{
                assert simhash1 != null;
                System.out.println(simhash1.hash(txt));
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("读取，创建失败");
            }
    }
}
