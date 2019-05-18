$(function(){
	//用户模型控制
	var model = {
		path : adminContextPath + '/role',
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
	var table = $("#table");
	table.bootstrapTable({
		 url: '/role/getpage.do',         //请求后台的URL（*）
        method: 'get',                      //请求方式（*）
        columns: [{
            checkbox: true
        }, {
            field: 'name',
            title: '角色编号'
        }, {
            field: 'title',
            title: '角色名称'
        }, {
			field : 'dataScope',
			title : '数据范围',
			formatter : function(value, row, index) {
				if (value == '0') {//全部
					value = "<span class='label label-primary'>全部</span>";
				} else if (value == '1'){//本部门及以下
					value = "<span class='label label-success'>本部门及以下</span>";
				} else if (value == '2') {//本部门
					value = "<span class='label label-info'>本部门</span>"
				}  else if (value == '3') {//本人
					value = "<span class='label label-default'>本人</span>"
				}  
				return value;
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
             table: table, 
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
   Table.bindevent(table);  
   //搜索
   $("#search").click(function(){
	   table.bootstrapTable('refresh');
	   return false;
   });
	// 校验
   $("#data-form").bootstrapValidator().on("success.form.bv", function(e) {// 提交
		e.preventDefault();
		var id = model.getFormData().id;
		var optUrl = model.path + "/save.do";
		if (id) {
			optUrl = model.path + "/update.do";
		}
		var nodes = model.menuTree.getCheckedNodes(true);
		var menuIds = [];
		for(var i=0; i<nodes.length; i++) {
			menuIds.push(nodes[i].id);
       }
		$.post(optUrl, $("#data-form").serialize() + "&" + $.param({menuIds:menuIds},true), function(respone) {
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
		var rows = table.bootstrapTable("getSelections");
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
		var rows = table.bootstrapTable("getSelections");
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
   
});