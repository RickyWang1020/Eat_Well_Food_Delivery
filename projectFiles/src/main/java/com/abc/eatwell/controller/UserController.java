package com.abc.eatwell.controller;

import com.abc.eatwell.common.R;
import com.abc.eatwell.entity.User;
import com.abc.eatwell.service.UserService;
import com.abc.eatwell.utils.SMSUtils;
import com.abc.eatwell.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * send message verification code
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        // get phone number
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            // generate 4 digit code
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code = {}", code);

            // send message
//            SMSUtils.sendMessage("Eat Well", "", phone, code);

            // save the verification code to Session
//            session.setAttribute(phone, code);

            // save the verification code to Redis, and set the expiration time as 2 minutes
            redisTemplate.opsForValue().set(phone, code, 2, TimeUnit.MINUTES);


            return R.success("send message success");
        }
        return R.error("failed to send message");
    }

    /**
     * user login
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {

        // get phone number
        String phone = map.get("phone").toString();

        // get verification code
        String code = map.get("code").toString();

        // get saved verification code from session
//        Object codeInSession = session.getAttribute(phone);

        // get saved verification code from Redis
        Object codeInSession = redisTemplate.opsForValue().get(phone);

        // compare the codes
        if (codeInSession != null && codeInSession.equals(code)) {
            // if they are the same, login success
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            // check whether the phone number is a new user, if so, then auto register
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());

            // if login success, then delete verification code from Redis
            redisTemplate.delete(phone);

            return R.success(user);
        }
        return R.error("failed to login");
    }
}
