package vn.dh_shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "storage")
@Setter
@Getter
public class Storage extends AbstractEntity {

    @Column(name = "name")
    private String name;

}
