package top.otsuland.market.controller;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import top.otsuland.market.common.Result;
import top.otsuland.market.dto.OrderStatusReq;
import top.otsuland.market.entity.Order;
import top.otsuland.market.service.OrderService;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //create
    @Test
    void testCreate_Success() {
        Order order = new Order();
        when(orderService.create(1, order)).thenReturn(100);

        Result<?> result = orderController.create(1, order);

        assertEquals(1, result.getCode());
        assertEquals("创建成功！", result.getMsg());
        assertEquals(100, result.getData());
    }

    @Test
    void testCreate_MissingInfo() {
        Order order = new Order();
        when(orderService.create(1, order)).thenReturn(-1);

        Result<?> result = orderController.create(1, order);

        assertEquals(-1, result.getCode());
        assertEquals("缺少信息！", result.getMsg());
    }

    @Test
    void testCreate_UserNotExist() {
        Order order = new Order();
        when(orderService.create(1, order)).thenReturn(0);

        Result<?> result = orderController.create(1, order);

        assertEquals(0, result.getCode());
        assertEquals("用户不存在！", result.getMsg());
    }

    //editPaymentMethod
    @Test
    void testEditPaymentMethod_Success() {
        when(orderService.pay(10, 2)).thenReturn(1);

        Result<?> result = orderController.editPaymentMethod(10, 2);

        assertEquals(1, result.getCode());
        assertEquals("修改成功！", result.getMsg());
    }

    @Test
    void testEditPaymentMethod_Fail() {
        when(orderService.pay(10, 2)).thenReturn(0);

        Result<?> result = orderController.editPaymentMethod(10, 2);

        assertEquals(0, result.getCode());
        assertEquals("修改失败！", result.getMsg());
    }

    //editStatus
    @Test
    void testEditStatus_Success() {
        OrderStatusReq req = new OrderStatusReq();
        when(orderService.status(5, req)).thenReturn(1);

        Result<?> result = orderController.editStatus(5, req);

        assertEquals(1, result.getCode());
        assertEquals("修改成功！", result.getMsg());
    }

    @Test
    void testEditStatus_Fail() {
        OrderStatusReq req = new OrderStatusReq();
        when(orderService.status(5, req)).thenReturn(0);

        Result<?> result = orderController.editStatus(5, req);

        assertEquals(0, result.getCode());
        assertEquals("修改失败！", result.getMsg());
    }

    //getOrders
    @Test
    void testGetOrders_Success() {
        Order order = new Order();
        when(orderService.get(1)).thenReturn(List.of(order));

        Result<?> result = orderController.getOrders(1);

        assertEquals(1, result.getCode());
        assertEquals("获取成功！", result.getMsg());
        assertNotNull(result.getData());
    }

    @Test
    void testGetOrders_Fail() {
        when(orderService.get(1)).thenReturn(Collections.emptyList());

        Result<?> result = orderController.getOrders(1);

        assertEquals(0, result.getCode());
        assertEquals("获取失败！", result.getMsg());
    }
}
