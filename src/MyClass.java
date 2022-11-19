import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyClass {
    public Map<? extends Comparable<String>, ? super Comparable<String>> someMethodCanReturnable() {
//        Map<A, Comparable<String>> a = new HashMap<>();
        return new ReturnableClass();
    }

    public Map<? extends Comparable<Double>, ? super Comparable<String>> someMethodCantReturnable() {
        Map<Comparable<Double>, Comparable<String>> a = new HashMap<>();
        return a;
    }

    public Map<Integer, Double> someSimpleMethod() {
        Map<Integer, Double> a = new HashMap<>();
        return a;
    }
}

//interface MySuperInterFace extends Comparable<String>{
//
//}

class A implements Comparable<String> {

    @Override
    public int compareTo(String o) {
        return 0;
    }
}

class ReturnableClass implements Map<A, Comparable<String>> {

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Comparable<String> get(Object key) {
        return null;
    }

    @Override
    public Comparable<String> put(A key, Comparable<String> value) {
        return null;
    }

    @Override
    public Comparable<String> remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends A, ? extends Comparable<String>> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<A> keySet() {
        return null;
    }

    @Override
    public Collection<Comparable<String>> values() {
        return null;
    }

    @Override
    public Set<Entry<A, Comparable<String>>> entrySet() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
