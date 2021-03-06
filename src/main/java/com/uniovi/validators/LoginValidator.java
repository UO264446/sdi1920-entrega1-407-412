package com.uniovi.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.uniovi.entities.User;
import com.uniovi.services.UsersService;

@Component
public class LoginValidator implements Validator {

	@Autowired
	private UsersService usersService;

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "Error.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Error.empty");

		//El email no está en la base de datos
		if (usersService.getUserByEmail(user.getEmail()) == null) {
			errors.rejectValue("email", "Error.signup.email.notExist");
		}
		//Las contraseñas no coinciden
		if (!usersService.getUserByEmail(user.getEmail()).getPassword().equals(user.getPassword())) {
			errors.rejectValue("password", "Error.signup.passwordConfirm.coincidence");
		}
	}

}
