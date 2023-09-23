# easy-event
简单的事件处理Kotlin库 (自用)

## 使用方法

java代码
~~~ java
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
~~~

输出
~~~
myEvent1
myEvent2
myEvent3
Event
~~~

## 添加到依赖
[![](https://jitpack.io/v/cn.jjaw/easy-event.svg)](https://jitpack.io/#cn.jjaw/easy-event)
~~~ gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
~~~
~~~ gradle
	dependencies {
	        implementation 'cn.jjaw:easy-event:v1.1.1'
	}
~~~
