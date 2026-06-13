package it.uniroma3.siw.progetto.controller;

import it.uniroma3.siw.progetto.service.TavoloService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/tavoli")
public class TavoloController {

    private final TavoloService tavoloService;

    public TavoloController(TavoloService tavoloService) {
        this.tavoloService = tavoloService;
    }

    @GetMapping
    public String lista(Model model){
        model.addAttribute("tavoli", tavoloService.findAll());
        return "admin/tavoli/lista";
    }
}
