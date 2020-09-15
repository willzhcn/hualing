package com.hualing.repository;

import com.hualing.domain.OrderExpress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Will on 04/08/2019.
 */
public interface OrderExpressRepository  extends JpaRepository<OrderExpress, Long>, JpaSpecificationExecutor {
    @Query(value = "select oe.status from t_express_distribute e "
                    + "inner join t_order_distribute o on e.distribute_id = o.id "
                    + "inner join t_order_express oe on e.order_express_id=oe.id "
                    + "where o.id = ?1", nativeQuery = true)
    List<Object[]> getAllExpressDistributeStatus(Long orderDistributeId);

    @Query(value = "select d.status from t_order o inner join t_order_distribute d on o.id = d.order_id where o.id = ?1", nativeQuery = true)
    List<Object[]> getAllOrderDistributeStatus(Long orderId);
}
