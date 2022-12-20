package klimov.impl;

import klimov.interfaces.CheckMaker;
import klimov.models.Product;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckMakerImplTest {

  public static final String ROUND_VALUE = "12.23";
  public static final String ROUND_UP_VALUE = "12.24";
  public static final String PARAM_TEST_NEGATIVE = "-1234";
  public static final String PARAM_TEST = "1234";
  private final CheckMaker checkCreator = new CheckMakerImpl();

  private static final Map<Integer, Integer> map;
  private static final Map<Integer, Product> productsList;
  private static final Map<Product, Double> totalPriceList;

  static {
    map = new HashMap<>();
    map.put(1, 6);
    map.put(3, 1);
    map.put(2, 6);
    map.put(5, 6);
    map.put(6, 6);

    productsList = new HashMap<>();
    productsList.put(1, Product.builder().name("Cookies").price(1.15).discount(false).build());
    productsList.put(2, Product.builder().name("Bananas").price(2.36).discount(false).build());
    productsList.put(
        3, Product.builder().name("Milk chocolate").price(1.35).discount(true).build());
    productsList.put(
        4, Product.builder().name("Chickens meat").price(4.60).discount(false).build());
    productsList.put(5, Product.builder().name("Oatmeal").price(0.50).discount(true).build());
    productsList.put(6, Product.builder().name("Beef").price(7.15).discount(false).build());
    productsList.put(7, Product.builder().name("Fish").price(8.10).discount(false).build());
    productsList.put(
        8, Product.builder().name("Cottage cheese").price(1.05).discount(false).build());
    productsList.put(9, Product.builder().name("Milk").price(0.98).discount(true).build());
    productsList.put(10, Product.builder().name("Bread").price(0.86).discount(false).build());

    totalPriceList = new HashMap<>();
    totalPriceList.put(
        Product.builder().name("Cookies").price(1.15).discount(false).build(), 6.9);
    totalPriceList.put(
        Product.builder().name("Bananas").price(2.36).discount(false).build(), 14.10);
    totalPriceList.put(
        Product.builder().name("Milk chocolate").price(1.35).discount(true).build(), 15.14);
  }

  @Test
  void testBuilderStringForStartParameters() {
    String[] testString =
        new String[] {"1-6 3-1 2-6 5-6 6-6 card-1234 src\\main\\resources\\StoreList.csv"};
    testString = checkCreator.createInputMass(testString);

    assertEquals(PARAM_TEST_NEGATIVE, testString[1]);
  }

  @Test
  void testSplitCardNumber() {
    String testString = "-1234 ";
    testString = checkCreator.getActualCardNumber(testString);

    assertEquals(PARAM_TEST, testString);
  }

  @Test
  void testCreateMapByItemIdQuantity() {
    String testString = "1-6 3-1 2-6 5-6 6-6 ";
    Map<Integer, Integer> testMap = checkCreator.createMapForItemIdAndQuantity(testString);

    assertEquals(testMap.keySet(), map.keySet());
    assertEquals(testMap.get(3), map.get(3));
  }

  @Test
  void testRound() {
    String test1 = checkCreator.round(12.23000000, 2);
    String test2 = checkCreator.round(12.23600000, 2);

    assertEquals(ROUND_VALUE, test1);
    assertEquals(ROUND_UP_VALUE, test2);
  }

  @Test
  void testCalculatePrice() {
    Map<Product, Double> testTotalPrice = checkCreator.createMapWithTotalPrice(map, productsList);
    Product testProduct = Product.builder().name("Cookies").price(1.15).discount(false).build();

    assertEquals(testTotalPrice.get(testProduct), totalPriceList.get(testProduct));
  }
}
