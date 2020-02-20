package poc.raj.advanced.optimization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UIFareSearchCriteria {
    private static final long serialVersionUID = 5014583174238891923L;

    private String pdt;
    private String origin;
    private String destination;
    private String market;
    private String carrier;
    private String fareClasses;
    private String cabins;
    private String fareTypeCode;
    private String passengerTypeCode;
    private String footnote;
    private String currency;
    private String rbd;
    private String minStay;
    private String advancedPurchase;
    private String seasonalityType;
    private String seasonality;
    private String accountCode;
    private String owrt;
    private String rule;
    private String routing;
    private String itineraryTripType;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Optional<LocalDate> outboundDate = Optional.ofNullable(null);
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Optional<LocalDate> inboundDate = Optional.ofNullable(null);
}
