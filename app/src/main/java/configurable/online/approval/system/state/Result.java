package configurable.online.approval.system.state;

/**
 * 操作結果的通用回傳類別
 */
public class Result {
    private String code;     // 結果代碼, e.g., "0000" for success, "0001" for failure
    private String message;  // 結果訊息

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }

    // Getters
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}