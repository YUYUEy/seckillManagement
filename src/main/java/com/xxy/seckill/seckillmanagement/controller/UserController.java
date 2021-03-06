package com.xxy.seckill.seckillmanagement.controller;

import com.xxy.seckill.seckillmanagement.controller.viewobject.UserVO;
import com.xxy.seckill.seckillmanagement.error.BusinessException;
import com.xxy.seckill.seckillmanagement.error.EmBusinessError;
import com.xxy.seckill.seckillmanagement.response.CommonRetrunType;
import com.xxy.seckill.seckillmanagement.service.DictService;
import com.xxy.seckill.seckillmanagement.service.UserService;
import com.xxy.seckill.seckillmanagement.service.model.UserModel;
import com.xxy.seckill.seckillmanagement.util.validatecode.IVerifyCodeGen;
import com.xxy.seckill.seckillmanagement.util.validatecode.SimpleCharVerifyCodeGenImpl;
import com.xxy.seckill.seckillmanagement.util.validatecode.VerifyCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;

/**
 * @ClassName: UserController
 * @Description: 用户控制类
 * @Author: 13688
 * @Date: 2019/4/28 22:06
 * @Version: v1.0
 **/
@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", origins = {"*"})
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private DictService dictService;

    @RequestMapping(value = "/verifyCode")
    public void verifyCode(HttpServletResponse response) throws BusinessException {
        IVerifyCodeGen iVerifyCodeGen = new SimpleCharVerifyCodeGenImpl();
        try {
            //设置长宽
            VerifyCode verifyCode = iVerifyCodeGen.generate(80, 28);
            String code = verifyCode.getCode();
            //LOGGER.info(code);
            //将VerifyCode绑定session
            this.httpServletRequest.getSession().setAttribute("VERIFY_CODE", code);
            //设置响应头
            response.setHeader("Pragma", "no-cache");
            //设置响应头
            response.setHeader("Cache-Control", "no-cache");
            //在代理服务器端防止缓冲
            response.setDateHeader("Expires", 0);
            //设置响应内容类型
            response.setContentType("image/jpeg");
            response.getOutputStream().write(verifyCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            throw new BusinessException(EmBusinessError.VALIDATION_ERROR);
        }
    }

    /**
     * 用户登录接口
     *
     * @param telphone
     * @param password
     * @return
     */
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonRetrunType login(@RequestParam(name = "telphone") String telphone,
                                  @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if (StringUtils.isEmpty(telphone) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //验证码校验
        /*if (!verifyCode.equals((String) this.httpServletRequest.getSession().getAttribute("VERIFY_CODE"))) {
            throw new BusinessException(EmBusinessError.VALIDATION_ERROR);
        }*/
        //用户登录服务，校验用户端登录是否合法
        UserModel userModel = userService.validateLogin(telphone, this.encodeByMd5(password));

        //将登录凭证加入到用户登录成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);

        return CommonRetrunType.create(userModel);
    }

    /**
     * 用户注册接口
     *
     * @param telphone
     * @param otpCode
     * @param name
     * @param gender
     * @param age
     * @param password
     * @return
     * @throws BusinessException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonRetrunType register(@RequestParam(name = "telphone") String telphone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "gender") Integer gender,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号是否和otpCode相符合
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
        if (!com.alibaba.druid.util.StringUtils.equals(otpCode, inSessionOtpCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "您输入的验证码不正确");
        }
        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.encodeByMd5(password));
        userService.register(userModel);
        return CommonRetrunType.create(null);
    }

    public String encodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64En = new BASE64Encoder();
        //加密字符串
        String newStr = base64En.encode(md5.digest(str.getBytes("utf-8")));
        return newStr;
    }

    /**
     * 用户获取otp短信接口
     *
     * @param telphone
     * @return
     */
    @RequestMapping(value = "/getotp", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonRetrunType getOtp(@RequestParam(name = "telphone") String telphone) {
        //需要按照一定的规则生成otp验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        //将otp验证码同对应用户的手机号关联，使用httpSession的方式绑定用户的手机号与otpCode
        httpServletRequest.getSession().setAttribute(telphone, otpCode);

        //将otp验证码通过短信通道发送给用户，省略
        System.out.println("telphone = " + telphone + " & otpCode = " + otpCode);

        return CommonRetrunType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonRetrunType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        //调用service服务器获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);

        //若获取的对应用户信息不存在
        if (null == userModel) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        //将核心领域模型用户对象转化为可供UI使用的viewObject
        UserVO userVO = convertFormModel(userModel);

        //返回通用对象
        return CommonRetrunType.create(userVO);
    }

    /**
     * 页面加载的用户登录信息
     *
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/getuserinfo")
    @ResponseBody
    public CommonRetrunType getUserInfo() throws BusinessException {
        //获取session 中的登陆信息
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if (null == isLogin || !isLogin.booleanValue()) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN, "用户未登录，请先登陆");
        }
        //获取用户登录信息
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");
        //调用getUser方法返回用户信息
        return getUser(userModel.getId());
    }

    private UserVO convertFormModel(UserModel userModel) {
        Map<String, String> genderMap = dictService.getCodeDict("GENDER");
        if (null == userModel) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        userVO.setGender(genderMap.get(String.valueOf(userModel.getGender())));
        return userVO;
    }

    @RequestMapping("/userInfo")
    public String userInfo(Model model, HttpServletResponse response) {
        model.addAttribute("name", "子慕鱼");
        return "userinfo";
    }
}
