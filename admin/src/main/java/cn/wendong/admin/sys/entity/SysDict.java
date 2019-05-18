package cn.wendong.admin.sys.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
 * 系统字典
 * @author MB
 * @date 2018-12-02
 */
@Entity
@Table(name="sys_dict")
@Getter
@Setter
public class SysDict extends BaseEntity<SysDict> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6764614635240537684L;
	private String name;//名称
    private String code;//编码
    private String type;//类型
    private String remark;//备注
    private Integer sort;//排序
    
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
    
    private Byte status = StatusEnum.NORMAL.getCode();//状态
}
