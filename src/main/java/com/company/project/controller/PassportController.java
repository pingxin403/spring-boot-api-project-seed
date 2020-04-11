package com.company.project.controller;


import com.company.project.business.annotation.BussinessLog;
import com.company.project.business.consts.JwtConstant;
import com.company.project.business.service.ISysUserService;
import com.company.project.business.service.RedisService;
import com.company.project.business.vo.user.LoginReqVO;
import com.company.project.business.vo.user.RegisterReqVO;
import com.company.project.business.vo.user.UpdatePasswordReqVO;
import com.company.project.framework.exception.code.BaseResponseCode;
import com.company.project.framework.object.ResponseVO;
import com.company.project.util.JwtTokenUtil;
import com.company.project.util.ResultUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 登录相关
 */

@Slf4j
@RestController
@RequestMapping(value = "/passport")
@Api(tags = "系统模块-认证管理")
public class PassportController {
    @Autowired
    private RedisService redisService;
    @Autowired
    private ISysUserService userService;

    /**
     * 登录
     *
     * @return
     */
    @PostMapping("/signin")
    @ResponseBody
    public ResponseVO submitLogin(@RequestBody LoginReqVO vo) {
        return ResultUtil.success(userService.login(vo));
    }

    @PostMapping("/register")
    @ApiOperation(value = "用户注册接口")
    public ResponseVO register(@RequestBody @Valid RegisterReqVO vo) {
        return ResultUtil.success(userService.register(vo));
    }

    @GetMapping("/token")
    @ApiOperation(value = "用户刷新token接口")
    @BussinessLog(value = "用户管理", action = "用户刷新token")
    public ResponseVO refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(JwtConstant.REFRESH_TOKEN);
        String accessToken = request.getHeader(JwtConstant.ACCESS_TOKEN);
        return ResultUtil.success("ok", userService.refreshToken(refreshToken, accessToken));
    }

    @GetMapping("/unLogin")
    @ApiOperation(value = "引导客户端去登录")
    public ResponseVO unLogin() {
        return ResultUtil.error(BaseResponseCode.TOKEN_ERROR);
    }

    @GetMapping("/logout")
    @ApiOperation(value = "退出接口")
    @BussinessLog(value = "用户管理", action = "退出")
    public ResponseVO logout(HttpServletRequest request) {
        String accessToken = request.getHeader(JwtConstant.ACCESS_TOKEN);
        String refreshToken = request.getHeader(JwtConstant.REFRESH_TOKEN);
        userService.logout(accessToken, refreshToken);
        return ResultUtil.success("退出");
    }

    @PutMapping("/pwd")
    @ApiOperation(value = "修改密码接口")
    @BussinessLog(value = "用户管理", action = "更新密码")
    public ResponseVO updatePwd(@RequestBody UpdatePasswordReqVO vo, HttpServletRequest request) {
        String accessToken = request.getHeader(JwtConstant.ACCESS_TOKEN);
        String refreshToken = request.getHeader(JwtConstant.REFRESH_TOKEN);
        Long userId = Long.parseLong(JwtTokenUtil.getUserId(accessToken));
        userService.updatePwd(vo, userId, accessToken, refreshToken);
        return ResultUtil.success("更新密码");
    }

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    /**
     * 获取验证码
     *
     * @param response
     */
    @GetMapping("/code")
    public void getGifCode(HttpServletResponse response, HttpServletRequest request) throws IOException {
        byte[] verByte = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            String uuid = UUID.randomUUID().toString();

            //生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            response.setHeader(JwtConstant.CAPTCHA_PARAM, uuid);
            redisService.set(JwtConstant.KAPTCHA_CACHE_KEY + uuid, createText, 10, TimeUnit.MINUTES);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        verByte = jpegOutputStream.toByteArray();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(verByte);
        responseOutputStream.flush();
        responseOutputStream.close();
    }


}
