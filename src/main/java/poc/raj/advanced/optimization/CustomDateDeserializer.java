package poc.raj.advanced.optimization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;


public class CustomDateDeserializer extends JsonDeserializer<Optional<LocalDate>> {
    public static final String ATPCO_DATE_FORMAT = "ddMMMyy";

    public CustomDateDeserializer() {
    }

    public Optional<LocalDate> deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException {
        return StringUtils.isNotEmpty(jsonparser.getText()) ? DateUtil.parseToOptionalLocalDate(jsonparser.getText(), ATPCO_DATE_FORMAT) : Optional.empty();
    }
}
