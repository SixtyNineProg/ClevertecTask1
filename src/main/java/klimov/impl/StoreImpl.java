package klimov.impl;

import klimov.interfaces.Store;
import klimov.models.Card;
import klimov.models.Product;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Log
public class StoreImpl implements Store {
  public static final String CARDS = "Cards";
  public static final String REGEX = ";";
  private Map<Integer, Product> productsList;
  private List<Card> cardsList;

  @Override
  public Map<Integer, Product> readProducts(String fileName) {
    productsList = new HashMap<>();
    cardsList = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line = reader.readLine();
      int count = 0;
      int productsCounter = 1;
      while (line != null && count == 0) {
        String[] productParameters = line.split(REGEX);
        productsList.put(
            productsCounter,
            Product.builder()
                .name(productParameters[0])
                .price(Double.parseDouble(productParameters[1]))
                .discount(Boolean.parseBoolean(productParameters[2]))
                .build());
        productsCounter++;
        line = reader.readLine();
        if (line.equals(CARDS)) {
          count++;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return productsList;
  }

  @Override
  public List<Card> readCards(String fileName) {
    cardsList = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line = reader.readLine();
      int count = 0;
      while (line != null) {
        if (line.equals(CARDS)) {
          count++;
          line = reader.readLine();
        } else if (count != 0) {
          String[] cardParameters = line.split(REGEX);
          cardsList.add(
              Card.builder()
                  .cardNumber(Integer.parseInt(cardParameters[0]))
                  .discount(Integer.parseInt(cardParameters[1]))
                  .build());
          line = reader.readLine();
        } else {
          line = reader.readLine();
        }
      }
    } catch (IOException e) {
      log.warning(e.toString());
    }
    return cardsList;
  }

  @Override
  public Card createCard(String cardNumber, List<Card> cardsList) {
    Card actualCard = Card.builder().build();
    for (Card card : cardsList) {
      if (card.getCardNumber() == Integer.parseInt(cardNumber)) {
        actualCard = card;
      }
    }
    return actualCard;
  }
}
