package it.uniroma3.siw.taskmanager.controller.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.service.CredentialsService;

@Component
public class CredentialsValidator implements Validator{
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private SessionData sessionData;

	final Integer MAX_USERNAME_LENGTH=20;
	final Integer MIN_USERNAME_LENGTH=4;
	final Integer MAX_PASSWORD_LENGTH=25;
	final Integer MIN_PASSWORD_LENGTH=6;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Credentials.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Credentials credentials= (Credentials)target;
		String username= credentials.getUsername().trim();
		String password= credentials.getPassword().trim();
		
		if(username.isBlank()) { errors.rejectValue("username", "required"); }
		else if(username.length()< MIN_USERNAME_LENGTH || username.length()> MAX_USERNAME_LENGTH) {
			errors.rejectValue("username", "size");
		}
		
		if(this.credentialsService.getCredentials(username)!=null) {
			errors.rejectValue("username", "duplicate");
		}
		
		if(password.isBlank()) { errors.rejectValue("password", "required"); }
		else if(password.length()< MIN_PASSWORD_LENGTH || username.length()> MAX_PASSWORD_LENGTH) {
			errors.rejectValue("password", "size");
		}
	}
	
	
	public void validateForUpdate(Object target, Errors errors) {
		Credentials credentials= (Credentials)target;
		String username= credentials.getUsername().trim();
		String password= credentials.getPassword().trim();
		Credentials loggedCredentials= sessionData.getLoggedCredentials();
		
		if(username.isBlank()) { errors.rejectValue("username", "required"); }
		else if(username.length()< MIN_USERNAME_LENGTH || username.length()> MAX_USERNAME_LENGTH) {
			errors.rejectValue("username", "size");
		}
		
		if(this.credentialsService.getCredentials(username)!= null && !username.equals(loggedCredentials.getUsername())) {
			errors.rejectValue("username", "duplicate");
		}
		
		if(password.isBlank()) { errors.rejectValue("password", "required"); }
		else if(password.length()< MIN_PASSWORD_LENGTH || username.length()> MAX_PASSWORD_LENGTH) {
			errors.rejectValue("password", "size");
		}
	}

}
