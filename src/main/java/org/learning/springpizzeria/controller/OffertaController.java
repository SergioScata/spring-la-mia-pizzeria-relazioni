package org.learning.springpizzeria.controller;

import org.learning.springpizzeria.model.Offerta;
import org.learning.springpizzeria.model.Pizza;
import org.learning.springpizzeria.repository.OffertaRepository;
import org.learning.springpizzeria.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/offers")
public class OffertaController {
    @Autowired
    private PizzaRepository pizzaRepository;
    @Autowired
    private OffertaRepository offertaRepository;

    @GetMapping("/create")
    public String create(@RequestParam(name = "pizzaId", required = true) Integer pizzaId, Model model) {
        Optional<Pizza> result = pizzaRepository.findById(pizzaId);
        if (result.isPresent()){
            Pizza pizzaWithOffer = result.get();
            model.addAttribute("pizza", pizzaWithOffer);
            Offerta newOfferta = new Offerta();
            newOfferta.setPizza(pizzaWithOffer);
            newOfferta.setStartDate(LocalDate.now());
            newOfferta.setEndDate(LocalDate.now().plusDays(30));
            model.addAttribute("newOfferta", newOfferta);
            return "offers/create";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Book with id " + pizzaId + " not found");
        }
    }







}
