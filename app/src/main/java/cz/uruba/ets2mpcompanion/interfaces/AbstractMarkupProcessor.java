package cz.uruba.ets2mpcompanion.interfaces;

import java.util.Map;

public abstract class AbstractMarkupProcessor {
    public abstract String processMarkup(String inputString);

    protected String getStringFromMarkup(String string, Map<String, String> matchMap) {
        for (Map.Entry<String, String> mapElement : matchMap.entrySet()) {
            String markupMatcher = String.format("%%%s%%", mapElement.getKey());
            string = string.replace(markupMatcher, mapElement.getValue());
        }

        return string;
    }
}
