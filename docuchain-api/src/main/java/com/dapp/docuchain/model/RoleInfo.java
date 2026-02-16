// package com.dapp.docuchain.model;

// import lombok.*;

// import javax.persistence.*;
// import java.util.Set;

// @Entity
// @Getter
// @Setter
// @Table(name = "role_Info")
// public class RoleInfo {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Enumerated(EnumType.STRING)
//    	@Column(name = "role")
//    	private Role roleName;

//     @OneToMany(mappedBy = "roleId", fetch = FetchType.LAZY)
//     private Set<UserProfileInfo> userProfiles;

//     public RoleInfo(){}

//     public RoleInfo(Long id, Role role){
//     	this.id = id;
//     	this.roleName = role;
//     }

// }

package com.dapp.docuchain.model;

import lombok.*;
import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "role_info")
public class RoleInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- CRITICAL FIX ---
    @Enumerated(EnumType.STRING)
    @Column(name = "role") // This tells Java: "The column is named 'role', not 'role_name'"
    private Role roleName;
    // --------------------

    @OneToMany(mappedBy = "roleId", fetch = FetchType.LAZY)
    private Set<UserProfileInfo> userProfiles;

    public RoleInfo(){}

    public RoleInfo(Long id, Role role){
        this.id = id;
        this.roleName = role;
    }
}
