package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Role;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    public List<String> addRoles(List<String> roleNameList) {
        if (roleNameList.isEmpty()) {
            logger.info("Role list shouldn't be empty");
            throw new BadRequestException("Role list shouldn't be empty");
        }

        return roleNameList.stream()
                .filter(name -> roleRepository.findRoleByName(name).isEmpty())
                .peek(name -> {
                    Role role = new Role().setName(name);
                    roleRepository.save(role);
                })
                .toList();
    }
}
