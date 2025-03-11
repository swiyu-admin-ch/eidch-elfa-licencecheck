package ch.admin.astra.vz.lc.domain.vam.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@Builder
@Jacksonized
public class ErrorDto implements Serializable {

    private HttpStatus status;
    private String detail;

}
