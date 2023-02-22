import java.util.List;

public class OrderGenerator {

    public static Order getDefault() {
        return new Order(List.of("61c0c5a71d1f82001bdaaa6d"));
    }

    public static Order getWithErrorHash() {
        return new Order(List.of("100500"));
    }
}
