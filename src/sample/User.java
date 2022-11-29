package sample;

public class User {

    private String firstname;
    private String name;
    private String middlename;
    private String email;
    private String password;

    public User(String firstname, String name, String middlename, String email, String password) {
        this.firstname = firstname;
        this.name = name;
        this.middlename = middlename;
        this.email = email;
        this.password = password;
    }

    public User() { }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
