package configurable.online.approval.system.factory;

import configurable.online.approval.system.domain.models.Application;
import configurable.online.approval.system.domain.models.LeaveApplication;
import configurable.online.approval.system.domain.models.User;
import java.util.Date;

public class LeaveApplicationFactory implements IApplicationFactory {

    @Override
    public Application create(User applicant, Object... args) {
        // 參數校驗
        if (args.length < 3 || !(args[0] instanceof Date) || !(args[1] instanceof Date) || !(args[2] instanceof String)) {
            throw new IllegalArgumentException("創建請假單需要提供 Date 開始時間、Date 結束時間和 String 事由。");
        }

        // 解析參數
        Date startDate = (Date) args[0];
        Date endDate = (Date) args[1];
        String reason = (String) args[2];

        return new LeaveApplication(applicant, startDate, endDate, reason);
    }
}