package com.mywork.demoTwitterTheThird.repository;

import com.mywork.demoTwitterTheThird.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByRole(String role);
}
