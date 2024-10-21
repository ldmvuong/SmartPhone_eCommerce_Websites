package vn.ute.smartphoneshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ute.smartphoneshop.entity.CategoryEntity;
import vn.ute.smartphoneshop.service.impl.CategoryServiceImpl;

import java.util.List;
@Controller
public class CategoryController{
    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    @GetMapping(value = "user/categories")
    public String category(Model model){
        List<CategoryEntity>  list = this.categoryServiceImpl.findAll();
        model.addAttribute("categories",list);
        return "web/shop-right-sidebar";
    }

    @GetMapping(value = "admin/list-category")
    public String listCategory(Model model){
        List<CategoryEntity> list = this.categoryServiceImpl.findAll();
        model.addAttribute("categories",list);
        return "web/category";
    }
    @GetMapping(value = "/admin/edit-category/{id}")
    public String editCategory(Model model, @PathVariable("id") int id){
        CategoryEntity category = this.categoryServiceImpl.findById(id);
        model.addAttribute("category",category);
        return "web/categories/edit-category";
    }
    @PostMapping(value = "/admin/edit")
    public String edit( Model model, @ModelAttribute("category") CategoryEntity category){
        if(this.categoryServiceImpl.update(category)){
            return "redirect:/admin/list-category";
        }
        else {
            model.addAttribute("category", category); // Truyền lại category vào model
            return "redirect:/edit-category/"+ category.getCategoryId();
        }
    }

    @GetMapping(value = "/admin/add-category")
    public String addCategory(Model model){
        CategoryEntity category = new CategoryEntity();
        category.setStatus(1);
        model.addAttribute("category",category);
        return "web/categories/add-category";
    }

    @PostMapping(value = "/admin/add")
    public String add(@ModelAttribute("category") CategoryEntity category){
        category.setStatus(1);
        if (this.categoryServiceImpl.addCategory(category)){
            return "redirect:/admin/list-category";
        }
        else {
            return "redirect:/add-category";
        }
    }

    @GetMapping(value = "/admin/delete-category/{id}")
    public String deleteCategory( @PathVariable("id") int id){
        if(categoryServiceImpl.deleteCategory(id)){
            return "redirect:/admin/list-category";
        }
        else {
            return "redirect:/add-category";
        }
    }
}
