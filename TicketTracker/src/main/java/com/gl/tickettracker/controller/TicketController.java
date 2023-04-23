package com.gl.tickettracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gl.tickettracker.service.TicketService;

import jakarta.servlet.http.HttpSession;

import com.gl.tickettracker.entity.Ticket;

@Controller
public class TicketController {

	@Autowired
	private TicketService ticketService;

	public TicketController(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@GetMapping("/tickets")
	public String listTickets(Model model) {
		model.addAttribute("tickets", ticketService.getAllTickets());
		return "tickets";
	}

	@GetMapping("/tickets/new")
	public String createTicketForm(Model model) {
		// create ticket object to hold ticket form data
		Ticket ticket = new Ticket();
		model.addAttribute("ticket", ticket);
		return "create_ticket";
	}

	@PostMapping("/tickets")
	public String saveTicket(@ModelAttribute("ticket") Ticket ticket) {
		ticketService.saveTicket(ticket);
		return "redirect:/tickets";
	}

	@GetMapping("/tickets/edit/{id}")
	public String editTicketForm(@PathVariable Long id, Model model) {
		model.addAttribute("ticket", ticketService.getTicketById(id));
		return "edit_ticket";
	}

	@PostMapping("/tickets/{id}")
	public String updateTicket(@PathVariable Long id, @ModelAttribute("ticket") Ticket ticket, Model model) {
		// get ticket from database by id
		Ticket existingTicket = ticketService.getTicketById(id);
		existingTicket.setId(id);
		existingTicket.setTitle(ticket.getTitle());
		existingTicket.setDescription(ticket.getDescription());
		existingTicket.setContent(ticket.getContent());
		// save updated ticket object
		ticketService.updateTicket(existingTicket);
		return "redirect:/tickets";
	}

	@GetMapping("/tickets/{id}/delete")
	public String deleteTicket(@PathVariable Long id) {
		ticketService.deleteTicketById(id);
		return "redirect:/tickets";
	}

	@GetMapping("/tickets/{id}")
	public String ticketCard(@PathVariable Long id, Model model) {
		model.addAttribute("ticket", ticketService.getTicketById(id));
		return "ticket_details";
	}

	@PostMapping("/tickets/search")
	public String listSearchedTickets(Model model, @RequestParam("searchText") String text) {
		model.addAttribute("tickets", ticketService.getTicketsByTitleOrDescription(text, text));
		return "search_page";
	}

}