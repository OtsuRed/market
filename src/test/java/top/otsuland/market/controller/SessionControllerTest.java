package top.otsuland.market.controller;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import top.otsuland.market.common.Result;
import top.otsuland.market.dto.SessionReq;
import top.otsuland.market.dto.SessionResp;
import top.otsuland.market.entity.Session;
import top.otsuland.market.entity.SessionMes;
import top.otsuland.market.service.SessionService;

class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private SessionController sessionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //session
    @Test
    void testSession_Success() {
        when(sessionService.createSession(1, 2)).thenReturn(10);

        Result<?> result = sessionController.session(1, 2);

        assertEquals(1, result.getCode());
        assertEquals("创建成功！", result.getMsg());
        assertTrue(result.getData() instanceof SessionResp);
        assertEquals(10, ((SessionResp) result.getData()).getSesid());
    }

    @Test
    void testSession_Fail_UserNotExistOrAlreadyCreated() {
        when(sessionService.createSession(1, 2)).thenReturn(0);

        Result<?> result = sessionController.session(1, 2);

        assertEquals(0, result.getCode());
        assertEquals("用户不存在或已经创建！", result.getMsg());
    }

    //msg
    @Test
    void testMsg_Success() {
        SessionReq req = new SessionReq();
        req.setMsg("hello");

        when(sessionService.createMsg(1, 10, "hello")).thenReturn(1);

        Result<?> result = sessionController.msg(1, 10, req);

        assertEquals(1, result.getCode());
        assertEquals("创建成功！", result.getMsg());
    }

    @Test
    void testMsg_Fail() {
        SessionReq req = new SessionReq();
        req.setMsg("fail");

        when(sessionService.createMsg(1, 10, "fail")).thenReturn(0);

        Result<?> result = sessionController.msg(1, 10, req);

        assertEquals(0, result.getCode());
        assertEquals("创建失败！", result.getMsg());
    }

    //ses
    @Test
    void testSes_Success() {
        Session session = new Session();
        when(sessionService.getSes(1)).thenReturn(List.of(session));

        Result<?> result = sessionController.ses(1);

        assertEquals(1, result.getCode());
        assertEquals("获取成功", result.getMsg());
        assertNotNull(result.getData());
    }

    @Test
    void testSes_Fail() {
        when(sessionService.getSes(1)).thenReturn(Collections.emptyList());

        Result<?> result = sessionController.ses(1);

        assertEquals(0, result.getCode());
        assertEquals("获取失败！", result.getMsg());
    }

    //getMsg
    @Test
    void testGetMsg_Success() {
        SessionMes mes = new SessionMes();
        when(sessionService.getMsg(10, 1)).thenReturn(List.of(mes));

        Result<?> result = sessionController.getMsg(1, 10);

        assertEquals(1, result.getCode());
        assertEquals("获取成功", result.getMsg());
        assertNotNull(result.getData());
    }

    @Test
    void testGetMsg_Fail() {
        when(sessionService.getMsg(10, 1)).thenReturn(Collections.emptyList());

        Result<?> result = sessionController.getMsg(1, 10);

        assertEquals(0, result.getCode());
        assertEquals("获取失败", result.getMsg());
    }
}
