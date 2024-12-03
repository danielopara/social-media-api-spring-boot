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

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfilePhoto profilePhoto;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "following", cascade = CascadeType.ALL)
    private Set<Follow> followers = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "follower", cascade = CascadeType.ALL)
    private Set<Follow> following = new HashSet<>();

    public void addFollower(Follow follow) {
        followers.add(follow);
        follow.setFollowing(this);
    }

    public void addFollowing(Follow follow) {
        following.add(follow);
        follow.setFollower(this);
    }

    public void removeFollower(Follow follow) {
        followers.remove(follow);
        follow.setFollowing(null);
    }

    public void removeFollowing(Follow follow) {
        following.remove(follow);
        follow.setFollower(null);
    }
}
