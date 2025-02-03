package group.demoapp.repository.entity;

import java.util.List;

/**
 * Projection for {@link User}
 */
public interface UserProjection {
    String getName();

    String getEmail();

    List<OrderProjection> getOrders();
}