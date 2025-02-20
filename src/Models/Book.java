package Models;

public class Book {
    public int Id;
    public String Name;
    public String Price;
    public int TotalQuantity;
    public int RentQuantity;
    public int AvailableQuantity;
    public int  last_changes_id;
    public String creation_date;
    public Book(int Id, String Name, String Price, int TotalQuantity, int RentQuantity, int AvailableQuantity, int last_changes_id, String creation_date) {
        this.Id = Id;
        this.Name = Name;
        this.Price = Price;
        this.TotalQuantity = TotalQuantity;
        this.RentQuantity = RentQuantity;
        this.AvailableQuantity = AvailableQuantity;
        this.last_changes_id = last_changes_id;
        this.creation_date = creation_date;
    }
}