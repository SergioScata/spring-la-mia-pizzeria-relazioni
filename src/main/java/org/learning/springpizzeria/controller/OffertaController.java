package org.learning.springpizzeria.controller;

import jakarta.validation.Valid;
import org.learning.springpizzeria.model.Offerta;
import org.learning.springpizzeria.model.Pizza;
import org.learning.springpizzeria.repository.OffertaRepository;
import org.learning.springpizzeria.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("newOfferta") Offerta formOfferta, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("pizza", formOfferta.getPizza());
            return "offers/create";
        }if (formOfferta.getEndDate() != null && formOfferta.getEndDate().isBefore(formOfferta.getStartDate())) {
            formOfferta.setEndDate(formOfferta.getStartDate().plusDays(30));
        }
        Offerta storedOfferta = offertaRepository.save(formOfferta);
        return "redirect:/pizza/show/" + storedOfferta.getPizza().getId();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Optional<Offerta> result =offertaRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("offerta", result.get());
            return "offers/edit";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer with id " + id
                    + " not found");
        }
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("borrowing") Offerta formOfferta, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "offers/edit";
        }
       Offerta updatedOfferta = offertaRepository.save(formOfferta);
        return "redirect:/pizza/show/" + updatedOfferta.getPizza().getId();
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        Optional<Offerta> result = offertaRepository.findById(id);
        if (result.isPresent()) {
            Offerta offertaToDelete = result.get();
            offertaRepository.delete(offertaToDelete);
            return "redirect:/pizza/show/" + offertaToDelete.getPizza().getId();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Offer with id " + id + " not found");
        }
    }

}
