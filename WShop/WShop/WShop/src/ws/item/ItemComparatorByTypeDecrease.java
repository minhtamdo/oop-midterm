package ws.item;

import java.util.Comparator;

public class ItemComparatorByTypeDecrease implements Comparator<Item> {

    @Override
    public int compare(Item o1, Item o2) {
        if (o1.getType().compareTo(o2.getType()) < 0) {
            return -1;
        } else if (o1.getType().compareTo(o2.getType()) > 0) {
            return 1;
        } else {
            if (o1.getId() != null && o2.getId() != null) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
            if (o1.getId() == null && o2.getId() != null) {
                return -1;
            }
            if (o1.getId() != null && o2.getId() == null) {
                return 1;
            }
            return 0;
        }
    }
}
