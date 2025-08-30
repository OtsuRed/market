package top.otsuland.market.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import top.otsuland.market.common.Result;
import top.otsuland.market.dto.PageResult;
import top.otsuland.market.dto.UserFollowVO;
import top.otsuland.market.dto.UserFollowVO2;
import top.otsuland.market.dto.UserLoginResp;
import top.otsuland.market.dto.UserMetaResp;
import top.otsuland.market.dto.UserProfResp;
import top.otsuland.market.entity.User;
import top.otsuland.market.entity.UserProfile;
import top.otsuland.market.service.UserService;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //list
    @Test
    void testList() {
        when(userService.getUsersList()).thenReturn(Collections.singletonList(new User()));
        Result<?> result = userController.list();
        assertEquals(1, result.getCode());
        assertTrue(result.getData() instanceof List);
    }

    //register
    @Test
    void testRegister_Success() {
        User user = new User();
        when(userService.register(user)).thenReturn(1);
        Result<?> result = userController.register(user);
        assertEquals(1, result.getCode());
        assertEquals("注册成功！", result.getMsg());
    }

    @Test
    void testRegister_UserExists() {
        User user = new User();
        when(userService.register(user)).thenReturn(-1);
        Result<?> result = userController.register(user);
        assertEquals(-1, result.getCode());
        assertEquals("用户名已经被使用！", result.getMsg());
    }

    //login
    @Test
    void testLogin_Success() {
        User user = new User();
        when(userService.login(user)).thenReturn(100); 
        when(userService.withId(user)).thenReturn(user);
        Result<?> result = userController.login(user);
        assertEquals(1, result.getCode());
        assertTrue(result.getData() instanceof UserLoginResp);
    }

    @Test
    void testLogin_WrongPassword() {
        User user = new User();
        when(userService.login(user)).thenReturn(-4);
        Result<?> result = userController.login(user);
        assertEquals(-4, result.getCode());
        assertEquals("密码不正确！", result.getMsg());
    }

    //meta
    @Test
    void testMeta_Success() {
        User user = new User();
        when(userService.meta(1, user)).thenReturn(1);
        UserMetaResp meta = mock(UserMetaResp.class);
        when(userService.getMeta(1)).thenReturn(meta);

        Result<?> result = userController.meta(1, user);
        assertEquals(1, result.getCode());
        assertEquals("修改成功！", result.getMsg());
        assertSame(meta, result.getData());
    }

    @Test
    void testMeta_Fail() {
        User user = new User();
        when(userService.meta(1, user)).thenReturn(0);
        Result<?> result = userController.meta(1, user);
        assertEquals(0, result.getCode());
        assertEquals("修改失败！", result.getMsg());
    }

    //prof
    @Test
    void testProf_Success() {
        UserProfile profile = new UserProfile();
        when(userService.prof(1, profile)).thenReturn(1);
        Result<?> result = userController.prof(1, profile);
        assertEquals(1, result.getCode());
        assertEquals("修改成功！", result.getMsg());
    }

    @Test
    void testProf_UserNotExist() {
        UserProfile profile = new UserProfile();
        when(userService.prof(1, profile)).thenReturn(0);
        Result<?> result = userController.prof(1, profile);
        assertEquals(0, result.getCode());
        assertEquals("用户不存在！", result.getMsg());
    }

    //getProf
    @Test
    void testGetProf_Success() {
        UserProfile profile = new UserProfile();
        when(userService.getProf(1)).thenReturn(profile);
        UserMetaResp meta = mock(UserMetaResp.class);
        when(meta.getUsername()).thenReturn("testUser");
        when(meta.getFollow()).thenReturn(5);
        when(meta.getFans()).thenReturn(3);
        when(meta.getTel()).thenReturn("12345");
        when(userService.getMeta(1)).thenReturn(meta);

        Result<?> result = userController.getProf(1);
        assertEquals(1, result.getCode());
        assertTrue(result.getData() instanceof UserProfResp);

        UserProfResp upr = (UserProfResp) result.getData();
        assertEquals("testUser", upr.getUsername());
        assertEquals(5, upr.getFollow());
        assertEquals(3, upr.getFans());
        assertEquals("12345", upr.getTel());
    }

    @Test
    void testGetProf_Fail() {
        when(userService.getProf(1)).thenReturn(null);
        Result<?> result = userController.getProf(1);
        assertEquals(0, result.getCode());
        assertEquals("获取失败！", result.getMsg());
    }

    //getProfWithoutUid
    @Test
    void testGetProfWithoutUid_Success() {
        UserProfile profile = new UserProfile();
        when(userService.getProf(1)).thenReturn(profile);

        UserMetaResp meta = mock(UserMetaResp.class);
        when(meta.getUsername()).thenReturn("selfUser");
        when(meta.getFollow()).thenReturn(2);
        when(meta.getFans()).thenReturn(1);
        when(meta.getTel()).thenReturn("54321");
        when(userService.getMeta(1)).thenReturn(meta);

        Result<?> result = userController.getProfWithoutUid(1);
        assertEquals(1, result.getCode());
        assertTrue(result.getData() instanceof UserProfResp);

        UserProfResp upr = (UserProfResp) result.getData();
        assertEquals("selfUser", upr.getUsername());
        assertEquals(2, upr.getFollow());
        assertEquals(1, upr.getFans());
        assertEquals("54321", upr.getTel());
    }

    @Test
    void testGetProfWithoutUid_Fail() {
        when(userService.getProf(1)).thenReturn(null);
        Result<?> result = userController.getProfWithoutUid(1);
        assertEquals(0, result.getCode());
        assertEquals("获取失败！", result.getMsg());
    }

    //icon
    @Test
    void testIcon_Success() throws IOException {
        when(userService.icon(1, multipartFile)).thenReturn(1);
        Result<?> result = userController.icon(1, multipartFile);
        assertEquals(1, result.getCode());
        assertEquals("上传成功！", result.getMsg());
    }

    @Test
    void testIcon_IOError() throws IOException {
        when(userService.icon(1, multipartFile)).thenThrow(new IOException());
        Result<?> result = userController.icon(1, multipartFile);
        assertEquals(-2, result.getCode());
        assertEquals("输入输出异常！", result.getMsg());
    }

    //loadIcon2
    @Test
    void testLoadIcon2_Success() {
        byte[] image = new byte[]{1, 2, 3};
        when(userService.getIcon(1)).thenReturn(image);
        ResponseEntity<?> response = userController.loadIcon2(1);
        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(image, (byte[]) response.getBody());
    }

    @Test
    void testLoadIcon2_Null() {
        when(userService.getIcon(1)).thenReturn(null);
        ResponseEntity<?> response = userController.loadIcon2(1);
        assertEquals(200, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    //follow
    @Test
    void testFollow_Success() {
        when(userService.follow(1, 2)).thenReturn(1);
        Result<?> result = userController.follow(1, 2);
        assertEquals(1, result.getCode());
        assertEquals("关注成功！", result.getMsg());
    }

    @Test
    void testFollow_AlreadyFollowed() {
        when(userService.follow(1, 2)).thenReturn(-1);
        Result<?> result = userController.follow(1, 2);
        assertEquals(-1, result.getCode());
        assertEquals("已关注！", result.getMsg());
    }

    //disfollow
    @Test
    void testDisfollow_Success() {
        when(userService.disfollow(1, 2)).thenReturn(1);
        Result<?> result = userController.disfollow(1, 2);
        assertEquals(1, result.getCode());
        assertEquals("取消关注！", result.getMsg());
    }

    @Test
    void testDisfollow_NotFollowing() {
        when(userService.disfollow(1, 2)).thenReturn(-1);
        Result<?> result = userController.disfollow(1, 2);
        assertEquals(-1, result.getCode());
        assertEquals("未关注！", result.getMsg());
    }

    //getfollower
    @Test
    void testGetFollower_Success() {
        Page<UserFollowVO> page = new Page<>();
        page.setRecords(Collections.singletonList(new UserFollowVO()));
        when(userService.getFolloweePage(any(), eq(1))).thenReturn(page);
        Result<?> result = userController.getfollower(1, 1, 10);
        assertEquals(1, result.getCode());
        assertTrue(result.getData() instanceof PageResult);
    }

    //getfollowing
    @Test
    void testGetFollowing_Success() {
        Page<UserFollowVO2> page = new Page<>();
        page.setRecords(Collections.singletonList(new UserFollowVO2()));
        when(userService.getFollowerPage(any(), eq(1))).thenReturn(page);
        Result<?> result = userController.getfollowing(1, 1, 10);
        assertEquals(1, result.getCode());
        assertTrue(result.getData() instanceof PageResult);
    }

    //isFollowing
    @Test
    void testIsFollowing_Yes() {
        when(userService.isFollowing(1, 2)).thenReturn(1);
        Result<?> result = userController.isFollowing(1, 2);
        assertEquals(1, result.getCode());
        assertEquals("已关注", result.getMsg());
    }

    @Test
    void testIsFollowing_No() {
        when(userService.isFollowing(1, 2)).thenReturn(0);
        Result<?> result = userController.isFollowing(1, 2);
        assertEquals(0, result.getCode());
        assertEquals("未关注", result.getMsg());
    }
}

