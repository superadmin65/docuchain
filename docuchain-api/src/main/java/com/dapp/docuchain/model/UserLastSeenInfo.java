package com.dapp.docuchain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "userlastseen_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserLastSeenInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String userName;

    @Column(name = "lastseen_date_time")
    private Date userLastSeenDateTime;


}
