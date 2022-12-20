package klimov.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
  private String name;
  private double price;
  private boolean discount;
}
