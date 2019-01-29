package java8;

import java.util.HashMap;
import java.util.Map;

/**
 * j
 * @author linlazy
 */
public class Java8Test {
    public static void main(String[] args) {
        Map<String,String> map = new HashMap<>();
        map.put("key","value");
        map.merge("key","value",(String v1,String v2)-> {
            return v1 +"hheee"+ v2;
        });
        System.out.println(map);
    }
}
