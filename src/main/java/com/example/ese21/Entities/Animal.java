package com.example.ese21.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String colour;
    private String profilePicture;
}
