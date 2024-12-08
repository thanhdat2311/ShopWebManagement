package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "social_media")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider", length = 20)
    private String provider;
    @Column(name = "provider_id", length = 50)
    private String provider_id;
    @Column(name = "email", length = 150)
    private String email;
    @Column(name = "name", length = 100)
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
