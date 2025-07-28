package configurable.online.approval.system.observer;

import configurable.online.approval.system.domain.models.Application;

/**
 * 觀察者介面 (Observer)
 * 所有對申請單狀態變更感興趣的監聽者都需要實現此介面。
 */
public interface IObserver {

    /**
     * 當被觀察的事件發生時，此方法會被呼叫。
     * @param eventType 觸發的事件類型
     * @param application 觸發事件的申請單物件
     */
    void update(EventType eventType, Application application);

}