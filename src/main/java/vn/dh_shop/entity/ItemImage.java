package vn.dh_shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "ItemImage")
@Setter
@Getter
public class ItemImage extends CommonEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "url")
    private String URL;


}
