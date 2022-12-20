package klimov.impl;

import klimov.interfaces.Store;
import klimov.models.Card;
import klimov.models.Product;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StoreImplTest {
  public static final String FILE_NAME = "src\\main\\resources\\StoreList.csv";
  Store store = new StoreImpl();

  private static final List<Card> cardsList;

  static {
    cardsList = new ArrayList<>();
    cardsList.add(Card.builder().cardNumber(1234).discount(5).build());
    cardsList.add(Card.builder().cardNumber(1233).discount(0).build());
    cardsList.add(Card.builder().cardNumber(1232).discount(3).build());
    cardsList.add(Card.builder().cardNumber(1231).discount(0).build());
    cardsList.add(Card.builder().cardNumber(1230).discount(2).build());
  }

  @Test
  void testCreateCard() {
    Card testCard = store.createCard("1234", cardsList);

    assertEquals(1234, testCard.getCardNumber());
    assertEquals(5, testCard.getDiscount());
  }

  @Test
  void testReadCards() {
    List<Card> testList = store.readCards(FILE_NAME);

    assertEquals(1234, testList.get(0).getCardNumber());
    assertEquals(5, testList.get(0).getDiscount());
  }

  @Test
  void testReadProducts() {
    Map<Integer, Product> testProductsList =
        store.readProducts("src\\main\\resources\\StoreList.csv");

    assertEquals("Milk chocolate", testProductsList.get(3).getName());
    assertTrue(testProductsList.get(3).isDiscount());
  }
}
