package it.uniroma3.siw.taskmanager.controller.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.taskmanager.model.User;

@Component
public class UserValidator implements Validator {

	final static Integer MAX_NAME_LENGTH= 100;
	final static Integer MIN_NAME_LENGTH= 2;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user=(User)target;
		String firstName= user.getFirstName().trim();
		String lastName= user.getLastName().trim();
		
		if(firstName.isEmpty()) {
			errors.rejectValue("firstName", "required");
		} else if(firstName.length()< MIN_NAME_LENGTH || firstName.length()> MAX_NAME_LENGTH) {
			errors.rejectValue("firstName", "size");
		}
		
		if(lastName.isBlank()) {
			errors.rejectValue("lastName", "required");
		} else if(lastName.length() < MIN_NAME_LENGTH || lastName.length()> MAX_NAME_LENGTH) {
			errors.rejectValue("lastName", "size");
		}
	}

}
