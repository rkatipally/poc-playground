package poc.raj.advanced.optimization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


public class CustomDateSerializer extends JsonSerializer<Optional<LocalDate>> {

    private static final String ATPCO_DATE_FORMAT = "ddMMMyy";

    public CustomDateSerializer() {
    }

    @Override
    public void serialize(Optional<LocalDate> localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (localDate.isPresent()){
            jsonGenerator.writeString(localDate.get().format(DateTimeFormatter.ofPattern(ATPCO_DATE_FORMAT)));
        }else {
            jsonGenerator.writeString("");
        }
    }


}
