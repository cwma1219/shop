package com.example.rd.service.impl;

import com.example.rd.dto.Result;
import com.example.rd.dto.UserDto;
import com.example.rd.entity.User;
import com.example.rd.repository.UserRepository;
import com.example.rd.service.UserService;
import com.example.rd.util.RandomUtil;
import com.example.rd.util.RegexUtil;
import com.example.rd.vo.LoginVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.example.rd.util.RedisConstants.*;
import static com.example.rd.util.SystemConstants.USER_NICK_NAME_PREFIX;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;


    private Random random = new Random();

    @Override
    public Result sendCode(String phone) {

        if (!RegexUtil.isPhoneValid(phone)) {
            return Result.fail("手機格式錯誤或無輸入");
        }

        int sixDigitNumber = 100000 + random.nextInt(900000);
        String code = String.format("%06d", sixDigitNumber);

        //以login:code:0912345678 作為key
        //以code作為value存入 Redis
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);
        log.debug("已發送驗證碼至：" + phone);
        log.debug("驗證碼:{}", code);
        return Result.ok();
    }

    @Override
    @Transactional
    public Result login(LoginVo vo) throws IllegalAccessException {
        //驗證手機格式
        if (!RegexUtil.isPhoneValid(vo.getPhone())) {
            return Result.fail("手機格式錯誤或無輸入");
        }
        String code = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + vo.getPhone());
        if (StringUtils.isBlank(code) || !code.equals(vo.getCode())) {
            return Result.fail("登入失敗");
        }
        //取得用戶資訊，若無用戶則進行註冊
        User user = userRepository.findByPhone(vo.getPhone());
        if (user == null) {
            user = createUserWithPhone(vo.getPhone());
        }


        UserDto userDto = new UserDto(user.getId(), user.getNickName(), user.getIcon());

        //將用戶資訊存入Redis
        String token = UUID.randomUUID().toString();
        String tokenKey = LOGIN_USER_KEY + token;
        Field[] fields = UserDto.class.getDeclaredFields();
        Map<String, String> map = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(userDto);
            if (value == null) {
                continue;
            }
            map.put(fieldName, String.valueOf(value));
        }
        stringRedisTemplate.opsForHash().putAll(tokenKey, map);
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);


        return Result.ok(token);
    }

    @Override
    public Result getResultById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        return Result.ok(user);
    }

    private User createUserWithPhone(String phone) {
        User user = User.builder()
                .phone(phone)
                .nickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10))
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        userRepository.save(user);
        return user;
    }
}
