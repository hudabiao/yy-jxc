package io.renren.modules.security.vo;

import io.renren.modules.sys.dto.SysMenuDTO;
import io.renren.modules.sys.dto.SysUserDTO;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Data
public class UserInfoVO {
    private SysUserDTO userInfo;
    private List<SysMenuDTO> menuList;
    private Map<String, Object> token;
}
