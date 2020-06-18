package it.uniroma3.siw.taskmanager.controller;

import java.time.LocalDateTime;
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
import it.uniroma3.siw.taskmanager.controller.validation.CredentialsValidator;
import it.uniroma3.siw.taskmanager.controller.validation.UserValidator;
import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.CredentialsService;
import it.uniroma3.siw.taskmanager.service.TaskService;
import it.uniroma3.siw.taskmanager.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	SessionData sessionData;
	
	@Autowired 
	UserService userService;
	
	@Autowired
	CredentialsService credentialsService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	CredentialsValidator credentialsValidator;
	
	 @Autowired
	 UserValidator userValidator;
	
	/**
	 * This method is called when a GET request is sent by the User to URL "/home"
	 * This method prepares and dispatches the User home view.
	 * @param model
	 * @return name of the target view
	 */
	@RequestMapping(value= {"/home"}, method= RequestMethod.GET)
	public String home(Model model) {
		User loggedUser= sessionData.getLoggedUser();
		model.addAttribute("user", loggedUser);
		model.addAttribute("credentials", sessionData.getLoggedCredentials());
		return "home";
	}
	
	@RequestMapping(value= {"/users/me"}, method= RequestMethod.GET)
	public String me(Model model) {
		User loggedUser= sessionData.getLoggedUser();
		Credentials credentials= sessionData.getLoggedCredentials();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("credentials", credentials);
		return "userProfile";
	}
	
	@RequestMapping(value= {"/users/me/update"}, method= RequestMethod.GET) 
	public String updateUserForm(Model model) {
		User loggedUser= sessionData.getLoggedUser();
		Credentials credentials= sessionData.getLoggedCredentials();
		model.addAttribute("userForm", loggedUser);
		model.addAttribute("credentialsForm", credentials);
		return "updateUser";
	}
	
	@RequestMapping(value= {"/user/profile/updateForm"}, method= RequestMethod.POST, params="comando=save")
	public String confirmUpdate(@Valid @ModelAttribute("userForm") User user, BindingResult userBindingResult,
            @Valid @ModelAttribute("credentialsForm") Credentials credentials,BindingResult credentialsBindingResult,
            Model model) {
		
		Credentials credFromDb= this.credentialsService.getCredentials(sessionData.getLoggedCredentials().getId());
		User userFromDb= this.userService.getUser(sessionData.getLoggedUser().getId());
		
		//validate user and credentials fields
        this.userValidator.validate(user, userBindingResult);
        this.credentialsValidator.validateForUpdate(credentials, credentialsBindingResult);

        // if neither of them had invalid contents, store the User and the Credentials into the DB
        if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {
			
			userFromDb.setFirstName(user.getFirstName());
			userFromDb.setLastName(user.getLastName());
			userFromDb.setLastUpdateTimeStamp(LocalDateTime.now());
			userFromDb.setCreationTimeStamp(sessionData.getLoggedUser().getCreationTimeStamp());
			
			credFromDb.setUsername(credentials.getUsername());
			credFromDb.setPassword(credentials.getPassword());
			credFromDb.setUser(userFromDb);
			
			sessionData.setUser(userFromDb);
			sessionData.setCredentials(credFromDb);
			
            credentialsService.saveforUpdateCredentials(credFromDb, credFromDb.getRole());
            return "updateUserSuccessful";
        }
		
		return "updateUser";
	}
	
	@RequestMapping(value= {"/user/profile/updateForm"}, method= RequestMethod.POST, params="comando=cancel")
	public String cancelUpdate(Model model) {
		return "redirect:/users/me";
	}
	
	/*******************************************************************ADMIN**********************************************************************/
	
	/**
	 * This method is called when a GET request is sent by the User to URL "/admin"
	 * This method prepares and dispatches the welcome view for admin usage.
	 * 
	 * @param model
	 * @return the name of the target view
	 */
	@RequestMapping(value = {"/admin"}, method = RequestMethod.GET)
    public String admin(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        model.addAttribute("loggedUser", loggedUser);
        return "admin";
    }
	
	/**
	 * This method is called when a GET request is sent by the User to URL "/admin/users"
	 * This method prepares and dispatches the view with the list of all users for admin usage.
	 * 
	 * @param model
	 * @return the name of the target view
	 */
	@RequestMapping(value= {"/admin/users"}, method= RequestMethod.GET)
	public String usersList(Model model) {
		User loggedUser= sessionData.getLoggedUser();
		
		List<Credentials> credentialsList= this.credentialsService.getAllCredentials();
		
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("credentialsList", credentialsList);
		
		return "allUsers";
	}
	
	/**
	 * This method is called when a POST request is sent by the User to URL "/admin/users/{username}/delete"
	 * This method deletes the user with the username chosen.
	 * 
	 * @param model, username 
	 * @return the name of the target view
	 */
	@RequestMapping(value= {"/admin/users/{username}/delete"}, method=RequestMethod.POST)
	public String removeUser(Model model, @PathVariable String username) {
		for(Project p : credentialsService.getCredentials(username).getUser().getVisibleProjects()) {
			p.deleteMember(credentialsService.getCredentials(username).getUser());
		}
		
		for(Task t: credentialsService.getCredentials(username).getUser().getTasks()) {
			t.setUser(null);
		}
		
		this.credentialsService.deleteCredentials(username);
		return "redirect:/admin/users";
	}
}
