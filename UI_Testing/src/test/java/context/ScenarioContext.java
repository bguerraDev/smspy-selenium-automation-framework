package context;

import java.util.HashMap;
import java.util.Map;

/**
 * Because it is injected by PicoContainer, it is inherently thread-safe (one instance per scenario/thread).
 */

public class ScenarioContext {

    // HINT: Use a Map to store arbitrary objects by key
    private Map<String, Object> scenarioData = new HashMap<>();

    public void setContext(String key, Object value) {
        scenarioData.put(key, value);
    }

    /**
     * Returns the value for the given key, or null if not present
     */
    public Object getContext(String key) {
        return scenarioData.get(key);
    }

    /**
     * Type-safe getter with automatic cast
     * Usage: String user = scenarioContext.getContext("currentUser", String.class);
     */
    public <T> T getContext(String key, Class<T> clazz) {
        Object value = getContext(key);
        if (value == null) {
            return null;
        }
        return clazz.cast(value);
    }

    //----------

    /**
     * Type-safe getter
     * Returns null if key doesn't exist or value is null
     */
/*    public <T> T getContext(String key, Class<T> expectedType) {
        Object value = scenarioData.get(key);
        if (value == null) {
            return null;
        }
        try {
            return expectedType.cast(value);
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "Value for key '" + key + "' is not of type " + expectedType.getSimpleName() +
                            ". Actual type: " + value.getClass().getSimpleName()
            );
        }
    }*/

    public boolean containsKey(String key) {
        return scenarioData.containsKey(key);
    }

    public boolean hasKey(String key) {
        return containsKey(key);
    }

    public void clear() {
        scenarioData.clear();
    }
}
