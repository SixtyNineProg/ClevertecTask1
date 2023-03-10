package klimov;

import klimov.impl.CheckMakerImpl;
import klimov.impl.StoreImpl;
import klimov.interfaces.Store;
import klimov.models.Card;
import klimov.models.Product;

import java.util.List;
import java.util.Map;

public class App {
  public static void main(String[] args) {

    Store store = new StoreImpl();
    CheckMakerImpl checkMaker = new CheckMakerImpl();

    String[] newArgs = checkMaker.createInputMass(args);
    Map<Integer, Product> productMap = store.readProducts(newArgs[2]);
    List<Card> cardList = store.readCards(newArgs[2]);
    Card actualCard = store.createCard(checkMaker.getActualCardNumber(newArgs[1]), cardList);
    Map<Integer, Integer> itemsMap = checkMaker.createMapForItemIdAndQuantity(newArgs[0]);
    Map<Product, Double> productsMap = checkMaker.createMapWithTotalPrice(itemsMap, productMap);
    checkMaker.writeCheck(productsMap, actualCard);
  }
}
