# Content

<!--toc:start-->

- [Content](#content)
  - [Cơ bản về Event & Listener](#cơ-bản-về-event-listener)
  - [Ví dụ thực tế](#ví-dụ-thực-tế)
  - [Event](#event)
  - [Event Publisher](#event-publisher)
  - [EventListener](#eventlistener)
  - [Chạy thử](#chạy-thử)
  - [@Async](#async)
  - [Chạy thử lần 2](#chạy-thử-lần-2)
  <!--toc:end-->

## Cơ bản về Event & Listener

Về cơ bản khi chương trình đang vận hành và có một công việc nào đó, ta không muốn xử lý trực tiếp tại Class hiện hành hoặc muốn thông báo cho các Đối tượng khác biết vừa làm gì.
Khi đẩy ra một object gọi là `Event` (sự kiện), có hoặc không thông tin đi kèm, và nhiệm vụ của các đối tượng khác là lắng hay nghe đón lấy sự kiện đó để xủ lý nghiệm vụ riêng của nó (đối tượng xử lý gọi là `Listener`). Đối tượng gây xự kiện sẽ gọi là `Source`. Đối tượng chuyển giao sự kiện cho `Listener` gọi là `Pushlisher`.

## Ví dụ thực tế

Giả sử nhà có chuông cửa, khi có người bấm chuông -> chuông sẽ kêu. Trong nhà có chó, nó nghe thấy tiếng chuông -> chó sẽ sủa.
Khi đó:

- `Source`: người bấm chuông cửa, người gây ra sự kiện.
- `Event`: sự kiện bấm chuông cửa.
- `Pushlisher`: Chuông phát ra âm thanh (sự kiện thông báo).
- `Listener`: Con chó lắng nghe và xử lý sự kiện.

## Event

Một event (sự kiện) muốn được **Spring Boot** hỗ trợ phải kế thừa `ApplicationEnvent`.

```java
import org.springframework.context.ApplicationEvent;

/**
 * DoorBellEvent DoorBellEvent phải kế thừa lớp ApplicationEvent của Spring Như vậy nó mới được coi
 * là một sự kiện hợp lệ.
 */
public class DoorBellEvent extends ApplicationEvent {
    /**
     * Mọi Class kế thừa ApplicationEvent sẽ phải gọi Constructor tới lớp cha.
     *
     * @param source
     * @param guestName
     */
    public DoorBellEvent(Object source, String guestName) {
        // Object source là object tham chiếu tới
        // nơi đã phát ra event này!
        super(source);
    }
}
```

## Event Publisher

để đẩy ra một sự kiện sử dụng đối tượng `ApplicationEventPublisher`. Đây là một `Bean` có sẵn trong `Context`.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/** MyHouse */
@Component
public class MyHouse {
    @Autowired ApplicationEventPublisher applicationEventPublisher;

    /**
     * Hành động bấm chuông cửa
     *
     * @param guestName
     */
    public void rangDoorBellBy(String guestName) {
        // Phát ra một sự kiện DoorBellEvent
        // source (Nguồn phát ra) chính là class này
        applicationEventPublisher.publishEvent(new DoorBellEvent(this, guestName));
    }
}
```

## EventListener

Để lắng nghe các sự kiện do `ApplicationEventPublisher` đẩy ra, cần sử dụng `@EventListener`

```java
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/** MyDog */
@Component
public class MyDog {

    /**
     * @EventListener sẽ lắng nghe mọi sự kiện xảy ra Nếu có một sự kiện DoorBellEvent được bắn ra,
     * nó sẽ đón lấy và đưa vào hàm để xử lý
     *
     * @param doorBellEvent
     * @throws InterruptedException
     */
    @EventListener
    public void doorBellEventListener(DoorBellEvent doorBellEvent) throws InterruptedException {
        // Giả sử con chó đang ngủ, 1 giây sau mới dậy
        Thread.sleep(1000);

        // Sự kiện DoorBellEvent được lắng nghe và xử lý tại đây
        System.out.println(Thread.currentThread().getName() + ": Chó ngủ dậy");
        System.out.println(
                String.format(
                        "%s: Go go!! Có người tên là %s gõ cửa!!!",
                        Thread.currentThread().getName(), doorBellEvent.getGuestName()));
    }
}
```

## Chạy thử

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/** App */
@SpringBootApplication
public class App {
    @Autowired MyHouse myHouse;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    CommandLineRunner run() {
        return args -> {
            System.out.println(Thread.currentThread().getName() + ": Toan đi tới cửa nhà !!!");
            System.out.println(
                    Thread.currentThread().getName() + ": => Toan bấm chuông và khai báo họ tên!");
            // gõ cửa
            myHouse.rangDoorBellBy("Toan");
            System.out.println(Thread.currentThread().getName() + ": Toan quay lưng bỏ đi");
        };
    }
}
```

Output:

```text
main: Toan đi tới cửa nhà !!!
main: => Toan bấm chuông và khai báo họ tên!
main: Chó ngủ dậy
main: Go go!! Có người tên là null gõ cửa!!!
main: Toan quay lưng bỏ đi
```

Có thể thấy luồng được xử lý tuần tự (chương trình chờ sự kiện xử lý xong mới chạy tiếp).

## @Async

Đa phần xử lý Synchronous không phải điều mong muốn (ta muốn việc xử lý sự kiện có thể hoạt động riêng không ảnh hưởng tới luồng chính).
Nói cách khác, ta muốn sự kiện được xử lý ở một Thread khác, đây gọi là **bất đồng bộ (Asynchronous)**. Để làm được điều này, cần kích hoạt chức năng xử lý bất đồng bộ của **Spring Boot**, bằng cách bổ xung `@EnableAsync`.

```java
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

/** ListenerConfiguration */
@Configurable
@EnableAsync
public class ListenerConfiguration {
    /**
     * Tạo ra Executor cho Async
     *
     * @return
     */
    @Bean
    TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
}
```

**Spring Boot** khi thấy Annotation này, sẽ khích hoạt cho phép sử dụng dạng Async. Các `Event` sẽ được gửi vào một `Executor` (đơn giản nhất là `SimpleAsyncTaskExecutor`) và chờ được xử lý.

Sau khi kích hoạt tính năng `Async`, bất ký sự kiện nào muốn xử lý Async cần đánh dấu nó bởi `@Async`.

```java
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/** MyDog */
@Component
public class MyDog {

    /**
     * @EventListener sẽ lắng nghe mọi sự kiện xảy ra Nếu có một sự kiện DoorBellEvent được bắn ra,
     * nó sẽ đón lấy và đưa vào hàm để xử lý
     *
     * @param doorBellEvent
     * @throws InterruptedException
     */
    /**
     * @Async là cách lắng nghe sự kiện ở một Thread khác, không ảnh hưởng tới luồng chính
     */
    @Async
    @EventListener
    public void doorBellEventListener(DoorBellEvent doorBellEvent) throws InterruptedException {
        // Giả sử con chó đang ngủ, 1 giây sau mới dậy
        Thread.sleep(1000);

        // Sự kiện DoorBellEvent được lắng nghe và xử lý tại đây
        System.out.println(Thread.currentThread().getName() + ": Chó ngủ dậy");
        System.out.println(
                String.format(
                        "%s: Go go!! Có người tên là %s gõ cửa!!!",
                        Thread.currentThread().getName(), doorBellEvent.getGuestName()));
    }
}
```

## Chạy thử lần 2

Output

```text
main: Toan đi tới cửa nhà !!!
main: => Toan bấm chuông và khai báo họ tên!
main: Toan quay lưng bỏ đi
SimpleAsyncTaskExecutor-1: Chó ngủ dậy!!!
SimpleAsyncTaskExecutor-1: Go go!! Có người tên là Toan gõ cửa!!!
```
