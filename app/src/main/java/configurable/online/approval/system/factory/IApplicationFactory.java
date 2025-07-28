package configurable.online.approval.system.factory;

import configurable.online.approval.system.domain.models.Application;
import configurable.online.approval.system.domain.models.User;

/**
 * 申請單工廠介面 (Factory Interface)
 * 定義了創建申請單的統一方法。
 * 我們使用泛型和可變參數來增加介面的靈活性，使其能適應不同申請單的創建需求。
 */
public interface IApplicationFactory {

    /**
     * 創建一個新的申請單
     * @param applicant 申請人
     * @param args      創建申請單所需的其他參數，例如金額、事由、日期等
     * @return 一個 Application 物件的實例
     */
    Application create(User applicant, Object... args);

}