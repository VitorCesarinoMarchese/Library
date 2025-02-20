package Models.Request;

public class BookRequest {
    public String Name;
    public String Price;
    public int TotalQuantity;
    public int RentQuantity;
    public int AvailableQuantity;
    public BookRequest(String Name, String Price, int TotalQuantity, int RentQuantity, int AvailableQuantity) {
        this.Name = Name;
        this.Price = Price;
        this.TotalQuantity = TotalQuantity;
        this.RentQuantity = RentQuantity;
        this.AvailableQuantity = AvailableQuantity;
    }
}
