package klimov.interfaces;


import klimov.models.Card;
import klimov.models.Product;

import java.util.List;
import java.util.Map;

public interface Store {
  Card createCard(String cardNumber, List<Card> cardsList);

  Map<Integer, Product> readProducts(String fileName);

  List<Card> readCards(String fileName);
}
