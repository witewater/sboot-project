package cn.wendong.admin.sys.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.wendong.admin.common.base.TreeEntity;
import cn.wendong.admin.core.enums.StatusEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 菜单实体
 * @author MB
 * @date 2018-12-01
 */
@Entity
@Table(name="sys_menu")
@Getter
@Setter
public class SysMenu extends TreeEntity<SysMenu> {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -4738547703527898584L;
	
    private String name;//名称
    private String url;//链接
    private String icon;//图标
    private Byte type;//类型 1|2|3
    private String remark;//备注
	private String target;//目标
	private String permission;//权限标识
	private Byte isShow;//是否在菜单中显示1显示，0 不显示
    
    private Byte status = StatusEnum.NORMAL.getCode();
	
    @CreatedBy
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="create_by")
    @JsonIgnore
    private SysUser createBy;
    @LastModifiedBy
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="update_by")
    @JsonIgnore
    private SysUser updateBy;

    @ManyToMany(mappedBy = "menus")
    @JsonIgnore
    private Set<SysRole> roles = new HashSet<>(0);

    @Transient
    @JsonIgnore
    private Map<Long,SysMenu> children = new HashMap<>();
	
 
}