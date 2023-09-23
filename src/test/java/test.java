import cn.jjaw.easyevent.CancelableEvent;
import cn.jjaw.easyevent.Event;
import cn.jjaw.easyevent.EventManager;
import cn.jjaw.easyevent.Priority;

class MyEvent extends CancelableEvent {

}
public class test {
    public static void main(String[] args) {
        EventManager eventManager = new EventManager();
        eventManager.regEventListen(Event.class,(e)->{
            System.out.println("Event");
            return null;
        });
        eventManager.regEventListen(MyEvent.class,(e)->{
            System.out.println("myEvent3");
            return null;
        },Priority.Low);

        eventManager.regEventListen(MyEvent.class,(e)->{
            System.out.println("myEvent2");
            return null;
        });
        eventManager.regEventListen(MyEvent.class,(e)->{
            System.out.println("myEvent1");
            return null;
        }, Priority.High);

        eventManager.execute(new MyEvent());
    }
}
