package cn.jjaw.easyevent
abstract class Event {
    /**
     * 停止事件冒泡，当前类的监听器依然可以全部收到事件父类监听器不会再收到事件
     */
    var stopPropagation = false;

    /**
     * 停止事件继续传播，不会再有下一个监听器收到事件
     */
    var stopImmediatePropagation =  false;
}

abstract class CancelableEvent: Event() {
    /**
     * 取消事件
     */
    var cancel = false;
}