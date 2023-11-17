package org.lesson.java.springLaMiaPizzeriaCrud.controller;

import java.util.Arrays;
import java.util.List;

import org.lesson.java.springLaMiaPizzeriaCrud.DB.Ingredient;
import org.lesson.java.springLaMiaPizzeriaCrud.DB.Pizze;
import org.lesson.java.springLaMiaPizzeriaCrud.DB.serv.PizzaService;
import org.lesson.java.springLaMiaPizzeriaCrud.DB.serv.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {

    @Autowired
    private PizzaService pizzaService;

    @Autowired
    private IngredientService ingredientService;

    @GetMapping
    public String ingredientIndex(Model model) {

        List<Ingredient> ingredients = ingredientService.findAll();
        model.addAttribute("ingredients", ingredients);

        return "ingredient_index";
    }

    @GetMapping("/create")
    public String ingredientCreate(Model model) {
        List<Pizze> pizzas = pizzaService.findAll();
        Ingredient ingredient = new Ingredient();

        model.addAttribute("pizzas",pizzas);
        model.addAttribute("ingredient",ingredient);

        return "ingredient_create";
    }

    @PostMapping("/create")
    public String ingredientStore(Model model, @Valid @ModelAttribute Ingredient ingredient, BindingResult bindingResult) {
        ingredientService.save(ingredient);
        List<Pizze> pizzas = ingredient.getPizzas();
        for (Pizze pizza : pizzas) {
            System.out.println(pizza);
            pizza.addIngredients(ingredient);
            pizzaService.save(pizza);
        }

        return "redirect:/ingredients";
    }

    @GetMapping("/edit/{id}")
    public String ingredientEdit(Model model, @PathVariable int id) {

        Ingredient ingredient = ingredientService.findById(id);
        List<Pizze> pizzas = pizzaService.findAll();

        model.addAttribute("pizzas", pizzas);
        model.addAttribute("ingredient", ingredient);

        return "ingredient_create";
    }

    @PostMapping("/edit/{id}")
    public String ingredientUpdate(Model model, @Valid @ModelAttribute Ingredient ingredient, BindingResult bindingResult) {
        ingredientService.save(ingredient);

        List<Pizze> pizzas = pizzaService.findAll();
        for(Pizze pizza : pizzas) {
            if(ingredient.hasPizza(pizza)) {
                if(!pizza.hasIngredient(ingredient)) {
                    pizza.addIngredients(ingredient);
                }
            }
            else pizza.removeIngredients(ingredient);

            pizzaService.save(pizza);
        }

        return "redirect:/ingredients";
    }

    @PostMapping("/delete/{ingredient_id}")
    public String ingredientDelete(@PathVariable("ingredient_id") int id) {

        List<Pizze> pizzas = pizzaService.findAll();
        Ingredient ingredient = ingredientService.findById(id);
        for(Pizze pizza : pizzas) {
            if(pizza.hasIngredient(ingredient)) {
                pizza.removeIngredients(ingredient);
            }

            pizzaService.save(pizza);
        }
        ingredientService.deleteIngredient(ingredient);

        return "redirect:/ingredients";
    }
}