package it.uniroma3.siw.taskmanager.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.model.Comment;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.service.CommentService;
import it.uniroma3.siw.taskmanager.service.TaskService;

@Controller
public class CommentController {
	
	@Autowired
	SessionData sessionData;
	
	@Autowired
	CommentService commentService;

	@Autowired
	TaskService	taskService;
	
	@RequestMapping(value="/task/{taskId}/addComment", method= RequestMethod.POST)
	public String addComment(Model model, @PathVariable("taskId")Long taskId, @Valid @ModelAttribute("comment")Comment comment) {
		Task taskFromDb= this.taskService.getTask(taskId);
		taskFromDb.addtComment(comment);
		comment.setTask(taskFromDb);
		this.taskService.saveTask(taskFromDb);
		return "redirect:/tasks/" + taskId;
	}
	
	@RequestMapping(value="/task/{taskId}/deleteComment/{commentId}", method= RequestMethod.POST)
	public String deleteComment(Model model, @PathVariable("taskId")Long taskId, @PathVariable("commentId")Long commentId) {
		Comment commentFromDb= this.commentService.getComment(commentId);
		Task taskFromDb=this.taskService.getTask(taskId);
		taskFromDb.getComments().remove(commentFromDb);
		this.commentService.deleteComment(commentFromDb);
		
		this.taskService.saveTask(taskFromDb);
		return "redirect:/tasks/" + taskId;
	}

}
