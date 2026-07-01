package example.save2.cli;

import example.save2.cli.enums.ParameterKey;

import java.util.HashMap;
import java.util.Map;

public class ParametersStorage {

    private static final Map<ParameterKey, Object> parameters = new HashMap<>();

    private ParametersStorage() {
    }

    public static void addParameter(ParameterKey key, Object value) {
        parameters.put(key, value);
    }

    public static boolean containsKey(ParameterKey key) {
        return parameters.containsKey(key);
    }

    public static <T> T getParameter(ParameterKey key, Class<T> valueType) {
        return valueType.cast(parameters.get(key));
    }

}
