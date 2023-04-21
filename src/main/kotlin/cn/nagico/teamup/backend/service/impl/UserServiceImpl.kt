package cn.nagico.teamup.backend.service.impl

import cn.nagico.teamup.backend.mapper.UserMapper
import cn.nagico.teamup.backend.model.User
import cn.nagico.teamup.backend.service.UserService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : ServiceImpl<UserMapper, User>(), UserService {

}
