package cn.wendong.admin.sys.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.wendong.admin.common.base.BaseEntity;
import cn.wendong.admin.core.enums.StatusEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色实体
 * @author MB
 * @date 2018-12-01
 */
@Entity
@Table(name="sys_role")
@Getter
@Setter
public class SysRole extends BaseEntity<SysRole> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 582465388635362804L;
    private String name;//角色编号
    private String title;//角色名称
    private String remark;//备注
    /**
     * 数据范围,0:全部，1：本部门，2：本部门及以下，3.本人
     */
    @Column(name="data_scope",columnDefinition = "char(1) ")
    private String dataScope;//数据范围
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
    
    private Byte status = StatusEnum.NORMAL.getCode();

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<SysUser> users = new HashSet<>(0);

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_role_menu",
            joinColumns = @JoinColumn(name="role_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id"))
    @JsonIgnore
    private Set<SysMenu> menus = new HashSet<>(0);

}
