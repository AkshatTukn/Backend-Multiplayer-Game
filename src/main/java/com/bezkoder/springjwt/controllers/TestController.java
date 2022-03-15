package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.UserInfo;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserinfoRepository;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static jdk.nashorn.internal.objects.NativeMath.min;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController
{
  @Autowired
  RoleRepository roleRepository;
  @Autowired
  UserinfoRepository userinfoRepository;

  @GetMapping("/all")
  public String allAccess() {
    Role role = new Role();
    role.setId(1);
    role.setName(ERole.ROLE_USER);
    roleRepository.save(role);
    Role role2 = new Role();
    role2.setId(2);
    role2.setName(ERole.ROLE_MODERATOR);
    roleRepository.save(role2);
    Role role3 = new Role();
    role3.setId(3);
    role3.setName(ERole.ROLE_ADMIN);
    roleRepository.save(role3);
    return "Public Content.";
  }
  @GetMapping("/leaderboard")
  public ResponseEntity<List<String>> LIST()
  {
    List<UserInfo> userInfoList = userinfoRepository.findTop10ByOrderByIdDesc();
    List<String> ans = null;
    int k = 0;
    for (int i = 0; i < min(userInfoList.size(),8); i++)
    {
      UserInfo userInfo = userInfoList.get(i);
      ans.add(userInfo.getUsername());
    }
    return new ResponseEntity<List<String>>(ans,HttpStatus.OK);
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
