package project.people;

public class Lecturer extends Customer
{

  private final String className = Lecturer.class.getSimpleName();

  public Lecturer(String id, String name, String Email, String Address)
  {
    super(id, name, Email, Address);
  }

  public String getClassName()
  {
    return className;
  }

  @Override public String toString()
  {
    return super.getId() + " - (Lecturer)";
  }
}
