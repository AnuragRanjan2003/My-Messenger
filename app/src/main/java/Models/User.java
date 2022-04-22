package Models;

public class User {
    String UserName,ImageUrl;

    public User() {
    }

    public User(String userName, String imageUrl) {
        UserName = userName;
        ImageUrl = imageUrl;
    }

    public User(String userName) {
        UserName = userName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
