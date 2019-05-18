/**
*获取字典值 配合jqTmpl.html 模板使用 jquery-tmpl.html
*/
$(function(){
	/**
	 * 字典列表code
	 */
	$.extend({
		// 获取字典列表
		getDictList : function(type) {
			if (!type) {
				return null;
			}
			// 先从sessionStorage 取
			var typeDict = sessionStorage.getItem("model.dict." + type);
			if (typeDict) {
				return $.parseJSON(typeDict);
			} else {
				// 取消异步
				$.ajaxSetup({
					async : false
				});
				var dict = [];
				$.get(requestPath + "/public/dict/getDictsByType.do", {
					type : type
				}, function(respone) {
					if (respone.code === 200) {
						dict = respone.data;
						// 存入sessionStorage
						if (dict) {
							sessionStorage.setItem("model.dict." + type, JSON.stringify(dict));
						}
					}
				});
				$.ajaxSetup({
					async : true
				});
				return dict;
			}
		},// 字典code 转value
		getDictName : function(type, value) {
			if (!type || !value) {
				return null;
			}
			// 先从sessionStorage 取
			var typeMap = sessionStorage.getItem("model.dict.map." + type);
			if (typeMap) {
				var json = $.parseJSON(typeMap);
				return json[value];
			} else {
				var map = {};
				var dictList = $.getDictList(type);
				if (dictList) {
					$.each(dictList, function(i, v) {
						map[v.code] = v.name
					});
					if (map) {
						sessionStorage.setItem("model.dict.map." + type, JSON
								.stringify(map));
					}
					return map[value];
				}
			}
		},
		dictInputhandler:function(){
			/**
			 * eg:
			 * <div class="col-sm-5"><label class="radio-inline"><input type="radio"
			 * required way-data="status" name="status" value="1">有效 </label> <label
			 * class="radio-inline"> <input type="radio" required way-data="status"
			 * name="status" value="0">无效 </label> </div>
			 * 
			 * <div class="col-sm-5"><label class="checkbox-inline"><input
			 * type="checkbox" required way-data="status" name="status" value="1">有效
			 * </label> <label class="checkbox-inline"> <input type="checkbox" required
			 * way-data="status" name="status" value="0">无效 </label> </div>
			 * 
			 */
			// 字典渲染
			$(".sb-radio,.sb-checkbox").each(function(i, v) {
				var inputName = $(this).data("sb-input-name");
				var dictType = $(this).data("sb-dict-type");
				var required = $(this).data("sb-required");

				var dictList = $.getDictList(dictType);
				if (dictList) {
					$.each(dictList, function(j, k) {
						k.inputName = inputName;
						if (required) {
							k.required = required
						}
						// 禁用
						if (k.status == '0') {
							k.disabled = "disabled";
						}
					});
				}
				if ($(this).hasClass("sb-radio")) {
					$("#sb-radio-template").tmpl(dictList).appendTo(this);
				} else {
					$("#sb-checkbox-template").tmpl(dictList).appendTo(this);
				}
			});
			/**
			 * eg:
			 * <div class="form-group">
			 *	<label class="col-sm-3 control-label">用户类型</label>
			 *	<div class="col-sm-5">
			 *		<select class="form-control sf-select" way-data="userType"
			 *			name="userType" data-sf-dict-type="sys_user_type">
			 *			<option value="">请选择</option>
			 *		</select>
			 *	</div>
			 * </div>
			 */
			$(".sb-select").each(function(i, v) {
				var dictType = $(this).data("sb-dict-type");
				var dictList = $.getDictList(dictType);
				if (dictList) {
					$.each(dictList, function(j, k) {
						// 禁用
						if (k.status == '0') {
							k.disabled = "disabled";
						}
					});
				}
				$("#sb-select-template").tmpl(dictList).appendTo(this);
				//重新渲染
				$('.selectpicker').selectpicker('refresh');
			});
		},
		//日期控件
		inputDateHandler:function(){
			// 日期控件
			$(".date").attr("readonly", "readonly");
			$(".date").each(function(){
				//默认时间 0  是当天，-1 是前一天，1 后一天
				var defaultDay = $(this).data("default-day");
				var dateClear = $(this).data("date-clear");
				$(this).datepicker({
					format : 'yyyy-mm-dd',
					language : 'zh-CN',
					clearBtn : typeof(dateClear)!='undefined'? dateClear : true,
					autoclose : true,
					todayHighlight : true
				});
				if (typeof(defaultDay)!='undefined') {
					var date = new Date();
				    date.setDate(date.getDate() + defaultDay);
					$(this).datepicker("setDate",date);
				}
			});
		},
		// checkBox 反选
		/**
		 * 节点，选中值
		 */
		checkBoxCheck : function($input, array) {
			if (array) {
				$.each($input, function(i, v) {
					if (-1 != $.inArray($(v).val(), array)) {
						$(v).attr("checked", "checked");
					} else {
						$(v).removeAttr("checked");
					}
				})
			}
		},
	});
	$.dictInputhandler();
	$.inputDateHandler();
});

/**扩展属性操作**/

/**
 * 格式化字符串
 *
 * @param a a
 * @returns {*} a
 */
String.prototype.formatContent = function (a) {
   a = a.replace(/\r\n/g, '<br/>');
   a = a.replace(/\n/g, '<br/>');
   a = a.replace(/\s/g, ' ');
   return a;
}

/**
 * 封装查询参数
 */
String.prototype.addQueryParams = function(params) {
    var split = '?';
    if (this.indexOf('?') > -1) {
        split = '&';
    }
    var queryParams = '';
    for(var i in params) {
        queryParams += i + '=' + params[i] + '&';
    }
    queryParams = queryParams.substr(0, queryParams.length -1)
    return this + split + queryParams;
}

//去除内容空格
String.prototype.trimAll=function() {
    return this.toString().replace(/\s+/g, ""); 
}

//清除两边的空格 
String.prototype.trim=function() { 
  return this.toString().replace(/(^\s*)|(\s*$)/g, ""); 
}

//判断结尾字符
String.prototype.endWith=function(s){
  if(s==null||s==""||this.length==0||s.length>this.length)
     return false;
  if(this.substring(this.length-s.length)==s)
     return true;
  else
     return false;
  return true;
}

//判断开始字符
String.prototype.startWith=function(s){
  if(s==null||s==""||this.length==0||s.length>this.length)
   return false;
  if(this.substr(0,s.length)==s)
     return true;
  else
     return false;
  return true;
}
/**  
 * 日期格式化（原型扩展或重载）  
 * 格式 YYYY/yyyy/ 表示年份  
 * MM/M 月份  
 * dd/DD/d/D 日期  
 * @param {formatStr} 格式模版  
 * @type string  
 * @returns 日期字符串  
 */  
Date.prototype.format = function(fmt){
	var o = {
	        "M+": this.getMonth() + 1, //月份 
	        "d+": this.getDate(), //日 
	        "h+": this.getHours(), //小时 
	        "m+": this.getMinutes(), //分 
	        "s+": this.getSeconds() //秒 
    };
    if (/(y+)/.test(fmt)){ //根据y的长度来截取年
    	fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o){
    	if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    }
    return fmt;
} 
//URL编码
function URLencode(sStr){  
   return encodeURIComponent(sStr).replace(/\+/g, '%2B');  
}