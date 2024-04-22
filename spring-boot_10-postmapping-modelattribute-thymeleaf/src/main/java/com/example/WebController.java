package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** WebController */
@Controller
public class WebController {
    List<Todo> todoList = new CopyOnWriteArrayList<>();

    @GetMapping("/listTodo")
    public String index(
            Model model, @RequestParam(value = "limit", required = false) Integer limit) {
        model.addAttribute("todoList", limit != null ? todoList.subList(0, limit) : todoList);
        return "listTodo";
    }

    @GetMapping("/addTodo")
    public String addTodo(Model model) {
        // Thêm mới một đối tượng Todo vào model
        model.addAttribute("todo", new Todo());
        // Trả về template addTodo.html
        return "addTodo";
    }

    /*
    @ModelAttribute đánh dấu đối tượng Todo được gửi lên bởi Form Request
     */
    @PostMapping("/addTodo")
    public String addTodo(@ModelAttribute Todo todo) {
        // Thêm đối tượng todo vào list
        todoList.add(todo);
        // Trả về trang thành công success.html
        return "success";
    }
}
