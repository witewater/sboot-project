$(function(){
	//用户模型控制
	var model = {
		path : adminContextPath + '/user',
		resetDataForm : function() {
			$("#data-form").bootstrapValidator('resetForm', true);
			way.set("model.form.data",{
				status:'1',
			});
		},
		getFormData : function() {
			var data =  way.get("model.form.data");
			return data?data:{};
		},
		setFormTitle : function(title) {
			way.set("model.form.title", title);
		},
		tableRefresh : function() {
			$('#table').bootstrapTable("refresh");
		},
		setFormDataById:function(id,callback){
			$.get(this.path + "/get.do",{id:id},function(respone){
				way.set("model.form.data",respone.data);
				if (callback) {
					callback(respone.data);
				}
			})
		},
		init:function(){
			//do something
		}
	};
	model.init();
	Table.init({
		 extend: {
           index_url: ' ',
           add_url: ' ',
           edit_url: ' ',
           del_url: ' ',
           multi_url: ' ',
         }
	});
	var $table = $("#table");
	$table.bootstrapTable({
		url: '/user/getpage.do',         //请求后台的URL（*）
        method: 'get',                  //请求方式（*）
        sortable:true,
        onPostBody:function(){//渲染完后执行
        	//主要判断权限
			//$.bntPermissionHandler();
		},
        columns: [{
            checkbox: true
        }, {
            field: 'username',
            title: '用户名'
        }, /*{
			field : 'pictureFullUrl',
			title : '头像',
			cellStyle:function cellStyle(value, row, index, field) {
				  return {
					    css: {"width": "50px", "padding": "3px","text-align":"center","cursor":'pointer'}
					  };
					},
			formatter : function(value, row, index) {
				if (value) {
					return "<image src='" + value  + "' class='viewImages' style='width:40px;height:40px;' data-url='" + value + "'/>";
				} else {
					return value;
				}
			}
		},*/ {
            field: 'nickname',
            title: '用户昵称'
        }, {
            field: 'phone',
            title: '电话'
        }, {
            field: 'email',
            title: '邮箱'
        }, {
            field: 'sex',
            title: '性别',
            formatter : function(value, row, index) {
				return $.getDictName('sex',value);
			}
        }, {
            field: 'status',
            title: '状态',
            formatter : function(value, row, index) {
				//return $.getDictName('data_status',value);
            	if (value == '1'){//正常
					value = "<span class='label label-success'>正常</span>";
				} else if (value == '0') {//禁用
					value = "<span class='label label-danger'>禁用</span>"
				} 
				return value;
			}
        }, {
            field : 'updateDate',
			title : '更新时间',
			formatter: Table.formatter.datetime,
			sortName : 'update_date',
			sortable : true,
			order : 'desc'
        }, {
             field: 'operate',
             title: '操作',
             table : $table,
             events: Table.events.operate, 
             formatter: Table.formatter.operate,
//             formatter:  function(value, row, index) {
//           	 return [
//           		 '<a href="javascript:;" class="btn btn-xs btn-primary btn-dragsort" data-toggle="tooltip" title="拖动进行排序" data-table-id="table" data-field-index="9" data-row-index="0" data-button-index="0"><i class="fa fa-arrows"></i></a>&nbsp;',
//	            	 '<a href="#" class="btn btn-xs btn-success btn-editone" data-toggle="tooltip" title="" data-table-id="table" data-field-index="9" data-row-index="0" data-button-index="1" data-original-title="编辑"><i class="fa fa-pencil"></i></a>&nbsp;',
//	            	 '<a href="javascript:;" class="btn btn-xs btn-danger btn-delone" data-toggle="tooltip" title="删除" data-table-id="table" data-field-index="9" data-row-index="0" data-button-index="2"><i class="fa fa-trash"></i></a>',
//           	 ].join('');
//            } //自定义方法，添加操作按钮
          },
        ]
	});
	 
   // 为表格绑定事件
   Table.bindevent($table);  
   //搜索
   $("#search").click(function(){
	   model.tableRefresh();
	   return false;
   });
   //选择部门
	$("#office-input").click(function(){
		//readOnly 不会触发bootstrap valiator 重新校验，所以选择部门后，手动修改校验状态
		$.seezoon.chooseDept(way.get("model.form.data.deptId"),function(treeNode){
			way.set("model.form.data.deptId",treeNode.id);
			way.set("model.form.data.deptName",treeNode.name);
			$("#data-form").data('bootstrapValidator').updateStatus('deptName', 'VALID');
			return true;
		},function(){
			way.set("model.form.data.deptId",null);
			way.set("model.form.data.deptName",null);
			$("#data-form").data('bootstrapValidator').updateStatus('deptName', 'INVALID');
		});
	});
	// 校验
	$("#data-form").bootstrapValidator({
		fields:{
			password:{
				validators:{
					identical:{
						message:'两次密码不一致',
						field:'conFirmPassword'
					},
					different:{
						message:'密码不能和用户名相同',
						field:'loginName'
					},
					callback: {
						message:'新用户必须填密码',
						callback:function(value, validator){//框架验证完回调
							//修改不做处理，新增value 为空即为false
							return model.getFormData().id?true:$.trim(value) != '';
						}
					}
				}
			},
			conFirmPassword:{
				validators:{
					identical:{
						message:'两次密码不一致',
						field:'password'
					},
					different:{
						message:'密码不能和用户名相同',
						field:'loginName'
					},
					callback: {
						message:'新用户必须填密码',
						callback:function(value, validator){//框架验证完回调
							//修改不做处理，新增value 为空即为false
							return model.getFormData().id?true:$.trim(value)!='';
						}
					}
				}
			},
			loginName:{
				threshold:5,
				validators:{
					remote: {
	                    url: model.path + "/checkLoginName.do",//验证登录用户名
	                    data:{
		                    id:function(){
		                    	return model.getFormData().id;
		                    },
		                    loginName:function(){
		                    	return model.getFormData().loginName;
		                    },
	                    },
	                    message: '用户名已存在',//提示消息
	                    type:'post',
	                    delay: 500,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
	                },
				}
			}
		}
	}).on("success.form.bv", function(e) {// 提交
		e.preventDefault();
		//请选择角色
//		if ($("input[name='roleIds']:checked").length == 0) {
//			layer.msg("请勾选角色");
//			$('#data-form').bootstrapValidator('disableSubmitButtons', false);  
//			return false;
//		}
		var id = model.getFormData().id;
		var optUrl = model.path + "/save.do";
		if (id) {
			optUrl = model.path + "/update.do";
		}
		$.post(optUrl, $("#data-form").serialize(), function(respone) {
			if (respone.responeCode == '0') {
				layer.msg("保存成功");
				model.tableRefresh();
				$("#form-panel").modal('toggle');
			}
		});
	});
   // 添加
   $("#add").click(function() {
	    var title = $(this).data("title");
		model.resetDataForm();
		model.setFormTitle("<i class='fa fa-plus'>添加</i>");
		$("#form-panel").modal('toggle');
   });
   // 编辑
   $("#edit").click(function() {
		var rows = $table.bootstrapTable("getSelections");
		if (rows.length == 0) {
			layer.msg("请选择一行");
		} else {
			var title = $(this).data("title");
			model.resetDataForm();
			model.setFormDataById(rows[0].id,function(data){
				$.checkBoxCheck($("input[name='roleIds']"),data.roleIds);
			});
			model.setFormTitle("<i class='fa fa-edit'>编辑</i>");
			$("#form-panel").modal('toggle');
		}
   });
   // 删除
   $("#delete").click(function() {
		var rows = $table.bootstrapTable("getSelections");
		if (rows.length == 0) {
			layer.msg("请选择一行");
		} else {
			layer.confirm('确定删除？', {
				shadeClose : true,
				icon : 3,
				anim : 6,
				btn : [ '确定', '取消' ]
			// 按钮
			}, function() {
				$.post(model.path  + "/delete.do", {
					id : rows[0].id
				}, function(respone) {
					if (respone.responeCode === 200) {
						layer.msg("删除成功");
						model.tableRefresh();
					} 
				});
			});
		}
	});
   
    //查看图像
	$("body").on("click", ".viewImages", function() {
		var url = $(this).data("url");
		layer.photos({
		    photos: {
		    	title: "图像", //相册标题
		    	data:[{src:url}]
		    },
		    anim: 5 //0-6的选择，指定弹出图片动画类型，默认随机（请注意，3.0之前的版本用shift参数）
		  });
	});
	
	//设置状态
	$("body").on("click", ".setStatus", function() {
		var id = $(this).data("id");
		var status = $(this).data("status");
		$.post(model.path + "/setStatus.do",{id:id,status:status},function(respone){
			model.tableRefresh();
		});
	});
   
	//图像上传
	$('#userImageUpload').fileupload({
		 url:adminContextPath + "/file/uploadImage",
		 type:'POST',
		 formData:null,
		 change: function (e, data) {
			 var file = data.files[0];
		    if (file.size > 2 * 1024 * 1024) {
		    		layer.msg(file.name + " 文件大小超过2M,请重新选择");
		    		return false;
		    }
		  // 开头为image/
			var reg = /^image\//
		    if (!reg.test(file.type)) {
			    	layer.msg(file.name + " 不是图片格式");
		    		return false;
		    }
		  },
		 done: function (e, response) {//设置文件上传完毕事件的回调函数 
			 if (response.result.responeCode == '0') {
				// layer.msg(response.result.data.originalFilename + " 上传成功");
				 way.set("model.form.data.photo",response.result.data.relativePath);
				 way.set("model.form.data.photoFullUrl",response.result.data.fullUrl);
			 } 
        }, 
	 });
	
});