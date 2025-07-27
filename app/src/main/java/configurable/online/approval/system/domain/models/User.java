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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    // toString() for debugging ...
}