package top.otsuland.market.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import top.otsuland.market.common.Result;
import top.otsuland.market.dto.PageResult;
import top.otsuland.market.dto.ProductCreateReq;
import top.otsuland.market.dto.ProductPicMetaDTO;
import top.otsuland.market.dto.ProductVO;
import top.otsuland.market.entity.Product;
import top.otsuland.market.service.ProductService;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //add
    @Test
    void testAdd_Success() {
        ProductCreateReq req = new ProductCreateReq();
        when(productService.add(1, req)).thenReturn(10);
        Result<?> result = productController.add(1, req);
        assertEquals(1, result.getCode());
        assertEquals("上传成功！", result.getMsg());
        assertEquals(10, result.getData());
    }

    @Test
    void testAdd_UserNotExist() {
        ProductCreateReq req = new ProductCreateReq();
        when(productService.add(1, req)).thenReturn(-1);
        Result<?> result = productController.add(1, req);
        assertEquals(-1, result.getCode());
        assertEquals("用户不存在！", result.getMsg());
    }

    //pic
    @Test
    void testPic_Success() throws IOException {
        when(productService.pic(0, 1, 2, multipartFile)).thenReturn(1);
        Result<?> result = productController.pic(1, 0, 2, multipartFile);
        assertEquals(1, result.getCode());
        assertEquals("上传成功！", result.getMsg());
    }

    @Test
    void testPic_IOException() throws IOException {
        when(productService.pic(0, 1, 2, multipartFile)).thenThrow(new IOException());
        Result<?> result = productController.pic(1, 0, 2, multipartFile);
        assertEquals(-5, result.getCode());
        assertEquals("输入输出异常！", result.getMsg());
    }

    //picEdit
    @Test
    void testPicEdit_Success() throws IOException {
        when(productService.picEdit(1, 5, multipartFile)).thenReturn(1);
        Result<?> result = productController.picEdit(1, 5, multipartFile);
        assertEquals(1, result.getCode());
        assertEquals("修改成功！", result.getMsg());
    }

    @Test
    void testPicEdit_IOException() throws IOException {
        when(productService.picEdit(1, 5, multipartFile)).thenThrow(new IOException());
        Result<?> result = productController.picEdit(1, 5, multipartFile);
        assertEquals(-3, result.getCode());
        assertEquals("输入输出异常！", result.getMsg());
    }

    //picDel
    @Test
    void testPicDel_Success() {
        when(productService.picDel(1, 5)).thenReturn(1);
        Result<?> result = productController.picDel(1, 5);
        assertEquals(1, result.getCode());
        assertEquals("成功删除！", result.getMsg());
    }

    @Test
    void testPicDel_NoPermission() {
        when(productService.picDel(1, 5)).thenReturn(-2);
        Result<?> result = productController.picDel(1, 5);
        assertEquals(-2, result.getCode());
        assertEquals("无权限或商品不存在！", result.getMsg());
    }

    //get(self list)
    @Test
    void testGet_Success() {
        Page<Product> page = new Page<>();
        page.setRecords(Collections.singletonList(new Product()));
        when(productService.getListPage(any(), eq(1))).thenReturn(page);
        Result<?> result = productController.get(1, 1, 10);
        assertEquals(1, result.getCode());
        assertTrue(result.getData() instanceof PageResult);
    }

    //getMethodName(other list)
    @Test
    void testGetMethodName_Success() {
        Page<Product> page = new Page<>();
        when(productService.getListPage(any(), eq(2))).thenReturn(page);
        Result<?> result = productController.getMethodName(2, 1, 10);
        assertEquals(1, result.getCode());
        assertTrue(result.getData() instanceof PageResult);
    }

    //edit
    @Test
    void testEdit_Success() {
        Product product = new Product();
        when(productService.edit(1, product)).thenReturn(1);
        Result<?> result = productController.edit(1, product);
        assertEquals(1, result.getCode());
        assertEquals("修改成功！", result.getMsg());
    }

    @Test
    void testEdit_NoPermission() {
        Product product = new Product();
        when(productService.edit(1, product)).thenReturn(-2);
        Result<?> result = productController.edit(1, product);
        assertEquals(-2, result.getCode());
        assertEquals("无权限或商品不存在！", result.getMsg());
    }

    //del
    @Test
    void testDel_Success() {
        when(productService.del(1, 9)).thenReturn(1);
        Result<?> result = productController.del(1, 9);
        assertEquals(1, result.getCode());
        assertEquals("成功删除！", result.getMsg());
    }

    @Test
    void testDel_UserNotExist() {
        when(productService.del(1, 9)).thenReturn(-1);
        Result<?> result = productController.del(1, 9);
        assertEquals(-1, result.getCode());
        assertEquals("用户不存在！", result.getMsg());
    }

    //fav
    @Test
    void testFav_Success() {
        when(productService.fav(1, 100)).thenReturn(1);
        Result<?> result = productController.fav(1, 100);
        assertEquals(1, result.getCode());
        assertEquals("收藏成功！", result.getMsg());
    }

    @Test
    void testFav_AlreadyFav() {
        when(productService.fav(1, 100)).thenReturn(-3);
        Result<?> result = productController.fav(1, 100);
        assertEquals(-3, result.getCode());
        assertEquals("已经收藏过该商品了！", result.getMsg());
    }

    //favDel
    @Test
    void testFavDel_Success() {
        when(productService.favDel(1, 100)).thenReturn(1);
        Result<?> result = productController.favDel(1, 100);
        assertEquals(1, result.getCode());
        assertEquals("成功删除！", result.getMsg());
    }

    @Test
    void testFavDel_NotExist() {
        when(productService.favDel(1, 100)).thenReturn(-1);
        Result<?> result = productController.favDel(1, 100);
        assertEquals(-1, result.getCode());
        assertEquals("收藏不存在！", result.getMsg());
    }

    //favList(self)
    @Test
    void testFavList_Success() {
        Page<Product> page = new Page<>();
        when(productService.getFavPage(any(), eq(1))).thenReturn(page);
        Result<?> result = productController.favList(1, 1, 10);
        assertEquals(1, result.getCode());
        assertTrue(result.getData() instanceof PageResult);
    }

    //favListOther
    @Test
    void testFavListOther_Success() {
        Page<Product> page = new Page<>();
        when(productService.getFavPage(any(), eq(2))).thenReturn(page);
        Result<?> result = productController.favListOther(2, 1, 10);
        assertEquals(1, result.getCode());
    }

    //isFav
    @Test
    void testIsFav_Yes() {
        when(productService.isFav(1, 200)).thenReturn(1);
        Result<?> result = productController.isFav(1, 200);
        assertEquals(1, result.getCode());
        assertEquals("已收藏", result.getMsg());
    }

    @Test
    void testIsFav_No() {
        when(productService.isFav(1, 200)).thenReturn(0);
        Result<?> result = productController.isFav(1, 200);
        assertEquals(0, result.getCode());
        assertEquals("未收藏", result.getMsg());
    }

    //getPicMeta
    @Test
    void testGetPicMeta_Success() {
        List<ProductPicMetaDTO> list = Arrays.asList(new ProductPicMetaDTO(1, 0));
        when(productService.getPicsMeta(10)).thenReturn(list);
        Result<?> result = productController.getPicMeta(10);
        assertEquals(1, result.getCode());
        assertEquals("获取成功！", result.getMsg());
    }

    @Test
    void testGetPicMeta_Empty() {
        when(productService.getPicsMeta(10)).thenReturn(Collections.emptyList());
        Result<?> result = productController.getPicMeta(10);
        assertEquals(-1, result.getCode());
        assertEquals("商品不存在或列表为空！", result.getMsg());
    }

    //getPic
    @Test
    void testGetPic_Success() {
        byte[] img = new byte[]{1, 2, 3};
        when(productService.getPic(5)).thenReturn(img);
        ResponseEntity<byte[]> response = productController.getPic(5);
        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(img, response.getBody());
    }

    @Test
    void testGetPic_NotFound() {
        when(productService.getPic(5)).thenReturn(null);
        ResponseEntity<byte[]> response = productController.getPic(5);
        assertEquals(404, response.getStatusCodeValue());
    }

    //list(search)
    @Test
    void testList_Success() {
        Page<Product> page = new Page<>();
        page.setRecords(Collections.singletonList(new Product()));
        when(productService.list(any(), eq(1), any(), any(), any(), any())).thenReturn(page);
        Result<?> result = productController.list(1, 1, 10, null, null, null, "test");
        assertEquals(1, result.getCode());
        assertTrue(result.getData() instanceof PageResult);
        PageResult<ProductVO> pr = (PageResult<ProductVO>) result.getData();
        assertFalse(pr.getRecords().isEmpty());
    }

    //getProduct
    @Test
    void testGetProduct_Success() {
        Product product = new Product();
        product.setId(50);
        when(productService.getProduct(50)).thenReturn(product);
        Result<?> result = productController.getProduct(50);
        assertEquals(1, result.getCode());
        assertEquals(product, result.getData());
    }
}
