package com.danielopara.Social_Media_API.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profile_photo")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfilePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private String fileName;

    private String filePath;

    private Long fileSize;
}
