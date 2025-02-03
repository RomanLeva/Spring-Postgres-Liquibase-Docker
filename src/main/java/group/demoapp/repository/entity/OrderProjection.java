package group.demoapp.repository.entity;

/**
 * Projection for {@link Order}
 */
public interface OrderProjection {
    String getInfo();

    String getStatus();

    Integer getSum();
}