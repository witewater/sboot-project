$(function() {
	var model = {
		path : adminContextPath + "/menu",
		resetDataForm : function() {
			$("#data-form-ml,#data-form-cd,#data-form-an").bootstrapValidator('resetForm', true);
			$(".icon").attr("class","icon");
			way.set("model.form.data",{
				target:"main",
				isShow:"1",
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
		setFormDataById:function(id,callback){
			$.get(this.path + "/get.do",{id:id},function(respone){
				way.set("model.form.data",respone.data);
				model.setParent(respone.data.parentId);
				if (callback){
					callback(respone.data);
				}
			})
		},
		setParent:function(parentId){
			if (parentId && parentId !='0') {
				//查询上级菜单名称
				$.get(this.path + "/get.do",{id:parentId},function(respone1){
					way.set("model.form.data.parentId",respone1.data.id);
					way.set("model.form.data.parentName",respone1.data.name);
				});
			}
		}
	};
	
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
	    url: '/menu/getpage.do',         //请求后台的URL（*）
        method: 'get',                      //请求方式（*）
        pagination:false,                    //不分页
        clickToSelect: false,                //是否启用点击选中行
        idField: 'id',
        responseHandler : function(res) {
			return {
				rows : res.data
			};
	    },
	    columns : [ {
		    field : 'check',
			checkbox : true,
			formatter: function (value, row, index) {
                if (row.check == true) {
                    //设置选中
                    return {  checked: true };
                }
            }
	    }, {
			field : 'name',
			title : '标题',
	    }, {
			field : 'icon',
			title : '图标',
			formatter : function(value, row, index) {
				if (value) {
					value = "<i class='"+ value +" fa-lg'></i>"
				}
				return value;
			}
	    }, {
			field : 'type',
			title : '类型',
			formatter : function(value, row, index) {
				if (value == '1') {//目录
					value = "<span class='label label-primary'>目录</span>";
				} else if (value == '2'){//菜单
					value = "<span class='label label-success'>菜单</span>";
				} else if (value == '3') {//按钮
					value = "<span class='label label-info'>按钮</span>"
				} 
				return value;
			}
	    }, {
			field : 'sort',
			title : '排序',
			editable:{
				mode:'inline',
				showbuttons:false,
				onblur:'submit',
				validate: function(value) {
					value = $.trim(value)
				    if(value == '') {
				        return '不能为空';
				    }
				    if(!/^\d+$/.test( value )){
			    			return '输入整数数字';
				    }
				    if(value.length >10){
		    				return '不能超过10位';
				    }
				}
			}
		 }, /*{
			field : 'target',
			title : '目标',
			formatter : function(value, row, index) {
				if (value == 'main'){//是
					value = "<span class='label label-success'>主窗口</span>";
				} else if (value == '_blank') {//
					value = "<span class='label label-danger'>弹出页</span>"
				} 
				return value;
			}
		},*/ {
			field : 'permission',
			title : '授权标识',
			editable:{
				mode:'inline',
				showbuttons:false,
				emptytext:'-',
				onblur:'submit',
				validate: function(value) {
					value = $.trim(value)
				    if(value.length >200){
		    				return '不能超过200位';
				    }
				}
			}
		}, {
			field : 'isShow',
			title : '是否显示',
			formatter : function(value, row, index) {
				if (value == '1'){//是
					value = "<span class='label label-success'>显示</span>";
				} else if (value == '0') {//否
					value = "<span class='label label-danger'>隐藏</span>"
				} 
				return value;
			}
			
		}, {
			field : 'operate',
			title : '操作',
			formatter : function(value, row, index) {
				var oper =  "<a  href='#' class='text-success  addMenu sf-permission-ctl' data-sf-permission='sys:menu:save' data-id='" + row.id+ "' >添加下级</a> | " +
						"<a  href='#' class='text-success  editMenu sf-permission-ctl' data-sf-permission='sys:menu:update' data-id='" + row.id+ "' >修改</a> | " +
								"<a  href='#' class='text-danger  deleteMenu sf-permission-ctl' data-sf-permission='sys:menu:delete' data-id='" + row.id+ "' >删除</a>";
				return oper;
			}
		}],
		
	    // bootstrap-table-treegrid.js 插件配置 -- start
		parentIdField:'pid', //指定父id列
        // 定义哪一列作为展开项 
        treeShowField:'name', //在哪一列展开树形
		onResetView:function(){//渲染完后执行
    	   $table.treegrid({
			    treeColumn:1,//指明第几列数据改为树形 ；树中表格中的哪一列
				initialState:'collapsed',//初始化时树的状态'expanded' - 所有节点都展开 'collapsed' - 所有节点都折叠
			    //saveState:true,//如果是true树的再次加载页面的时候树的状态将保存
			    expanderExpandedClass: 'glyphicon glyphicon-minus',//图标样式
		        expanderCollapsedClass: 'glyphicon glyphicon-plus',
		        onChange:function(){
		        	$table.bootstrapTable('resetWidth');
		        }
			});
    	   
    	   $table.treegrid('render');
    	   
    	   //只展开树形的第一级节点
           //$table.treegrid('getRootNodes').treegrid('expand');

		},
	    onCheck:function(row){
           var datas = $table.bootstrapTable('getData');
           // 勾选子类
           selectChilds(datas,row,"id","pid",true);
           // 勾选父类
           selectParentChecked(datas,row,"id","pid")
           // 刷新数据
           $table.bootstrapTable('load', datas);
        },
        onUncheck:function(row){
           var datas = $table.bootstrapTable('getData');
           // 取消 勾选子类
           selectChilds(datas,row,"id","pid",false);
           // 刷新数据
           $table.bootstrapTable('load', datas);
        },
		
	});
	 
	// 为表格绑定事件
	Table.bindevent($table);  
	//搜索
	$("#search").click(function(){
	   model.tableRefresh();
	   return false;
	});
	
	// 校验
	$("#data-form-ml,#data-form-cd,#data-form-an").bootstrapValidator().on("success.form.bv", function(e) {// 提交
		e.preventDefault();
		var id = model.getFormData().id;
		var optUrl = model.path + "/save.do";
		if (id) {
			optUrl = model.path + "/update.do";
		}
		$.post(optUrl, $(this).serialize(), function(respone) {
			if (respone.responeCode == '0') {
				layer.msg("保存成功");
				model.tableRefresh();
				$("#form-panel").modal('toggle');
			}
		});
	});
		
	//批量保存
	$("#batch-save").click(function(){
		var chanegeData = [];
		 $("#table tbody tr").each(function(i,v){
			var unsavedData = $(v).find(".editable-unsaved");
			 var rowData = {};
			 $.each(unsavedData,function(k,j){
				 rowData.id = $(j).data("pk");
				 rowData[$(j).data("name")] = $(j).data("value");
				 chanegeData.push(rowData);
			 });
		 });
		 if (chanegeData.length == 0) {
			 layer.msg("无数据改动");
			 return false;
		 }
		 $.ajax({
				url:model.path+"/batchSave.do",
				contentType : "application/json;charset=UTF-8",
				data:JSON.stringify(chanegeData),
				success:function(respone){
					if (respone.responeCode == "0") {
						layer.msg("保存成功");
						model.tableRefresh();
					} 
				}
			});
	});
	//获取图标
	$(".get-icon").click(function(){
		layer.open({
			  title:"图标",
			  type: 2, //iframe
			  maxmin:true,
			  area: ['95%', '95%'],
			  content: '/pages/common/icon.html'  //这里content是一个URL，如果你不想让iframe出现滚动条，你还可以content: ['http://sentsin.com', 'no']
			}); 
	});
	//点击父菜单选择框
	$(".parent-input").click(function(){
		$.seezoon.chooseMenu(way.get("model.form.data.parentId"),function(treeNode){
			//自己选择自己时候提示
			var currentId = way.get("model.form.data.id");
			if (treeNode.id == currentId) {
				layer.msg("不能选择当前菜单");
				return false;
			}
			if ( currentId && -1!=treeNode.parentIds.indexOf(currentId)) {//不能选择自己的子节点
				layer.msg("不能选择子节点");
				return false;
			}
			way.set("model.form.data.parentId",treeNode.id);
			way.set("model.form.data.parentName",treeNode.name);
			return true;
		},function(){
			way.set("model.form.data.parentId",null);
			way.set("model.form.data.parentName",null);
		});
	});
	// 添加
	$("#add").click(function() {
		model.resetDataForm();
		model.setFormTitle("<i class='fa fa-plus'>添加</i>");
		$('#myTabs a[href="#tab_ml"]').tab('show');
		//编辑后的再点添加需要移除之前的状态
		//$("#myTabs a").attr("data-toggle","tab").parent().removeClass("disabled").eq(0).tab("show");
		$("#form-panel").modal('toggle');
	});
	//添加下级菜单
	$("body").on("click", ".addMenu", function(){
		model.resetDataForm();
		model.setParent($(this).data("id"));
		model.setFormTitle("<i class='fa fa-plus'>添加</i>");
		$('#myTabs a[href="#tab_cd"]').tab('show');
		$("#form-panel").modal('toggle');
	});
	//修改菜单
	$("body").on("click", ".editMenu", function(){
		$('#table').bootstrapTable('checkBy', {field:"id", values:[$(this).data("id")]});
		$("#edit").click();
	});
	//删除菜单
	$("body").on("click", ".deleteMenu", function(){
		$('#table').bootstrapTable('checkBy', {field:"id", values:[$(this).data("id")]});
		$("#delete").click();
	});
	
	// 编辑
	$("#edit").click(function() {
		var rows = $('#table').bootstrapTable("getSelections");
		if (rows.length == 0) {
			layer.msg("请选择一行");
		} else {
			model.resetDataForm();
			model.setFormTitle("<i class='fa fa-edit'>编辑</i>");
			model.setFormDataById(rows[0].id,function(data){
				//选中tab
				var type = data.type;
				var icon = data.icon;
				$(".icon").addClass(icon);
				//禁用其他
				//$("#myTabs a").removeAttr("data-toggle").parent().addClass("disabled");
				//$('#myTabs a[href="#tab_ml"]').attr("data-toggle","tab").tab('show').parent().removeClass("disabled");
				if (type == '0') {//目录
					$('#myTabs a[href="#tab_ml"]').tab('show');
				} else if (type == '1') {//菜单
					$('#myTabs a[href="#tab_cd"]').tab('show');
				} else if (type == '2') {//按钮
					$('#myTabs a[href="#tab_an"]').tab('show');
				}
			});
			$("#form-panel").modal('toggle');
		}
	});
	// 删除
	$("#delete").click(function() {
		var rows = $('#table').bootstrapTable("getSelections");
		if (rows.length == 0) {
			layer.msg("请选择一行");
		} else {
			layer.confirm('将一起删除下级菜单,确定删除？', {
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

/**
 * 会出现 Maximum call stack size exceeded 错误 无线循环
 * 选中父项时，同时选中子项
 * @param datas 所有的数据
 * @param row 当前数据
 * @param id id 字段名
 * @param pid 父id字段名
 */
function selectChilds(datas,row,id,pid,checked) {
    for(var i in datas){
        if(datas[i][pid] == row[id]){
            datas[i].check=checked;
            selectChilds(datas,datas[i],id,pid,checked);
        };
    }
}
function selectParentChecked(datas,row,id,pid){
    for(var i in datas){
        if(datas[i][id] == row[pid]){
            datas[i].check=true;
            selectParentChecked(datas,datas[i],id,pid);
        };
    }
}