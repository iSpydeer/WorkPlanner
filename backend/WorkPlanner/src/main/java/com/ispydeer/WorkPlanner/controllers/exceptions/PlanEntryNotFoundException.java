package com.ispydeer.WorkPlanner.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlanEntryNotFoundException extends RuntimeException {}
