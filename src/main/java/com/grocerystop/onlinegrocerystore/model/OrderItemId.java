package com.grocerystop.onlinegrocerystore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class OrderItemId implements Serializable {
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "item_id")
    private Long itemId;

    public OrderItemId() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + ((itemId == null) ? 0 : itemId.hashCode());
        result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return  false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        OrderItemId other = (OrderItemId) obj;
        if (orderId == null && other.orderId != null) {
            return false;
        } else if (!orderId.equals(other.orderId)) {
            return false;
        } else if (itemId == null && other.itemId != null) {
            return false;
        } else if (!itemId.equals(other.itemId)) {
            return false;
        }

        return true;
    }
}
