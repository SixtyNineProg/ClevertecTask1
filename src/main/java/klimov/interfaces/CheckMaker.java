package klimov.interfaces;

import klimov.models.Product;

import java.util.Map;

public interface CheckMaker {
  String[] createInputMass(String[] s);

  String getActualCardNumber(String s);

  Map<Integer, Integer> createMapForItemIdAndQuantity(String itemIdQuantityNumbers);

  String round(Double value, int places);

  Map<Product, Double> createMapWithTotalPrice(
      Map<Integer, Integer> map, Map<Integer, Product> products);
}
