package ua.com.dev_club.agrogeorgia.helpers;

import java.util.Vector;

import ua.com.dev_club.agrogeorgia.models.ComplexWork;
import ua.com.dev_club.agrogeorgia.models.ComplexWorkItem;

/**
 * Created by Taras on 26.04.2016.
 */
public class Total {
    protected Vector items;
    protected Double total;

    public Total() {
        items = new Vector();
        total = 0d;
    }

    public void addItem(ComplexWorkItem newItem)
    {
        int currIndex = items.indexOf(newItem);

        if (currIndex == -1) {
            items.addElement(newItem);
        } else {
            ComplexWorkItem currItem = (ComplexWorkItem) items.elementAt(currIndex);
            currItem.add(newItem);
        }

        total += newItem.getComplexWork().getHours();
    }

    public void removeItem(ComplexWorkItem oldItem)
    {
        int currIndex = items.indexOf(oldItem);

        if (currIndex == -1) {
            return;
        } else {
            ComplexWorkItem currItem =
                    (ComplexWorkItem)
                            items.elementAt(currIndex);

            if (oldItem.getTotalHours() > currItem.getTotalHours()) {
                oldItem.setTotalHours(currItem.getTotalHours());
            }

            currItem.subtract(oldItem);

            total = total - oldItem.getTotalHours();

            if (currItem.getTotalHours() == 0) {
                items.removeElementAt(currIndex);
            }

        }

    }

    public Vector getItems(){
        return items;
    }

    public Double getTotal(){
        return total;
    }
}
