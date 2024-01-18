package org.learning.springpizzeria.controller;

import org.learning.springpizzeria.model.Ingredient;
import org.learning.springpizzeria.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {

    @Autowired
    private IngredientRepository ingredientRepository;

    @GetMapping
    public String index(Model model){
        List<Ingredient> ingredientList;
        model.addAttribute("ingredientList" , ingredientList);
    }
}
