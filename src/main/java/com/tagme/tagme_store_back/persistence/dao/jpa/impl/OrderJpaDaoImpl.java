package com.tagme.tagme_store_back.persistence.dao.jpa.impl;

import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.persistence.dao.jpa.OrderJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.OrderItemJpaEntity;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.OrderJpaEntity;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.ShippingInfoJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.*;

public class OrderJpaDaoImpl implements OrderJpaDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<OrderJpaEntity> findAll(int page, int size) {
        int pageIndex = Math.max(page - 1, 0);
        int validSize = Math.max(size, 1);

        String sql = "SELECT o FROM OrderJpaEntity o ORDER BY o.id";
        TypedQuery<OrderJpaEntity> query = entityManager
                .createQuery(sql, OrderJpaEntity.class)
                .setFirstResult(pageIndex * validSize)
                .setMaxResults(validSize);

        return query.getResultList();
    }

    @Override
    public Optional<OrderJpaEntity> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(entityManager.find(OrderJpaEntity.class, id));
    }

    @Override
    public OrderJpaEntity insert(OrderJpaEntity orderJpaEntity) {
        if (orderJpaEntity == null) {
            throw new IllegalArgumentException("Order entity cannot be null");
        }
        if (orderJpaEntity.getUser() == null) {
            throw new IllegalArgumentException("Order must have a user");
        }
        if (orderJpaEntity.getOrderStatus() == null) {
            throw new IllegalArgumentException("Order must have a status");
        }

        entityManager.persist(orderJpaEntity);
        return orderJpaEntity;
    }

    @Override
    public OrderJpaEntity update(OrderJpaEntity orderJpaEntity) {
        if (orderJpaEntity == null) {
            throw new IllegalArgumentException("Order entity cannot be null");
        }
        if (orderJpaEntity.getId() == null) {
            throw new IllegalArgumentException("Order ID cannot be null for update");
        }

        // Verificar que existe
        OrderJpaEntity existing = entityManager.find(OrderJpaEntity.class, orderJpaEntity.getId());
        if (existing == null) {
            throw new ResourceNotFoundException("Order not found with id: " + orderJpaEntity.getId());
        }

        // Actualizar campos de la orden existente
        existing.setUser(orderJpaEntity.getUser());
        existing.setOrderStatus(orderJpaEntity.getOrderStatus());
        existing.setShippingCost(orderJpaEntity.getShippingCost());
        existing.setPaidDate(orderJpaEntity.getPaidDate());

        // Manejar order items - actualizar existentes y agregar nuevos
        updateOrderItems(existing, orderJpaEntity.getOrderItems());

        // Manejar ShippingInfo - actualizar existente o crear nuevo
        if (orderJpaEntity.getShippingInfo() != null) {
            if (existing.getShippingInfo() != null) {
                // Actualizar el ShippingInfo existente
                ShippingInfoJpaEntity existingShipping = existing.getShippingInfo();
                ShippingInfoJpaEntity newShipping = orderJpaEntity.getShippingInfo();
                existingShipping.setFirstName(newShipping.getFirstName());
                existingShipping.setLastName(newShipping.getLastName());
                existingShipping.setEmail(newShipping.getEmail());
                existingShipping.setAddress(newShipping.getAddress());
                existingShipping.setCity(newShipping.getCity());
                existingShipping.setPostalCode(newShipping.getPostalCode());
                existingShipping.setCountry(newShipping.getCountry());
            } else {
                // Crear nuevo ShippingInfo
                orderJpaEntity.getShippingInfo().setOrder(existing);
                existing.setShippingInfo(orderJpaEntity.getShippingInfo());
            }
        } else {
            // Si el nuevo no tiene ShippingInfo, eliminar el existente
            existing.setShippingInfo(null);
        }

        return existing;
    }

    private void updateOrderItems(OrderJpaEntity existing, List<OrderItemJpaEntity> newItems) {
        if (newItems == null || newItems.isEmpty()) {
            existing.getOrderItems().clear();
            return;
        }

        // Crear un mapa de items existentes por ID para búsqueda rápida
        Map<Long, OrderItemJpaEntity> existingItemsMap = new HashMap<>();
        for (OrderItemJpaEntity item : existing.getOrderItems()) {
            if (item.getId() != null) {
                existingItemsMap.put(item.getId(), item);
            }
        }

        // Crear set de IDs de nuevos items para saber cuáles eliminar
        Set<Long> newItemIds = new HashSet<>();
        for (OrderItemJpaEntity item : newItems) {
            if (item.getId() != null) {
                newItemIds.add(item.getId());
            }
        }

        // Eliminar items que ya no están en la nueva lista
        existing.getOrderItems().removeIf(item ->
            item.getId() != null && !newItemIds.contains(item.getId())
        );

        // Actualizar existentes y agregar nuevos
        for (OrderItemJpaEntity newItem : newItems) {
            if (newItem.getId() != null && existingItemsMap.containsKey(newItem.getId())) {
                // Actualizar item existente
                OrderItemJpaEntity existingItem = existingItemsMap.get(newItem.getId());
                existingItem.setProduct(newItem.getProduct());
                existingItem.setProductName(newItem.getProductName());
                existingItem.setProductImage(newItem.getProductImage());
                existingItem.setProductImageName(newItem.getProductImageName());
                existingItem.setQuantity(newItem.getQuantity());
                existingItem.setBasePrice(newItem.getBasePrice());
                existingItem.setDiscountPercentage(newItem.getDiscountPercentage());
            } else {
                // Agregar nuevo item
                newItem.setId(null); // Asegurar que es nuevo
                newItem.setOrder(existing);
                existing.getOrderItems().add(newItem);
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }

        OrderJpaEntity entity = entityManager.find(OrderJpaEntity.class, id);
        if (entity == null) {
            throw new ResourceNotFoundException("Order not found with id: " + id);
        }
        entityManager.remove(entity);
    }

    @Override
    public Long count() {
        String sql = "SELECT COUNT(o) FROM OrderJpaEntity o";
        return entityManager.createQuery(sql, Long.class).getSingleResult();
    }

    @Override
    public List<OrderJpaEntity> findByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        String sql = "SELECT o FROM OrderJpaEntity o WHERE o.user.id = :userId ORDER BY o.createdAt DESC";
        TypedQuery<OrderJpaEntity> query = entityManager
                .createQuery(sql, OrderJpaEntity.class)
                .setParameter("userId", userId);

        return query.getResultList();
    }

    @Override
    public Optional<OrderJpaEntity> findActiveOrderByUserId(Long userId) {
        if (userId == null) {
            return Optional.empty();
        }

        String sql = "SELECT o FROM OrderJpaEntity o WHERE o.user.id = :userId AND o.orderStatus = :status";
        TypedQuery<OrderJpaEntity> query = entityManager
                .createQuery(sql, OrderJpaEntity.class)
                .setParameter("userId", userId)
                .setParameter("status", OrderStatus.PENDING);

        List<OrderJpaEntity> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }

    @Override
    public Optional<OrderStatus> getOrderStatus(Long orderId) {
        if (orderId == null) {
            return Optional.empty();
        }

        String sql = "SELECT o.orderStatus FROM OrderJpaEntity o WHERE o.id = :orderId";
        TypedQuery<OrderStatus> query = entityManager
                .createQuery(sql, OrderStatus.class)
                .setParameter("orderId", orderId);

        List<OrderStatus> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }
}

