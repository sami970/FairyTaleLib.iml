package project.utils;

import java.time.LocalDate;

public class Warning
{
  private final String title;
  private final String className;
  private final LocalDate returnBy;
  private final String email;

  public Warning(String title, String className, LocalDate returnBy,
      String email)
  {
    this.title = title;
    this.className = className;
    this.returnBy = returnBy;
    this.email = email;
  }

  public String getTitle()
  {
    return title;
  }

  public String getClassName()
  {
    return className;
  }

  public LocalDate getReturnBy()
  {
    return returnBy;
  }

  public String getEmail()
  {
    return email;
  }
}
