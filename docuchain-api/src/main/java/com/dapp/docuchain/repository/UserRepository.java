package com.dapp.docuchain.repository;

import com.dapp.docuchain.model.User;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends LdapRepository<User> {

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    User findByUsernameLikeIgnoreCase(String username);

    List<User> findByUsernameAndBusinessCategory(String username, String businessCategory);

    List<User> findByBusinessCategory(String businessCategory);

    List<User> findAll();

    List<User> findByMail(String emailId);

    User findUserByMail(String emailId);


}
