package com.mobylab.springbackend.config.security;

import com.mobylab.springbackend.entity.Role;
import com.mobylab.springbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<com.mobylab.springbackend.entity.User> optionalUser = userRepository.findUserByEmail(email);
        if (optionalUser.isPresent()) {
            com.mobylab.springbackend.entity.User user = optionalUser.get();
            return new User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
        } else
            throw new UsernameNotFoundException("User not found");
    }
    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles){
        return roles.stream().map(role->new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
