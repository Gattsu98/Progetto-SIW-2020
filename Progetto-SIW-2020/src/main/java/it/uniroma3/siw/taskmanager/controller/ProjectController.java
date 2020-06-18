package it.uniroma3.siw.taskmanager.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.controller.validation.ProjectValidator;
import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.CredentialsService;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.UserService;

@Controller
public class ProjectController {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired 
	private UserService userService;
	
	@Autowired
	private SessionData sessionData;
	
	@Autowired
	private ProjectValidator projectValidator;
	
	//lista dei progetti per l'utente loggato
	@RequestMapping(value={"/projects"}, method= RequestMethod.GET)
	public String myOwnedProjects(Model model) {
		User loggedUser= sessionData.getLoggedUser();
		List<Project> projectsList= this.projectService.retrieveProjectsOwnedBy(loggedUser);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectsList", projectsList);
		return "projects";
		
	}
	
	//mostra il singolo progetto
	@RequestMapping(value= {"/projects/{projectId}"}, method= RequestMethod.GET)
	public String project(Model model, @PathVariable Long projectId) {
		Project project = projectService.getProject(projectId);
		if(project==null) { return "redirect:/projects"; }
		
		User loggedUser= sessionData.getLoggedUser();
		
		List<User> members = userService.getMembers(project);
		List<Credentials> credentialsMembers = this.credentialsService.getCredentialsByUsers(members);
		
		if(!project.getOwner().equals(loggedUser) && !members.contains(loggedUser)) {
			return "redirect:/projects";
		}
		
		model.addAttribute("credentialsService", this.credentialsService);
		model.addAttribute("credentialsMembers", credentialsMembers);
		model.addAttribute("ownerCredentials", this.credentialsService.getCredentialsByUserId(project.getOwner().getId()));
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("project", project);
		
		return "project";
	}
	
	//prepara la form per inserire il progetto
	@RequestMapping(value={"/projects/add"}, method= RequestMethod.GET)
	public String createProjectForm(Model model) {
		User loggedUser= sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectForm", new Project());
		return "addProject";
	}
	
	//salva il progetto
	@RequestMapping(value= {"/projects/add"}, method= RequestMethod.POST)
	public String createProject(Model model, @Valid @ModelAttribute("projectForm") Project projectForm, BindingResult projectBindingResult) {
		User loggedUser= sessionData.getLoggedUser();
		this.projectValidator.validate(projectForm, projectBindingResult);
		if(!projectBindingResult.hasErrors()) {
			projectForm.setOwner(loggedUser);
			this.projectService.saveProject(projectForm);
			return ("redirect:/projects/" + projectForm.getId());
		}
		
		model.addAttribute("loggedUser", loggedUser);
		return "addProject";
	}
	
	@RequestMapping(value= {"/projects/shared"}, method= RequestMethod.GET)
	public String myVisibleProjects(Model model) {
		User loggedUser= sessionData.getLoggedUser();
		List<Project> projectsList= this.projectService.retrieveVisibleProjects(loggedUser);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectsList", projectsList);
		return "projectsShared";
	}
	
	
/**************************************************ELIMINA PROGETTO**************************************/
	
	/**
	 * 
	 * @param model
	 * @param projectId
	 * @return mapping value in ProjectController
	 */
	@RequestMapping(value= {"/projects/{projectId}/delete"}, method= RequestMethod.POST)
	public String deleteProject(Model model, @PathVariable Long projectId) {
		for(User u : this.userService.getAllUsers()) {
			u.deleteVisibleProject(this.projectService.getProject(projectId));
		}
		this.projectService.deleteProjectById(projectId);
		return "redirect:/projects";
	}	
	
/*******************************************MODIFICA PROGETTO****************************************************************************/
	@RequestMapping(value= {"/projects/{projectId}/editProjectForm"}, method=RequestMethod.GET)
	public String showUpdateProjectForm(Model model, @PathVariable Long projectId) {
		Project projFromDb= this.projectService.getProject(projectId);
		model.addAttribute("projectForm", projFromDb);
		return "editProject";
	}
	
	@RequestMapping(value= {"/projects/{projectId}/edit"}, method= RequestMethod.POST, params="comando=save")
	public String editProject(Model model, @PathVariable Long projectId, @Valid @ModelAttribute("projectForm") Project project, BindingResult projectBindingResult) {
		Project projFromDb= this.projectService.getProject(projectId);
		
		this.projectValidator.validate(projFromDb, projectBindingResult);
		if(!projectBindingResult.hasErrors()) {
			projFromDb.setName(project.getName());
			projFromDb.setDescription(project.getDescription());
			this.projectService.saveProject(projFromDb);
			return "redirect:/projects/" + projFromDb.getId() ;
		}
		return "editProject";
	}
	
	@RequestMapping(value= {"/projects/{projectId}/edit"}, method= RequestMethod.POST, params="comando=cancel")
	public String cancelEditProject(Model model, @PathVariable Long projectId) {
		Project project = projectService.getProject(projectId);
		model.addAttribute("project", project);
		return "redirect:/projects/{projectId}";
	}

/***********************************************************CONDIVIDI PROGETTO*********************************************************/
	
	@RequestMapping(value= {"/projects/{projectId}/shareProjectForm"}, method= RequestMethod.GET)
	public String showShareForm(Model model, @PathVariable Long projectId) {
		User loggedUser= sessionData.getLoggedUser();
		Project projFromDb= this.projectService.getProject(projectId);
		List<User> availableUsersList= this.userService.retrieveUsersNotMembers(projFromDb.getMembers(), loggedUser);
		List<Credentials> availableCredentialsList= this.credentialsService.getCredentialsByUsers(availableUsersList);
		
		model.addAttribute("project", projFromDb);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("availableCredentialsList", availableCredentialsList);
		
		return "shareProject";
	}
	
	@RequestMapping(value= {"/projects/{projectId}/shareProject/{userId}"}, method=RequestMethod.POST)
	public String shareProject(Model model, @PathVariable("userId") Long userId, @PathVariable("projectId") Long projectId) {
		User userFromDb= this.userService.getUser(userId);
		Project projFromDb= this.projectService.getProject(projectId);
		
		userFromDb.addVisibleProject(projFromDb);
		projFromDb.addMembers(userFromDb);
		this.projectService.saveProject(projFromDb);
		this.userService.saveUser(userFromDb);
		return "redirect:/projects/" + projectId;
	}
	
}
