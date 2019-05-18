package cn.wendong.admin.sys.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.wendong.admin.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 文件实体
 * 
 * @author MB
 * @date 2018-12-02
 */
@Entity
@Table(name = "sys_file")
@Getter
@Setter
public class SysFile extends BaseEntity<SysFile> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5147104704191477059L;
	// 文件名
	private String name;
	// 文件路径
	private String path;
	// 文件类型
	private String mime;
	// 文件大小
	private Long size;
	// 文件md5值
	private String md5;
	// 文件sha1值
	private String sha1;
	//宽度
	private String imageWidth;
	//高度
	private String imageHeight;
	//图片类型
	private String imageType;
	//图片帧数
	private int imageFrames;
	//存储位置
	private String storage; //local 
	//上传时间
	private Date uploadTime;
	// 创建者
	@CreatedBy
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "create_by")
	@JsonIgnore
	private SysUser createBy;
	//修改者
	@LastModifiedBy
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "update_by")
	@JsonIgnore
	private SysUser updateBy;
}
