$(function() {
	var model = {
		path : adminContextPath + "/log",
		resetDataForm : function() {
			$("#data-form").bootstrapValidator('resetForm', true);
			way.set("model.form.data",{
				status:'1',
				sort:10,
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
		setFormDataById:function(id){
			$.get(this.path + "/get.do",{id:id},function(respone){
				way.set("model.form.data",respone.data);
			})
		},
		setViewDataById:function(id){
			$.get(this.path + "/get.do",{id:id},function(respone){
				way.set("model.view",respone.data);
			})
		},
		init:function(){
			//类型模板
//			$.get(this.path + "/getTypes.do",function(respone){
//				var data = [];
//				$.each(respone.data,function(i,v){
//					data.push({
//						code:v,
//						name:v
//					});
//				});
//				$("#sf-select-template").tmpl(data).appendTo($("#search-type"));
//				$('#search-type').selectpicker('refresh');
//			})
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
	// 列表
	$table.bootstrapTable({
	   url: '/log/getpage.do',         //请求后台的URL（*）
       method: 'get',                      //请求方式（*）
       columns: [{
           checkbox: true
       }, {
			field : 'name',
			title : '日志名称',
		}, {
			field : 'opusername',
			title : '操作人',
		}, {
			field : 'type',
			title : '日志类型',
			formatter : function(value, row, index) {
				return $.getDictName('log_type',value);
			}
		}, {
			field : 'message',
			title : '日志消息',
			formatter : function(value, row, index) {
				 
				return value;
			}
		}, {
			field : 'createDate',
			title : '记录时间',
			sortName : 'type',
			sortable : true,
			order : 'desc'
		}]
	});
	// 校验
	$("#data-form").bootstrapValidator().on("success.form.bv", function(e) {// 提交
		e.preventDefault();
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
	// 查询
	$("#search").click(function() {
		model.tableRefresh();
	});
	// 添加
	$("#add").click(function() {
		model.resetDataForm();
		model.setFormTitle("<i class='fa fa-plus'>添加</i>");
		$("#form-panel").modal('toggle');
	});
	//添加同类字典
	$("body").on("click", ".addDict", function(){
		model.resetDataForm();
		way.set("model.form.data.type",$(this).data("type"));
		model.setFormTitle("<i class='fa fa-plus'>添加</i>");
		$("#form-panel").modal('toggle');
	});
	// 编辑
	$("#edit").click(function() {
		var rows = $('#table').bootstrapTable("getSelections");
		if (rows.length == 0) {
			layer.msg("请选择一行");
		} else {
			model.resetDataForm();
			model.setFormDataById(rows[0].id);
			model.setFormTitle("<i class='fa fa-edit'>编辑</i>");
			$("#form-panel").modal('toggle');
		}
	});
	// 删除
	$("#delete").click(function() {
		var rows = $('#table').bootstrapTable("getSelections");
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
					if (respone.responeCode == "0") {
						layer.msg("删除成功");
						model.tableRefresh();
					} 
				});
			});
		}
	});

});