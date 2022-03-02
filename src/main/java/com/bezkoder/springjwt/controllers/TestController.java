package com.bezkoder.springjwt.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController
{
 @GetMapping("/all")
  public String allAccess() {
    Role role = new Role();
    role.setId(1);
    role.setName(ERole.valueOf("ROLE_USER"));
    roleRepository.save(role);
    Role role2 = new Role();
    role.setId(2);
    role.setName(ERole.valueOf("ROLE_MODERATOR"));
    roleRepository.save(role);
    Role role3 = new Role();
    role.setId(3);
    role.setName(ERole.valueOf("ROLE_ADMIN"));
    roleRepository.save(role);
    return "Public Content.";
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/mod")
  @PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess() {
    return "Moderator Board.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }
}
