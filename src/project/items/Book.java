package project.items;

import project.people.Customer;
import project.people.Lecturer;
import project.people.Student;

import java.time.LocalDate;

public class Book extends Item
{

  private final String ISBN;
  private final String className = Book.class.getSimpleName();

  public Book(String id, String title, String ISBN, String author,
      LocalDate creationDate)
  {
    super(id, title, author, creationDate);
    this.ISBN = ISBN;
  }

  @Override LocalDate computeReturnByDate(LocalDate borrowedAt,
      Customer lendingTo)
  {

    if (lendingTo instanceof Student)
    {
      return borrowedAt.plusMonths(1);
    }
    else if (lendingTo instanceof Lecturer)
    {
      return borrowedAt.plusMonths(6);
    }

    throw new IllegalArgumentException("Unknown customer type");
  }

  @Override public String getSimpleName()
  {
    return className;
  }

  public String getISBN()
  {
    return ISBN;
  }

  public String getClassName()
  {
    return className;
  }
}
