package com.victorlevin.yandextaxischeduler.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class MomentPrice {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private LocalDateTime date;
    private Double price;

    public MomentPrice(LocalDateTime date, Double price) {
        this.date = date;
        this.price = price;
    }
}
