package configurable.online.approval.system.observer;

import configurable.online.approval.system.domain.models.Application;

public class HistoryLogger implements IObserver {

    @Override
    public void update(EventType eventType, Application application) {
        System.out.println(
            "【日誌紀錄】[觀察者]: 申請單 " + application.getApplicationId() +
            " 觸發了 " + eventType + " 事件。" +
            " 目前狀態為: " + application.getStatus()
        );
    }
}