package it.uniroma3.siw.taskmanager.controller.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.taskmanager.model.Credentials;

import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.repository.CredentialsRepository;

/**
 * SessionData is an interface to save and retrieve specific objects from the current session.
 * It is mainly used to store the currently logged User and HIS Credentials.
 * @author Daniele Laino
 *
 */
@Component
@Scope(value="session", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class SessionData {
	private User user;         				//currently logged User
	private Credentials credentials;        //currently Credentials's User
	
	
	@Autowired
	private CredentialsRepository credentialsRepository;
	
	/**
	 * Store the Credentials and User object for the currently logged user in Session
	 */
	private void update() {
		Object o= SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails loggedUserDetails = (UserDetails)o;
		
		this.credentials= this.credentialsRepository.findByUsername(loggedUserDetails.getUsername()).get();
		this.credentials.setPassword("[PROTECTED]");
		this.user= this.credentials.getUser();
	}
	
	/**
	 * Retrieve credentials for the currently logged user.
	 * If they are not stored in SessionData already, get em the SecurityContext and from the DB
	 * @return  credentials for the currently logged user.
	 */
	public Credentials getLoggedCredentials() {
		if(this.credentials==null) { this.update(); }
		return this.credentials;
	}
	
	/**
	 * Retrieve the currently logged user.
	 * If they are not stored in SessionData already, get him the SecurityContext and from the DB
	 * @return  the currently logged user.
	 */
	public User getLoggedUser() {
		if(this.user==null) {this.update(); }
		return this.user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @param credentials the credentials to set
	 */
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

}
