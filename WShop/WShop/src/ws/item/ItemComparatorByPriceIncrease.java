package ws.item;

import java.util.Comparator;

public class ItemComparatorByPriceIncrease implements Comparator<Item> {
	
    @Override
    public int compare(Item o1, Item o2) {
        if (o1.getPrice() > o2.getPrice()) {
            return 1;
        } else if (o1.getPrice() < o2.getPrice()) {
            return -1;
        } else {
            if (o1.getType() != null && o2.getType() != null) {
                return o1.getType().compareTo(o2.getType());
            }
            if (o1.getId() == null && o2.getId() != null) {
                return 1;
            }
            if (o1.getId() != null && o2.getId() == null) {
                return -1;
            }
            return 0;
        }
    }
}
