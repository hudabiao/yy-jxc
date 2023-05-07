/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.security.controller;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.utils.ConvertUtils;
import io.renren.common.utils.IpUtils;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.modules.log.entity.SysLogLoginEntity;
import io.renren.modules.log.enums.LoginOperationEnum;
import io.renren.modules.log.enums.LoginStatusEnum;
import io.renren.modules.log.service.SysLogLoginService;
import io.renren.modules.security.dto.LoginDTO;
import io.renren.modules.security.password.PasswordUtils;
import io.renren.modules.security.service.CaptchaService;
import io.renren.modules.security.service.SysUserTokenService;
import io.renren.modules.security.user.SecurityUser;
import io.renren.modules.security.user.UserDetail;
import io.renren.modules.security.vo.UserInfoVO;
import io.renren.modules.sys.dto.SysMenuDTO;
import io.renren.modules.sys.dto.SysUserDTO;
import io.renren.modules.sys.enums.UserStatusEnum;
import io.renren.modules.sys.service.SysMenuService;
import io.renren.modules.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 登录
 *
 * @author Mark sunlightcs@gmail.com
 */
@Slf4j
@RestController
@Api(tags="登录管理")
public class LoginController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private SysLogLoginService sysLogLoginService;
    @Autowired
    private SysMenuService sysMenuService;

    @GetMapping("captcha")
    @ApiOperation(value = "验证码", produces="application/octet-stream")
    @ApiImplicitParam(paramType = "query", dataType="string", name = "uuid", required = true)
    public void captcha(HttpServletResponse response, String uuid)throws IOException {
        //uuid不能为空
        AssertUtils.isBlank(uuid, ErrorCode.IDENTIFIER_NOT_NULL);

        //生成验证码
        captchaService.create(response, uuid);
    }

    @PostMapping("login")
    @ApiOperation(value = "登录")
    public Result login(HttpServletRequest request, @RequestBody LoginDTO login) {
//		//效验数据
//		ValidatorUtils.validateEntity(login);
//
//		//验证码是否正确
//		boolean flag = captchaService.validate(login.getUuid(), login.getCaptcha());
//		if(!flag){
//			return new Result().error(ErrorCode.CAPTCHA_ERROR);
//		}
        //用户信息
        SysUserDTO user = sysUserService.getByUsername(login.getUsername());

        SysLogLoginEntity log = new SysLogLoginEntity();
        log.setOperation(LoginOperationEnum.LOGIN.value());
        log.setCreateDate(new Date());
        log.setIp(IpUtils.getIpAddr(request));
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        log.setIp(IpUtils.getIpAddr(request));

        //用户不存在
        if(user == null){
            log.setStatus(LoginStatusEnum.FAIL.value());
            log.setCreatorName(login.getUsername());
            sysLogLoginService.save(log);

            throw new RenException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }
        //密码错误
        if(!PasswordUtils.matches(login.getPassword(), user.getPassword())){
            log.setStatus(LoginStatusEnum.FAIL.value());
            log.setCreator(user.getId());
            log.setCreatorName(user.getUsername());
            sysLogLoginService.save(log);

            throw new RenException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }

        //账号停用
        if(user.getStatus() == UserStatusEnum.DISABLE.value()){
            log.setStatus(LoginStatusEnum.LOCK.value());
            log.setCreator(user.getId());
            log.setCreatorName(user.getUsername());
            sysLogLoginService.save(log);

            throw new RenException(ErrorCode.ACCOUNT_DISABLE);
        }

        //登录成功
        log.setStatus(LoginStatusEnum.SUCCESS.value());
        log.setCreator(user.getId());
        log.setCreatorName(user.getUsername());
        sysLogLoginService.save(log);
        final Map<String, Object> token = sysUserTokenService.createToken(user.getId());
        return Result.ok(token);
    }

    @PostMapping("appLogin")
    @ApiOperation(value = "APP登录")
    public Result appLogin(HttpServletRequest request, @RequestBody LoginDTO login) {

        //用户信息
        SysUserDTO user = sysUserService.getByUsername(login.getUsername());
        SysLogLoginEntity log = new SysLogLoginEntity();
        log.setOperation(LoginOperationEnum.LOGIN.value());
        log.setCreateDate(new Date());
        log.setIp(IpUtils.getIpAddr(request));
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        log.setIp(IpUtils.getIpAddr(request));
        //用户不存在
        if(user == null){
            log.setStatus(LoginStatusEnum.FAIL.value());
            log.setCreatorName(login.getUsername());
            sysLogLoginService.save(log);
            throw new RenException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }
        //密码错误
        if(!PasswordUtils.matches(login.getPassword(), user.getPassword())){
            log.setStatus(LoginStatusEnum.FAIL.value());
            log.setCreator(user.getId());
            log.setCreatorName(user.getUsername());
            sysLogLoginService.save(log);

            throw new RenException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }
        //账号停用
        if(user.getStatus() == UserStatusEnum.DISABLE.value()){
            log.setStatus(LoginStatusEnum.LOCK.value());
            log.setCreator(user.getId());
            log.setCreatorName(user.getUsername());
            sysLogLoginService.save(log);

            throw new RenException(ErrorCode.ACCOUNT_DISABLE);
        }
        //登录成功
        log.setStatus(LoginStatusEnum.SUCCESS.value());
        log.setCreator(user.getId());
        log.setCreatorName(user.getUsername());
        sysLogLoginService.save(log);
        // 菜单信息
        final UserDetail user1 = ConvertUtils.sourceToTarget(user, UserDetail.class);
        List<SysMenuDTO> menuList = sysMenuService.getUserMenuList(user1, null);
        // token
        final Map<String, Object> token = sysUserTokenService.createToken(user.getId());
        final UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUserInfo(user);
        userInfoVO.setMenuList(menuList);
        userInfoVO.setToken(token);
        return Result.ok(userInfoVO);
    }

    @PostMapping("logout")
    @ApiOperation(value = "退出")
    public Result logout(HttpServletRequest request) {
        UserDetail user = SecurityUser.getUser();

        //退出
        sysUserTokenService.logout(user.getId());

        //用户信息
        SysLogLoginEntity log = new SysLogLoginEntity();
        log.setOperation(LoginOperationEnum.LOGOUT.value());
        log.setIp(IpUtils.getIpAddr(request));
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        log.setIp(IpUtils.getIpAddr(request));
        log.setStatus(LoginStatusEnum.SUCCESS.value());
        log.setCreator(user.getId());
        log.setCreatorName(user.getUsername());
        log.setCreateDate(new Date());
        sysLogLoginService.save(log);

        return new Result();
    }

    @GetMapping("/weixinLogin/{code}")
    @ApiOperation(value = "APP-微信登录")
    public Result weixinLogin(@PathVariable String code) throws JsonProcessingException {
        String unionid = getWeixinUnionid(code);
        SysUserDTO user = sysUserService.getByUnionId(unionid);
        // 菜单信息
        final UserDetail user1 = ConvertUtils.sourceToTarget(user, UserDetail.class);
        List<SysMenuDTO> menuList = sysMenuService.getUserMenuList(user1, null);
        // token
        final Map<String, Object> token = sysUserTokenService.createToken(user.getId());
        final UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUserInfo(user);
        userInfoVO.setMenuList(menuList);
        userInfoVO.setToken(token);
        return Result.ok(userInfoVO);
    }


    private String getWeixinUnionid(String code) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
//        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wxa27abc31e982349e&secret=4e287d3587a05ac887b7666c542abc13&js_code=%s&grant_type=authorization_code";
        url = String.format(url, code);
        String res = HttpUtil.get(url);
        log.info("token信息={}", res);
        JsonNode jsonNode = objectMapper.readTree(res);
        return jsonNode.path("openid").asText();
//        String accessToken = accessTokenNode.asText();
//        String userUrl = "https://api.weixin.qq.com/sns/jscode2session?access_token=%s&openid=wxa27abc31e982349e";
//        url = String.format(userUrl, accessToken);
//        String userRes = HttpUtil.get(url);
//        log.info("用户信息={}", userRes);
//        JsonNode userNode = objectMapper.readTree(userRes);
//        return openidNode.asText();
    }
}