package com.company.project.controller;


import com.company.project.business.annotation.BussinessLog;
import com.company.project.business.service.ISysUserService;
import com.company.project.business.service.RedisService;
import com.company.project.business.vo.user.LoginReqVO;
import com.company.project.business.vo.user.RegisterReqVO;
import com.company.project.business.vo.user.UpdatePasswordReqVO;
import com.company.project.framework.exception.code.BaseResponseCode;
import com.company.project.framework.holder.RequestHolder;
import com.company.project.framework.object.ResponseVO;
import com.company.project.framework.property.AppProperties;
import com.company.project.persistence.beans.SysUser;
import com.company.project.plugin.kaptcha.Captcha;
import com.company.project.plugin.kaptcha.GifCaptcha;
import com.company.project.util.ResultUtil;
import com.company.project.util.SessionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

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

    @Autowired
    private AppProperties config;

    /**
     * 登录
     *
     * @return
     */
    @PostMapping("/signin")
    @ResponseBody
    public ResponseVO submitLogin(@RequestBody LoginReqVO vo) {
        if (config.isEnableKaptcha()) {
            if (StringUtils.isEmpty(vo.getCaptcha()) || !vo.getCaptcha().equals(SessionUtil.getKaptcha())) {
                return ResultUtil.error("验证码错误！");
            }
            SessionUtil.removeKaptcha();
        }
        UsernamePasswordToken token = new UsernamePasswordToken(vo.getUsername(), vo.getPassword(), vo.getRememberMe());
        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        try {
            // 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            // 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            // 所以这一步在调用login(token)方法时,它会走到xxRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            currentUser.login(token);
            SysUser user = userService.getByUserName(vo.getUsername());
            SessionUtil.setUser(user);
            SavedRequest savedRequest = WebUtils.getSavedRequest(RequestHolder.getRequest());
            String historyUrl = null;
            if (null != savedRequest) {
                if (!"POST".equals(savedRequest.getMethod())) {
                    historyUrl = savedRequest.getRequestUrl();
                }
            }
            return ResultUtil.success(null, historyUrl);
        } catch (Exception e) {
            log.error("登录失败，用户名[{}]：{}", vo.getUsername(), e.getMessage());
            token.clear();
            return ResultUtil.error(e.getMessage());
        }
    }

    @PostMapping("/register")
    @ApiOperation(value = "用户注册接口")
    public ResponseVO register(@RequestBody @Valid RegisterReqVO vo) {
        return ResultUtil.success(userService.register(vo));
    }


    @GetMapping("/unLogin")
    @ApiOperation(value = "引导客户端去登录")
    public ResponseVO unLogin() {
        return ResultUtil.error(BaseResponseCode.TOKEN_ERROR);
    }

    @GetMapping("/logout")
    @ApiOperation(value = "退出接口")
    @BussinessLog(value = "用户管理", action = "退出")
    public ModelAndView logout(RedirectAttributes redirectAttributes) {
        // http://www.oschina.net/question/99751_91561
        // 此处有坑： 退出登录，其实不用实现任何东西，只需要保留这个接口即可，也不可能通过下方的代码进行退出
        // SecurityUtils.getSubject().logout();
        // 因为退出操作是由Shiro控制的
        redirectAttributes.addFlashAttribute("message", "您已安全退出");
        return ResultUtil.redirect("index");
    }

    @PutMapping("/pwd")
    @ApiOperation(value = "修改密码接口")
    @BussinessLog(value = "用户管理", action = "更新密码")
    public ResponseVO updatePwd(@RequestBody UpdatePasswordReqVO vo, HttpServletRequest request) {
        Long userId = SessionUtil.getUser().getId();
        userService.updatePwd(vo, userId);
        SessionUtil.removeAllSession();
        return ResultUtil.success("更新密码");
    }

    /**
     * 获取验证码
     *
     * @param response
     */
    @GetMapping("/code")
    public void getGifCode(HttpServletResponse response, HttpServletRequest request) throws IOException {
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/gif");
            /**
             * gif格式动画验证码
             * 宽，高，位数。
             */
            Captcha captcha = new GifCaptcha(146, 33, 4);
            //输出
            captcha.out(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取验证码异常：{}", e.getMessage());
        }
    }


}
