package SimHash;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
public class SimHash {
    public String txt; //字符串
    public BigInteger simHashValue;//字符产的hash值
    public int hashbits; // 分词后的hash数;
    //默认构造函数
    public SimHash()
    {
        this.txt = null;
        this.simHashValue = new BigInteger("0");
        this.hashbits = 0;
    }

    public SimHash(String txt, int hashbits) {
        this.txt = txt;
        this.hashbits = hashbits;
        this.simHashValue = this.simHash();
    }
    //海明距离
    public int hammingDistance(SimHash other) {
        //差集
        var subtract = new BigInteger("1").shiftLeft(this.hashbits).subtract(new BigInteger("1"));
        //异或
        var xor = this.simHashValue.xor(other.simHashValue).and(subtract);
        var total = 0;
        while (xor.signum() != 0) {
            total += 1;
            xor = xor.and(xor.subtract(new BigInteger("1")));
        }
        return total;
    }
    //相似度
    public double getSemblance(SimHash other) {
        double otherHamingDistacne = this.hammingDistance(other);
        var rate = (1 - otherHamingDistacne / this.hashbits) * 100;
        return Double.parseDouble(String.format("%.2f", rate));
    }


    //删除转义字符，和标点符号
    public static String toDBC(String txt) {
        char[] chars = txt.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '\u3000') {
                chars[i] = ' ';
            } else if (chars[i] > '\uFF00' && chars[i] < '\uFF5F') {
                chars[i] = (char) (chars[i] - 65248);
            }
        }
        return new String(chars);
    }

    //更改文本格式
    public String clearCharacters(String txt) {
        // 将内容转换为小写
        txt = StringUtils.lowerCase(txt);
        // 过来HTML标签
        txt = Jsoup.clean(txt, Whitelist.none());
        // 过滤特殊字符
        String[] strings = {" ", "\n", "\r", "\t", "\\r", "\\n", "\\t", "&nbsp;", "&amp;", "&lt;", "&gt;", "&quot;", "&qpos;"};
        for (String string : strings) {
            txt = txt.replaceAll(string, "");
        }
        //符号转换
        txt = toDBC(txt);
        //去空格
        txt = StringUtils.deleteWhitespace(txt);
        return txt;
    }

    //hash函数计算hansh值
    public BigInteger hash(String txt) {
        if (StringUtils.isEmpty(txt)) {
            return new BigInteger("0");
        } else {
            //分词拓位，如果词过短Hash算法会失效
            while (txt.length() < 4) {
                txt = txt + txt.charAt(0);
            }
            //分词位运算
            char[] txtArray = txt.toCharArray();
            BigInteger x = BigInteger.valueOf(((long) txtArray[0]) << 7);
            BigInteger m = new BigInteger("1000003");
            //pow运算
            BigInteger mask = new BigInteger("2").pow(this.hashbits).subtract(new BigInteger("1"));
            for (char item : txtArray) {
                BigInteger temp = BigInteger.valueOf(item);
                x = x.multiply(m).xor(temp).and(mask);
            }
            x = x.xor(new BigInteger(String.valueOf(txt.length())));
            if (x.equals(new BigInteger("-1"))) {
                x = new BigInteger("-2");
            }
            return x;
        }
    }

    public BigInteger simHash() {
        //删除转义字符
        txt = clearCharacters(txt);
        //
        int[] hashInt = new int[this.hashbits];
        // 对文本字符串进行分词
        List<Term> termList = StandardTokenizer.segment(this.txt);
        // 设置词的权重
        var wordWeigth = new HashMap<String, Integer>();
        //权重设置
        wordWeigth.put("n", 2);
        wordWeigth.put("v",3);
        //设置忽略词
        var wordIgnore = new HashMap<String, String>();
        wordIgnore.put("w", "");
        //设定超频词汇的界限 ;
        var overCount = 5;
        var wordCount = new HashMap<String, Integer>();

        //计算词的出现次数，忽略掉超出频率的词
        for (var term : termList) {
            var word = term.word;
            var nature = term.nature.toString();
            //  忽略超频词
            if (wordCount.containsKey(word)) {
                var count = wordCount.get(word);
                if (count > overCount) {
                    continue;
                }
                wordCount.put(word, count + 1);
            } else {
                wordCount.put(word, 1);
            }
            //过滤掉停用词性
            if (wordIgnore.containsKey(nature)) {
                continue;
            }

            var t = this.hash(word);
            for (int i = 0; i < this.hashbits; ++i) {
                var bitmask = new BigInteger("1").shiftLeft(i);
                // 对每个分词hash后的列进行判断，例如：1000...1，则数组的第一位和末尾一位加1,中间的62位减一，也就是，逢1加1，逢0减1，一直到把所有的分词hash列全部判断完
                var weight = 1;  //添加权重
                if (wordWeigth.containsKey(nature)) {
                    weight = wordWeigth.get(nature);
                }
                if (t.and(bitmask).signum() != 0) {
                    // 这里是计算整个文档的所有特征的向量和
                    hashInt[i] += weight;
                } else {
                    hashInt[i] -= weight;
                }
            }
        }

        BigInteger fingerprint = new BigInteger("0");
        for (int i = 0; i < this.hashbits; i++) {
            if (hashInt[i] >= 0) {
                fingerprint = fingerprint.add(new BigInteger("1").shiftLeft(i));
            }
        }
        return fingerprint;
    }
}

