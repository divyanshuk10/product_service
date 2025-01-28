package com.divyanshu.productservice.repository;

import com.divyanshu.productservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
