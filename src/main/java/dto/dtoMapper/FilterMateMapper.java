package dto.dtoMapper;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FilterMateMapper {

    public static Map<String, FilterMeta> buildFilterMetaMap(Map<String, String> raw) {
        if (raw == null) return Collections.emptyMap();

        Map<String, FilterMeta> out = new HashMap<>();
        raw.forEach((field, value) -> {
            // Convert frontend field names to proper nested paths
            String processedField = field.replace('_', '.');

            out.put(processedField, new FilterMeta(
                    processedField,  // Field name with dot notation
                    null,             // No columnKey
                    null,             // No ValueExpression
                    MatchMode.CONTAINS, // Default match mode
                    value             // Filter value
            ));
        });
        return out;
    }
}
