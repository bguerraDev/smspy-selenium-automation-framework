package context;

import java.util.HashMap;
import java.util.Map;

/**
 * ScenarioContext to share state between steps/tests.
 */
public class ScenarioContext {

    private Map<String, Object> scenarioData = new HashMap<>();

    public void setContext(String key, Object value) {
        scenarioData.put(key, value);
    }

    public Object getContext(String key) {
        return scenarioData.get(key);
    }

    public <T> T getContext(String key, Class<T> clazz) {
        Object value = getContext(key);
        if (value == null) {
            return null;
        }
        return clazz.cast(value);
    }

    public boolean containsKey(String key) {
        return scenarioData.containsKey(key);
    }

    public void clear() {
        scenarioData.clear();
    }
}
