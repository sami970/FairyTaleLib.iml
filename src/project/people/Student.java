package project.people;

public class Student extends Customer
{

  private final String className = Student.class.getSimpleName();

  public Student(String id, String name, String Email, String Adress)
  {
    super(id, name, Email, Adress);
  }

  public String getClassName()
  {
    return className;
  }

  @Override public String toString()
  {
    return super.getId() + " - (Student)";
  }
}
