package com.dapp.docuchain.model;

import lombok.*;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import javax.persistence.Column;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entry(base = "ou=users", objectClasses = {"person", "inetOrgPerson", "top"})
public class User {

    @Id
    private Name id;

    private @Attribute(name = "cn")
    String username;
    private @Attribute(name = "sn")
    String surname;
    private @Attribute(name = "userPassword")
    String password;
    private @Attribute(name = "businessCategory")
    String businessCategory;
    @Column(unique = true)
    private @Attribute(name = "mail")
    String mail;

    @Override
    public String toString() {
        return username;
    }

}
