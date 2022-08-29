package project.items;

import project.people.Customer;
import project.people.Lecturer;
import project.people.Student;

import java.time.LocalDate;

public class Article extends Item {

    private final String className = Article.class.getSimpleName();

    public Article(String id, String title, String author,
        LocalDate creationDate) {
        super(id, title, author, creationDate);
    }

    @Override
    LocalDate computeReturnByDate(LocalDate borrowedAt, Customer lendingTo) {
        return borrowedAt.plusDays(14);
    }

    @Override
    public String getSimpleName() {
        return className;
    }

    public String toString() {

        return super.toString();
    }

    public String getClassName()
    {
        return className;
    }
}
