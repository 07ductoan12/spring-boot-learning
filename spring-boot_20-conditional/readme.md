# Content

<!--toc:start-->

- [Content](#content)
  - [Giới thiệu](#giới-thiệu)
  - [Cách tạo Bean có điều kiện](#cách-tạo-bean-có-điều-kiện)
  - [@ConditionalOnBean](#conditionalonbean)
  - [@ConditionalOnProperty](#conditionalonproperty)
  - [@ConditionalOnExpression](#conditionalonexpression)
  - [ConditionalOnMissingBean](#conditionalonmissingbean)
  - [ConditionalOnResource](#conditionalonresource)
  - [@ConditionalOnClass](#conditionalonclass)
  - [@ConditionalOnMissingClass](#conditionalonmissingclass)
  - [@ConditionalOnJava](#conditionalonjava)
  <!--toc:end-->

## Giới thiệu

Khi xây đựng chương trình với **Spring Boot** đôi lúc có một **Bean** chỉ được load lên hoặc khởi tạo theo một điều kiện nào đó. Ví dụ trong môi trường **Test**, còn trong môi trường thật thì không cần nữa.

## Cách tạo Bean có điều kiện

Trong **Spring Boot** có nhiều cách để tạo ra `Bean` như: `@Component`, `@Configuration`, `@Bean`, `@Service`, ... Để tạo một `Bean` với điều kiện thêm tiền tố `@Conditional...`.
Cách thức hoạt động của mọi `@Conditional` như sau:

```java
@Configuration
public class ConditionalOnBeanExample {
    /*
    ABeanWithCondition chỉ được tạo ra khi @Condition thỏa mãn
    */
    @Bean
    @Conditional...
    ABeanWithCondition aBeanWithACondition() {
        return new ABeanWithCondition();
    }
}

```

Nếu gắn nó trên một `@Conditional` thì toàn bộ các `@Bean` bên trong sẽ bị chịu tác động.

```java
@Conditional...
@Configuration
public class ConditionalOnBeanExample {
    /*
    SomeOtherBean chỉ được tạo ra khi @Condition thỏa mãn
    */
    @Bean
    SomeOtherBean someOtherBean() {
        return new SomeOtherBean();
    }
       /*
    SomeOtherBean2 chỉ được tạo ra khi @Condition thỏa mãn
    */
    @Bean
    SomeOtherBean2 someOtherBean2() {
        return new SomeOtherBean2();
    }
}
```

tương tự cho tất cả các annotation khác như: `@Component`, `@Service`, `@Repository`, ...

```java
@Conditional...
@Component
public class ConditionalOnBeanExample {
}
```

## @ConditionalOnBean

`@ConditionalOnBean` sử dụng khi muốn tạo ra một Bean, chỉ khi có một Bean khác tồn tại.

```java
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** ConditionalOnBeanExample */
@Configuration
public class ConditionalOnBeanExample {

    // @Bean
    RandomBean randomBean() {
        return new RandomBean();
    }

    @Bean
    @ConditionalOnBean(RandomBean.class)
    ABeanWithCondition aBeanWithCondition() {
        return new ABeanWithCondition();
    }
}
```

## @ConditionalOnProperty

Dùng `@ConditionalOnProperty` khi muốn quyết định sự tồn tại của Bean thông qua cấu hình property.

```java
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** ConditionalOnPropertyExample */
@Configuration
public class ConditionalOnPropertyExample {

    /**
     * @ConditionalOnProperty giúp gắn điều kiện cho @Bean dựa theo property trong config
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(
            value = "toan.bean2.enable",
            havingValue =
                    "true", // Nếu giá trị loda.bean2.enabled  = true thì Bean mới được khởi tạo
            matchIfMissing =
                    false) // matchIFMissing là giá trị mặc định nếu không tìm thấy property
                           // toan.bean2.enabled
    ABeanWithCondition2 aBeanWithCondition2() {
        return new ABeanWithCondition2();
    }
}
```

## @ConditionalOnExpression

Sử dụng trong trường hợp muốn thỏa mãn nhiều điều kiện trong property.

```java
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

/** ConditionalOnExpressionExample */
@Configuration
@ConditionalOnExpression("${toan.expression1.enable:true} and ${toan.expression2.enable:true}")
public class ConditionalOnExpressionExample {

}
```

## ConditionalOnMissingBean

Nếu trong `Context`chưa tồn tại bất kỳ một Bean nào tương tự, thì `@ConditionalOnMissingBean` sẽ thỏa mãn điều kiện và tạo ra một Bean như thế.

```java
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

/** ConditionalOnMissingBeanExample */
@Configuration
public class ConditionalOnMissingBeanExample {

    public static class SomeMissingBean {}

    /**
     * Nếu trong Context chưa tồn tại một SomeMissingBean nào Thì Bean ở dưới đây mới được tạo
     *
     * @return
     */
    @ConditionalOnMissingBean
    SomeMissingBean someMissingBean() {
        return new SomeMissingBean();
    }
}
```

## ConditionalOnResource

`@ConditionalOnResource` thỏa mãn khi có một resource nào đó chỉ định tồn tại

```java
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Configuration;

/**
 * ConditionalOnResourceExample Nếu Spring Boot không tìm thấy file application.properties thì class
 * này không được tạo
 */
@Configuration
@ConditionalOnResource(resources = "/application.properties")
public class ConditionalOnResourceExample {

}
```

## @ConditionalOnClass

`@ConditionalOnClass` thỏa mãn khi trong classpath có tồn tại class yêu cầu

```java
@Conditional
@ConditionalOnClass(name= "com.example.toan")
class ConditionalOnClassExample {
}
```

## @ConditionalOnMissingClass

`@ConditionalOnClass` thỏa mãn khi trong classpath **Không** tồn tại class yêu cầu

```java
@Conditional
@ConditionalOnMissClass(name= "com.example.toan")
class ConditionalOnMissingClassExample {
}
```

## @ConditionalOnJava

`@ConditionalOnJava` thỏa mãn nếu môi trường chạy version Java đúng với điều kiện

```java
@Configuration
@ConditionalOnJava(JavaVersion.EIGHT)
class ConditionalOnJavaExample {
}
```
