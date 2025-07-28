package configurable.online.approval.system.observer;

import configurable.online.approval.system.domain.models.Application;

/**
 * 用於測試的 Mock 觀察者
 * 它不執行任何真實邏輯，只記錄 update 方法是否被呼叫，以及被呼叫時的參數。
 */
public class MockObserver implements IObserver {

    public boolean wasCalled = false;
    public EventType receivedEventType;
    public Application receivedApplication;
    public int callCount = 0;

    @Override
    public void update(EventType eventType, Application application) {
        this.wasCalled = true;
        this.receivedEventType = eventType;
        this.receivedApplication = application;
        this.callCount++;
        System.out.println("MockObserver's update method was called!");
    }

    public void reset() {
        this.wasCalled = false;
        this.receivedEventType = null;
        this.receivedApplication = null;
        this.callCount = 0;
    }
}