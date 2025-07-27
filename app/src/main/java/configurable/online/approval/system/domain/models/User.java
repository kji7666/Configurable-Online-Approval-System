package configurable.online.approval.system.domain.models;


public class User {
    private String userId;
    private String userName;
    private String department;

    // 建構函式 (Constructor)
    public User(String userId, String userName, String department) {
        this.userId = userId;
        this.userName = userName;
        this.department = department;
    }

    // Getters and Setters ...
    // toString() for debugging ...
}