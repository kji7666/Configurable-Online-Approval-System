package configurable.online.approval.system.observer;

import configurable.online.approval.system.domain.models.Application;
import configurable.online.approval.system.domain.models.User;

public class EmailNotifier implements IObserver {

    @Override
    public void update(EventType eventType, Application application) {
        // 我們只對核准和駁回事件感興趣
        if (eventType == EventType.APPROVE || eventType == EventType.REJECT) {
            User applicant = application.getApplicant();
            System.out.println(
                "【郵件通知】[觀察者]: 正在向 " + applicant.getUserName() + "(" + applicant.getUserId() + ")" +
                " 發送郵件... 內容：您的申請單 " + application.getApplicationId() +
                " 已被 " + eventType + "."
            );
        }
    }
}