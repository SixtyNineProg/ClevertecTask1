package klimov.impl;

import klimov.interfaces.CheckMaker;
import klimov.models.Card;
import klimov.models.Product;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CheckMakerImpl implements CheckMaker {
  public static final int MIN_COUNT_FOR_DISCOUNT = 6;
  public static final double DISCOUNT_COEFFICIENT_FOR_THIS_PRODUCT = 0.9;
  public static final String OUTPUT_FILE_NAME = "src\\main\\resources\\Check.txt";

  @Override
  public String[] createInputMass(String[] args) {
    StringBuilder sB = new StringBuilder();
    for (String arg : args) {
      sB.append(arg).append(" ");
    }
    if (sB.toString().indexOf("card") < 4) {
      log.warn("String doesn't match format");
    }
    String[] newArgs = sB.toString().split("card");
    String[] cardAndFileName = newArgs[1].split(" ");
    newArgs = new String[] {newArgs[0], cardAndFileName[0], cardAndFileName[1]};
    return newArgs;
  }

  @Override
  public String getActualCardNumber(String s) {
    StringBuilder actualCardNumber = new StringBuilder();
    Pattern patternForNumber = Pattern.compile("\\d");
    Matcher matcherForNumber = patternForNumber.matcher(s);
    while (matcherForNumber.find()) {
      actualCardNumber.append(s, matcherForNumber.start(), matcherForNumber.end());
    }
    return actualCardNumber.toString();
  }

  @Override
  public Map<Integer, Integer> createMapForItemIdAndQuantity(String itemIdQuantityNumbers) {
    if (!Pattern.matches("(\\d-\\d )+", itemIdQuantityNumbers)) {
      log.warn("String doesn't match format");
    }
    Map<Integer, Integer> mapForItemIdAndQuantity = new HashMap<>();
    String[] idAndQtys = itemIdQuantityNumbers.split(" ");
    for (String idAndQty : idAndQtys) {
      String[] idQTY = idAndQty.split("-");
      mapForItemIdAndQuantity.put(Integer.parseInt(idQTY[0]), Integer.parseInt(idQTY[1]));
    }
    return mapForItemIdAndQuantity;
  }

  @Override
  public String round(Double value, int places) {
    if (places < 0) throw new IllegalArgumentException();
    BigDecimal bd = BigDecimal.valueOf(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return String.valueOf(bd);
  }

  @Override
  public Map<Product, Double> createMapWithTotalPrice(
      Map<Integer, Integer> map, Map<Integer, Product> products) {
    Map<Product, Double> menu = new HashMap<>();
    try {
      Integer[] itemId = map.keySet().toArray(new Integer[0]);
      for (Integer item : itemId) {
        Double price = products.get(item).getPrice();
        Integer quantity = map.get(item);
        Double totalPrice = price * quantity;
        menu.put(products.get(item), Double.valueOf(round(totalPrice, 2)));
      }
    } catch (Exception e) {
      log.warn("Not found this product");
    }
    return menu;
  }

  public void writeCheck(Map<Product, Double> menu, Card card) {

    try (PrintStream printStream = new PrintStream(OUTPUT_FILE_NAME)) {
      double totalDiscountsMoney = 0;
      double totalPriceAll = 0;
      printStream.printf("%46s%n", "CASH RECEIPT");
      printStream.printf("%48s%n", "SUPERMARKET 123");
      printStream.printf("%52s%n", "12, MILKYWAY Galaxy/ Earth");
      printStream.printf("%49s%n", "Tel :123-456-7890");
      printStream.printf(
          "%s%10s%47s%td/%tm/%tY%n",
          "CASHIER", "№1520", "DATE:", new Date(), new Date(), new Date());
      printStream.printf("%64s%tH:%tM:%tS%n", "TIME:", new Date(), new Date(), new Date());
      printStream.println(
          "-------------------------------------------------------------------------------");
      printStream.printf("%2s%24s%33s%14s%n", "QTI", "DESCRIPTION", "PRICE", "TOTAL");
      for (Product p : menu.keySet().toArray(new Product[0])) {
        String quantity = round(menu.get(p) / p.getPrice(), 0);
        if (Double.parseDouble(quantity) >= MIN_COUNT_FOR_DISCOUNT && p.isDiscount()) {
          totalDiscountsMoney =
              totalDiscountsMoney
                  + (menu.get(p) - (menu.get(p) * DISCOUNT_COEFFICIENT_FOR_THIS_PRODUCT));
          menu.put(p, menu.get(p) * DISCOUNT_COEFFICIENT_FOR_THIS_PRODUCT);
        }
        totalPriceAll = totalPriceAll + menu.get(p);
        printStream.printf(
            "%2s%24s%30s%s%10s%s%n",
            quantity, p.getName(), "$", round(p.getPrice(), 2), "$", round(menu.get(p), 2));
      }
      printStream.println(
          "===============================================================================");
      String discountsPercent =
          round(((totalDiscountsMoney / totalPriceAll) * 100) + card.getDiscount(), 0);
      totalDiscountsMoney =
          totalDiscountsMoney + (totalPriceAll * (Double.parseDouble(discountsPercent) / 100));
      Double totalPriceAllWithDiscount = totalPriceAll - totalDiscountsMoney;
      printStream.printf("%s%59s%s%n", "TAXABLE TOT", "$", round(totalPriceAllWithDiscount, 2));
      printStream.printf(
          "%s%s%s%65s%s%n", "VAT", discountsPercent, "%", "$", round(totalDiscountsMoney, 2));
      printStream.printf("%s%65s%s%n", "TOTAL", "$", round(totalPriceAll, 2));
    } catch (Exception e) {
      log.warn(String.valueOf(e));
    } finally {
      log.info("OutputFile located to:" + OUTPUT_FILE_NAME);
    }
  }
}
