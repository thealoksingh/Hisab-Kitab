

package com.hisabKitab.springProject.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "colors")
public class ColorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long colorId;

    @Column(unique = true, nullable = false)
    private String hexValue;

    public ColorEntity() {
    }

    public ColorEntity(String hexValue) {
        this.hexValue = hexValue;
    }

    public Long getColorId() {
        return colorId;
    }

    public void setColorId(Long colorId) {
        this.colorId = colorId;
    }

    public String getHexValue() {
        return hexValue;
    }

    public void setHexValue(String hexValue) {
        this.hexValue = hexValue;
    }

    @Override
    public String toString() {
        return "ColorEntity [colorId=" + colorId + ", hexValue=" + hexValue + "]";
    }
}
