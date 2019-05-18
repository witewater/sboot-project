package cn.wendong.admin.sys.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
 * 组织部门 存在上下级 指公司或单位
 * @author MB yangtdo@qq.com
 * @date 2019-02-02
 */
@Entity
@Table(name="sys_office")
@Getter
@Setter
public class SysOffice extends TreeEntity<SysOffice>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3584669705528913499L;
	
    private String name;//名称
	private String officeCode;//部门编号
	private String type;//部门类型
	private String rank;//部门级别
	
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
	
 	@Transient
    @JsonIgnore
    private Map<Long,SysOffice> children = new HashMap<>();
}
