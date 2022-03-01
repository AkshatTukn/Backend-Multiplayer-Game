package com.bezkoder.springjwt.security.services;

import com.bezkoder.springjwt.models.Friend;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.models.UserDto;
import com.bezkoder.springjwt.payload.request.AddFriend;
import com.bezkoder.springjwt.repository.FriendRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FriendService {

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SecurityService securityService;

    public void saveFriend(UserDetailsImpl userDto1, Long id) throws NullPointerException{

        User user = userRepository.getById(id);
        //UserDetailsImpl userDto2 = modelMapper.map(user,UserDetailsImpl.class);
        Friend friend = new Friend();
        User user1 = userRepository.findByEmail(userDto1.getEmail());
        User user2 = user;
        User firstuser = user1;
        User seconduser = user2;
        if(user1.getId() > user2.getId())
        {
            firstuser = user2;
            seconduser = user1;
        }
        if( !(friendRepository.existsByFirstUserAndSecondUser(firstuser,seconduser)) )
        {
            friend.setCreatedDate(new Date());
            friend.setFirstUser(firstuser);
            friend.setSecondUser(seconduser);
            friendRepository.save(friend);
        }
    }
    public List<AddFriend> getFriends()
    {
        UserDetailsImpl currentUserDto = securityService.getUser();
        User currentUser = userRepository.findByEmail(currentUserDto.getEmail());
        List<Friend> friendsByFirstUser = friendRepository.findByFirstUser(currentUser);
        List<Friend> friendsBySecondUser = friendRepository.findBySecondUser(currentUser);
        List<User> friendUsers = new ArrayList<>();
           /* suppose there are 3 users with id 1,2,3.
            if user1 add user2 as friend database record will be first user = user1 second user = user2
            if user3 add user2 as friend database record will be first user = user2 second user = user3
            it is because of lexicographical order
            while calling get friends of user 2 we need to check as a both first user and the second user
         */
        for (Friend friend : friendsByFirstUser)
        {
            User user = userRepository.findById(friend.getSecondUser().getId())
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username"));
            friendUsers.add(user);
        }
      /*  for (Friend friend : friendsBySecondUser)
        {
            User user = userRepository.findById(friend.getFirstUser().getId())
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username" ));
            friendUsers.add(user);
        }*/
        List<AddFriend> friends = new ArrayList<>();
        int k =0;
     for( User user :friendUsers)
     {
         AddFriend addFriend = new AddFriend();
         addFriend.setId(user.getId());
         addFriend.setUsername(user.getUsername());
         friends.add(addFriend);
         k++;
         if(k>=7){break;}
     }
        return friends;

       // return friendUsers;

    }
}
