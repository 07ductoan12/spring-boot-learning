# Content

<!--toc:start-->

- [Content](#content)
  - [Cách @Autowired vận hành](#cách-autowired-vận-hành)
  <!--toc:end-->

## Cách @Autowired vận hành

`@Autowired` đánh dấu cho Spring biết rằng sẽ tự động inject bean tương ứng vào vị trí được đánh dấu.

```java
@Component
public class Girl {
    // Đánh dấu để Spring inject một đối tượng Outfit vào đây
    @Autowired
    Outfit outfit;

    public Girl(Outfit outfit) {
        this.outfit = outfit;
    }

    // GET
    // SET
}
```

Tuy nhiên, quá trình `@Autowired` yêu cầu Class đó phải có `Constructor` hoặc `Setter` cho thuộc tính cần inject.
Như ví dụ trên Class Girl có một `Constructor` là `public Girl(Outfit outfit)` để **Spring** có thể truyền giá trị `Outfit` vào bên trong `Girl`.
Nếu bỏ `Constructor` này đi, ta cần thay thế bằng một hàm `Setter` cho Girl.

```java
@Component
public class Girl(){
    @Autowired
    Outfit outfit;

    public Girl(){}

    public void setOutfit(Outfit outfit){
        this.outfit = outfit;
    }

}
```
