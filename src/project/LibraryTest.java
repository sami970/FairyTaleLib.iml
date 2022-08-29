package project;

import javafx.beans.property.SimpleStringProperty;
import project.items.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.people.Customer;
import project.people.Lecturer;
import project.people.Student;
import project.utils.Warning;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LibraryTest extends Application
{

  public static void main(String[] args)
  {
    launch();
  }

  private final ModelManager modelManager = new ModelManager();

  public void init() throws IOException, ClassNotFoundException
  {
    modelManager.readState();
  }

  @Override public void start(Stage stage) throws Exception
  {

    TableView itemTableView = getItemTableView();
    TableView customerTableView = getCustomerTableView();
    TableView warningTableView = getWarningTableView();

    VBox customersTabContent = getCustomersTabContent(customerTableView);
    VBox itemTabContent = getItemTabContent(itemTableView);
    VBox bookingTabContent = getBookingTabContent(itemTableView);
    VBox returnItemTabContent = getReturnItemTabContent();
    VBox reserveItemTabContent = getReserveItemTabContent();
    VBox warningTabContent = getWarningTabContent(warningTableView);

    //Whatever
    TabPane tabPane = new TabPane();
    Tab tab1 = new Tab("Customers", customersTabContent);
    Tab tab2 = new Tab("Items", itemTabContent);
    Tab tab3 = new Tab("Booking", bookingTabContent);
    Tab tab4 = new Tab("Returning", returnItemTabContent);
    Tab tab5 = new Tab("Reserving", reserveItemTabContent);
    Tab tab6 = new Tab("Warnings", warningTabContent);

    tab1.setClosable(false);
    tab2.setClosable(false);
    tab3.setClosable(false);
    tab4.setClosable(false);
    tab5.setClosable(false);
    tab6.setClosable(false);

    tabPane.getTabs().add(tab1);
    tabPane.getTabs().add(tab2);
    tabPane.getTabs().add(tab3);
    tabPane.getTabs().add(tab4);
    tabPane.getTabs().add(tab5);
    tabPane.getTabs().add(tab6);

    Scene scene = new Scene(tabPane, 1200, 800);
    stage.setTitle("FairyTale Library Software");
    stage.setScene(scene);
    stage.show();
  }

  private TableView getWarningTableView()
  {
    TableView warningView = new TableView();

    TableColumn<Warning, String> column = new TableColumn<>("Title");
    column.setCellValueFactory(new PropertyValueFactory<>("title"));

    TableColumn<Warning, String> column2 = new TableColumn<>("Type");
    column2.setCellValueFactory(new PropertyValueFactory<>("className"));

    TableColumn<Warning, LocalDate> column3 = new TableColumn<>(
        "Return by date");
    column3.setCellValueFactory(new PropertyValueFactory<>("returnBy"));

    TableColumn<Warning, String> column4 = new TableColumn<>("Email");
    column4.setCellValueFactory(new PropertyValueFactory<>("email"));

    warningView.getColumns().add(column);
    warningView.getColumns().add(column2);
    warningView.getColumns().add(column3);
    warningView.getColumns().add(column4);

    return warningView;
  }

  private VBox getWarningTabContent(TableView tableView)
  {

    Button findRemindButton = new Button("Find all people to remind");
    Button remainedEmailsDone = new Button("All reminder email sent");
    Button findFinesButton = new Button("Find all people to fine");
    Button fineEmailsDone = new Button("All fine emails sent");

    HBox reminderHbox = new HBox(findRemindButton, remainedEmailsDone);
    reminderHbox.setSpacing(10.0);
    HBox fineHbox = new HBox(findFinesButton, fineEmailsDone);
    fineHbox.setSpacing(10.0);

    VBox vBox = new VBox(reminderHbox, fineHbox, tableView);

    findRemindButton.onActionProperty().setValue(new EventHandler<ActionEvent>()
    {
      @Override public void handle(ActionEvent event)
      {
        List<Item> found = modelManager.findAllItemsInWarningPeriod();
        List<Warning> warnings = found.stream().map(
                item -> new Warning(item.getTitle(), item.getSimpleName(),
                    item.getReturnBy(), item.getLentToCustomer().getEmail()))
            .collect(Collectors.toList());

        tableView.getItems().clear();
        tableView.getItems().addAll(warnings);
      }
    });

    remainedEmailsDone.onActionProperty()
        .setValue(new EventHandler<ActionEvent>()
        {
          @Override public void handle(ActionEvent event)
          {
            if (tableView.getItems().isEmpty())
            {
              return;
            }

            List<Item> found = modelManager.findAllItemsInWarningPeriod();

            for (Item item : found)
            {
              item.setWarningMailSent(true);
            }

            tableView.getItems().clear();
          }
        });

    findFinesButton.onActionProperty().setValue(new EventHandler<ActionEvent>()
    {
      @Override public void handle(ActionEvent event)
      {
        List<Item> found = modelManager.findAllItemsSuitableForFine();
        List<Warning> fines = found.stream().map(
                item -> new Warning(item.getTitle(), item.getSimpleName(),
                    item.getReturnBy(), item.getLentToCustomer().getEmail()))
            .collect(Collectors.toList());

        tableView.getItems().clear();
        tableView.getItems().addAll(fines);
      }
    });

    fineEmailsDone.onActionProperty().setValue(new EventHandler<ActionEvent>()
    {
      @Override public void handle(ActionEvent event)
      {

        if (tableView.getItems().isEmpty())
        {
          return;
        }

        List<Item> found = modelManager.findAllItemsSuitableForFine();

        for (Item item : found)
        {
          item.setFineEmailSent(true);
        }

        tableView.getItems().clear();
      }
    });

    vBox.alignmentProperty().setValue(Pos.TOP_CENTER);
    vBox.setSpacing(10.0);
    vBox.paddingProperty().setValue(new Insets(20.0, 20.0, 20.0, 20.0));

    return vBox;
  }

  private VBox getReserveItemTabContent()
  {
    Label itemIdLabel = new Label("Item ID");
    Label customerIdLabel = new Label("Customer ID");

    HBox titles = new HBox(itemIdLabel, customerIdLabel);
    titles.setSpacing(150.0);

    TextField itemId = new TextField("");
    TextField customerId = new TextField("");
    Button searchById = new Button("Reserve item");

    HBox searchByIdBar = new HBox(itemId, customerId, searchById);
    searchByIdBar.setSpacing(10.0);

    Label resultOfBookingLabel = new Label("");

    searchById.onActionProperty().setValue(new EventHandler<ActionEvent>()
    {
      @Override public void handle(ActionEvent event)
      {

        resultOfBookingLabel.setText("");

        Customer customer = modelManager.findCustomerByIdOrNull(
            customerId.getText());
        if (customer == null)
        {
          resultOfBookingLabel.setText("Customer not found");
          customerId.clear();
          itemId.clear();
          return;
        }

        Item item = modelManager.findItemById(itemId.getText());
        if (item == null)
        {
          resultOfBookingLabel.setText("Item not found");
          customerId.clear();
          itemId.clear();
          return;
        }

        if (!item.isLent())
        {
          resultOfBookingLabel.setText(
              "Item is available to book, thus it can not be reserved");
          customerId.clear();
          itemId.clear();
          return;
        }

        if (item.getLentToCustomer().getId().equals(customer.getId()))
        {
          resultOfBookingLabel.setText(
              "Customer can not reserve book which he or she has currently lent");
          customerId.clear();
          itemId.clear();
          return;
        }

        for (Customer iterated : item.getCustomers())
        {
          if (iterated.getId().equals(customer.getId()))
          {
            resultOfBookingLabel.setText(
                "You are already in the reservation list.");
            customerId.clear();
            itemId.clear();
            return;
          }
        }

        resultOfBookingLabel.setText(
            "Item successfully reserved. Before you there is: "
                + (item.getCustomers().size()) + " customers.");
        item.reserveItem(customer);
        customerId.clear();
        itemId.clear();
      }
    });

    VBox bookingLayout = new VBox(titles, searchByIdBar, resultOfBookingLabel);
    bookingLayout.alignmentProperty().setValue(Pos.TOP_CENTER);
    bookingLayout.setSpacing(10.0);
    bookingLayout.paddingProperty()
        .setValue(new Insets(20.0, 20.0, 20.0, 20.0));
    return bookingLayout;

  }

  private VBox getReturnItemTabContent()
  {

    Label label = new Label("Item ID");
    TextField itemId = new TextField("");
    Button returnButton = new Button("Return Item");
    Label result = new Label("");

    returnButton.onActionProperty().setValue(new EventHandler<ActionEvent>()
    {
      @Override public void handle(ActionEvent event)
      {
        result.setText("");

        Item item = modelManager.findItemById(itemId.getText());
        if (item == null)
        {
          result.setText("Item not found!");
          itemId.clear();
          return;
        }

        if (!item.isLent())
        {
          result.setText("Item is currently not lent out");
          itemId.clear();
          return;
        }

        if (item.isFineEmailSent())
        {
          item.returnItem();
          result.setText("Successfully returned the item, but Pay the FINE");
          itemId.clear();
        }
        else
        {
          item.returnItem();
          result.setText("Successfully returned the item");
          itemId.clear();
        }
      }
    });

    VBox returnLayout = new VBox(label, itemId, returnButton, result);
    returnLayout.alignmentProperty().setValue(Pos.TOP_CENTER);
    returnLayout.setSpacing(10.0);
    returnLayout.paddingProperty().setValue(new Insets(20.0, 20.0, 20.0, 20.0));
    return returnLayout;
  }

  private VBox getBookingTabContent(TableView itemTableView)
  {
    Label itemIdLabel = new Label("Item ID");
    Label customerIdLabel = new Label("Customer ID");

    HBox titles = new HBox(itemIdLabel, customerIdLabel);
    titles.setSpacing(150.0);

    TextField itemId = new TextField("");
    TextField customerId = new TextField("");
    Button searchById = new Button("Borrow item");

    HBox searchByIdBar = new HBox(itemId, customerId, searchById);
    searchByIdBar.setSpacing(10.0);

    Label resultOfBookingLabel = new Label("");

    searchById.onActionProperty().setValue(new EventHandler<ActionEvent>()
    {
      @Override public void handle(ActionEvent event)
      {

        resultOfBookingLabel.setText("");

        Customer customer = modelManager.findCustomerByIdOrNull(
            customerId.getText());
        if (customer == null)
        {
          resultOfBookingLabel.setText("Customer not found");
          return;
        }

        Item item = modelManager.findItemById(itemId.getText());
        if (item == null)
        {
          resultOfBookingLabel.setText("Item not found");
          return;
        }

        if (item.isLent())
        {
          resultOfBookingLabel.setText("Item already lent");
          return;
        }

        if (item.isBooked())
        {
          Customer firstInReserveOrder = item.getFirstInReservationOrderOrNull();

          if (!customer.getId().equals(firstInReserveOrder.getId()))
          {
            resultOfBookingLabel.setText(
                "Someone else is before you in queue for this item ");
            return;
          }
        }

        item.loanItem(LocalDate.now(), customer);
        resultOfBookingLabel.setText("Item successfully booked");
        itemTableView.refresh();
        itemId.clear();
        customerId.clear();
      }
    });

    VBox bookingLayout = new VBox(titles, searchByIdBar, resultOfBookingLabel);
    bookingLayout.alignmentProperty().setValue(Pos.TOP_CENTER);
    bookingLayout.setSpacing(10.0);
    bookingLayout.paddingProperty()
        .setValue(new Insets(20.0, 20.0, 20.0, 20.0));
    return bookingLayout;
  }

  private VBox getItemTabContent(TableView itemTableView)
  {
    TextField query = new TextField();
    query.setPromptText("Search by title");
    Button search = new Button("Search in archive");
    Button showAll = new Button("Show All/Refresh");

    Label label = new Label("Add Item");
    TextField id = new TextField();
    id.setPromptText("Id");
    TextField title = new TextField();
    title.setPromptText("Title");
    TextField author = new TextField();
    author.setPromptText("Author");
    TextField isbn = new TextField();
    isbn.setPromptText("ISBN");
    Button clickButton = new Button("Add Item");
    Button removeButton = new Button("Remove Item");

    RadioButton book = new RadioButton("Book");
    RadioButton article = new RadioButton("Article");
    RadioButton cd = new RadioButton("CD");
    RadioButton dvd = new RadioButton("DVD");
    Label deleteResult = new Label("");
    Label result = new Label("");

    ToggleGroup itemTypeGroup = new ToggleGroup();
    book.setToggleGroup(itemTypeGroup);
    article.setToggleGroup(itemTypeGroup);
    cd.setToggleGroup(itemTypeGroup);
    dvd.setToggleGroup(itemTypeGroup);
//    itemTypeGroup.selectToggle(article);

    HBox radioButtonBox = new HBox(book, article, cd, dvd);
    radioButtonBox.setSpacing(10.0);

    search.onActionProperty().setValue(new EventHandler<ActionEvent>()
    {
      @Override public void handle(ActionEvent event)
      {
        List<Item> found = modelManager.findAllItemsByTitle(query.getText());
        itemTableView.getItems().clear();
        itemTableView.getItems().addAll(found);
      }
    });

    showAll.onActionProperty().setValue(new EventHandler<ActionEvent>()
    {
      @Override public void handle(ActionEvent event)
      {
        List<Item> allItems = modelManager.getAllItems();
        itemTableView.getItems().clear();
        itemTableView.getItems().addAll(allItems);
      }
    });

    clickButton.onActionProperty().setValue(new EventHandler<ActionEvent>()
    {
      @Override public void handle(ActionEvent event)
      {

        Item found = modelManager.findItemById(id.getText());
        if (found != null)
        {
          result.setText("Item with the same Id already exists.");
          return;
        }

        RadioButton selectedRadioButton = (RadioButton) itemTypeGroup.getSelectedToggle();
        String itemType = selectedRadioButton.getText();

        Item toCreate = null;

        if (Objects.equals(itemType, "Book"))
        {
          toCreate = new Book(id.getText(), title.getText(), isbn.getText(),
              author.getText(), LocalDate.now());
        }
        if (Objects.equals(itemType, "Article"))
        {
          toCreate = new Article(id.getText(), title.getText(),
              author.getText(), LocalDate.now());
        }
        if (Objects.equals(itemType, "CD"))
        {
          toCreate = new Cd(id.getText(), title.getText(), author.getText(),
              LocalDate.now());
        }
        if (Objects.equals(itemType, "DVD"))
        {
          toCreate = new Dvd(id.getText(), title.getText(), author.getText(),
              LocalDate.now());
        }

        modelManager.addItem(toCreate);
        itemTableView.getItems().add(toCreate);
        id.clear();
        title.clear();
        author.clear();
        isbn.clear();
      }
    });

    removeButton.onActionProperty().setValue(new EventHandler<ActionEvent>()
    {
      @Override public void handle(ActionEvent event)
      {
        deleteResult.setText("");
        Object selected = itemTableView.getSelectionModel().getSelectedItem();;

        Item item = (Item) selected;
        if (item == null)
        {
          deleteResult.setText("No item is selected.");
          return;
        }
        if (item.isLent())
        {
          deleteResult.setText("This item is lent to someone, unable to delete item.");
          return;
        }

        modelManager.removeItem((Item) selected);
        itemTableView.getItems().remove(selected);

      }
    });

    HBox searchBar = new HBox(query, search, showAll);
    searchBar.setSpacing(10.0);

    VBox itemLayout = new VBox(searchBar, itemTableView, label, id, title,
        author, isbn, radioButtonBox, result, clickButton, removeButton);
    itemLayout.alignmentProperty().setValue(Pos.TOP_CENTER);
    itemLayout.setSpacing(10.0);
    itemLayout.paddingProperty().setValue(new Insets(20.0, 20.0, 20.0, 20.0));
    return itemLayout;
  }

  private VBox getCustomersTabContent(TableView customerTableView)
  {
    Label label = new Label("Add new Customer");
    TextField id = new TextField();
    id.setPromptText("Id");
    TextField name = new TextField();
    name.setPromptText("Name");
    TextField email = new TextField();
    email.setPromptText("Email");
    TextField address = new TextField();
    address.setPromptText("Address");
    Button clickButton = new Button("Add Customer");
    Button removeButton = new Button("Remove Customer");

    RadioButton lecturer = new RadioButton("Lecturer");
    RadioButton student = new RadioButton("Student");

    ToggleGroup customerTypeGroup = new ToggleGroup();
    lecturer.setToggleGroup(customerTypeGroup);
    student.setToggleGroup(customerTypeGroup);
//    customerTypeGroup.selectToggle(student);

    HBox radioButtonBox = new HBox(lecturer, student);
    radioButtonBox.setSpacing(10.0);

    Label deleteResult = new Label("");
    Label result = new Label("");

    clickButton.onActionProperty().setValue(new EventHandler<ActionEvent>()
    {
      @Override public void handle(ActionEvent event)
      {

        Customer found = modelManager.findCustomerByIdOrNull(id.getText());
        if (found != null)
        {
          result.setText("Customer with this Id already exists.");
          return;
        }

        RadioButton selectedRadioButton = (RadioButton) customerTypeGroup.getSelectedToggle();
        String customerType = selectedRadioButton.getText();

        Customer toCreate;

        if (Objects.equals(customerType, "Student"))
        {
          toCreate = new Student(id.getText(), name.getText(), email.getText(),
              address.getText());
        }
        else
        {
          toCreate = new Lecturer(id.getText(), name.getText(), email.getText(),
              address.getText());
        }

        modelManager.addCustomer(toCreate);
        customerTableView.getItems().add(toCreate);
        id.clear();
        name.clear();
        email.clear();
        address.clear();
      }
    });

    removeButton.onActionProperty().setValue(new EventHandler<ActionEvent>()
    {
      @Override public void handle(ActionEvent event)
      {
        deleteResult.setText("");

        Object selected = customerTableView.getSelectionModel().getSelectedItem();;

        Customer customer = (Customer) selected;
        if (selected == null)
        {
          deleteResult.setText("No customer is selected.");
          return;
        }
        if (modelManager.doesCustomerHaveAnythingLent(customer.getId()))
        {
          deleteResult.setText(
              "Customer has some item lend, unable to delete customer.");
          return;
        }
        modelManager.removeCustomer(customer);
        customerTableView.getItems().remove(selected);

      }
    });

    VBox customerLayout = new VBox(customerTableView, deleteResult, label, id,
        name, email, address, radioButtonBox, clickButton, result, removeButton);
    customerLayout.alignmentProperty().setValue(Pos.TOP_CENTER);
    customerLayout.setSpacing(10.0);
    customerLayout.paddingProperty()
        .setValue(new Insets(20.0, 20.0, 20.0, 20.0));
    return customerLayout;
  }

  private TableView getCustomerTableView()
  {
    TableView customerTableView = new TableView();

    TableColumn<Customer, String> idColumn = new TableColumn<>("ID");
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

    TableColumn<Customer, String> nameColumn = new TableColumn<>("Name");
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

    TableColumn<Customer, String> typeColumn = new TableColumn<>("Type");
    typeColumn.setCellValueFactory(new PropertyValueFactory<>("className"));

    TableColumn<Customer, String> emailColumn = new TableColumn<>("Email");
    emailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));

    TableColumn<Customer, String> addressColumn = new TableColumn<>("Address");
    addressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));

    customerTableView.getColumns().add(idColumn);
    customerTableView.getColumns().add(nameColumn);
    customerTableView.getColumns().add(typeColumn);
    customerTableView.getColumns().add(emailColumn);
    customerTableView.getColumns().add(addressColumn);

    List<Customer> customers = modelManager.getAllCustomers();
    for (Customer customer : customers)
    {
      customerTableView.getItems().add(customer);
    }
    return customerTableView;
  }

  private TableView getItemTableView()
  {
    TableView itemTableView = new TableView();

    TableColumn<Item, String> idColumn = new TableColumn<>("ID");
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

    TableColumn<Item, String> titleColumn = new TableColumn<>("Title");
    titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

    TableColumn<Item, String> classColumn = new TableColumn<>("Type");
    classColumn.setCellValueFactory(new PropertyValueFactory<>("className"));

    TableColumn<Item, String> authorColumn = new TableColumn<>("Author");
    authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

    TableColumn<Item, String> isbnColumn = new TableColumn<>("ISBN");
    isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));

    TableColumn<Item, LocalDate> borrowAtColumn = new TableColumn<>(
        "Borrow date");
    borrowAtColumn.setCellValueFactory(
        new PropertyValueFactory<>("borrowedAt"));

    TableColumn<Item, LocalDate> returnByColumn = new TableColumn<>(
        "Return by date");
    returnByColumn.setCellValueFactory(new PropertyValueFactory<>("returnBy"));

    TableColumn<Item, LocalDate> lendTo = new TableColumn<>(
        "Lender ID - (Type)");
    lendTo.setCellValueFactory(new PropertyValueFactory<>("lentToCustomer"));

    TableColumn<Item, String> nextInLine = new TableColumn<>("Reserved to");
    nextInLine.setCellValueFactory(cellData -> {
      Item item = cellData.getValue();
      String toShow = item.getCustomers().isEmpty() ? "" : item.getCustomers().get(0).getId();
      return new SimpleStringProperty(toShow);
    });

    itemTableView.getColumns().add(idColumn);
    itemTableView.getColumns().add(titleColumn);
    itemTableView.getColumns().add(classColumn);
    itemTableView.getColumns().add(authorColumn);
    itemTableView.getColumns().add(isbnColumn);
    itemTableView.getColumns().add(borrowAtColumn);
    itemTableView.getColumns().add(returnByColumn);
    itemTableView.getColumns().add(lendTo);
    itemTableView.getColumns().add(nextInLine);

    List<Item> items = modelManager.getAllItems();
    for (Item item : items)
    {
      itemTableView.getItems().add(item);
    }
    return itemTableView;
  }

  public void stop() throws IOException
  {
    modelManager.saveState();
  }

}
