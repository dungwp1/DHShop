package vn.dh_shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "ram")
@Setter
@Getter
public class Ram extends AbstractEntity {
    @Column(name = "name")
    private String name;

}
