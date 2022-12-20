package klimov.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Card {
    private int cardNumber;
    private int discount;
}
