package cn.jjaw.easyevent
/**
 * 事件管理器
 */
class EventManager {
    private val eventSuperClass = Event::class.java;
    private val eventTypeGroupMap:EventTypeGroupMap = EventTypeGroupMap();

    /**
     * 注册事件监听器
     * @param type 事件类型
     * @param execute 当事件发生时的处理器
     * @param priority 优先级
     * @return 事件监听器对象
     */
    fun <T: Event> regEventListen(type:Class<T>, execute:(T)->Unit,priority: Priority = Priority.Medium):EventListen<T>{
        val group = eventTypeGroupMap.computeIfNew(type);
        return group.addEventListen(priority,execute);
    }
    /**
     * 注册事件监听器
     * @param type 事件类型
     * @param ex 当事件发生时的处理器
     * @return 事件监听器对象
     */
    fun <T: Event> regEventListen(type:Class<T>, ex:(T)->Unit):EventListen<T>{
        val group = eventTypeGroupMap.computeIfNew(type);
        return group.addEventListen(Priority.Medium,ex);
    }

    /**
     * 执行事件
     * @param event 要执行的事件
     */
    fun execute(event: Event){
        var exClass:Class<*>? = event.javaClass
        cs@while (exClass!=null) {
            if (event.stopPropagation || event.stopImmediatePropagation) {
                break@cs;
            }
            eventTypeGroupMap.get(exClass as Class<Event>)?.execute(event);
            if (exClass == eventSuperClass) {
                break@cs;
            }
            exClass = exClass.superclass;
        }
    }
}



/**
 * 一个泛型映射对象
 */
class EventTypeGroupMap{
    private val map:MutableMap<Class<*>,EventTypeGroup<*>> = mutableMapOf();
    internal fun <T: Event> put(type:Class<T>, execute:EventTypeGroup<T>):EventTypeGroup<T>?{
        return map.put(type,execute) as EventTypeGroup<T>?;
    }
    internal fun <T: Event> get(type:Class<T>):EventTypeGroup<T>? {
        return map.get(type) as EventTypeGroup<T>?;
    }
    internal fun <T: Event> computeIfNew(type:Class<T>):EventTypeGroup<T>{
        return map.computeIfAbsent(type){ EventTypeGroup<T>() } as EventTypeGroup<T>;
    }
}

/**
 * 优先级
 */
enum class Priority{
    /**
     * 优先级极高
     */
    VeryHigh,
    /**
     * 优先级高
     */
    High,
    /**
     * 优先级中
     */
    Medium,
    /**
     * 优先级低
     */
    Low,
    /**
     * 优先级极低
     */
    ExtremelyLow;
}
val PriorityOrder = arrayOf(
    Priority.VeryHigh,
    Priority.High,
    Priority.Medium,
    Priority.Low,
    Priority.ExtremelyLow
);

class EventTypeGroup<T: Event>{
    private val priorityGroupMap:MutableMap<Priority,PriorityGroup<T>> = mutableMapOf();

    internal fun addEventListen(priority:Priority,ex:(event:T)->Unit):EventListen<T>{
        return priorityGroupMap.computeIfAbsent(priority){PriorityGroup()}.addEventListen(ex);
    }

    internal fun execute(event:T){
        all@for (the in PriorityOrder){
            if (event.stopImmediatePropagation){
                break@all;
            }
            priorityGroupMap[the]?.execute(event);
        }
    }
}

/**
 * 事件类型组
 */
class PriorityGroup<T: Event>{
    private val eventListens:MutableList<EventListen<T>> = mutableListOf();

    internal fun addEventListen(ex:(event:T)->Unit):EventListen<T>{
        val eventListen = EventListen(this,ex);
        this.eventListens.add(eventListen);
        return eventListen;
    }

    internal fun removeEventListen(eventListen: EventListen<T>){
        this.eventListens.remove(eventListen);
    }
    internal fun execute(event:T){
        all@for(exe in eventListens){
            if(event.stopImmediatePropagation){
                break@all;
            }
            try {
                exe.execute(event);
            }catch (a:Throwable){
                a.printStackTrace();
            }
        }
    }
}

/**
 * @param group 所在的组
 */
class EventListen<T: Event>(private val group:PriorityGroup<T>, private val ex:(event:T)->Unit){

    /**
     * 关闭这个监听器，不再监听事件
     */
    fun close(){
        this.group.removeEventListen(this);
    }

    /**
     * 执行这个事件
     */
    fun execute(event:T){
        this.ex(event);
    }
}