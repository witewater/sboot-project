package cn.wendong.admin.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.wendong.admin.common.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户操作日志表
 * @author MB yangtdo@qq.com
 * @date 2019-02-05
 */
@Entity
@Table(name="sys_opt_log")
@Getter
@Setter
@NoArgsConstructor
public class SysOptLog extends BaseEntity<SysOptLog> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5769266915135512035L;
    private String name;//日志名称
    private Byte type;//日志类型
    private String clazz;//拦截类名
    private String method;//获取方法
    private String model;//表名
    private Long recordId;//数据id
    @Lob @Column(columnDefinition="TEXT")//columnDefinition可以进行额外指定大文本
    private String message;//日志消息
    @Column(name = "execute_time")
	private Long executeTime;//执行时间
    @Lob
	private String params;//请求参数
    private String exceptionDetail;//异常详细  是否需要
    private String os;//操作系统
	private String browser;//操作浏览器
	private Date optime;//操作时间
	private String opip;//操作IP
	private String opusername;//操作用户名
    
    @CreatedBy
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="create_by")
    @JsonIgnore
    private SysUser createBy;

    /**
     * 封装日志对象
     * @param name 日志名称
     * @param message 日志消息
     */
    public SysOptLog(String name, String message){
        this.name = name;
        this.message = message;
    }
}
