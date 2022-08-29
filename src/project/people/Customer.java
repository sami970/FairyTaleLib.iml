package project.people;

import java.io.Serializable;

public abstract class Customer implements Serializable
{

  private final String id;
  private String name;
  private String email;
  private String address;

  public Customer(String id, String name, String email, String address)
  {
    this.id = id;
    this.name = name;
    this.email = email;
    this.address = address;
  }

  public String getId()
  {
    return id;
  }

  public String getEmail()
  {
    return this.email;
  }

  public String getAddress()
  {
    return this.address;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }

  public String getName()
  {
    return name;
  }

}
