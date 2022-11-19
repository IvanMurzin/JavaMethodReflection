import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class M {
    public <T extends Number> T m1(T val) {
        return val;
    }

    public <T extends Comparable<String>> T m2(T val) {
        return val;
    }

    public Number[] m3(Number[] val) {
        return new Integer[1];
    }

    public Map<? extends List<? extends Number>, ? extends Comparable<? super String>> m4() {
        return new HashMap<>();
    }
}