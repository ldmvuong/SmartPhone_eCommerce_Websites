package vn.ute.smartphoneshop.controller.admin;

import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.ute.smartphoneshop.entity.BrandEntity;
import vn.ute.smartphoneshop.model.dto.BrandDTO;
import vn.ute.smartphoneshop.service.IBrandService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/brands")
public class BrandController {

    @Autowired
    public IBrandService brandService;

    @GetMapping("")
    public String brands(Model model) {
        List<BrandEntity> brands = brandService.findAll();
        model.addAttribute("brands", brands);
        return "/admin/brand-list";
    }

    @GetMapping("/add-brand")
    public String addBrand(Model model) {
        BrandDTO brandDTO = new BrandDTO();
        model.addAttribute("brand", brandDTO);
        return "/admin/new-brand";
    }

    @PostMapping("/insert")
    public String addBrand(@Valid@ModelAttribute("brand") BrandDTO brand, Model model, @RequestParam("files") MultipartFile files, BindingResult bindingResult) throws IOException {
        String msg="";
        if (bindingResult.hasErrors()) {
            msg = bindingResult.getFieldError().getDefaultMessage();
            model.addAttribute("msg", msg);
            return "/admin/brand-list";
        }
        if(brandService.findBrandByName(brand.getName()) != null){
            msg = "Name already exists";
            model.addAttribute("msg", msg);
            return "/admin/new-brand";
        }

        try {
            if(brandService.addBrand(brand,files)){
                return "redirect:/admin/brands";
            }
            else {
                msg = "An error occurred while adding the brand.";
                model.addAttribute("msg", msg);
            }
        }catch (BadRequestException e){
            msg = e.getMessage();
            model.addAttribute("msg", msg);
        }catch (IOException e){
            msg = "Could not add brand: " + e.getMessage();
            model.addAttribute("msg", msg);
        }
        return "/admin/new-brand";
    }

    @GetMapping("/edit-brand")
    public String editBrand(@RequestParam("brandId") Long id, Model model) {
        BrandEntity brand = brandService.findBrandById(id).get();
        model.addAttribute("brand", brand);
        return "/admin/edit-brand";
    }

    @PostMapping("/edit-brand")
    public String editBrand(@Valid@ModelAttribute("brand") BrandDTO brand, Model model, @RequestParam("files") MultipartFile files, BindingResult bindingResult,@RequestParam("oldImg") String oldImg, @RequestParam("id") Long id) throws IOException {
        String msg = "";
        if (bindingResult.hasErrors()) {
            msg = bindingResult.getFieldError().getDefaultMessage();
            model.addAttribute("msg", msg);
            return "/admin/brand-list";
        }
        BrandEntity brandEntity = brandService.findBrandByName(brand.getName());
        BrandEntity brandOld = brandService.findBrandById(id).get();
        if (brandEntity != null && !brandEntity.getId().equals(id)) {
            msg = "Name already exists";
            model.addAttribute("msg", msg);
            model.addAttribute("brand", brandEntity);
            return "/admin/edit-brand";
        }
        try {
            if (brandService.updateBrand(id, brand, oldImg, files)) {
                return "redirect:/admin/brands";
            } else {
                msg = "An error occurred while editing the brand.";
                model.addAttribute("msg", msg);
                model.addAttribute("brand", brandOld);
                return "/admin/edit-brand";
            }
        } catch (BadRequestException e) {
            msg = e.getMessage();
            model.addAttribute("msg", msg);
        } catch (IOException e) {
            msg = "Could not add brand: " + e.getMessage();
            model.addAttribute("msg", msg);
        }
        return "/admin/edit-brand";
    }

    @GetMapping("/delete-brand")
    public String deleteBrand(@RequestParam("brandId") Long id) {
        brandService.deleteBrand(id);
        return "redirect:/admin/brands";
    }
}
