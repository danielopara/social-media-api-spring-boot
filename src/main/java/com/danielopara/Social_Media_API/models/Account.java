package com.danielopara.Social_Media_API.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String displayName;

    private String username;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "following", cascade = CascadeType.ALL)
    private Set<Follow> followers = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "follower", cascade = CascadeType.ALL)
    private Set<Follow> following = new HashSet<>();
}
