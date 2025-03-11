package ch.admin.astra.vz.lc.domain.vam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@Jacksonized
public class ResponseDataDto {

    @JsonProperty("error_code")
    private ErrorCodeDto errorCode;

    @JsonProperty("error_description")
    private String errorDescription;

    @JsonProperty("credential_subject_data")
    private Map<String, Object> credentialSubjectData;

    public Map<String, String> getCredentialSubject() {
        if (credentialSubjectData != null) {
            Map<String, String> result = new HashMap<>();
            credentialSubjectData.forEach((key, value) -> {
                switch (value) {
                    case String stringValue -> result.put(key, stringValue);
                    case Number numberValue -> {
                        double doubleValue = (numberValue).doubleValue();
                        // Check if the value is a whole number
                        if (doubleValue == Math.floor(doubleValue)) {
                            // No decimals, so cast to integer and store as a string
                            result.put(key, String.valueOf((int) doubleValue));
                        } else {
                            // Has decimals, keep it as a double in string form
                            result.put(key, value.toString());
                        }
                    }
                    default -> result.put(key, value.toString());
                }
            });
            return result;
        }
        return Collections.emptyMap();
    }
}

