public class Neighbor {

    private int NID;
    private String phoneNumber;
    private String firstname;
    private String lastname;
    private String email;
    private String creationDate;
    private String lastVisit;
    private int flag;
    private int numberOfPeople;
    private int numSeniors;
    private int numAdults;
    private int numChildren;
    private String notes;
    public Neighbor(int NID, String phoneNumber, String firstname, String lastname,
                    String email, String creationDate, String lastVisit, int flag,
                    int numberOfPeople, int numSeniors, int numAdults, int numChildren, String notes){
        this.NID = NID;
        this.phoneNumber = phoneNumber;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.creationDate = creationDate;
        this.lastVisit = lastVisit;
        this.flag = flag;
        this.numberOfPeople = numberOfPeople;
        this.numSeniors = numSeniors;
        this.numAdults = numAdults;
        this.numChildren = numChildren;
        this.notes = notes;
    }

    public int getNID() {
        return NID;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public String getEmail() {
        return email;
    }
    public String getCreationDate() {
        return creationDate;
    }
    public String getLastVisit() {
        return lastVisit;
    }
    public int getFlag() {
        return flag;
    }
    public int getNumberOfPeople() {
        return numberOfPeople;
    }
    public int getNumSeniors() {
        return numSeniors;
    }
    public int getNumAdults() {
        return numAdults;
    }
    public int getNumChildren() {
        return numChildren;
    }
    public String getNotes() {
        return notes;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
