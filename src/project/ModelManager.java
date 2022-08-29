package project;

import javafx.scene.control.Alert;
import project.items.Item;
import project.people.Customer;

import java.io.*;
import java.util.*;

public class ModelManager
{

  private ArrayList<Item> itemsList = new ArrayList<>();
  private ArrayList<Customer> customerList = new ArrayList<>();

  public ModelManager()
  {

  }

  public List<Item> findAllItemsInWarningPeriod()
  {

    List<Item> collector = new ArrayList<>();
    for (Item item : itemsList)
    {
      if (item.isInWarningPeriod())
      {
        collector.add(item);
      }
    }

    return collector;
  }

  public List<Item> findAllItemsSuitableForFine()
  {

    List<Item> collector = new ArrayList<>();
    for (Item item : itemsList)
    {
      if (item.isFineApplicable())
      {
        collector.add(item);
      }
    }

    return collector;
  }

  public Customer findCustomerByIdOrNull(String id)
  {
    for (int i = 0; i < customerList.size(); i++)
    {
      if (Objects.equals(customerList.get(i).getId(), id))
      {
        return customerList.get(i);
      }
    }

    return null;
  }

  public Item findItemById(String id)
  {
    for (int i = 0; i < itemsList.size(); i++)
    {
      if (Objects.equals(itemsList.get(i).getId(), id))
      {
        return itemsList.get(i);
      }
    }

    return null;
  }

  public List<Item> findAllItemsByTitle(String title)
  {
    List<Item> ret = new ArrayList<>();
    for (int i = 0; i < itemsList.size(); i++)
    {
      if (itemsList.get(i).getTitle().equals(title))
      {
        ret.add(itemsList.get(i));
      }

    }
    return ret;
  }

  public ArrayList<Item> getAllItems()
  {
    return itemsList;
  }

  public ArrayList<Customer> getAllCustomers()
  {
    return customerList;
  }

  public void addCustomer(Customer customer)
  {
    customerList.add(customer);
  }

  public void addItem(Item item)
  {
    itemsList.add(item);
  }

  public void removeCustomer(Customer customer)
  {
    customerList.remove(customer);
  }

  public void removeItem(Item item)
  {
    itemsList.remove(item);
  }

  public boolean doesCustomerHaveAnythingLent(String customerId)
  {
    for (Item item : itemsList)
    {
      if (item.getLentToCustomer() != null && item.getLentToCustomer().getId()
          .equals(customerId))
      {
        return true;
      }
    }
    return false;
  }

  public void saveState()
  {
    try
    {
      ObjectOutputStream out = new ObjectOutputStream(
          new FileOutputStream("src/saves/itemList.ser"));
      out.writeObject(itemsList);
      out = new ObjectOutputStream(
          new FileOutputStream("src/saves/customerList.ser"));
      out.writeObject(customerList);
      out.close();
    }
    catch (Exception e)
    {

      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(e.getMessage());
      alert.show();
    }
  }

  public void readState()
  {
    try
    {
      ObjectInputStream in = new ObjectInputStream(
          new FileInputStream("src/saves/itemList.ser"));
      itemsList = (ArrayList<Item>) in.readObject();
      in = new ObjectInputStream(
          new FileInputStream("src/saves/customerList.ser"));
      customerList = (ArrayList<Customer>) in.readObject();
      in.close();
    }
    catch (Exception e)
    {

      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(e.getMessage());
      alert.show();

    }
  }

}
