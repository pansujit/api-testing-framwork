package com.glide.qa.backend.admin;


import com.glide.qa.BaseAbstract;
import com.glide.qa.backend.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is abstract class..
 *
 * @author sujitpandey
 *
 */
public abstract class AdminAbstract extends BaseAbstract {


  @Autowired
  protected UserService userService;




    }
