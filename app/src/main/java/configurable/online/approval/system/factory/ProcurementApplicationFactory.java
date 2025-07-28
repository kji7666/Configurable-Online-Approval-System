package configurable.online.approval.system.factory;

import configurable.online.approval.system.domain.models.Application;
import configurable.online.approval.system.domain.models.ProcurementApplication;
import configurable.online.approval.system.domain.models.User;

import java.math.BigDecimal;
import java.util.Date;

public class ProcurementApplicationFactory implements IApplicationFactory {
    @Override
    public Application create(User applicant, Object... args) {
        // 參數校驗
        if (args.length < 5 || !(args[0] instanceof Date) || !(args[1] instanceof Date) || !(args[2] instanceof String)) {
            throw new IllegalArgumentException("創建請假單需要提供 Date 開始時間、Date 結束時間和 String 事由。");
        }
        
        // 解析參數
        String itemName = (String) args[0];
        int quantity = (int) args[1];
        BigDecimal unitPrice = (BigDecimal) args[2];
        String reason = (String) args[3];

        return new ProcurementApplication(applicant, itemName, quantity, unitPrice, reason);
    }
}
