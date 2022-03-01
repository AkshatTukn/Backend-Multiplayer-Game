package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Friend;
import com.bezkoder.springjwt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend,Long> {
    boolean existsByFirstUserAndSecondUser(User first,User second);

    List<Friend> findByFirstUser(User user);
    List<Friend> findBySecondUser(User user);
}
