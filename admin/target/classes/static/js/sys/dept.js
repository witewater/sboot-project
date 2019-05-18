/**
 * 部门选择组件
 * $.chooseDept('需要默认选中的treeId','点确定后的回调方法','点清清除的回调方法');
 * @param $
 * @returns
 */
;(function($) {
	$("body").append("<div id='deptLayer' class='undisplay pd10'><ul id='deptTree' class='ztree'></ul></div>");
	$.seezoon = {
	    index:0,
		chooseDept:function(treeId,confirmCallback,clearCallback){
			var setting = {
					data : {
						simpleData : {
							enable : true,
							idKey : "id",
							pIdKey : "parentId",
						}
					},
					callback : {
						onDblClick : function(event, treeId, treeNode) {
							if (confirmCallback(treeNode)){
								layer.close($.seezoon.index);
							}
						}
					}
				};
				$.post(adminContextPath + "/sys/dept/qryAll.do", function(respone) {
					//选择上级tree
					$.fn.zTree.init($("#deptTree"), setting,respone.data);
					var treeObj = $.fn.zTree.getZTreeObj("deptTree");
					treeObj.expandAll(true);
					if (treeId) {//默认反选
						treeObj.selectNode(treeObj.getNodeByParam("id",treeId));
					}
				});
				this.index = layer.open({
				 	  title:'上级部门',
				 	  offset: '50px',
		              skin: 'layui-layer-molv',
		              shadeClose:true,
				 	  icon: 1,
				 	  area: ['300px', '450px'],
					  type: 1, 
					  btn: ['确认','清除'],
					  yes:function(){
						  var treeObj = $.fn.zTree.getZTreeObj("deptTree");
						  var nodes = treeObj.getSelectedNodes();
						  if (nodes.length == 0) {
							  layer.msg("请选择部门");
							  return false;
						  }
						  if (confirmCallback(nodes[0])){
								layer.close($.seezoon.index);
						   }
					  },
					  btn2:function(){
						  clearCallback();
						  layer.close($.seezoon.index);
					  },
					  content:  $("#deptLayer") 
					});
		}
	}
})(jQuery);