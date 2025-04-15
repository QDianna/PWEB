package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class BasicsService {
    protected final UserRepository userRepository;

    public BasicsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected User getCurrentUser() {
        org.springframework.security.core.userdetails.User springUser =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        return userRepository.findUserByEmail(springUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    protected boolean isAdmin(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));
    }

    protected void checkOwnershipOrAdmin(User resourceOwner, User currentUser) {
        if (!isAdmin(currentUser) && !resourceOwner.getId().equals(currentUser.getId())) {
            throw new com.mobylab.springbackend.exception.ForbiddenException("Access denied - you are not admin/owner of this resource!");
        }
    }

}
