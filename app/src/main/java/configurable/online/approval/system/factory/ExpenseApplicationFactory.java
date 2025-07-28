package configurable.online.approval.system.factory;

import configurable.online.approval.system.domain.models.Application;
import configurable.online.approval.system.domain.models.ExpenseApplication;
import configurable.online.approval.system.domain.models.User;
import java.math.BigDecimal;

public class ExpenseApplicationFactory implements IApplicationFactory {

    @Override
    public Application create(User applicant, Object... args) {
        // 參數校驗
        if (args.length < 2 || !(args[0] instanceof BigDecimal) || !(args[1] instanceof String)) {
            throw new IllegalArgumentException("創建費用報銷單需要提供 BigDecimal 金額和 String 事由。");
        }

        // 解析參數
        BigDecimal amount = (BigDecimal) args[0];
        String reason = (String) args[1];

        // 呼叫 ExpenseApplication 的建構函式來創建實例
        return new ExpenseApplication(applicant, amount, reason);
    }
}