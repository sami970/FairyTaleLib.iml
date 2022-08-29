package project.items;

import project.people.Customer;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public abstract class Item implements Serializable
{
  private final String id;
  private final String title;
  private final String author;
  private final LocalDate creationDate;
  private LocalDate borrowedAt;
  private LocalDate returnBy;
  private boolean isLent;
  private Customer lentToCustomer;
  private final ArrayList<Customer> customers = new ArrayList<>();

  private boolean warningMailSent = false;
  private boolean fineEmailSent = false;

  abstract LocalDate computeReturnByDate(LocalDate borrowedAt,
      Customer lendingTo);

  public abstract String getSimpleName();

  public Item(String id, String title, String author, LocalDate creationDate)
  {
    this.id = id;
    this.title = title;
    this.author = author;
    this.creationDate = creationDate;
  }

  public String getTitle()
  {
    return this.title;
  }

  public LocalDate getCreationDate()
  {
    return this.creationDate;
  }

  public boolean isLent()
  {
    return isLent;
  }

  public boolean isBooked()
  {
    return customers.size() > 0;
  }

  public void reserveItem(Customer customer)
  {
    this.customers.add(customer);
  }

  public void returnItem()
  {
    this.isLent = false;
    this.borrowedAt = null;
    this.returnBy = null;
    this.warningMailSent = false;
    this.fineEmailSent = false;
    this.lentToCustomer = null;
  }

  public void loanItem(LocalDate borrowedAt, Customer customer)
  {
    this.isLent = true;
    this.lentToCustomer = customer;
    this.borrowedAt = borrowedAt;
    this.returnBy = computeReturnByDate(borrowedAt, customer);

    Customer firstInReservationOrder = getFirstInReservationOrderOrNull();
    if (firstInReservationOrder != null)
    {
      this.getCustomers().remove(0);
    }
  }

  public Customer getFirstInReservationOrderOrNull()
  {
    return customers.isEmpty() ? null : customers.get(0);
  }

  public String getId()
  {
    return id;
  }

  public Customer getLentToCustomer()
  {
    return lentToCustomer;
  }

  public ArrayList<Customer> getCustomers()
  {
    return customers;
  }

  public LocalDate getReturnBy()
  {
    return returnBy;
  }

  public void setWarningMailSent(boolean warningMailSent)
  {
    this.warningMailSent = warningMailSent;
  }

  public void setFineEmailSent(boolean fineEmailSent)
  {
    this.fineEmailSent = fineEmailSent;
  }

  public boolean isFineEmailSent()
  {
    return fineEmailSent;
  }

  public Boolean isInWarningPeriod()
  {
    if (!isLent)
    {
      return false;
    }

    if (warningMailSent)
    {
      return false;
    }
    return ChronoUnit.DAYS.between(LocalDate.now(), returnBy) <= 365L;
  }

  public Boolean isFineApplicable()
  {
    if (!isLent)
    {
      return false;
    }

    if (fineEmailSent)
    {
      return false;
    }
    return ChronoUnit.DAYS.between(returnBy, LocalDate.now()) >= -365L;
  }

  public String getAuthor()
  {
    return author;
  }

  public LocalDate getBorrowedAt()
  {
    return borrowedAt;
  }

  public boolean isWarningMailSent()
  {
    return warningMailSent;
  }

}
