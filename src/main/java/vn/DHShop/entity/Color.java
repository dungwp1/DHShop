package vn.DHShop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "color")
@Setter
@Getter
public class Color extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "hexColor")
    private String hexColor;


}
