package org.lesson.java.springLaMiaPizzeriaCrud.DB;

import java.util.Arrays;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 128, nullable=false)
    @NotBlank(message="cannot be blank")
    @Length(min = 3, message="must be at least 3 characters")
    private String name;

    @Column(length = 255, nullable=true)
    private String description;

    @ManyToMany(mappedBy = "ingredients")
    private List<Pizze> pizzas;

    public Ingredient() {}

    public Ingredient(String name, String description, Pizze... pizzas) {
        setName(name);
        setDescription(description);
        setPizzas(Arrays.asList(pizzas));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Pizze> getPizzas() {
        return pizzas;
    }

    public void setPizzas(List<Pizze> pizzas) {
        this.pizzas = pizzas;
    }

    public boolean hasPizza(Pizze pizza) {
        if(getPizzas() == null) return false;

        for(Pizze tempPizza : getPizzas()) {
            if(tempPizza.getId() == pizza.getId()) return true;
        }

        return false;
    }

    public void addPizzas(Pizze... pizzas) {
        getPizzas().addAll(Arrays.asList(pizzas));
    }

    public void removePizzas(Pizze... pizzas) {
        getPizzas().removeAll(Arrays.asList(pizzas));
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Ingredient)) return false;
        Ingredient incomingIngr = (Ingredient)obj;
        return getId() == incomingIngr.getId();
    }

    @Override
    public String toString() {
        return "[" + getId() + "] Ingredients:\n"
                + "name: " + getName() + "\n"
                + "description: " + getDescription() + "\n"
                + "pizzas including the ingredient: " + getPizzas();
    }

}