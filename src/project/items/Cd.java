package project.items;

import project.people.Customer;
import project.people.Lecturer;
import project.people.Student;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Cd extends Item
{

  private final String className = Cd.class.getSimpleName();

  public Cd(String id, String title, String author, LocalDate creationDate)
  {
    super(id, title, author, creationDate);
  }

  public Boolean isOld()
  {
    return ChronoUnit.YEARS.between(this.getCreationDate(), LocalDate.now())
        >= 1L;
  }


  @Override LocalDate computeReturnByDate(LocalDate borrowedAt,
      Customer lendingTo)
  {
    if (isOld())
    {
      if (lendingTo instanceof Student)
      {
        return borrowedAt.plusMonths(1);
      }
      else if (lendingTo instanceof Lecturer)
      {
        return borrowedAt.plusMonths(6);
      }
    }
    else
    {
      return borrowedAt.plusDays(14);
    }

    throw new IllegalArgumentException("Unknown type of customer");
  }

  @Override public String getSimpleName()
  {
    return className;
  }

  public String getClassName()
  {
    return className;
  }
}
