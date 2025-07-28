package configurable.online.approval.system.observer;

import configurable.online.approval.system.domain.models.Application;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件管理器 (Subject / Observable)
 * 負責管理所有事件的訂閱者，並在事件發生時通知它們。
 */
public class EventManager {

    // 使用 Map 來管理不同事件類型的訂閱者列表
    private final Map<EventType, List<IObserver>> listeners = new HashMap<>();

    public EventManager(EventType... eventTypes) {
        // 在建構時，為傳入的每種事件類型都初始化一個空的監聽者列表
        for (EventType eventType : eventTypes) {
            this.listeners.put(eventType, new ArrayList<>());
        }
    }

    /**
     * 訂閱事件
     * @param eventType 要訂閱的事件類型
     * @param observer  觀察者實例
     */
    public void subscribe(EventType eventType, IObserver observer) {
        List<IObserver> users = listeners.get(eventType);
        if (users != null) {
            users.add(observer);
        }
    }

    /**
     * 取消訂閱事件
     * @param eventType 要取消訂閱的事件類型
     * @param observer  觀察者實例
     */
    public void unsubscribe(EventType eventType, IObserver observer) {
        List<IObserver> users = listeners.get(eventType);
        if (users != null) {
            users.remove(observer);
        }
    }

    /**
     * 通知訂閱者
     * @param eventType 觸發的事件類型
     * @param application 相關的申請單物件
     */
    public void notify(EventType eventType, Application application) {
        List<IObserver> users = listeners.get(eventType);
        if (users == null || users.isEmpty()) {
            return;
        }
        for (IObserver observer : users) {
            observer.update(eventType, application);
        }
    }
}