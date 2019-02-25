package id.eudeka.firebasesample;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String nama;
    public String email;
    public String telp;


    public User() {
    }

    public User(String nama, String email, String telp) {
        this.nama = nama;
        this.email = email;
        this.telp = telp;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getTelp() {
        return telp;
    }
}
