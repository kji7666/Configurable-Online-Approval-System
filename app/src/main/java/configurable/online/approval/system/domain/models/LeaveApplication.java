package configurable.online.approval.system.domain.models;

import configurable.online.approval.system.domain.enums.ApplicationType;
import java.util.Date;

public class LeaveApplication extends Application {

    private Date startDate; // 請假開始時間
    private Date endDate;   // 請假結束時間
    private String reason;  // 請假事由

    public LeaveApplication(User applicant, Date startDate, Date endDate, String reason) {
        super(ApplicationType.LEAVE, applicant);
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
    }

    // Getters and Setters ...
}